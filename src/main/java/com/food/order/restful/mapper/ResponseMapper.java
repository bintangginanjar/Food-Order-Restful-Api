package com.food.order.restful.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.food.order.restful.entity.CategoryEntity;
import com.food.order.restful.entity.FoodEntity;
import com.food.order.restful.entity.OrderEntity;
import com.food.order.restful.entity.OrderItemEntity;
import com.food.order.restful.entity.ProfileEntity;
import com.food.order.restful.entity.UserEntity;
import com.food.order.restful.model.CategoryResponse;
import com.food.order.restful.model.CategoryWithFoodResponse;
import com.food.order.restful.model.FoodResponse;
import com.food.order.restful.model.OrderItemResponse;
import com.food.order.restful.model.OrderResponse;
import com.food.order.restful.model.OrderWithItemResponse;
import com.food.order.restful.model.ProfileResponse;
import com.food.order.restful.model.UserResponse;

public class ResponseMapper {

    public static CategoryResponse ToCategoryResponse(CategoryEntity category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static CategoryWithFoodResponse ToCategoryResponseWithResponse(CategoryEntity category, List<FoodResponse> foods) {
        return CategoryWithFoodResponse.builder()
                                .id(category.getId())
                                .name(category.getName())
                                .foods(foods)
                                .build();
    }

    public static FoodResponse ToFoodResponse(FoodEntity food) {
        return FoodResponse.builder()
                .id(food.getId())
                .code(food.getCode())
                .name(food.getName())
                .price(food.getPrice())
                .isReady(food.getIsReady())
                .photoUrl(food.getPhotoUrl())
                .build();
    }

    public static List<FoodResponse> ToFoodResponseList(List<FoodEntity> foods) {
        return foods.stream()
                    .map(
                        p -> new FoodResponse(
                            p.getId(),
                            p.getCode(),
                            p.getName(),                            
                            p.getPrice(),
                            p.getIsReady(),
                            p.getPhotoUrl()                                                      
                        )).collect(Collectors.toList());
    }

    public static List<OrderItemResponse> ToOrderItemResponseList(List<OrderItemEntity> items) {
        return items.stream()
                    .map(p -> new OrderItemResponse(
                            p.getId(),
                            p.getQuantity(),
                            p.getSubTotal()                                     
                    )).collect(Collectors.toList());
    }

    public static OrderResponse ToOrderResponse(OrderEntity order) {
        return OrderResponse.builder()
                .id(order.getId())
                .orderId(order.getOrderId())
                .date(order.getDate())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                //.orderItemEntity(order.getOrderItemEntity())
                .build();
    }

    public static OrderWithItemResponse ToOrderWithItemResponse(OrderEntity order, List<OrderItemResponse> itemList) {
        return OrderWithItemResponse.builder()
                .id(order.getId())
                .orderId(order.getOrderId())
                .date(order.getDate())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .items(itemList)
                .build();   
    }

    public static ProfileResponse ToProfileResponseMapper(ProfileEntity profile) {
        return ProfileResponse.builder()
                .id(profile.getId())
                .firstname(profile.getFirstname())
                .lastname(profile.getLastname())
                .email(profile.getEmail())
                .address(profile.getAddress())
                .phoneNumber(profile.getPhoneNumber())
                .city(profile.getCity())
                .province(profile.getProvince())
                .postalCode(profile.getPostalCode())                
                .build();
    }

    public static UserResponse ToUserResponseMapper(UserEntity user) {
        return UserResponse.builder()
                .email(user.getEmail())                
                .build();
    }
}
