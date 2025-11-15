package com.example.userprofiles.controller;

import com.example.userprofiles.dto.UserProfileCreateDTO;
import com.example.userprofiles.dto.UserProfileResponseDTO;
import com.example.userprofiles.dto.UserProfileUpdateDTO;
import com.example.userprofiles.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
@Validated
public class UserProfileController {

    private final UserProfileService profileService;

    @GetMapping
    public ResponseEntity<List<UserProfileResponseDTO>> getAllProfiles(
            @RequestParam(defaultValue = "0") int skip,
            @RequestParam(defaultValue = "100") int limit) {
        List<UserProfileResponseDTO> profiles = profileService.getAllProfiles(skip, limit);
        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponseDTO> getProfile(@PathVariable Long id) {
        try {
            UserProfileResponseDTO profile = profileService.getProfileById(id);
            return ResponseEntity.ok(profile);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    @PostMapping
    public ResponseEntity<?> createProfile(@Valid @RequestBody UserProfileCreateDTO createDTO) {
        try {
            UserProfileResponseDTO profile = profileService.createProfile(createDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(profile);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(
            @PathVariable Long id,
            @Valid @RequestBody UserProfileUpdateDTO updateDTO) {
        try {
            UserProfileResponseDTO profile = profileService.updateProfile(id, updateDTO);
            return ResponseEntity.ok(profile);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfile(@PathVariable Long id) {
        try {
            profileService.deleteProfile(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}

