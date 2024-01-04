package com.example.daret.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.daret.dtos.DaretDto;
import com.example.daret.dtos.UserDto;
import com.example.daret.models.Daret;
import com.example.daret.repositories.DaretRepository;
import com.example.daret.requests.StoreDaretRequest;

@Service
public class DaretService {
    private final DaretRepository daretRepository;
    private AuthService authService;
    private UserTokenService userTokenService;

    @Autowired
    public DaretService(DaretRepository daretRepository,AuthService authService,UserTokenService userTokenService){
        this.daretRepository = daretRepository;
        this.authService = authService;
        this.userTokenService = userTokenService;


    }

    public boolean createDaret(String token,StoreDaretRequest request){
     UserDto user = userTokenService.getUserOfToken(token);
     if(user != null){
        if(user.isAdmin()){
            daretRepository.save(Daret.toDaret(request));
            return true;
        }
     }
     return false;
    }

    public List<DaretDto> index(){
        List <DaretDto> daretsDto  = new ArrayList<DaretDto>();
        List <Daret> darets  = daretRepository.findAll();
        for(Daret daret : darets){
            daretsDto.add(DaretDto.toDaretDto(daret));
        }
        return daretsDto;
        

    }
    public DaretDto show(long id){
        Optional<Daret> daret = daretRepository.findFirstById(id);
        if(daret.isPresent()){
            return DaretDto.toDaretDto(daret.get());
        }
        return null ;


    }
    public boolean update(String token,long id ,StoreDaretRequest request){
        UserDto user = userTokenService.getUserOfToken(token);
        if(user != null){
            if(user.isAdmin()){
                Optional<Daret> daret = daretRepository.findFirstById(id);
                if(daret.isPresent()){
                    Daret newDaret = daret.get();
                    newDaret.setPrice(request.getPrice());
                    newDaret.setPNumber(request.getPnumber());
                    newDaret.setDuration(request.getDuration());
                    newDaret.setDType(request.getDtype());
                    daretRepository.save(newDaret);
                    return true;
                }

            }
           
            return false ;

        }
        return false;

       
    }

    public boolean delete(String token,long id){
        UserDto user = userTokenService.getUserOfToken(token);
        if(user != null){
            if(user.isAdmin()){
                Optional<Daret> daret = daretRepository.findFirstById(id);
                if(daret.isPresent()){
                    daretRepository.delete(daret.get());
                     return true ;
                }
               

            }
            return false;
        }
        return false;

    }



    
}