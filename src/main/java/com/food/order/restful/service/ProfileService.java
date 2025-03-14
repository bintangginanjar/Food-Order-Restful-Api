package com.food.order.restful.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.food.order.restful.entity.ProfileEntity;
import com.food.order.restful.entity.UserEntity;
import com.food.order.restful.mapper.ResponseMapper;
import com.food.order.restful.model.ProfileResponse;
import com.food.order.restful.model.RegisterProfileRequest;
import com.food.order.restful.model.UpdateProfileRequest;
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

    public ProfileResponse create(UserEntity user, RegisterProfileRequest request) {
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

        return ResponseMapper.ToProfileResponseMapper(profile);
    }

    @Transactional(readOnly = true)
    public ProfileResponse get(UserEntity user) {
        ProfileEntity profile = profileRepository.findByUserEntity(user)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Profile not found"));

        return ResponseMapper.ToProfileResponseMapper(profile);
    }

    @Transactional
    public ProfileResponse update(UserEntity user, UpdateProfileRequest request) {
        validationService.validate(request);

        ProfileEntity profile = profileRepository.findByUserEntity(user)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Profile not found"));

        if (Objects.nonNull(request.getFirstname())) {
            profile.setFirstname(request.getFirstname());
        }

        if (Objects.nonNull(request.getLastname())) {
            profile.setLastname(request.getLastname());
        }

        if (Objects.nonNull(request.getEmail())) {
            profile.setEmail(request.getEmail());
        }

        if (Objects.nonNull(request.getAddress())) {
            profile.setAddress(request.getAddress());
        }

        if (Objects.nonNull(request.getPhoneNumber())) {
            profile.setPhoneNumber(request.getPhoneNumber());
        }

        if (Objects.nonNull(request.getCity())) {
            profile.setCity(request.getCity());
        }

        if (Objects.nonNull(request.getProvince())) {
            profile.setProvince(request.getProvince());
        }

        if (Objects.nonNull(request.getPostalCode())) {
            profile.setPostalCode(request.getPostalCode());
        }

        profileRepository.save(profile);

        return ResponseMapper.ToProfileResponseMapper(profile);
    }

}
