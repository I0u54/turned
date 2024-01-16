package com.example.daret.services;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.daret.dtos.CountMonth;
import com.example.daret.dtos.DaretDto;
import com.example.daret.dtos.GenralStatsDto;
import com.example.daret.dtos.UserDto;
import com.example.daret.models.Daret;
import com.example.daret.repositories.DaretRepository;
import com.example.daret.repositories.ParticipationRepository;
import com.example.daret.repositories.UserRepository;
import com.example.daret.requests.StoreDaretRequest;

@Service
public class DaretService {
    private final DaretRepository daretRepository;
    private AuthService authService;
    private UserTokenService userTokenService;
    private final UserRepository userRepository;
    private final ParticipationRepository participationRepository;
    

    @Autowired
    public DaretService(DaretRepository daretRepository,AuthService authService,UserTokenService userTokenService, UserRepository userRepository,ParticipationRepository participationRepository){
        this.daretRepository = daretRepository;
        this.authService = authService;
        this.userTokenService = userTokenService;
        this.userRepository = userRepository;
        this.participationRepository = participationRepository;


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
                Optional<Daret> daret =daretRepository.findFirstByIdAndStatus(id,"unactivated");
                if(daret.isPresent()){
                    long countParticipations = participationRepository.countByDaret(daret.get());
                    if(countParticipations <= 0 ){

                        Daret newDaret = daret.get();
                        newDaret.setPrice(request.getPrice());
                        newDaret.setPNumber(request.getPnumber());
                        newDaret.setDuration(request.getDuration());
                        newDaret.setDType(request.getDtype());
                        newDaret.setAvailable(request.getPnumber());
                        daretRepository.save(newDaret);
                        return true;
                    }
                    
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
                Optional<Daret> daret = daretRepository.findFirstByIdAndStatus(id,"unactivated");
                if(daret.isPresent()){
                    long countParticipations = participationRepository.countByDaret(daret.get());
                    if(countParticipations <= 0 ){

                        daretRepository.delete(daret.get());
                         return true ;
                    }

                }
               

            }
            return false;
        }
        return false;

    }
    public boolean endDaret(String token,long id){
        UserDto user = userTokenService.getUserOfToken(token);
        if(user != null){
            if(user.isAdmin()){
                Optional<Daret> daret = daretRepository.findById(id);
                if(daret.isPresent()){
                    Daret currentDaret = daret.get();
                    LocalDateTime expiredAt = currentDaret.getExpired_at().toLocalDateTime() ; 
                    if(Timestamp.valueOf(expiredAt).before(new Timestamp(System.currentTimeMillis()))){
                        currentDaret.setStatus("expired");
                        daretRepository.save(currentDaret);
                        return true;
                        
                    }

                }
               

            }
        }
        return false;

    }

// stats (i think khassni nheyed hadchi mn hna)
    public List<CountMonth> getCountAndMonth(String token) {
        UserDto user = userTokenService.getUserOfToken(token);
        if(user != null){
            if(user.isAdmin()){

                return daretRepository.getCountAndMonth();
            }

        }
        return null ;
    }
    public GenralStatsDto getGeneralStats(String token) {
        UserDto user = userTokenService.getUserOfToken(token);
        if(user != null){
            if(user.isAdmin()){
                GenralStatsDto genralStatsDto = new GenralStatsDto();
                genralStatsDto.setActiveUsers(userRepository.countActiveUsers());
                genralStatsDto.setUsersCount(userRepository.countUsers());
                genralStatsDto.setDaretsCount(daretRepository.count());


                return genralStatsDto;
            }

        }
        return null ;
    }


    
}
