package at.aau.ase.workouttrackeruserservice;

import java.util.Optional;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.aau.ase.workouttrackeruserservice.dto.UserDataResponse;
import at.aau.ase.workouttrackeruserservice.dto.UserLoginRequest;
import at.aau.ase.workouttrackeruserservice.dto.UserLoginResponse;
import at.aau.ase.workouttrackeruserservice.dto.UserRegistrationRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

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
    if (userDataService.userExistsByUsername(userRegistrationRequest.username())) {
      return ResponseEntity.badRequest().build();
    }

    UserData user = userDataService.createUser(
        userRegistrationRequest.username(), userRegistrationRequest.password(), userRegistrationRequest.email()
    );

    var userDataResponse = new UserDataResponse(user.getId(), user.getUsername(), user.getEmail());

    return ResponseEntity.ok(userDataResponse);
  }

  @PostMapping("/login")
  public ResponseEntity<UserLoginResponse> login(
      @Valid @RequestBody UserLoginRequest userLoginRequest,
      HttpServletResponse response
  ) {
    if (Boolean.FALSE.equals(userDataService.isValidUser(userLoginRequest.username(), userLoginRequest.password()))) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    Optional<OAuth2TokenResponse> oAuth2TokenResponseOptional = authService.getTokenFromAuthServer();

    if (oAuth2TokenResponseOptional.isEmpty()) {
      return ResponseEntity.internalServerError().build();
    }

    OAuth2TokenResponse oAuth2TokenResponse = oAuth2TokenResponseOptional.get();
//    Cookie jwtCookie = new Cookie("JWT-TOKEN", oAuth2TokenResponse.getAccessToken());
//
//    jwtCookie.setHttpOnly(true);
//    jwtCookie.setPath("/");
//    jwtCookie.setSecure(false);
//    jwtCookie.setMaxAge(oAuth2TokenResponse.getExpiresIn());
//
//    response.addCookie(jwtCookie);

    Optional<UserData> userDataOptional = userDataService.findByUsername(userLoginRequest.username());

    if (userDataOptional.isEmpty()) {
      return ResponseEntity.internalServerError().build();
    }

    var userLoginResponse = new UserLoginResponse(
        userDataOptional.get().getId(), oAuth2TokenResponse.getAccessToken()
    );

    return ResponseEntity.ok(userLoginResponse);
  }

//  @PostMapping("/logout")
//  public ResponseEntity<Void> logout(HttpServletResponse response) {
//    Cookie jwtCookie = new Cookie("JWT-TOKEN", null);
//
//    jwtCookie.setHttpOnly(true);
//    jwtCookie.setMaxAge(0); // Expire the cookie immediately
//    jwtCookie.setPath("/");
//    jwtCookie.setSecure(false);
//
//    response.addCookie(jwtCookie);
//
//    return ResponseEntity.ok().build();
//  }

  @GetMapping("/protected")
  public ResponseEntity<String> protectedHey() {
    return ResponseEntity.ok("protectedHey\n\n");
  }

}
