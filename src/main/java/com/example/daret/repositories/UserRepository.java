package com.example.daret.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.daret.models.User;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    @Query(value = "SELECT u.id, u.name, u.last_name, u.email, u.password, u.is_admin, u.created_at, u.updated_at " +
    "FROM user u " +
    "INNER JOIN personal_acces_token pat ON u.id = pat.user_id " +
    "WHERE pat.token = :token LIMIT 1", nativeQuery = true)
    Optional<User> findUserByToken(@Param("token") String token);
    Optional<User> findByEmailAndPassword(String email, String password);
    Optional<User> findFirstById(long id);

   
    @Query("SELECT COUNT(u) FROM User u ")
    Long countUsers(); 
    
    @Query(value = "SELECT COUNT(u.id) FROM User u WHERE u.id IN (SELECT user_id FROM participation)", nativeQuery = true)
    Long countActiveUsers();

    List<User> findFirst7ByOrderByIdDesc();
   // List<User> findFirst100ByOrderByIdDesc();


}
