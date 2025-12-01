package com.transactionservice.demo.client;

import com.transactionservice.demo.dto.CategoryDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Arrays;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Component
public class CategoryClient {

    private final RestTemplate restTemplate;

    public CategoryClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CategoryDto getCategoryById(Long id) {
        // Using service name for Eureka discovery
        String url = "http://categoryservice/api/categories/" + id;
        try {
            return restTemplate.getForObject(url, CategoryDto.class);
        } catch (Exception e) {
            System.out.println("❌ Failed to fetch category: " + e.getMessage());
            return null;
        }
    }

    public List<CategoryDto> getCategories(Long userId) {
        String url = "http://categoryservice/api/categories";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-User-Id", String.valueOf(userId));
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<CategoryDto[]> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, CategoryDto[].class
            );
            
            if (response.getBody() != null) {
                return Arrays.asList(response.getBody());
            }
        } catch (Exception e) {
            System.out.println("❌ Failed to fetch categories: " + e.getMessage());
        }
        return List.of();
    }
}
