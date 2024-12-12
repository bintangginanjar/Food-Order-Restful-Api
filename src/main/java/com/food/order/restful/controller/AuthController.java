package com.food.order.restful.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.food.order.restful.entity.UserEntity;
import com.food.order.restful.model.LoginUserRequest;
import com.food.order.restful.model.TokenResponse;
import com.food.order.restful.model.UserResponse;
import com.food.order.restful.model.WebResponse;
import com.food.order.restful.service.AuthService;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(
        path = "/api/auth/login",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TokenResponse> login(@RequestBody LoginUserRequest request) {
        TokenResponse tokenResponse = authService.login(request);
        
        return WebResponse.<TokenResponse>builder().data(tokenResponse).build();
    }

    @DeleteMapping(
        path = "/api/auth/logout",        
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> logout(UserEntity user) {
        authService.logout(user);
        
        return WebResponse.<UserResponse>builder()
                                            .status(true)
                                            .messages("User logout success")                                            
                                            .build();
    }
}
