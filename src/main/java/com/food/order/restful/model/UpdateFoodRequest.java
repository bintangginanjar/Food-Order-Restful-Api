package com.food.order.restful.model;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateFoodRequest {
    
    private String id;

    private String name;
    
    private Integer price;
        
    @Size(max = 1024)
    private String photoUrl;

}
