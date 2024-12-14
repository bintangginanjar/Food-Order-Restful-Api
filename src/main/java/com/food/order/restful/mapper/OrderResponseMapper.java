package com.food.order.restful.mapper;

import com.food.order.restful.entity.OrderEntity;
import com.food.order.restful.model.OrderResponse;

public class OrderResponseMapper {

    public static OrderResponse ToOrderResponse(OrderEntity order) {
        return OrderResponse.builder()
                .id(order.getId())
                .orderId(order.getOrderId())
                .date(order.getDate())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .orderItemEntity(order.getOrderItemEntity())
                .build();
    }

}
