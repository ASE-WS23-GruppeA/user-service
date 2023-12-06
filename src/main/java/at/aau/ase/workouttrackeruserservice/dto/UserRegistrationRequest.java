package at.aau.ase.workouttrackeruserservice.dto;

import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import at.aau.ase.workouttrackeruserservice.UserData;

/**
 * DTO for {@link UserData}
 */
public record UserRegistrationRequest(
    @Size(max = 20)
    @NotBlank(message = "Username is required")
    String username,
    @Size(max = 120)
    @NotBlank(message = "Password is required")
    String password,
    @Size(max = 50)
    @Email
    @NotBlank(message = "Email is required")
    String email
) implements Serializable {}