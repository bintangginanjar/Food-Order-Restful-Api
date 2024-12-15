package com.food.order.restful.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {
    
    private Integer id;

    private Integer quantity;

    private Integer subTotal;
    
    //private FoodEntity foodEntity;

    //private OrderEntity orderEntity;
}
