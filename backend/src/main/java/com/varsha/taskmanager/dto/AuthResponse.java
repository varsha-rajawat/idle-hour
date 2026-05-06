package com.varsha.taskmanager.dto;

import java.util.UUID;

import com.varsha.taskmanager.model.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private UUID userId;
    private String name;
    private String email;
    private Role role;
}
