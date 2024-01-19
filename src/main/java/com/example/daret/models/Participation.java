package com.example.daret.models;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.daret.requests.StoreParticipationRequest;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Participation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id ;

    @ManyToOne
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
     @JsonManagedReference
    private Daret daret;
    private float quantity;
    

    @Column(nullable = true)
    private Timestamp payDate;
    private int payCount = 1;
    private boolean payed = false ;

    
    


    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public static Participation toParticipation(StoreParticipationRequest request,Daret daret,User user){
        Participation participation = new Participation();
        participation.setQuantity(request.getQuantity());
        participation.setUser(user);
        participation.setDaret(daret);
        // ChronoUnit chronoUnit = daret.getDType().equals("month") ? ChronoUnit.MONTHS :daret.getDType().equals("week") ? ChronoUnit.WEEKS :  ChronoUnit.DAYS;  

        // if(isFirst){
        //     if(isUnderOne){
        //         participation.setPayDate(null);

        //     }else{
        //         LocalDateTime currentDateTime = LocalDateTime.now();
        //         participation.setPayDate(Timestamp.valueOf(currentDateTime.plus(1,chronoUnit)));

        //     }

           
        // }
        // else{
        //     if(isUnderOne){
        //         if(lastParticipation.length > 0 && lastParticipation[0] != null){
        //             LocalDateTime lastParticipationDateTime = lastParticipation[0].toLocalDateTime();
    
        //             participation.setPayDate(Timestamp.valueOf(lastParticipationDateTime.plus(1,chronoUnit)));

        //         }else{
        //             LocalDateTime currentDateTime = LocalDateTime.now();
        //             participation.setPayDate(Timestamp.valueOf(currentDateTime.plus(1,chronoUnit)));

        //         }
                


        //     }
        //     else{
        //         if(lastParticipation.length > 0 && lastParticipation[0] != null){

        //             LocalDateTime lastParticipationDateTime = lastParticipation[0].toLocalDateTime();
        //             participation.setPayDate(Timestamp.valueOf(lastParticipationDateTime.plus(1,chronoUnit)));
        //         }else{
        //             LocalDateTime currentDateTime = LocalDateTime.now();
        //             participation.setPayDate(Timestamp.valueOf(currentDateTime.plus(1,chronoUnit)));

        //         }
        //     }
            
           


        // }
        return participation;

    

    }

}
