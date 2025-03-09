package com.food.order.restful.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.food.order.restful.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findFirstByToken(String token);
    
}
