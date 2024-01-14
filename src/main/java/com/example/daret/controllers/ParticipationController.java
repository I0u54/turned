package com.example.daret.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.daret.dtos.ParticipationDto;
import com.example.daret.models.Participation;
import com.example.daret.requests.StoreDaretRequest;
import com.example.daret.requests.StoreParticipationRequest;
import com.example.daret.services.ParticipationService;
import com.example.daret.services.UserTokenService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class ParticipationController {

    private ParticipationService participationService;
    private UserTokenService userTokenService;

    @Autowired
    public ParticipationController(ParticipationService participationService, UserTokenService userTokenService) {

        this.participationService = participationService;
        this.userTokenService = userTokenService;
    }

    @PostMapping("participate")
    public ResponseEntity participate(@RequestHeader("Authorization") String token,
            @Valid @RequestBody StoreParticipationRequest request) {
        Map<String, Object> result = new HashMap<>();
        if (participationService.makeParticipation(userTokenService.extractToken(token), request)) {
            result.put("message", "you participated with success");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        result.put("error", "you have no permission to do this action !");
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

    }

    @GetMapping("participations")
    public ResponseEntity index(@RequestHeader("Authorization") String token) {
        Map<String, Object> result = new HashMap<>();
        List<Participation> participations = participationService
                .userParticipation(userTokenService.extractToken(token));
        if (participations != null) {

            result.put("participations", participations);
            return new ResponseEntity<>(result, HttpStatus.OK);

        }

        result.put("error", "user not found");
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }
    @GetMapping("allParticipations")
    public ResponseEntity allParticipations(@RequestHeader("Authorization") String token) {
        Map<String, Object> result = new HashMap<>();
        List<Participation> participations = participationService.participations(userTokenService.extractToken(token));
        if (participations != null) {

            result.put("participations", participations);
            return new ResponseEntity<>(result, HttpStatus.OK);

        }

        result.put("error", "unathorized action !");
        return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED );
    }

    @PostMapping("reparticipate/{id}")
    public ResponseEntity reparticipate(@RequestHeader("Authorization") String token, @PathVariable long id) {
        Map<String, Object> result = new HashMap<>();
        boolean reparticipate = participationService.reparticipate(userTokenService.extractToken(token), id);
        if (reparticipate != false) {

            result.put("message", "you reparticipated with success");
            return new ResponseEntity<>(result, HttpStatus.OK);

        }

        result.put("error", "unathorized action !");
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

    }

    @PutMapping("pay")
    public ResponseEntity payParticipants() {
        participationService.pay();
        Map<String, Object> result = new HashMap<>();
        result.put("message","payed");
        return new ResponseEntity<>(result, HttpStatus.OK);

    }
    
}
