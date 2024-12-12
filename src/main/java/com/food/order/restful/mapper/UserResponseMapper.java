package com.food.order.restful.mapper;

import com.food.order.restful.entity.UserEntity;
import com.food.order.restful.model.UserResponse;

public class UserResponseMapper {

    public static UserResponse ToUserResponseMapper(UserEntity user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }

}
