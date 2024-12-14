package com.food.order.restful.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.food.order.restful.entity.OrderEntity;
import com.food.order.restful.entity.UserEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

    Optional<OrderEntity> findFirstByUserEntityAndId(UserEntity user, Integer orderId);
}
