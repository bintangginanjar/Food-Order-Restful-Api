package com.food.order.restful.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.food.order.restful.entity.OrderEntity;
import com.food.order.restful.entity.UserEntity;
import com.food.order.restful.mapper.OrderResponseMapper;
import com.food.order.restful.model.OrderResponse;
import com.food.order.restful.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ValidationService validationService;

    public OrderService(OrderRepository orderRepository, ValidationService validationService) {
        this.orderRepository = orderRepository;
        this.validationService = validationService;
    }

    @Transactional
    public OrderResponse create(UserEntity user) {
        OrderEntity order = new OrderEntity();

        Date date = new Date();

        order.setOrderId(UUID.randomUUID().toString());
        order.setDate(date.toString());
        order.setTotalPrice(0);
        order.setStatus("Pending");
        order.setUserEntity(user);
        orderRepository.save(order);

        return OrderResponseMapper.ToOrderResponse(order);
    }
}
