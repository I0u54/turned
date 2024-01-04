package com.example.daret.services;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.daret.dtos.UserDto;
import com.example.daret.models.User;
import com.example.daret.repositories.PersonalAccessTokenRepository;
import com.example.daret.repositories.UserRepository;

@Service
public class UserTokenService {
     private final UserRepository userRepository;
    @Autowired
    UserTokenService(PersonalAccessTokenRepository personalAccessTokenRepository,UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public String generateToken() {
        String token = UUID.randomUUID().toString().replace("-","");
        
        return token.toString();
    }
    public String extractToken(String token){
        if(token != null  && token.startsWith("Bearer ")){
            return token.substring(7);
        }
        return null;

    }
    public UserDto getUserOfToken(String token){
        Optional<User> userOptional = userRepository.findUserByToken(token);
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return UserDto.toUserDto(user);
        } 
            return null; 
        

      

    }
}
