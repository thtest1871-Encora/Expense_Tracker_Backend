package com.example.expense.service;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.expense.dto.SubscriptionDto;

@Component
public class SubscriptionClient {

    private final RestTemplate rt;

    public SubscriptionClient(RestTemplate rt) {
        this.rt = rt;
    }

    public List<SubscriptionDto> fetchExpiring(int days) {
        String url = "http://subscription-service/subscriptions/expiring?days=" + days;

        return rt.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<SubscriptionDto>>() {}
        ).getBody();
    }
}
