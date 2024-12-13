package com.food.order.restful.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.food.order.restful.entity.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer>{

    Optional<CategoryEntity> findByName(String name);

}
