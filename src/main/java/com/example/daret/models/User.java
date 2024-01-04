package com.example.daret.models;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.example.daret.requests.StoreUserRequest;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@Data
public class User {
     public User() { 
        this.isAdmin = false;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String lastName;
    private String email;
    private String password;
    private boolean isAdmin;


    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    
    public static String hashPassword(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes());
            byte[] hashedBytes = messageDigest.digest();

            StringBuilder hexStringBuilder = new StringBuilder();
            for (byte hashedByte : hashedBytes) {
                hexStringBuilder.append(String.format("%02x", hashedByte));
            }

            return hexStringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    public static User toUser(StoreUserRequest request){
        User user = new User();
        user.setName(request.getName());
        user.setLastName(request.getLastName());
    
        user.setEmail(request.getEmail());
        user.setPassword(hashPassword(request.getPassword1()));
        return user;
    }
   
    
}
