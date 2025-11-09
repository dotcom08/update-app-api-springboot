package com.example.app.repositories;

import com.example.app.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Permission findByResourceAndOperation(String resource, String operation);
}
