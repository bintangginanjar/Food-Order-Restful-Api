package com.food.order.restful.mapper;

import com.food.order.restful.entity.CategoryEntity;
import com.food.order.restful.model.CategoryResponse;

public class CategoryResponseMapper {

    public static CategoryResponse ToCategoryResponse(CategoryEntity category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

}
