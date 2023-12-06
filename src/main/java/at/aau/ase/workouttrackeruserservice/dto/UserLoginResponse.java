package at.aau.ase.workouttrackeruserservice.dto;

public record UserLoginResponse(
    Long id,
    String jwtToken
) {}
