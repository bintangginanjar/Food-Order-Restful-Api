package com.food.order.restful.model;

import java.util.List;

import com.food.order.restful.entity.OrderItemEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {

    private Integer id;
    
    private String orderId;

    private String date;
    
    private Integer totalPrice;

    private String status;

    private List<OrderItemEntity> orderItemEntity;

}
