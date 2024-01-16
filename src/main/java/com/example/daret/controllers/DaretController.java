package com.example.daret.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.daret.dtos.CountMonth;
import com.example.daret.dtos.DaretDto;
import com.example.daret.dtos.GenralStatsDto;
import com.example.daret.models.Daret;
import com.example.daret.requests.StoreDaretRequest;
import com.example.daret.services.DaretService;
import com.example.daret.services.UserTokenService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class DaretController {
    private DaretService daretService;
    private UserTokenService userTokenService;
    

    @Autowired
    public DaretController(DaretService daretService, UserTokenService userTokenService) {
        this.daretService = daretService;
        this.userTokenService = userTokenService;

    }

    @PostMapping("/storeDaret")
    public ResponseEntity store(@RequestHeader("Authorization") String token,
            @Valid @RequestBody StoreDaretRequest request) {
        Map<String, Object> result = new HashMap<>();

        if (daretService.createDaret(userTokenService.extractToken(token), request)) {
            result.put("message", "Daret has been inserted with success ");
            return new ResponseEntity<>(result, HttpStatus.OK);

        }
        result.put("error", "you have no permission to do this action ! ");
        return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);

    }

    @GetMapping("/darets")
    public ResponseEntity index() {
        Map<String, Object> result = new HashMap<>();
        result.put("darets", daretService.index());
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @GetMapping("/darets/{id}")
    public ResponseEntity show(@PathVariable long id) {
        Map<String, Object> result = new HashMap<>();
        DaretDto daret = daretService.show(id);
        if (daret != null) {

            result.put("daret", daret);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.put("error", "daret not found ");
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);

        }

    }

    @PutMapping("darets_update/{id}")
    public ResponseEntity updaEntity(@RequestHeader("Authorization") String token, @PathVariable long id,
            @Valid @RequestBody StoreDaretRequest request) {
        Map<String, Object> result = new HashMap<>();
        if (daretService.update(userTokenService.extractToken(token), id, request)) {
            result.put("message", "Daret has been updated with success ");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        result.put("error", "you have no permission to do this action ! ");
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

    }

    @DeleteMapping("daretDelete/{id}")
    public ResponseEntity destroyEntity(@RequestHeader("Authorization") String token, @PathVariable long id) {
        Map<String, Object> result = new HashMap<>();
        if (daretService.delete(userTokenService.extractToken(token), id)) {
            result.put("message", "Daret has been deleted with success ");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        result.put("error", "you have no permission to do this action ! ");
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

    }
    @PostMapping("endDaret/{id}")
    public ResponseEntity endDaret(@RequestHeader("Authorization") String token, @PathVariable long id) {
        Map<String, Object> result = new HashMap<>();
        if (daretService.endDaret(userTokenService.extractToken(token), id)) {
            result.put("message", "Daret has been ended with success ");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        result.put("error", "you have no permission to do this action ! ");
        return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);

    }
    

    @GetMapping("/daretsPerMonth")
    public ResponseEntity daretsPerMonth(@RequestHeader("Authorization") String token) {
        Map<String, Object> result = new HashMap<>();

        List<CountMonth> myList = daretService.getCountAndMonth(userTokenService.extractToken(token));
        if (myList != null) {
            result.put("data", myList);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        result.put("error", "you have no permission to do this action ! ");
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

    }
    @GetMapping("/generalStats")
    public ResponseEntity generalStats(@RequestHeader("Authorization") String token) {
        Map<String, Object> result = new HashMap<>();

        GenralStatsDto myList = daretService.getGeneralStats(userTokenService.extractToken(token));
        if (myList != null) {
            result.put("data", myList);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        result.put("error", "you have no permission to do this action ! ");
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

    }
    
}