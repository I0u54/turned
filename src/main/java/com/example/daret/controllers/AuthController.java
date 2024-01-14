package com.example.daret.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.daret.dtos.GenralStatsDto;
import com.example.daret.dtos.UserDto;
import com.example.daret.models.User;
import com.example.daret.requests.StoreEmailRequest;
import com.example.daret.requests.StorePasswordResetRequest;
import com.example.daret.requests.StoreUserLoginRequest;
import com.example.daret.requests.StoreUserRequest;
import com.example.daret.services.AuthService;
import com.example.daret.services.MailService;
import com.example.daret.services.UserTokenService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class AuthController {

    private AuthService authService;
    private UserTokenService userTokenService;

    @Autowired
    public AuthController(AuthService authService, UserTokenService userTokenService) {
        this.authService = authService;
        this.userTokenService = userTokenService;
    }

    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody StoreUserRequest request) {
        Map<String, Object> result = authService.storeUser(request);

        if (request.passwordCheck()) {

            if (result.containsKey("user")) {

                return new ResponseEntity<>(result, HttpStatus.OK);
            }
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

        }
        result.remove("error");
        result.put("error", "the password does not match the password confirmation ");
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody StoreUserLoginRequest request) {
        Map<String, Object> result = authService.loginUser(request);
        if (result.containsKey("user")) {
            return new ResponseEntity<>(result, HttpStatus.OK);

        }
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/logout")
    public ResponseEntity logout(@RequestHeader("Authorization") String token) {
        boolean logout = authService.logoutUser(userTokenService.extractToken(token));
        Map<String, Object> result = new HashMap<>();

        if (logout) {
            result.put("message", "user logged out");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        result.put("error", "invalid token");
        return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/forget")
    public ResponseEntity forgetPassword(@Valid @RequestBody StoreEmailRequest request) {
        boolean fp = authService.forget(request.getEmail());
        if (fp) {

            return new ResponseEntity<>("token has been sent to your email adress", HttpStatus.OK);
        }
        return new ResponseEntity<>("this email do not exist in the database !", HttpStatus.BAD_REQUEST);

    }

    @PutMapping("/reset")
    public ResponseEntity resetPassword(@Valid @RequestBody StorePasswordResetRequest request) {
        Map<String, Object> result = authService.reset(request);
        if (request.passwordCheck()) {
            if (result.containsKey("user")) {

                return new ResponseEntity<>(result, HttpStatus.OK);
            }

            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

        }

        result.remove("error");
        result.put("error", "the password does not match the password confirmation ");
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/verify")
    public ResponseEntity verify(@RequestHeader("Authorization") String token) {
        boolean valid = authService.verifyToken(userTokenService.extractToken(token));
        if (valid) {
            return new ResponseEntity<>("AUTHORIZED", HttpStatus.OK);

        }
        return new ResponseEntity<>("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);

    }

    @GetMapping("/user")
    public ResponseEntity UserWithToken(@RequestHeader("Authorization") String token) {
        Map<String, Object> result = authService.UserWithToken(userTokenService.extractToken(token));
        if (result.containsKey("user")) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

    }

    @GetMapping("/latestUsers")
    public ResponseEntity latestUsers(@RequestHeader("Authorization") String token) {
        Map<String, Object> result = new HashMap<>();

        List<UserDto> myList = authService.latestUsers(userTokenService.extractToken(token));
        if (myList != null) {
            result.put("data", myList);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        result.put("error", "you have no permission to do this action ! ");
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

    }

}
