package com.example.daret.dtos;

import java.sql.Timestamp;

import com.example.daret.models.Participation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ParticipationDto {
    private long id;
    private UserDto user;
    private float quantity;
    private Timestamp payDate;
    private boolean payed;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private int payCount;
    // private DaretDto daret;

    public static ParticipationDto toParticipationDto(Participation participation) {
        ParticipationDto participationDto = new ParticipationDto();
        participationDto.setId(participation.getId());
        participationDto.setUser(UserDto.toUserDto(participation.getUser()));  
        participationDto.setQuantity(participation.getQuantity());
        participationDto.setPayDate(participation.getPayDate());
        participationDto.setPayed(participation.isPayed());
        participationDto.setCreatedAt(participation.getCreatedAt());
        participationDto.setUpdatedAt(participation.getUpdatedAt());
        participation.setPayCount(participation.getPayCount());
        // participationDto.setDaret(DaretDto.toDaretDto(participation.getDaret()));;

        return participationDto;
    }
}
