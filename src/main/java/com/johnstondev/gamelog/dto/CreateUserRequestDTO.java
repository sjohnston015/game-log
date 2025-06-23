package com.johnstondev.gamelog.dto;

import jakarta.validation.constraints.*;

public class CreateUserRequestDTO {

    @NotBlank(message = "A username is required.")
    @Size(min = 3, max = 25, message = "A username must be between 3 and 25 characters.")
    private String username;

    @NotBlank(message = "An email is required.")
    @Email(message = "Email must be valid.")
    private String email;

    @NotBlank(message = "A password is required.")
    @Size(min = 12, message = "A password must be AT LEAST 12 characters long.")
    private String password;

    // default constructor for JSON
    public CreateUserRequestDTO() {}

    public CreateUserRequestDTO(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // getters and setters !!!

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
