package com.food.order.restful.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.food.order.restful.entity.OrderItemEntity;
import com.food.order.restful.model.OrderItemResponse;

public class OrderItemResponseMapper {

    public static List<OrderItemResponse> ToOrderItemResponseList(List<OrderItemEntity> items) {
        return items.stream()
                    .map(p -> new OrderItemResponse(
                            p.getId(),
                            p.getQuantity(),
                            p.getSubTotal(),
                            p.getFoodEntity(),
                            p.getOrderEntity()                
                    )).collect(Collectors.toList());
    }

}
