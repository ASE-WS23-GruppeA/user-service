package at.aau.ase.workouttrackeruserservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


public record OAuth2TokenResponse(
    @JsonProperty("access_token") String accessToken,
    @JsonProperty("token_type") String tokenType,
    @JsonProperty("expires_in") int expiresIn
) {}
