package com.food.order.restful.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.food.order.restful.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

}
