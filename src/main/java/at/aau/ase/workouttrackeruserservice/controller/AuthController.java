package at.aau.ase.workouttrackeruserservice.controller;

import java.util.Optional;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.aau.ase.workouttrackeruserservice.dto.OAuth2TokenResponse;
import at.aau.ase.workouttrackeruserservice.dto.UserDataResponse;
import at.aau.ase.workouttrackeruserservice.dto.UserLoginRequest;
import at.aau.ase.workouttrackeruserservice.dto.UserLoginResponse;
import at.aau.ase.workouttrackeruserservice.dto.UserRegistrationRequest;
import at.aau.ase.workouttrackeruserservice.model.UserData;
import at.aau.ase.workouttrackeruserservice.service.AuthService;
import at.aau.ase.workouttrackeruserservice.service.UserDataService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

  private final UserDataService userDataService;
  private final AuthService authService;

  public AuthController(UserDataService userDataService, AuthService authService) {
    this.userDataService = userDataService;
    this.authService = authService;
  }

  @PostMapping("/register")
  public ResponseEntity<UserDataResponse> register(
      @Valid @RequestBody UserRegistrationRequest userRegistrationRequest
  ) {
    String username = userRegistrationRequest.username();
    String email = userRegistrationRequest.email();

    LOGGER.info("Got registration request for user - [userName={}, email={}]", username, email);

    if (userDataService.userExistsByUsernameOrEmail(username, email)) {
      LOGGER.warn("User already exists; return 409 - [userName={}, email={}]", username, email);

      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    UserData user = userDataService.createUser(username, userRegistrationRequest.password(), email);
    var userDataResponse = new UserDataResponse(user.getId(), user.getUsername(), user.getEmail());

    LOGGER.info("Successfully created user - [userId={}, userName={}, email={}]",
        user.getId(), user.getUsername(), user.getEmail()
    );

    return ResponseEntity.ok(userDataResponse);
  }

  @PostMapping("/login")
  public ResponseEntity<UserLoginResponse> login(@Valid @RequestBody UserLoginRequest userLoginRequest) {
    String username = userLoginRequest.username();
    String password = userLoginRequest.password();

    LOGGER.info("Got login request for user - [userName={}]", username);

    if (Boolean.FALSE.equals(userDataService.isValidUser(username, password))) {
      LOGGER.warn("Invalid credentials for user; return 401 - [userName={}]", username);

      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Optional<OAuth2TokenResponse> oAuth2TokenResponseOptional = authService.getTokenFromAuthServer();

    if (oAuth2TokenResponseOptional.isEmpty()) {
      LOGGER.warn("Could not get token from auth server; return 500");

      return ResponseEntity.internalServerError().build();
    }

    OAuth2TokenResponse oAuth2TokenResponse = oAuth2TokenResponseOptional.get();
    Optional<UserData> userDataOptional = userDataService.findByUsername(username);

    if (userDataOptional.isEmpty()) {
      LOGGER.warn("Could not find user data; return 500 - [userName={}]", username);

      return ResponseEntity.internalServerError().build();
    }

    Long userId = userDataOptional.get().getId();
    String accessToken = oAuth2TokenResponse.accessToken();
    var userLoginResponse = new UserLoginResponse(userId, accessToken);

    LOGGER.info("Successfully logged in user - [userId={}, userName={}]", userId, username);

    return ResponseEntity.ok(userLoginResponse);
  }

  @GetMapping("/protected")
  public ResponseEntity<String> protectedEndpoint() {
    return ResponseEntity.ok("Protected Hello World!");
  }

}
