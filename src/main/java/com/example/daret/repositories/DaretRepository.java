package com.example.daret.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.daret.dtos.CountMonth;
import com.example.daret.dtos.GenralStatsDto;
import com.example.daret.models.Daret;

public interface DaretRepository extends JpaRepository<Daret, Long> {
    Optional<Daret> findFirstById(long id);
    Optional<Daret> findFirstByIdAndStatus(long id,String status);

    @Query("SELECT NEW com.example.daret.dtos.CountMonth(COUNT(d), MONTH(d.createdAt)) " +
           "FROM Daret d " +
           "WHERE YEAR(d.createdAt) = YEAR(CURRENT_DATE) " +
           "GROUP BY MONTH(d.createdAt)")
    List<CountMonth> getCountAndMonth();

    long count();

    
    

}
