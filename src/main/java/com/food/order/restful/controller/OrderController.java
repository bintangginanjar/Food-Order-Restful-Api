package com.food.order.restful.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.food.order.restful.entity.UserEntity;
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
        path = "/api/users/orders",
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
}
