package com.food.order.restful.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.food.order.restful.entity.UserEntity;
import com.food.order.restful.model.UpdateOrderItemRequest;
import com.food.order.restful.model.OrderResponse;
import com.food.order.restful.model.WebResponse;
import com.food.order.restful.service.OrderService;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(
        path = "/api/orders",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<OrderResponse> create(UserEntity user) {

        OrderResponse response = orderService.create(user);

        return WebResponse.<OrderResponse>builder()
                                        .status(true)
                                        .messages("Order creation success")
                                        .data(response)
                                        .build();

    }

    @GetMapping(
        path = "/api/orders",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<OrderResponse>> list(UserEntity user) {

        List<OrderResponse> response = orderService.list(user);

        return WebResponse.<List<OrderResponse>>builder()
                                        .status(true)
                                        .messages("Order list fetching success")
                                        .data(response)
                                        .build();
    }

    @PatchMapping(
        path = "/api/orders/{orderId}/food/{foodId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<OrderResponse> updateOrder(UserEntity user,
                                                        @RequestBody UpdateOrderItemRequest request,
                                                        @PathVariable("orderId") String orderId,
                                                        @PathVariable("foodId") String foodId) {

        request.setOrderId(orderId);
        request.setFoodId(foodId);
        OrderResponse response = orderService.addItem(user, request, orderId, foodId);

        return WebResponse.<OrderResponse>builder()
                                        .status(true)
                                        .messages("Update order item success")
                                        .data(response)
                                        .build();
    }

}
