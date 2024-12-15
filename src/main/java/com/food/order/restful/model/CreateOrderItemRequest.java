package com.food.order.restful.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderItemRequest {
    
    @NotBlank
    @JsonIgnore
    private String orderId;

    @NotBlank
    @JsonIgnore
    private String foodId;

    @NotNull
    private Integer quantity;    

}
