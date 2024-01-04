package com.example.daret.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.daret.models.PasswordResetToken;


@Repository
public interface  PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Integer>{
    Optional <PasswordResetToken>findFirstByEmail(String email);
}
