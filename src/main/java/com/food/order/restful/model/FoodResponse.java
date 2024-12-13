package com.food.order.restful.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodResponse {

    private Integer id;
    
    private String code;

    
    private String name;
    
    
    private Integer price;

    
    private Boolean isReady;

    
    private String photoUrl;

}
