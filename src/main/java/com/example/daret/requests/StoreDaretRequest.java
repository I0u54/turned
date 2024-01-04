package com.example.daret.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StoreDaretRequest {
 
    @NotNull
    private Float price;
    
    @NotNull
    private Integer pnumber;
    @NotNull
    private Integer duration;
    @NotBlank
    private String dtype;
    
}
