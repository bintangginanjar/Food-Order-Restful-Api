package com.food.order.restful.service;

import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.food.order.restful.entity.CategoryEntity;
import com.food.order.restful.entity.FoodEntity;
import com.food.order.restful.entity.UserEntity;
import com.food.order.restful.mapper.FoodResponseMapper;
import com.food.order.restful.model.FoodResponse;
import com.food.order.restful.model.RegisterFoodRequest;
import com.food.order.restful.model.UpdateFoodRequest;
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

    @Transactional
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

    @Transactional(readOnly = true)
    public FoodResponse get(String strCategoryId, String strFoodId) {
        Integer categoryId;
        Integer foodId;

        try {
            categoryId = Integer.parseInt(strCategoryId);
            foodId = Integer.parseInt(strFoodId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad category id");
        }

        CategoryEntity category = categoryRepository.findById(categoryId)
                                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        FoodEntity food = foodRepository.findFirstByCategoryEntityAndId(category, foodId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Food not found"));

        return FoodResponseMapper.ToFoodResponse(food);
    }

    @Transactional
    public FoodResponse update(UserEntity user, UpdateFoodRequest request, String strCategoryId, String strFoodId) {
        validationService.validate(request);

        Integer categoryId;
        Integer foodId;

        try {
            categoryId = Integer.parseInt(strCategoryId);
            foodId = Integer.parseInt(strFoodId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad category id");
        }

        CategoryEntity category = categoryRepository.findById(categoryId)
                                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        FoodEntity food = foodRepository.findFirstByCategoryEntityAndId(category, foodId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Food not found"));
        
        if (Objects.nonNull(request.getName())) {
            food.setName(request.getName());
        }

        if (Objects.nonNull(request.getName())) {
            food.setName(request.getName());
        }

        if (Objects.nonNull(request.getPrice())) {
            food.setPrice(request.getPrice());
        }

        if (Objects.nonNull(request.getPhotoUrl())) {
            food.setPhotoUrl(request.getPhotoUrl());
        }

        foodRepository.save(food);

        return FoodResponseMapper.ToFoodResponse(food);
    }

    @Transactional
    public void delete(UserEntity user, String strCategoryId, String strFoodId) {
        Integer categoryId;
        Integer foodId;

        try {
            categoryId = Integer.parseInt(strCategoryId);
            foodId = Integer.parseInt(strFoodId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad category id");
        }

        CategoryEntity category = categoryRepository.findById(categoryId)
                                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        FoodEntity food = foodRepository.findFirstByCategoryEntityAndId(category, foodId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Food not found"));

        foodRepository.delete(food);
    }
}
