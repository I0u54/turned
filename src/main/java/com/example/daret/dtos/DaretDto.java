package com.example.daret.dtos;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.example.daret.models.Daret;
import com.example.daret.models.Participation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class DaretDto {
    private long id;
    private float price ;
    private int pNumber;
    private float available;
    private Timestamp expired_at;
    private int duration ;
    private String dType;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private List<ParticipationDto> participations;

    public static  DaretDto toDaretDto(Daret daret){
        List<ParticipationDto> ps = new ArrayList<>();
        DaretDto daretDto = new DaretDto();
        daretDto.setId(daret.getId());
        daretDto.setPNumber(daret.getPNumber());
        daretDto.setPrice(daret.getPrice());
        daretDto.setAvailable(daret.getAvailable());
        daretDto.setDuration(daret.getDuration());
        daretDto.setDType(daret.getDType());
        daretDto.setStatus(daret.getStatus());
        daretDto.setExpired_at(daret.getExpired_at());
        daretDto.setCreatedAt(daret.getCreatedAt());
        daretDto.setUpdatedAt(daret.getUpdatedAt());
        for(Participation p : daret.getParticipations()){
            ps.add(ParticipationDto.toParticipationDto(p));
        }
        
        daretDto.setParticipations(ps);


        return daretDto; 
        

    }
    
}
