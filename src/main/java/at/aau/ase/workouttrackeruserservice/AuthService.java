package at.aau.ase.workouttrackeruserservice;

import java.util.Optional;

interface AuthService {

  Optional<OAuth2TokenResponse> getTokenFromAuthServer();

}
