package com.biddingserver.security.entity;

import com.biddingserver.security.utility.Roles;
import lombok.Data;

import javax.management.relation.Role;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

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

    private boolean enabled = true;

    private boolean contactNumberVerified = false;
}
