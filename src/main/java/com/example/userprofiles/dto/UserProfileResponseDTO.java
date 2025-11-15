package com.example.userprofiles.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String bio;
    private String avatarUrl;
    private String phone;
    private String location;
    private String website;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

