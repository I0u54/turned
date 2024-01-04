package com.example.daret.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class StoreUserLoginRequest {
    
    @NotBlank(message = "email field is required")
    @Email(message = "invalid email format")
    private String email;
    @NotBlank(message = "Password is required")
    private String password; 

}
