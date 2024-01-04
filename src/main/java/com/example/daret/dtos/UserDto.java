package com.example.daret.dtos;

import java.sql.Timestamp;

import com.example.daret.models.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private Long id;
    private String name;
    private String lastName;
    private String email;

    private boolean isAdmin;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    static public UserDto toUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setCreatedAt(user.getCreatedAt());
        user.setUpdatedAt(user.getUpdatedAt());
        userDto.setAdmin(user.isAdmin());
        return userDto;
    }

    
}
