package at.aau.ase.workouttrackeruserservice.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.util.Optional;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import at.aau.ase.workouttrackeruserservice.dto.OAuth2TokenResponse;
import at.aau.ase.workouttrackeruserservice.dto.UserLoginRequest;
import at.aau.ase.workouttrackeruserservice.dto.UserRegistrationRequest;
import at.aau.ase.workouttrackeruserservice.repository.UserDataRepository;
import at.aau.ase.workouttrackeruserservice.service.AuthService;
import at.aau.ase.workouttrackeruserservice.service.UserDataService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest {

  private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

  @Autowired
  private UserDataService userDataService;

  @Autowired
  private UserDataRepository userDataRepository;

  @MockBean
  private AuthService authService;

  @LocalServerPort
  private Integer port;

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }

  @BeforeAll
  static void beforeAll() {
    postgres.start();
  }

  @AfterAll
  static void afterAll() {
    postgres.stop();
  }

  @BeforeEach
  void setUp() {
    RestAssured.baseURI = "http://localhost:%d".formatted(port);
    userDataRepository.deleteAll();
  }

  @AfterEach
  void tearDown() {
  }


  @Nested
  @DisplayName("When /api/auth/register is called")
  class Register {

    @Test
    @DisplayName("Given a valid request, it should return 200")
    void shouldReturn200_whenValidRequest() {
      given()
          .contentType(ContentType.JSON)
          .body(new UserRegistrationRequest("test", "password", "t@t.com"))
          .when()
          .post("/api/auth/register")
          .then()
          .statusCode(HttpStatus.OK.value())
          .body("username", equalTo("test"))
          .body("email", equalTo("t@t.com"))
          .log().all();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Given an invalid request username, it should return 400")
    void shouldReturn400_whenInvalidRequestUsername(String username) {
      given()
          .contentType(ContentType.JSON)
          .body(new UserRegistrationRequest(username, "password", "t@t.com"))
          .when()
          .post("/api/auth/register")
          .then()
          .statusCode(HttpStatus.BAD_REQUEST.value())
          .log().all();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Given an invalid request passwords, it should return 400")
    void shouldReturn400_whenInvalidRequestPassword(String password) {
      given()
          .contentType(ContentType.JSON)
          .body(new UserRegistrationRequest("test", password, "t@t.com"))
          .when()
          .post("/api/auth/register")
          .then()
          .statusCode(HttpStatus.BAD_REQUEST.value())
          .log().all();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Given an invalid request password, it should return 400")
    void shouldReturn400_whenInvalidRequestEmail(String email) {
      given()
          .contentType(ContentType.JSON)
          .body(new UserRegistrationRequest("test", "password", email))
          .when()
          .post("/api/auth/register")
          .then()
          .statusCode(HttpStatus.BAD_REQUEST.value())
          .log().all();
    }

    @Test
    @DisplayName("Given an existing user, it should return 409")
    void shouldReturn409_whenExistingUser() {
      userDataService.createUser("test", "password", "t@t.com");

      given()
          .contentType(ContentType.JSON)
          .body(new UserRegistrationRequest("test", "password", "t@t.com"))
          .when()
          .post("/api/auth/register")
          .then()
          .statusCode(HttpStatus.CONFLICT.value())
          .log().all();
    }

  }

  @Nested
  @DisplayName("When /api/auth/login is called")
  class Login {

    @Test
    @DisplayName("Given a valid request and user exists, it should return 200")
    void shouldReturn200_whenValidRequestAndUserExists() {
      var mockTokenResponse = Optional.of(new OAuth2TokenResponse("mock-token", "mock-type", 0));

      userDataService.createUser("test", "password", "t@t.com");
      Mockito.when(authService.getTokenFromAuthServer())
          .thenReturn(mockTokenResponse);

      given()
          .contentType(ContentType.JSON)
          .body(new UserLoginRequest("test", "password"))
          .when()
          .post("/api/auth/login")
          .then()
          .statusCode(HttpStatus.OK.value())
          .body("jwtToken", equalTo("mock-token"))
          .log().all();
    }

    @Test
    @DisplayName("Given a valid request and user does not exist, it should return 401")
    void shouldReturn401_whenValidRequestAndUserDoesNotExist() {
      var mockTokenResponse = Optional.of(new OAuth2TokenResponse("mock-token", "mock-type", 0));

      Mockito.when(authService.getTokenFromAuthServer()).thenReturn(mockTokenResponse);

      given()
          .contentType(ContentType.JSON)
          .body(new UserLoginRequest("test", "password"))
          .when()
          .post("/api/auth/login")
          .then()
          .statusCode(HttpStatus.UNAUTHORIZED.value())
          .log().all();
    }

    @Test
    @DisplayName("Given a valid request and user exists but JWT is empty, it should return 500")
    void shouldReturn500_whenValidRequestAndJwtEmpty() {
      userDataService.createUser("test", "password", "t@t.com");

      given()
          .contentType(ContentType.JSON)
          .body(new UserLoginRequest("test", "password"))
          .when()
          .post("/api/auth/login")
          .then()
          .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
          .log().all();
    }

  }

}