package com.food.order.restful.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.food.order.restful.entity.ProfileEntity;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Integer>{

    
} 
