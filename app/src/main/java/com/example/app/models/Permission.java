package com.example.app.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String resource;

    private String operation;

    @ManyToMany
    private Set<Role> roles;

    @Override
    public String getAuthority() {
        return String.format("%S:%S", resource.toUpperCase(), operation.toUpperCase());
    }

    public Permission(String string, String string2) {
        this.resource = string;
        this.operation = string2;
    }
}
