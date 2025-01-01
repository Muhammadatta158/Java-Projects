package com.muhammad.registration.register;

import org.springframework.stereotype.Service;

import com.muhammad.registration.appuser.AppUserRole;
import com.muhammad.registration.appuser.AppUserService;
import com.muhammad.registration.appuser.AppUser;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final EmailValidator emailValidator;
    private final AppUserService appUserService;

    public String register(RegistrationRequest request){
        boolean isValid = emailValidator.test(request.getEmail());
        if(!isValid){
            throw new IllegalStateException("Email not valid");
        }
        return appUserService.signUpUser(
            new AppUser(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword(),
                AppUserRole.USER
            )
        );
    }

}
