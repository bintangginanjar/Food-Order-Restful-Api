package com.food.order.restful.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;

import com.food.order.restful.entity.UserEntity;
import com.food.order.restful.model.FoodResponse;
import com.food.order.restful.model.RegisterFoodRequest;
import com.food.order.restful.model.UpdateFoodRequest;
import com.food.order.restful.model.WebResponse;
import com.food.order.restful.service.FoodService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;



@RestController
public class FoodController {

    @Autowired
    private FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @PostMapping(
        path = "/api/categories/{categoryId}/foods",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<FoodResponse> create(UserEntity user,
                                                @RequestBody RegisterFoodRequest request,
                                                @PathVariable("categoryId") String categoryId) {
        
        FoodResponse response = foodService.create(user, request, categoryId);

        return WebResponse.<FoodResponse>builder()
                                        .status(true)
                                        .messages("Food registration success")
                                        .data(response)
                                        .build();
    }
    
    @GetMapping(
        path = "/api/categories/{categoryId}/foods/{foodId}",        
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<FoodResponse> get(UserEntity user,                                            
                                            @PathVariable("categoryId") String categoryId,
                                            @PathVariable("foodId") String foodId) {
        
        FoodResponse response = foodService.get(categoryId, foodId);

        return WebResponse.<FoodResponse>builder()
                                        .status(true)
                                        .messages("Food fetching success")
                                        .data(response)
                                        .build();
    }
    
    @PatchMapping(
        path = "/api/categories/{categoryId}/foods/{foodId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<FoodResponse> update(UserEntity user,
                                            @RequestBody UpdateFoodRequest request,
                                            @PathVariable("categoryId") String categoryId,
                                            @PathVariable("foodId") String foodId) {

        request.setId(foodId);

        FoodResponse response = foodService.update(user, request, categoryId, foodId);

        return WebResponse.<FoodResponse>builder()
                                        .status(true)
                                        .messages("Food update success")
                                        .data(response)
                                        .build();
    }

    @DeleteMapping(
        path = "/api/categories/{categoryId}/foods/{foodId}",        
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(UserEntity user,                                     
                                            @PathVariable("categoryId") String categoryId,
                                            @PathVariable("foodId") String foodId) {
        foodService.delete(user, categoryId, foodId);

        return WebResponse.<String>builder()
                                        .status(true)
                                        .messages("Food delete success")                                        
                                        .build();
    }
}
