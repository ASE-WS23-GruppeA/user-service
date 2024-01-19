package at.aau.ase.workouttrackeruserservice.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import at.aau.ase.workouttrackeruserservice.dto.OAuth2TokenResponse;
import at.aau.ase.workouttrackeruserservice.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

  @Value("${api.api-gateway.base-url}")
  private String apiGatewayBaseUrl;

  @Override
  public Optional<OAuth2TokenResponse> getTokenFromAuthServer() {
    RestTemplate restTemplate = new RestTemplate();

    String authServerUrl = apiGatewayBaseUrl + "/oauth2/token";
    String clientId = "wt-client";
    String clientSecret = "secret";
    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.setBasicAuth(clientId, clientSecret);

    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

    map.add("grant_type", "client_credentials");

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

    ResponseEntity<OAuth2TokenResponse> tokenResponse = restTemplate.postForEntity(
        authServerUrl, request, OAuth2TokenResponse.class
    );

    if (tokenResponse.getStatusCode().isError()) {
      return Optional.empty();
    }

    return Optional.ofNullable(tokenResponse.getBody());
  }

}
