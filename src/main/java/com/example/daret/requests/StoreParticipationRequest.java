package com.example.daret.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StoreParticipationRequest {
  
    @NotNull
    private Long idDaret;
    @NotNull
    @DecimalMin(value = "0.25")
    private Float quantity;


}
