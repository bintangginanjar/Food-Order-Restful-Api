package com.food.order.restful.mapper;

import java.util.List;

import com.food.order.restful.entity.CategoryEntity;
import com.food.order.restful.model.CategoryResponse;
import com.food.order.restful.model.CategoryWithFoodResponse;
import com.food.order.restful.model.FoodResponse;

public class CategoryResponseMapper {

    public static CategoryResponse ToCategoryResponse(CategoryEntity category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static CategoryWithFoodResponse ToCategoryResponseWithResponse(CategoryEntity category, List<FoodResponse> foods) {
        return CategoryWithFoodResponse.builder()
                                .id(category.getId())
                                .name(category.getName())
                                .foods(foods)
                                .build();
    }

}
