package com.example.userprofiles;

import com.example.userprofiles.dto.UserProfileCreateDTO;
import com.example.userprofiles.dto.UserProfileUpdateDTO;
import com.example.userprofiles.model.UserProfile;
import com.example.userprofiles.repository.UserProfileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
class UserProfileControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserProfileRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        repository.deleteAll();
    }

    @Test
    void testCreateProfileSuccess() throws Exception {
        UserProfileCreateDTO createDTO = new UserProfileCreateDTO();
        createDTO.setName("John Doe");
        createDTO.setEmail("john.doe@example.com");
        createDTO.setBio("Software developer");
        createDTO.setPhone("1234567890");
        createDTO.setLocation("New York");
        createDTO.setWebsite("https://johndoe.com");

        mockMvc.perform(post("/api/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.bio").value("Software developer"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testCreateProfileDuplicateEmail() throws Exception {
        // Create first profile
        UserProfile profile = new UserProfile();
        profile.setName("John Doe");
        profile.setEmail("john@example.com");
        repository.save(profile);

        // Try to create another with same email
        UserProfileCreateDTO createDTO = new UserProfileCreateDTO();
        createDTO.setName("Jane Doe");
        createDTO.setEmail("john@example.com");

        mockMvc.perform(post("/api/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Email already exists: john@example.com"));
    }

    @Test
    void testCreateProfileInvalidEmail() throws Exception {
        UserProfileCreateDTO createDTO = new UserProfileCreateDTO();
        createDTO.setName("John Doe");
        createDTO.setEmail("invalid-email");

        mockMvc.perform(post("/api/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateProfileShortName() throws Exception {
        UserProfileCreateDTO createDTO = new UserProfileCreateDTO();
        createDTO.setName("J");
        createDTO.setEmail("john@example.com");

        mockMvc.perform(post("/api/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllProfiles() throws Exception {
        // Create some profiles
        UserProfile profile1 = new UserProfile();
        profile1.setName("John Doe");
        profile1.setEmail("john@example.com");
        repository.save(profile1);

        UserProfile profile2 = new UserProfile();
        profile2.setName("Jane Doe");
        profile2.setEmail("jane@example.com");
        repository.save(profile2);

        mockMvc.perform(get("/api/profiles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetProfileById() throws Exception {
        UserProfile profile = new UserProfile();
        profile.setName("John Doe");
        profile.setEmail("john@example.com");
        UserProfile saved = repository.save(profile);

        mockMvc.perform(get("/api/profiles/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testGetProfileNotFound() throws Exception {
        mockMvc.perform(get("/api/profiles/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateProfile() throws Exception {
        UserProfile profile = new UserProfile();
        profile.setName("John Doe");
        profile.setEmail("john@example.com");
        profile.setBio("Old bio");
        UserProfile saved = repository.save(profile);

        UserProfileUpdateDTO updateDTO = new UserProfileUpdateDTO();
        updateDTO.setName("John Updated");
        updateDTO.setBio("New bio");

        mockMvc.perform(put("/api/profiles/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Updated"))
                .andExpect(jsonPath("$.bio").value("New bio"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void testUpdateProfileNotFound() throws Exception {
        UserProfileUpdateDTO updateDTO = new UserProfileUpdateDTO();
        updateDTO.setName("Updated Name");

        mockMvc.perform(put("/api/profiles/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteProfile() throws Exception {
        UserProfile profile = new UserProfile();
        profile.setName("John Doe");
        profile.setEmail("john@example.com");
        UserProfile saved = repository.save(profile);

        mockMvc.perform(delete("/api/profiles/" + saved.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/profiles/" + saved.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteProfileNotFound() throws Exception {
        mockMvc.perform(delete("/api/profiles/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPhoneValidation() throws Exception {
        UserProfileCreateDTO createDTO = new UserProfileCreateDTO();
        createDTO.setName("John Doe");
        createDTO.setEmail("john@example.com");
        createDTO.setPhone("123"); // Too short

        mockMvc.perform(post("/api/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isBadRequest());
    }
}

