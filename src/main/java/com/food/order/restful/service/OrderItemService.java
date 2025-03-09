package com.food.order.restful.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.food.order.restful.entity.OrderEntity;
import com.food.order.restful.entity.OrderItemEntity;
import com.food.order.restful.entity.UserEntity;
import com.food.order.restful.mapper.ResponseMapper;
import com.food.order.restful.model.OrderItemResponse;
import com.food.order.restful.repository.OrderItemRepository;
import com.food.order.restful.repository.OrderRepository;

@Service
public class OrderItemService {

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    OrderRepository orderRepository;

    public OrderItemService(OrderItemRepository orderItemRepository, OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional(readOnly = true)
    public List<OrderItemResponse> listByOrder(UserEntity user, String strOrderId) {
        Integer orderId;   

        try {
            orderId = Integer.parseInt(strOrderId);            
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad order id");
        }

        OrderEntity order = orderRepository.findFirstByUserEntityAndId(user, orderId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        List<OrderItemEntity> items = orderItemRepository.findAllByOrderEntity(order);

        return ResponseMapper.ToOrderItemResponseList(items);
    }
}
