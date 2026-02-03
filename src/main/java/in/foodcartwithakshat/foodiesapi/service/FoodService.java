package in.foodcartwithakshat.foodiesapi.service;

import in.foodcartwithakshat.foodiesapi.io.FoodRequest;
import in.foodcartwithakshat.foodiesapi.io.FoodResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FoodService {


    String uploadFile(MultipartFile file);

    FoodResponse addFood(FoodRequest request, MultipartFile file);


}
