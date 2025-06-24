package com.johnstondev.gamelog.dto;

/*
* Allow updates to ONLY username and email. NO password updates.
*/

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UpdateUserRequestDTO {

    @Size(min = 3, max = 25, message = "A username must be between 3 and 25 characters.")
    private String username;

    @Email(message = "Email must be valid.")
    private String email;

    public UpdateUserRequestDTO() {}

    public UpdateUserRequestDTO(String username, String email) {
        this.username = username;
        this.email = email;
    }

    // getters and setters :FIRE: :FIRE: :FIRE:

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
