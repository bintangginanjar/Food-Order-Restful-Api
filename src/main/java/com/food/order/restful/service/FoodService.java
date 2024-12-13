package com.food.order.restful.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.food.order.restful.entity.CategoryEntity;
import com.food.order.restful.entity.FoodEntity;
import com.food.order.restful.entity.UserEntity;
import com.food.order.restful.mapper.FoodResponseMapper;
import com.food.order.restful.model.FoodResponse;
import com.food.order.restful.model.RegisterFoodRequest;
import com.food.order.restful.repository.CategoryRepository;
import com.food.order.restful.repository.FoodRepository;

@Service
public class FoodService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private ValidationService validationService;

    public FoodService(FoodRepository foodRepository, ValidationService validationService, CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        this.foodRepository = foodRepository;
        this.validationService = validationService;
    }

    public FoodResponse create(UserEntity user, RegisterFoodRequest request, String strCategoryId) {
        validationService.validate(request);

        Integer categoryId;

        try {
            categoryId = Integer.parseInt(strCategoryId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad category id");
        }

        CategoryEntity category = categoryRepository.findById(categoryId)
                                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        if (foodRepository.findByName(request.getName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Food already registered");
        }

        FoodEntity food = new FoodEntity();
        food.setCode(UUID.randomUUID().toString());
        food.setName(request.getName());
        food.setPrice(request.getPrice());
        food.setIsReady(true);
        food.setPhotoUrl(request.getPhotoUrl());
        food.setCategoryEntity(category);
        foodRepository.save(food);

        return FoodResponseMapper.ToFoodResponse(food);
    }
}
