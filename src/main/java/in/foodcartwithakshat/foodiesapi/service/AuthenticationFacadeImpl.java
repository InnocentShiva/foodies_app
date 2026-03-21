package in.foodcartwithakshat.foodiesapi.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationFacadeImpl implements AuthenticationFacade {
    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}

/*
    We can use this AuthenticationFacade to get the Authentication object.
    From Authentication object we will get the entity object of user in return.
    Then from the email-address we can get the user-id.
*/
