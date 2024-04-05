package com.example.SpringAuth.DTO;

import lombok.Data;

import java.util.List;

@Data
public class AuthResponseDto {
    private String accessToken;
    private String tokenType = "Bearer ";
    public AuthResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
