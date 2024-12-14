package com.food.order.restful.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.food.order.restful.entity.OrderItemEntity;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Integer>{

}
