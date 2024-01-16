package com.example.daret.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.daret.dtos.UserDto;
import com.example.daret.models.PasswordResetToken;
import com.example.daret.models.PersonalAccesToken;
import com.example.daret.models.User;
import com.example.daret.repositories.PasswordResetTokenRepository;
import com.example.daret.repositories.PersonalAccessTokenRepository;
import com.example.daret.repositories.UserRepository;
import com.example.daret.requests.StorePasswordResetRequest;
import com.example.daret.requests.StoreUserLoginRequest;
import com.example.daret.requests.StoreUserRequest;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PersonalAccessTokenRepository personalAccesTokenRepository;
    private UserTokenService userTokenService;
    private MailService mailService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    public AuthService(UserRepository userRepository, UserTokenService userTokenService,PersonalAccessTokenRepository personalAccesTokenRepository,PasswordResetTokenRepository passwordResetTokenRepository,MailService mailService) {
        this.userRepository = userRepository;
        this.userTokenService = userTokenService;
        this.personalAccesTokenRepository = personalAccesTokenRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.mailService = mailService;
    }
    public String storeToken(User user){
            List<PersonalAccesToken> tokensToDelete = personalAccesTokenRepository.findByUser(user);
            personalAccesTokenRepository.deleteAll(tokensToDelete);
            PersonalAccesToken  personalAccesToken = new PersonalAccesToken();
            String token = userTokenService.generateToken();
            personalAccesToken.setToken(token);
            personalAccesToken.setUser(user);
            personalAccesTokenRepository.save(personalAccesToken);
            return token;

    }
    public Map<String, Object> storeUser(StoreUserRequest request){
        Optional<User> oldUser = userRepository.findByEmail(request.getEmail());
        Map<String, Object> responseMap = new HashMap<>();
        if (oldUser.isPresent()) {
            responseMap.put("error", "User with the same email already exists");
            return responseMap;
        }
            User user   = userRepository.save(User.toUser(request));
            
            responseMap.put("user",UserDto.toUserDto(user));
            responseMap.put("token",storeToken(user));


            return responseMap;
        
       
        }
        public Map<String,Object> loginUser(StoreUserLoginRequest request){
             Map<String, Object> responseMap = new HashMap<>();
             Optional<User> user = userRepository.findByEmailAndPassword(request.getEmail(),User.hashPassword(request.getPassword()));
             if(user.isPresent()){
                responseMap.put("user",UserDto.toUserDto(user.get()));
                responseMap.put("token",storeToken(user.get()));
                return responseMap;

             }
             responseMap.put("error", "data mismatch");
             return responseMap;
             
    
        }
    public boolean logoutUser(String token){
        UserDto user = userTokenService.getUserOfToken(token);
         if (user != null){
                Optional<PersonalAccesToken> pat = personalAccesTokenRepository.findFirstByToken(token);
                if(pat.isPresent()){
                    personalAccesTokenRepository.delete(pat.get());
                    return true;
                }
               

            }
      
            return false ;
    }
    public boolean forget(String email){
       Optional<User> user = userRepository.findByEmail(email);
       if(user.isPresent()){
            Optional<PasswordResetToken> checkPrt = passwordResetTokenRepository.findFirstByEmail(email);
            if(checkPrt.isPresent()){
                passwordResetTokenRepository.delete(checkPrt.get());
            }

            PasswordResetToken prt= new PasswordResetToken();
            int token = (int) (Math.random() * 900000) + 100000;
            prt.setEmail(email); 
            prt.setToken(token);
            passwordResetTokenRepository.save(prt);
            mailService.sendEmail(email,"Reset password Token","your reset password token : "+token);
            return true;
        
    

       }
       return false;
       

    }
    public Map<String,Object> reset(StorePasswordResetRequest request){
        Map<String, Object> responseMap = new HashMap<>();
        Optional<PasswordResetToken> checkPrt = passwordResetTokenRepository.findFirstByEmail(request.getEmail());
        if(checkPrt.isPresent()){
            if(checkPrt.get().getToken() == request.getToken()){
                passwordResetTokenRepository.delete(checkPrt.get());    
                Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
                User user = userOpt.get();
                user.setPassword(User.hashPassword(request.getPassword1()));
                userRepository.save(user);
                responseMap.put("user",UserDto.toUserDto(user));
                responseMap.put("token",storeToken(user));
                return responseMap;

               

            }


        }
        responseMap.put("error","Token mismatch");
        return responseMap;

        
    }
    // i can use this to check if the user logged in 
    public boolean verifyToken(String token){
         UserDto user = userTokenService.getUserOfToken(token);
         if (user != null){
              
                    return true; 

            }
      
            return false ;

    }
     public Map<String,Object> UserWithToken( String token){
            Map<String, Object> responseMap = new HashMap<>();
            UserDto user = userTokenService.getUserOfToken(token);
              if (user != null){
                responseMap.put("user", user);
                responseMap.put("message", "user fetched with success");
                return responseMap;
                  

            }
             responseMap.put("error", "User not found");
             return responseMap;
             
    
        }
    public List<UserDto> latestUsers(String token){
        UserDto user = userTokenService.getUserOfToken(token);
        if(user != null){
            if(user.isAdmin()){
                List<User> users = userRepository.findFirst7ByOrderByIdDesc();
                List<UserDto> newList = new ArrayList<>();
                for(User u:users){
                    newList.add(UserDto.toUserDto(u));
                }
                return newList;

            }
        }
        return null ;

    }
    

}
