package com.food.order.restful.mapper;

import java.util.List;

import com.food.order.restful.entity.OrderEntity;
import com.food.order.restful.model.OrderItemResponse;
import com.food.order.restful.model.OrderResponse;
import com.food.order.restful.model.OrderWithItemResponse;

public class OrderResponseMapper {

    public static OrderResponse ToOrderResponse(OrderEntity order) {
        return OrderResponse.builder()
                .id(order.getId())
                .orderId(order.getOrderId())
                .date(order.getDate())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                //.orderItemEntity(order.getOrderItemEntity())
                .build();
    }

    public static OrderWithItemResponse ToOrderWithItemResponse(OrderEntity order, List<OrderItemResponse> itemList) {
        return OrderWithItemResponse.builder()
                .id(order.getId())
                .orderId(order.getOrderId())
                .date(order.getDate())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .items(itemList)
                .build();   
    }

}
