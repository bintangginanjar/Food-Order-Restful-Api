package com.food.order.restful.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterFoodRequest {

    @NotBlank
    private String name;
    
    @NotNull
    private Integer price;
    
    @NotBlank
    @Size(max = 1024)
    private String photoUrl;

}
