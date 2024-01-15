package com.example.daret.repositories;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.daret.models.Daret;
import com.example.daret.models.Participation;
import com.example.daret.models.User;

public interface ParticipationRepository extends JpaRepository<Participation,Long> {

    // long countByDaret(Daret daret);
    // Optional<Participation> findFirstByOrderByPayDateDesc();
    // long countByQuantityAndPayDateIsNullAndDaret(double quantity, Daret daret);
    // Optional<Participation> findFirstByDaretAndQuantityAndPayDateIsNull(Daret daret, double quantity);
    // Optional<Participation> findFirstByDaretAndPayDateIsNotNullOrderByPayDateDesc(Daret daret);
    // List<Participation> findByDaretAndQuantityAndPayDateIsNull(Daret daret, double quantity);
    List<Participation> findByDaretOrderByCreatedAtAsc(Daret daret);
    Long countByDaret(Daret daret);
    List<Participation> findByUserOrderByPayDateAsc(User user);

    Optional<Participation> findFirstById(long id);

    List<Participation> findByPayedIsFalseAndPayDateLessThanEqual(Timestamp currentTimestamp);
    List<Participation> findFirst7ByOrderByIdDesc();

   


    
}
