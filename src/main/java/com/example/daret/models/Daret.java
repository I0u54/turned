package com.example.daret.models;

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

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.daret.requests.StoreDaretRequest;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Daret {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id ;
    private float price ;
    private int pNumber;
    private float available;
    private String status = "unactivated";

    @Column(nullable = true)
    private Timestamp expired_at;

    private int duration ;
    private String dType;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    
    @OneToMany(mappedBy = "daret", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Participation> participations;
    
    public static Daret toDaret(StoreDaretRequest request){
        Daret daret = new Daret();
        daret.setPrice(request.getPrice());
        daret.setPNumber(request.getPnumber());
        daret.setAvailable(request.getPnumber());
        daret.setDuration(request.getDuration());
        daret.setDType(request.getDtype());
        return daret ; 

    }
}
