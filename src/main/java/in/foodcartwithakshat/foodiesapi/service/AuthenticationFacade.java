package in.foodcartwithakshat.foodiesapi.service;


import org.springframework.security.core.Authentication;

//To get the logged in userId for cart details savings in database we need to store the userIds in a spring context
public interface AuthenticationFacade {

    Authentication getAuthentication();




}
