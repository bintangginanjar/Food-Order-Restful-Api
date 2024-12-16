package com.food.order.restful.mapper;

import java.util.List;
import java.util.stream.Collectors;

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

    public static List<FoodResponse> ToFoodResponseList(List<FoodEntity> foods) {
        return foods.stream()
                    .map(
                        p -> new FoodResponse(
                            p.getId(),
                            p.getCode(),
                            p.getName(),                            
                            p.getPrice(),
                            p.getIsReady(),
                            p.getPhotoUrl()                                                      
                        )).collect(Collectors.toList());
    }

}
