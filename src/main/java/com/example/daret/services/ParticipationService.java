package com.example.daret.services;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.daret.dtos.ParticipationDto;
import com.example.daret.dtos.UserDto;
import com.example.daret.models.Daret;
import com.example.daret.models.Participation;
import com.example.daret.models.User;
import com.example.daret.repositories.DaretRepository;
import com.example.daret.repositories.ParticipationRepository;
import com.example.daret.repositories.UserRepository;
import com.example.daret.requests.StoreParticipationRequest;

@Service
public class ParticipationService {
    private UserTokenService userTokenService;
    private final DaretRepository daretRepository;
    private final ParticipationRepository participationRepository;
    private final UserRepository userRepository;

    @Autowired
    public ParticipationService(UserTokenService userTokenService, DaretRepository daretRepository,
            ParticipationRepository participationRepository, UserRepository userRepository) {
        this.userTokenService = userTokenService;
        this.daretRepository = daretRepository;
        this.participationRepository = participationRepository;
        this.userRepository = userRepository;
    }

    public boolean makeParticipation(String token, StoreParticipationRequest request) {
        UserDto user = userTokenService.getUserOfToken(token);
        if (user != null) {
            Optional<Daret> daret = daretRepository.findFirstById(request.getIdDaret());
            if (daret.isPresent()) {
                Daret currentDaret = daret.get();

                if (currentDaret.getAvailable() > request.getQuantity()) {
                    currentDaret.setAvailable(currentDaret.getAvailable() - request.getQuantity());
                    daretRepository.save(currentDaret);
                    Optional<User> currentUser = userRepository.findFirstById(user.getId());
                    participationRepository.save(Participation.toParticipation(request, currentDaret,
                            currentUser.get()));

                    return true;

                } else if (currentDaret.getAvailable() == request.getQuantity()) {
                    ChronoUnit chronoUnit = currentDaret.getDType().equals("month") ? ChronoUnit.MONTHS
                            : currentDaret.getDType().equals("week") ? ChronoUnit.WEEKS : ChronoUnit.DAYS;

                    currentDaret.setAvailable(currentDaret.getAvailable() - request.getQuantity());
                    Optional<User> currentUser = userRepository.findFirstById(user.getId());
                    participationRepository.save(Participation.toParticipation(request, currentDaret,
                            currentUser.get()));

                    List<Participation> allParticipations = participationRepository
                            .findByDaretOrderByCreatedAtAsc(currentDaret);
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    List<Participation> halfs = new ArrayList<>();
                    List<Participation> quarters = new ArrayList<>();

                    for (Participation p : allParticipations) {
                        if (p.getQuantity() >= 1) {
                            p.setPayDate(Timestamp.valueOf(currentDateTime));
                            currentDateTime = currentDateTime.plus(1, chronoUnit);

                        } else {
                            if (p.getQuantity() == 0.5) {
                                halfs.add(p);
                                if (halfs.size() == 2) {
                                    for (Participation h : halfs) {
                                        h.setPayDate(Timestamp.valueOf(currentDateTime));

                                    }
                                    currentDateTime = currentDateTime.plus(1, chronoUnit);
                                    participationRepository.saveAll(halfs);
                                    halfs.clear();

                                }
                            } else if (p.getQuantity() == 0.25) {
                                quarters.add(p);
                                if (quarters.size() == 4) {
                                    for (Participation h : quarters) {
                                        h.setPayDate(Timestamp.valueOf(currentDateTime));

                                    }
                                    currentDateTime = currentDateTime.plus(1, chronoUnit);
                                    participationRepository.saveAll(quarters);
                                    quarters.clear();

                                }

                            }
                        }

                    }
                    currentDaret.setStatus("activated");
                    LocalDateTime newCuDateTime = LocalDateTime.now();
                    currentDaret.setExpired_at(
                            Timestamp.valueOf(newCuDateTime.plus(currentDaret.getDuration() - 1, chronoUnit)));
                    participationRepository.saveAll(allParticipations);
                    daretRepository.save(currentDaret);

                    return true;

                }

            }

        }
        return false;

    }

    public List<Participation> userParticipation(String token) {

        UserDto user = userTokenService.getUserOfToken(token);
        if (user != null) {
            Optional<User> realUser = userRepository.findById(user.getId());

            List<Participation> participations = participationRepository.findByUserOrderByPayDateAsc(realUser.get());
            return participations;

        }
        return null;

    }
    public List<Participation> participations(String token) {

        UserDto user = userTokenService.getUserOfToken(token);
        if (user != null) {
            if(user.isAdmin()){

                List<Participation> participations = participationRepository.findFirst7ByOrderByIdDesc();
                return participations;
            }
           


        }
        return null;

    }

    public boolean reparticipate(String token, long id) {

        UserDto user = userTokenService.getUserOfToken(token);
        if (user != null) {
            Optional<Participation> participation = participationRepository.findFirstById(id);
            if (participation.isPresent()) {
                Participation currentParticipation = participation.get();
                Daret currentDaret = currentParticipation.getDaret();
                
                if (currentParticipation.getUser().getId() == user.getId()) {
                    ChronoUnit chronoUnit = currentDaret.getDType().equals("month") ? ChronoUnit.MONTHS
                    : currentDaret.getDType().equals("week") ? ChronoUnit.WEEKS : ChronoUnit.DAYS;

                    LocalDateTime lastCreatedAtDateTime = currentParticipation.getCreatedAt().toLocalDateTime();
                    LocalDateTime updatedDateTime = lastCreatedAtDateTime.plus(1, chronoUnit);
            
                    if (Timestamp.valueOf(updatedDateTime).before(new Timestamp(System.currentTimeMillis()))) {
                        currentParticipation.setCreatedAt(Timestamp.valueOf(updatedDateTime));
                        participationRepository.save(currentParticipation);
                        return true;
                    }
                }
            }

        }
        return false;
 
    }
    public void pay() {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        List<Participation> unpaidParticipations = participationRepository.findByPayedIsFalseAndPayDateLessThanEqual(currentTimestamp);
        for (Participation participation : unpaidParticipations) {
            participation.setPayed(true);
        }
        participationRepository.saveAll(unpaidParticipations);

        
    }
    public boolean deleteParticipation(String token,long id){
        
        UserDto user = userTokenService.getUserOfToken(token);
        if(user != null){
            if (user.isAdmin()){
                Optional<Participation> participation = participationRepository.findFirstById(id);
                if(participation.isPresent()){
                    Participation currentParticipation = participation.get();
                    if(currentParticipation.getDaret().getStatus() != "activated"){
                        participationRepository.delete(currentParticipation);
                        return true;
                    }
                }

            }
        }
        return false ;

    }
}
