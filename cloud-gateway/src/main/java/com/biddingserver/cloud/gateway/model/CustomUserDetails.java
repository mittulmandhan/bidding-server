package com.liquorstore.cloud.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserDetails {
    private String username;

    private Set<CustomGrantedAuthority> authorities;

    private boolean enabled;

}
