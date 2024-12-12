package com.food.order.restful.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.food.order.restful.entity.UserEntity;
import com.food.order.restful.model.RegisterUserRequest;
import com.food.order.restful.model.UpdateUserRequest;
import com.food.order.restful.model.UserResponse;
import com.food.order.restful.model.WebResponse;
import com.food.order.restful.service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(
        path = "/api/users",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> create(@RequestBody RegisterUserRequest request) {
        UserResponse response = userService.create(request);

        return WebResponse.<UserResponse>builder()
                                        .status(true)
                                        .messages("User registration success")
                                        .data(response)
                                        .build();        
    }

    @GetMapping(
        path = "/api/users/current",        
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> get(UserEntity user) {
        UserResponse response = userService.get(user);

        return WebResponse.<UserResponse>builder()
                                            .status(true)
                                            .messages("User fetching success")
                                            .data(response)
                                            .build();
    }

    @PatchMapping(
        path = "/api/users/current",
        consumes = MediaType.APPLICATION_JSON_VALUE,  
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> update(UserEntity user, @RequestBody UpdateUserRequest request) {
        UserResponse response = userService.update(user, request);

        return WebResponse.<UserResponse>builder()
                                            .status(true)
                                            .messages("User update success")
                                            .data(response)
                                            .build();
    }
}
