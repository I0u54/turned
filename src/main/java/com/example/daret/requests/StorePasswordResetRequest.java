package com.example.daret.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StorePasswordResetRequest {
    @NotBlank(message = "email field is required")
    @Email(message = "invalid email format")
    private String email;

   
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters ")
    private String password1;
    @NotBlank(message = "Password Confirmation is required")
    private String password2;

   @NotNull(message = "Token is required")
    @Min(value = 100000, message = "Token must be at least 100000")
    @Max(value = 999999, message = "Token must be at most 999999")
    private Integer token;
    public  boolean passwordCheck(){
        return password1.equals(password2);
    
      }
}
