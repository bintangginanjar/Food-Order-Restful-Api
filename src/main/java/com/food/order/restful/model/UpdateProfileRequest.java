package com.food.order.restful.model;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProfileRequest {
    
    private String id;

    private String firstname;
        
    private String lastname;

    @Email
    private String email;
    
    private String address;

    private String phoneNumber;

    private String city;

    private String province;
    
    private String postalCode;

}
