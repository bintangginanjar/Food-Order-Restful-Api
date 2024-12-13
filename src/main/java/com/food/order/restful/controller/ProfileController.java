package com.food.order.restful.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.food.order.restful.entity.UserEntity;
import com.food.order.restful.model.ProfileResponse;
import com.food.order.restful.model.RegisterProfileRequest;
import com.food.order.restful.model.UpdateProfileRequest;
import com.food.order.restful.model.WebResponse;
import com.food.order.restful.service.ProfileService;

@RestController
public class ProfileController {

    @Autowired
    ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping(
        path = "/api/profiles",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ProfileResponse> create(UserEntity user, @RequestBody RegisterProfileRequest request) {
        ProfileResponse response = profileService.create(user, request);

        return WebResponse.<ProfileResponse>builder()
                                        .status(true)
                                        .messages("Profile registration success")
                                        .data(response)
                                        .build();        
    }

    @GetMapping(
        path = "/api/profiles",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ProfileResponse> get(UserEntity user) {
        ProfileResponse response = profileService.get(user);

        return WebResponse.<ProfileResponse>builder()
                                        .status(true)
                                        .messages("Profile registration success")
                                        .data(response)
                                        .build();        
    }

    @PatchMapping(
        path = "/api/profiles",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ProfileResponse> update(UserEntity user, @RequestBody UpdateProfileRequest request) {
        ProfileResponse response = profileService.update(user, request);

        return WebResponse.<ProfileResponse>builder()
                                        .status(true)
                                        .messages("Profile update success")
                                        .data(response)
                                        .build();      
    }
}
