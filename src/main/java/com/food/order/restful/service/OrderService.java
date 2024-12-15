package com.food.order.restful.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.food.order.restful.entity.FoodEntity;
import com.food.order.restful.entity.OrderEntity;
import com.food.order.restful.entity.OrderItemEntity;
import com.food.order.restful.entity.UserEntity;
import com.food.order.restful.mapper.OrderResponseMapper;
import com.food.order.restful.model.UpdateOrderItemRequest;
import com.food.order.restful.model.OrderItemResponse;
import com.food.order.restful.model.OrderResponse;
import com.food.order.restful.model.OrderWithItemResponse;
import com.food.order.restful.repository.FoodRepository;
import com.food.order.restful.repository.OrderItemRepository;
import com.food.order.restful.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private FoodRepository foodRepository;

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

    @Transactional(readOnly = true)
    public List<OrderResponse> list(UserEntity user) {
        List<OrderEntity> orders = orderRepository.findAllByUserEntity(user);

        List<OrderResponse> orderList = orders.stream()
                                        .map(p -> new OrderResponse(
                                            p.getId(),
                                            p.getOrderId(),
                                            p.getDate(),
                                            p.getTotalPrice(),
                                            p.getStatus()                                            
                                        )).collect(Collectors.toList());
        
        return orderList;
    }

    @Transactional(readOnly = true)
    public OrderWithItemResponse<Object> get(UserEntity user, String strOrderId) {
        Integer orderId;        

        try {
            orderId = Integer.parseInt(strOrderId);            
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad order id");
        }

        OrderEntity order = orderRepository.findFirstByUserEntityAndId(user, orderId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        List<OrderItemEntity> items = orderItemRepository.findAllByOrderEntity(order);

        List<OrderItemResponse> itemList = items.stream()
                                                .map(
                                                    p -> new OrderItemResponse(
                                                        p.getId(),
                                                        p.getQuantity(),
                                                        p.getSubTotal()                                                        
                                                    )).collect(Collectors.toList());

        return OrderWithItemResponse.builder()
                                    .id(order.getId())
                                    .orderId(order.getOrderId())
                                    .date(order.getDate())
                                    .totalPrice(order.getTotalPrice())
                                    .status(order.getStatus())
                                    .items(itemList)
                                    .build();
    }

    @Transactional
    public OrderResponse updateItem(UserEntity user, UpdateOrderItemRequest request, String strOrderId, String strFoodId) {

        validationService.validate(request);

        Integer orderId;
        Integer foodId;

        try {
            orderId = Integer.parseInt(request.getOrderId());
            foodId = Integer.parseInt(request.getFoodId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad order id or food id");
        }

        OrderEntity order = orderRepository.findFirstByUserEntityAndId(user, orderId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        FoodEntity food = foodRepository.findFirstById(foodId)       
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Food not found"));                     

        OrderItemEntity item = new OrderItemEntity();
        item.setOrderEntity(order);
        item.setFoodEntity(food);
        item.setQuantity(request.getQuantity());                
        item.setSubTotal(food.getPrice() * request.getQuantity());
        orderItemRepository.save(item);

        Integer prevTotalPrice = order.getTotalPrice();
        order.setTotalPrice(prevTotalPrice + item.getSubTotal());
        order.getOrderItemEntity().add(item);
        orderRepository.save(order);

        return OrderResponseMapper.ToOrderResponse(order);   
    }

    @Transactional
    public void deleteItem(UserEntity user, String strOrderId, String strItemId) {        
        Integer orderId;
        Integer itemId;

        try {
            orderId = Integer.parseInt(strOrderId);
            itemId = Integer.parseInt(strItemId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad order id or food id");
        }

        OrderEntity order = orderRepository.findFirstByUserEntityAndId(user, orderId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        
        OrderItemEntity item = orderItemRepository.findByOrderEntityAndId(order, itemId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));

        orderItemRepository.delete(item);
    }
}
