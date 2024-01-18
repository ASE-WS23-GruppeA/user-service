package at.aau.ase.workouttrackeruserservice.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import at.aau.ase.workouttrackeruserservice.model.UserData;

/**
 * DTO for {@link UserData}
 */
public record UserLoginRequest(
    @Size(max = 20)
    @NotBlank(message = "Username is required")
    String username,
    @Size(max = 120)
    @NotBlank(message = "Password is required")
    String password
) implements Serializable {}