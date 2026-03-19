package in.foodcartwithakshat.foodiesapi.service;

import in.foodcartwithakshat.foodiesapi.io.UserRegistrationRequest;
import in.foodcartwithakshat.foodiesapi.io.UserRegistrationResponse;

public interface UserRegistrationService {

    UserRegistrationResponse registerUser(UserRegistrationRequest request);


}
