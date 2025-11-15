package com.example.userprofiles;

import com.example.userprofiles.dto.UserProfileCreateDTO;
import com.example.userprofiles.dto.UserProfileUpdateDTO;
import com.example.userprofiles.model.UserProfile;
import com.example.userprofiles.repository.UserProfileRepository;
import com.example.userprofiles.service.UserProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserProfileServiceTest {

    @Autowired
    private UserProfileService service;

    @Autowired
    private UserProfileRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void testCreateProfile() {
        UserProfileCreateDTO createDTO = new UserProfileCreateDTO();
        createDTO.setName("John Doe");
        createDTO.setEmail("john@example.com");
        createDTO.setBio("Test bio");

        var response = service.createProfile(createDTO);

        assertNotNull(response.getId());
        assertEquals("John Doe", response.getName());
        assertEquals("john@example.com", response.getEmail());
    }

    @Test
    void testCreateProfileDuplicateEmail() {
        UserProfile profile = new UserProfile();
        profile.setName("John Doe");
        profile.setEmail("john@example.com");
        repository.save(profile);

        UserProfileCreateDTO createDTO = new UserProfileCreateDTO();
        createDTO.setName("Jane Doe");
        createDTO.setEmail("john@example.com");

        assertThrows(RuntimeException.class, () -> service.createProfile(createDTO));
    }

    @Test
    void testGetProfileById() {
        UserProfile profile = new UserProfile();
        profile.setName("John Doe");
        profile.setEmail("john@example.com");
        UserProfile saved = repository.save(profile);

        var response = service.getProfileById(saved.getId());

        assertNotNull(response);
        assertEquals(saved.getId(), response.getId());
        assertEquals("John Doe", response.getName());
    }

    @Test
    void testGetProfileNotFound() {
        assertThrows(RuntimeException.class, () -> service.getProfileById(999L));
    }

    @Test
    void testUpdateProfile() {
        UserProfile profile = new UserProfile();
        profile.setName("John Doe");
        profile.setEmail("john@example.com");
        profile.setBio("Old bio");
        UserProfile saved = repository.save(profile);

        UserProfileUpdateDTO updateDTO = new UserProfileUpdateDTO();
        updateDTO.setName("John Updated");
        updateDTO.setBio("New bio");

        var response = service.updateProfile(saved.getId(), updateDTO);

        assertEquals("John Updated", response.getName());
        assertEquals("New bio", response.getBio());
        assertEquals("john@example.com", response.getEmail());
    }

    @Test
    void testDeleteProfile() {
        UserProfile profile = new UserProfile();
        profile.setName("John Doe");
        profile.setEmail("john@example.com");
        UserProfile saved = repository.save(profile);

        service.deleteProfile(saved.getId());

        assertFalse(repository.existsById(saved.getId()));
    }

    @Test
    void testPhoneValidation() {
        UserProfileCreateDTO createDTO = new UserProfileCreateDTO();
        createDTO.setName("John Doe");
        createDTO.setEmail("john@example.com");
        createDTO.setPhone("123"); // Too short

        assertThrows(RuntimeException.class, () -> service.createProfile(createDTO));
    }
}

