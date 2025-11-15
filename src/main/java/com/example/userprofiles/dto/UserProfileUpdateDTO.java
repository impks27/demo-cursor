package com.example.userprofiles.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileUpdateDTO {

    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z\\s\\-'\\.]+$", message = "Name can only contain letters, spaces, hyphens, apostrophes, and periods")
    private String name;

    @Email(message = "Email must be valid")
    @Size(max = 255, message = "Email must be less than 255 characters")
    private String email;

    @Size(max = 1000, message = "Bio must be less than 1000 characters")
    private String bio;

    @URL(message = "Avatar URL must be a valid URL")
    @Size(max = 500, message = "Avatar URL must be less than 500 characters")
    private String avatarUrl;

    @Pattern(regexp = "^[\\d\\s\\-\\(\\)\\+]+$", message = "Phone number contains invalid characters")
    @Size(min = 10, max = 20, message = "Phone number must be between 10 and 20 characters")
    private String phone;

    @Size(max = 100, message = "Location must be less than 100 characters")
    private String location;

    @URL(message = "Website must be a valid URL")
    @Size(max = 255, message = "Website must be less than 255 characters")
    private String website;
}

