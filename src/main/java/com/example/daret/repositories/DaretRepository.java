package com.example.daret.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.daret.models.Daret;

public interface DaretRepository extends JpaRepository<Daret,Long> {
    Optional<Daret> findFirstById(long id);
    
}
