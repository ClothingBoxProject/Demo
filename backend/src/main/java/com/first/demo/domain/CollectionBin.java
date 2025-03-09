package com.first.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class CollectionBin {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long binId;

    @Column(nullable = false, length = 100)
    private String locationName; // 수거함 별칭

    @Column(nullable = false)
    private double latitude; // 위도
    
    @Column(nullable = false)
    private double longitude; // 경도    

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }
}
