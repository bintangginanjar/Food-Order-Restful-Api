package com.food.order.restful.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.food.order.restful.entity.OrderEntity;
import com.food.order.restful.entity.OrderItemEntity;
import java.util.List;


public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Integer>{

    Optional<OrderItemEntity> findByOrderEntityAndId(OrderEntity order, Integer orderItemId);

    List<OrderItemEntity> findAllByOrderEntity(OrderEntity orderEntity);

}
