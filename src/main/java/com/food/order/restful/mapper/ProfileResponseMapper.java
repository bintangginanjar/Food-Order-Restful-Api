package com.food.order.restful.mapper;

import com.food.order.restful.entity.ProfileEntity;
import com.food.order.restful.model.ProfileResponse;

public class ProfileResponseMapper {

    public static ProfileResponse ToProfileResponseMapper(ProfileEntity profile) {
        return ProfileResponse.builder()
                .firstname(profile.getFirstname())
                .lastname(profile.getLastname())
                .email(profile.getEmail())
                .address(profile.getAddress())
                .phoneNumber(profile.getPhoneNumber())
                .city(profile.getCity())
                .province(profile.getProvince())
                .postalCode(profile.getPostalCode())                
                .build();
    }
}
