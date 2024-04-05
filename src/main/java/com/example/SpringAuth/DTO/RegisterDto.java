package com.example.SpringAuth.DTO;

import com.example.SpringAuth.Model.RoleEntity;
import lombok.Data;

import java.util.List;

@Data
public class RegisterDto {
    private String username;
    private String password;
    private List<RoleEntity> roles;
}
