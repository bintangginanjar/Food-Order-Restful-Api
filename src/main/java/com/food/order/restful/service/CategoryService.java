package com.food.order.restful.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.food.order.restful.entity.CategoryEntity;
import com.food.order.restful.entity.UserEntity;
import com.food.order.restful.mapper.CategoryResponseMapper;
import com.food.order.restful.model.CategoryResponse;
import com.food.order.restful.model.RegisterCategoryRequest;
import com.food.order.restful.model.UpdateCategoryRequest;
import com.food.order.restful.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ValidationService validationService;

    public CategoryService(CategoryRepository categoryRepository, ValidationService validationService) {
        this.categoryRepository = categoryRepository;
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
                                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found"));

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
        
        CategoryEntity category = categoryRepository.findByNameAndId(request.getName(), categoryId)
                                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found"));

        CategoryEntity categoryDb = categoryRepository.findByName(request.getName())
                                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found"));;

        if ((category.getName() == categoryDb.getName()) && (category.getId() != categoryDb.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category already registered");
        }

        category.setName(request.getName());
        categoryRepository.save(category);

        return CategoryResponseMapper.ToCategoryResponse(category);
    }

}
