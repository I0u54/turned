package com.example.daret.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreEmailRequest {
    @NotBlank(message = "email field is required")
    @Email(message = "invalid email format")
    private String email;
}
