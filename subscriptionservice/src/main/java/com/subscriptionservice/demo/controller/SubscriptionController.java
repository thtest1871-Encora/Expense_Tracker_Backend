package com.subscriptionservice.demo.controller;

import com.subscriptionservice.demo.dto.CreateSubscriptionRequest;
import com.subscriptionservice.demo.dto.ExpiringSubscriptionResponse;
import com.subscriptionservice.demo.dto.SubscriptionResponse;
import com.subscriptionservice.demo.dto.UpdateSubscriptionRequest;
import com.subscriptionservice.demo.service.SubscriptionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService service;

    public SubscriptionController(SubscriptionService service) {
        this.service = service;
    }

    // TODO: right now your Gateway is not yet setting userId.
    // After we fix JwtFilter, we'll read from header like "X-User-Id".
    private Long getUserId(HttpServletRequest request) {
        Object val = request.getAttribute("userId");
        if (val instanceof Long) return (Long) val;
        if (val instanceof String s) return Long.parseLong(s);
        throw new RuntimeException("UserId not found in request");
    }

    @PostMapping
    public ResponseEntity<SubscriptionResponse> create(
            @Valid @RequestBody CreateSubscriptionRequest req,
            HttpServletRequest request
    ) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(service.create(userId, req));
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionResponse>> list(HttpServletRequest request) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(service.listForUser(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionResponse> update(
            @PathVariable Long id,
            @RequestBody UpdateSubscriptionRequest req,
            HttpServletRequest request
    ) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(service.update(id, userId, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancel(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        Long userId = getUserId(request);
        service.cancel(id, userId);
        return ResponseEntity.ok("Subscription cancelled");
    }

    /**
     * Endpoint for Notification Service.
     * Example call: GET /subscriptions/expiring?days=2
     */
    @GetMapping("/expiring")
    public ResponseEntity<List<ExpiringSubscriptionResponse>> expiring(
            @RequestParam(defaultValue = "2") int days
    ) {
        return ResponseEntity.ok(service.findExpiring(days));
    }
}
