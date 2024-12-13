package com.food.order.restful.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.food.order.restful.entity.FoodEntity;

public interface FoodRepository extends JpaRepository<FoodEntity, Integer>{

    

}
