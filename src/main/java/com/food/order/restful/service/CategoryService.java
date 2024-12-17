package com.food.order.restful.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.food.order.restful.entity.CategoryEntity;
import com.food.order.restful.entity.FoodEntity;
import com.food.order.restful.entity.UserEntity;
import com.food.order.restful.mapper.CategoryResponseMapper;
import com.food.order.restful.mapper.FoodResponseMapper;
import com.food.order.restful.model.CategoryResponse;
import com.food.order.restful.model.CategoryWithFoodResponse;
import com.food.order.restful.model.FoodResponse;
import com.food.order.restful.model.RegisterCategoryRequest;
import com.food.order.restful.model.UpdateCategoryRequest;
import com.food.order.restful.repository.CategoryRepository;
import com.food.order.restful.repository.FoodRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private ValidationService validationService;

    public CategoryService(CategoryRepository categoryRepository, FoodRepository foodRepository, ValidationService validationService) {
        this.categoryRepository = categoryRepository;
        this.foodRepository = foodRepository;
        this.validationService = validationService;
    }

    public CategoryResponse create(UserEntity user, RegisterCategoryRequest request) {
        validationService.validate(request);

        if (categoryRepository.findByName(request.getName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category already registered");
        }

        CategoryEntity category = new CategoryEntity();
        category.setName(request.getName());
        categoryRepository.save(category);

        return CategoryResponseMapper.ToCategoryResponse(category);
    }

    @Transactional(readOnly = true)
    public CategoryResponse get(String strCategoryId) {
        Integer categoryId;

        try {
            categoryId = Integer.parseInt(strCategoryId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad category id");
        }

        CategoryEntity category = categoryRepository.findById(categoryId)
                                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        return CategoryResponseMapper.ToCategoryResponse(category);
    }

    @Transactional
    public CategoryResponse update(UserEntity user, UpdateCategoryRequest request) {
        Integer categoryId;

        try {
            categoryId = Integer.parseInt(request.getId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad category id");
        }
        
        CategoryEntity category = categoryRepository.findById(categoryId)
                                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        
        category.setName(request.getName());
        categoryRepository.save(category);

        return CategoryResponseMapper.ToCategoryResponse(category);        
    }

    @Transactional(readOnly = true)
    public CategoryWithFoodResponse list(UserEntity user, String strCategoryId) {
        Integer categoryId;

        try {
            categoryId = Integer.parseInt(strCategoryId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad category id");
        }

        CategoryEntity category = categoryRepository.findById(categoryId)
                                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        List<FoodEntity> foods = foodRepository.findAllByCategoryEntity(category);

        List<FoodResponse> foodList = FoodResponseMapper.ToFoodResponseList(foods);

        return CategoryResponseMapper.ToCategoryResponseWithResponse(category, foodList);
    }

}
