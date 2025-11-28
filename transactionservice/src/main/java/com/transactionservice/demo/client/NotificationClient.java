package com.transactionservice.demo.client;

import com.transactionservice.demo.dto.TransactionEvent;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NotificationClient {

    private final RestTemplate restTemplate;

    public NotificationClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendTransactionNotification(TransactionEvent event) {
        String url = "http://notification-service/notifications/transaction";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TransactionEvent> request = new HttpEntity<>(event, headers);

        try {
            restTemplate.postForObject(url, request, String.class);
        } catch (Exception ex) {
            System.out.println("❌ Failed to send transaction notification: " + ex.getMessage());
        }
    }
}
