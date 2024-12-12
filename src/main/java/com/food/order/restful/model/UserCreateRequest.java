package com.food.order.restful.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateRequest {

    @NotBlank
    @Size(max = 128)        
    private String username;
    
    @NotBlank
    @Size(max = 128)        
    private String password;

    @NotBlank
    @Size(max = 128)
    private String name;
    
}
