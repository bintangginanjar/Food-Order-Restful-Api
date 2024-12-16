package com.food.order.restful.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.food.order.restful.entity.UserEntity;
import com.food.order.restful.model.UpdateOrderItemRequest;
import com.food.order.restful.model.OrderResponse;
import com.food.order.restful.model.OrderWithItemResponse;
import com.food.order.restful.model.WebResponse;
import com.food.order.restful.service.OrderItemService;
import com.food.order.restful.service.OrderService;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    public OrderController(OrderService orderService, OrderItemService orderItemService) {
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
        OrderResponse response = orderService.updateItem(user, request, orderId, foodId);

        return WebResponse.<OrderResponse>builder()
                                        .status(true)
                                        .messages("Update order item success")
                                        .data(response)
                                        .build();
    }

    @DeleteMapping(
        path = "/api/orders/{orderId}/item/{itemId}",        
        produces = MediaType.APPLICATION_JSON_VALUE
    ) public WebResponse<String> deleteItem(UserEntity user,                                                    
                                    @PathVariable("orderId") String orderId,
                                    @PathVariable("itemId") String itemId) {

        orderService.deleteItem(user, orderId, itemId);

        return WebResponse.<String>builder()
                                            .status(true)
                                            .messages("Delete order item success")                                            
                                            .build();
    }

    @GetMapping(
        path = "/api/orders/{orderId}/items",
        produces = MediaType.APPLICATION_JSON_VALUE
    ) public WebResponse<OrderWithItemResponse> listItems(UserEntity user,                                                    
                                            @PathVariable("orderId") String orderId) {

        OrderWithItemResponse responses = orderService.get(user, orderId);

        return WebResponse.<OrderWithItemResponse>builder()
                                            .status(true)
                                            .messages("Fetching order success") 
                                            .data(responses)                                          
                                            .build();
    }
}
