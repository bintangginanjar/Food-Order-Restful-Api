package com.food.order.restful.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.food.order.restful.entity.CategoryEntity;
import com.food.order.restful.entity.FoodEntity;


public interface FoodRepository extends JpaRepository<FoodEntity, Integer>{

    Optional<FoodEntity> findByName(String name);

    Optional<FoodEntity> findFirstByCategoryEntityAndId(CategoryEntity categoryEntity, Integer foodId);

    Optional<FoodEntity> findFirstById(Integer foodId);

    List<FoodEntity> findAllByCategoryEntity(CategoryEntity categoryEntity);
}
