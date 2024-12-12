package com.food.order.restful.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    
}
