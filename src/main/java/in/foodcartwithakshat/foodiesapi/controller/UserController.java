package in.foodcartwithakshat.foodiesapi.controller;


import in.foodcartwithakshat.foodiesapi.io.UserRegistrationRequest;
import in.foodcartwithakshat.foodiesapi.io.UserRegistrationResponse;
import in.foodcartwithakshat.foodiesapi.service.UserRegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserRegistrationService userRegistrationService;


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserRegistrationResponse register(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        return userRegistrationService.registerUser(userRegistrationRequest);
    }
}
