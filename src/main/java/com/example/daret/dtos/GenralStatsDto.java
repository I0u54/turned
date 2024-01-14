package com.example.daret.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GenralStatsDto {
    private Long activeUsers;
    private Long usersCount;
    private Long daretsCount;
    
}
