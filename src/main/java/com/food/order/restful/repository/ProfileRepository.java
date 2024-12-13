package com.food.order.restful.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.food.order.restful.entity.ProfileEntity;
import java.util.Optional;

import com.food.order.restful.entity.UserEntity;


public interface ProfileRepository extends JpaRepository<ProfileEntity, Integer>{

    Optional<ProfileEntity>findByUserEntity(UserEntity userEntity);
} 
