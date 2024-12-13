package com.food.order.restful.mapper;

import com.food.order.restful.entity.FoodEntity;
import com.food.order.restful.model.FoodResponse;

public class FoodResponseMapper {

    public static FoodResponse ToFoodResponse(FoodEntity food) {
        return FoodResponse.builder()
                .id(food.getId())
                .code(food.getCode())
                .name(food.getName())
                .price(food.getPrice())
                .isReady(food.getIsReady())
                .photoUrl(food.getPhotoUrl())
                .build();
    }

}
