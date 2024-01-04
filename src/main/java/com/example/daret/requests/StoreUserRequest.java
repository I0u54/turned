package com.example.daret.requests;

import com.example.daret.models.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StoreUserRequest {

    @NotBlank(message = "Name is required")
    private String Name;

    @NotBlank(message = "Name is required") 
    private String lastName;

    @NotBlank(message = "email is required")
    @Email(message = "invalid email format")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters ")
    private String password1;
    @NotBlank(message = "Password Confirmation is required")
    private String password2;
    
  public  boolean passwordCheck(){
    return password1.equals(password2);

  }
    


}
