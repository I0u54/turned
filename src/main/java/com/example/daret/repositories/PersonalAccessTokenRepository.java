package com.example.daret.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.example.daret.models.PersonalAccesToken;
import com.example.daret.models.User;

@Repository
public interface PersonalAccessTokenRepository extends JpaRepository<PersonalAccesToken,Long> {
    
    
    Optional<PersonalAccesToken> findFirstByToken(String token);
    List<PersonalAccesToken> findByUser(User user);




}
