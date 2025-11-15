package com.example.userprofiles.service;

import com.example.userprofiles.dto.UserProfileCreateDTO;
import com.example.userprofiles.dto.UserProfileResponseDTO;
import com.example.userprofiles.dto.UserProfileUpdateDTO;
import com.example.userprofiles.model.UserProfile;
import com.example.userprofiles.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository repository;

    public List<UserProfileResponseDTO> getAllProfiles(int skip, int limit) {
        return repository.findAll()
                .stream()
                .skip(skip)
                .limit(limit)
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UserProfileResponseDTO getProfileById(Long id) {
        UserProfile profile = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + id));
        return toResponseDTO(profile);
    }

    @Transactional
    public UserProfileResponseDTO createProfile(UserProfileCreateDTO createDTO) {
        // Check if email already exists
        if (repository.existsByEmail(createDTO.getEmail())) {
            throw new RuntimeException("Email already exists: " + createDTO.getEmail());
        }

        // Validate phone number format
        validatePhoneNumber(createDTO.getPhone());

        UserProfile profile = toEntity(createDTO);
        UserProfile savedProfile = repository.save(profile);
        return toResponseDTO(savedProfile);
    }

    @Transactional
    public UserProfileResponseDTO updateProfile(Long id, UserProfileUpdateDTO updateDTO) {
        UserProfile profile = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + id));

        // Check if email is being changed and if it already exists
        if (updateDTO.getEmail() != null && !updateDTO.getEmail().equals(profile.getEmail())) {
            if (repository.existsByEmail(updateDTO.getEmail())) {
                throw new RuntimeException("Email already exists: " + updateDTO.getEmail());
            }
        }

        // Validate phone number if provided
        if (updateDTO.getPhone() != null) {
            validatePhoneNumber(updateDTO.getPhone());
        }

        // Update fields
        if (updateDTO.getName() != null) profile.setName(updateDTO.getName());
        if (updateDTO.getEmail() != null) profile.setEmail(updateDTO.getEmail());
        if (updateDTO.getBio() != null) profile.setBio(updateDTO.getBio());
        if (updateDTO.getAvatarUrl() != null) profile.setAvatarUrl(updateDTO.getAvatarUrl());
        if (updateDTO.getPhone() != null) profile.setPhone(updateDTO.getPhone());
        if (updateDTO.getLocation() != null) profile.setLocation(updateDTO.getLocation());
        if (updateDTO.getWebsite() != null) profile.setWebsite(updateDTO.getWebsite());

        UserProfile updatedProfile = repository.save(profile);
        return toResponseDTO(updatedProfile);
    }

    @Transactional
    public void deleteProfile(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Profile not found with id: " + id);
        }
        repository.deleteById(id);
    }

    public List<UserProfileResponseDTO> searchProfiles(String name, String email, String location) {
        List<UserProfile> profiles = repository.findAll();
        
        return profiles.stream()
                .filter(profile -> {
                    boolean matches = true;
                    if (name != null && !name.isEmpty()) {
                        matches = matches && profile.getName().toLowerCase().contains(name.toLowerCase());
                    }
                    if (email != null && !email.isEmpty()) {
                        matches = matches && profile.getEmail().toLowerCase().contains(email.toLowerCase());
                    }
                    if (location != null && !location.isEmpty()) {
                        matches = matches && profile.getLocation() != null && 
                                  profile.getLocation().toLowerCase().contains(location.toLowerCase());
                    }
                    return matches;
                })
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private void validatePhoneNumber(String phone) {
        if (phone != null && !phone.isEmpty()) {
            String digitsOnly = phone.replaceAll("\\D", "");
            if (digitsOnly.length() < 10 || digitsOnly.length() > 15) {
                throw new RuntimeException("Phone number must be between 10 and 15 digits");
            }
        }
    }

    private UserProfile toEntity(UserProfileCreateDTO dto) {
        UserProfile profile = new UserProfile();
        profile.setName(dto.getName());
        profile.setEmail(dto.getEmail().toLowerCase().trim());
        profile.setBio(dto.getBio());
        profile.setAvatarUrl(dto.getAvatarUrl());
        profile.setPhone(dto.getPhone());
        profile.setLocation(dto.getLocation());
        profile.setWebsite(dto.getWebsite());
        return profile;
    }

    private UserProfileResponseDTO toResponseDTO(UserProfile profile) {
        UserProfileResponseDTO dto = new UserProfileResponseDTO();
        dto.setId(profile.getId());
        dto.setName(profile.getName());
        dto.setEmail(profile.getEmail());
        dto.setBio(profile.getBio());
        dto.setAvatarUrl(profile.getAvatarUrl());
        dto.setPhone(profile.getPhone());
        dto.setLocation(profile.getLocation());
        dto.setWebsite(profile.getWebsite());
        dto.setCreatedAt(profile.getCreatedAt());
        dto.setUpdatedAt(profile.getUpdatedAt());
        return dto;
    }
}

