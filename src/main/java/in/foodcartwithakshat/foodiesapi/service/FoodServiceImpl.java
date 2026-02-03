package in.foodcartwithakshat.foodiesapi.service;

import in.foodcartwithakshat.foodiesapi.entity.FoodEntity;
import in.foodcartwithakshat.foodiesapi.io.FoodRequest;
import in.foodcartwithakshat.foodiesapi.io.FoodResponse;
import in.foodcartwithakshat.foodiesapi.repository.FoodRepository;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service

public class FoodServiceImpl implements FoodService {

    private final S3Client s3Client;

    private final S3Presigner s3Presigner;

    private final FoodRepository foodRepository;

    Logger logger = LogManager.getLogger(FoodServiceImpl.class);

    @Value("${aws.s3.bucketname}")
    private String bucketName;

    public FoodServiceImpl(S3Client s3Client, S3Presigner s3Presigner, FoodRepository foodRepository) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.foodRepository = foodRepository;
    }

    private static final long MAX_UPLOAD_SIZE_BYTES = 5 * 1024 * 1024; // 5 MB
    private static final Set<String> ALLOWED_TYPES =
            Set.of("image/jpeg", "image/png", "image/webp");

    @Override
    public String uploadFile(MultipartFile file) {

        validate(file);

        String key = generateKey(file);

        String filenameExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        String keyForURL = UUID.randomUUID().toString()+"."+filenameExtension;


        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .metadata(
                        java.util.Map.of(
                                "original-filename", file.getOriginalFilename(),
                                "uploaded-by", "foodies-api"
                        )
                )
                .build();

        try (InputStream inputStream = file.getInputStream()) {

            PutObjectResponse response = s3Client.putObject(
                    request,
                    RequestBody.fromInputStream(inputStream, file.getSize())
            );

            if (!response.sdkHttpResponse().isSuccessful()) {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "S3 upload failed"
                );
            }

            // IMPORTANT: return object key, NOT public URL
//            return key;

            return generatePresignedUrl(key);

//            return "https://"+bucketName+".s3.amazonaws.com/"+keyForURL;


        } catch (S3Exception e) {

            if (e.statusCode() == 503) {
                throw new ResponseStatusException(
                        HttpStatus.SERVICE_UNAVAILABLE,
                        "S3 temporarily unavailable",
                        e
                );
            }


            logger.error("S3 error code={}, message={}",
                    e.awsErrorDetails().errorCode(),
                    e.awsErrorDetails().errorMessage());
//            AwsCredentials creds = s3Client.serviceClientConfiguration()
//                    .credentialsProvider()
//                    .resolveCredentials();
//
//            logger.info("Using access key: {}", creds.accessKeyId());

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "S3 rejected the upload",
                    e
            );

        } catch (SdkClientException e) {

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "AWS client failure",
                    e
            );

        } catch (IOException e) {

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "File I/O error during upload",
                    e
            );
        }
    }

    @Override
    public FoodResponse addFood(FoodRequest request, MultipartFile file) {
        FoodEntity newFoodEntity = convertToEntity(request);
        String imageUrl = uploadFile(file);
        newFoodEntity.setImageUrl(imageUrl);
        newFoodEntity = foodRepository.save(newFoodEntity);
        return convertToResponse(newFoodEntity);


    }

    @Override
    public List<FoodResponse> readFoods() {
        List<FoodEntity> foodEntities =  foodRepository.findAll();
        // Use of JAVA-8 Stream API
        return foodEntities.stream().map(object -> convertToResponse(object)).collect(Collectors.toList());



    }

    private FoodEntity convertToEntity(FoodRequest foodRequest) {
        return FoodEntity.builder()
                .name(foodRequest.getName())
                .description(foodRequest.getDescription())
                .category(foodRequest.getCategory())
                .price(foodRequest.getPrice())
                .build();
    }

    private FoodResponse convertToResponse(FoodEntity foodEntity) {
        return FoodResponse.builder()
                .id(foodEntity.getId())
                .name(foodEntity.getName())
                .description(foodEntity.getDescription())
                .category(foodEntity.getCategory())
                .price(foodEntity.getPrice())
                .imageUrl(foodEntity.getImageUrl())
                .build();
    }



    private void validate(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "File is missing or empty"
            );
        }

        if (file.getSize() > MAX_UPLOAD_SIZE_BYTES) {
            throw new ResponseStatusException(
                    HttpStatus.PAYLOAD_TOO_LARGE,
                    "File too large"
            );
        }

        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new ResponseStatusException(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                    "Unsupported file type"
            );
        }
    }

    private String generateKey(MultipartFile file) {

        String extension = file.getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf('.') + 1);

        return "uploads/"
                + LocalDate.now()
                + "/"
                + UUID.randomUUID()
                + "."
                + extension;
    }
    public String generatePresignedUrl(String objectKey) {

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        GetObjectPresignRequest presignRequest =
                GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(15)) // expiry
                        .getObjectRequest(getObjectRequest)
                        .build();

        PresignedGetObjectRequest presignedRequest =
                s3Presigner.presignGetObject(presignRequest);

        return presignedRequest.url().toString();
    }

}


//String filenameExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
//String key = UUID.randomUUID().toString()+"."+filenameExtension;
//return "https://"+bucketName+".s3.amazonaws.com/"+key;