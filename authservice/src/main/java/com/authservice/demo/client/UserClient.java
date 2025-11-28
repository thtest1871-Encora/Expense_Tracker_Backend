package com.authservice.demo.client;

import com.authservice.demo.dto.LoginRequest;
import com.authservice.demo.dto.RegisterRequest;
import com.authservice.demo.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserClient {

    @Autowired
    private RestTemplate restTemplate;

    private final String BASE = "http://user-service/users";

    public UserResponse validateLogin(LoginRequest request) {
        ResponseEntity<UserResponse> response = restTemplate.exchange(
                BASE + "/login?email=" + request.getEmail() + "&password=" + request.getPassword(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<UserResponse>() {}
        );
        return response.getBody();
    }

    public UserResponse register(RegisterRequest request) {
        return restTemplate.postForObject(BASE + "/register", request, UserResponse.class);
    }
}
