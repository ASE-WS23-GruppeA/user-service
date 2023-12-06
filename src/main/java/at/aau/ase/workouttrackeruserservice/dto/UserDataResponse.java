package at.aau.ase.workouttrackeruserservice.dto;

import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for {@link at.aau.ase.workouttrackeruserservice.UserData}
 */
public record UserDataResponse(
    Long id,
    @Size(max = 20) @NotBlank String username,
    @Size(max = 50) @Email @NotBlank String email
) implements Serializable {}