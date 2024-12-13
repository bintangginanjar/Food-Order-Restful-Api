package com.food.order.restful.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.food.order.restful.entity.ProfileEntity;
import com.food.order.restful.entity.UserEntity;
import com.food.order.restful.mapper.ProfileResponseMapper;
import com.food.order.restful.model.ProfileResponse;
import com.food.order.restful.model.RegisterProfileRequest;
import com.food.order.restful.repository.ProfileRepository;

@Service
public class ProfileService {

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    ValidationService validationService;

    public ProfileService(ProfileRepository profileRepository, ValidationService validationService) {
        this.profileRepository = profileRepository;
        this.validationService = validationService;
    }

    public ProfileResponse register(UserEntity user, RegisterProfileRequest request) {
        validationService.validate(request);

        ProfileEntity profile = new ProfileEntity();
        profile.setFirstname(request.getFirstname());
        profile.setLastname(request.getLastname());
        profile.setEmail(request.getEmail());
        profile.setAddress(request.getAddress());
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setCity(request.getCity());
        profile.setProvince(request.getProvince());
        profile.setPostalCode(request.getPostalCode());
        profile.setUserEntity(user);

        profileRepository.save(profile);

        return ProfileResponseMapper.ToProfileResponseMapper(profile);
    }
}
