package com.edoride.user.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String keycloakId;
    private String email;
    private String name;
    
    @Enumerated(EnumType.STRING)
    private UserRole role;
    
    private String vehicleType;
    private Double reliabilityScore = 5.0;
}
