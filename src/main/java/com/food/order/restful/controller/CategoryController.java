package com.food.order.restful.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.food.order.restful.entity.UserEntity;
import com.food.order.restful.model.CategoryResponse;
import com.food.order.restful.model.CategoryWithFoodResponse;
import com.food.order.restful.model.RegisterCategoryRequest;
import com.food.order.restful.model.UpdateCategoryRequest;
import com.food.order.restful.model.WebResponse;
import com.food.order.restful.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping(
        path = "/api/categories",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<CategoryResponse> create(UserEntity user,
                                                @RequestBody RegisterCategoryRequest request) {
        CategoryResponse response = categoryService.create(user, request);

        return WebResponse.<CategoryResponse>builder()
                                        .status(true)
                                        .messages("Category registration success")
                                        .data(response)
                                        .build();        
    }

    @GetMapping(
        path = "/api/category/{categoryId}",        
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<CategoryResponse> get(UserEntity user, 
                                            @PathVariable("categoryId") String categoryId) {
        
        CategoryResponse response = categoryService.get(categoryId);

        return WebResponse.<CategoryResponse>builder()
                                        .status(true)
                                        .messages("Category fetching success")
                                        .data(response)
                                        .build();
    }
    
    @PutMapping(
        path = "/api/category/{categoryId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<CategoryResponse> update(UserEntity user, 
                                                @PathVariable("categoryId") String categoryId, 
                                                @RequestBody UpdateCategoryRequest request) {
        request.setId(categoryId);

        CategoryResponse response = categoryService.update(user, request);

        return WebResponse.<CategoryResponse>builder()
                                        .status(true)
                                        .messages("Category update success")
                                        .data(response)
                                        .build();        
    }

    @GetMapping(
        path = "/api/category/{categoryId}/foods",        
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<CategoryWithFoodResponse> list(UserEntity user, 
                                            @PathVariable("categoryId") String categoryId) {
        
        CategoryWithFoodResponse response = categoryService.list(user, categoryId);

        return WebResponse.<CategoryWithFoodResponse>builder()
                                        .status(true)
                                        .messages("Category fetching success")
                                        .data(response)
                                        .build();
    }
}
