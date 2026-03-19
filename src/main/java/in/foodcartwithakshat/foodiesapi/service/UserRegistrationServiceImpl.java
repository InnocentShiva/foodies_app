package in.foodcartwithakshat.foodiesapi.service;

import in.foodcartwithakshat.foodiesapi.entity.UserEntity;
import in.foodcartwithakshat.foodiesapi.io.UserRegistrationRequest;
import in.foodcartwithakshat.foodiesapi.io.UserRegistrationResponse;
import in.foodcartwithakshat.foodiesapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserRegistrationResponse registerUser(UserRegistrationRequest request) {
        UserEntity newUser = convertToEntity(request);

        newUser = userRepository.save(newUser);
        return convertToResponse(newUser);
    }

    private UserEntity convertToEntity(UserRegistrationRequest request){
        return UserEntity.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .build();

    }

    private UserRegistrationResponse convertToResponse(UserEntity registeredUser){
        return UserRegistrationResponse.builder()
                .id(registeredUser.getId())
                .name(registeredUser.getName())
                .email(registeredUser.getEmail())
                .build();
    }
}
