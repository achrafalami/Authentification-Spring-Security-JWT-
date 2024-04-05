package com.example.SpringAuth.Repository;

import com.example.SpringAuth.Model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity,Integer> {
    RoleEntity findByName(String name);
}
