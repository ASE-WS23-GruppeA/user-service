package at.aau.ase.workouttrackeruserservice.service;

import java.util.Optional;

import at.aau.ase.workouttrackeruserservice.dto.OAuth2TokenResponse;

public interface AuthService {

  Optional<OAuth2TokenResponse> getTokenFromAuthServer();

}
