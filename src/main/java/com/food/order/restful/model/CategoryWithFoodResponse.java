package com.food.order.restful.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryWithFoodResponse {

    private Integer id;

    private String name;

    private List<FoodResponse> foods;

}
