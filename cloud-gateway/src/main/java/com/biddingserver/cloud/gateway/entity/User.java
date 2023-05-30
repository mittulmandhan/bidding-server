package com.liquorstore.cloud.gateway.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String contactNumber;

    @Column(length = 60)
    private String password;

    private String role;

    private boolean enabled = false;

    private boolean contactNumberVerified = false;
}
