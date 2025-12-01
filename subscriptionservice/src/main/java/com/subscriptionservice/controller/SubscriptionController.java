package com.subscriptionservice.controller;

import com.subscriptionservice.dto.ApiResponse;
import com.subscriptionservice.dto.SubscriptionRequest;
import com.subscriptionservice.model.Subscription;
import com.subscriptionservice.service.SubscriptionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService service;

    public SubscriptionController(SubscriptionService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public ApiResponse<Subscription> addSubscription(@RequestHeader("X-User-Id") Long userId, @RequestBody @jakarta.validation.Valid SubscriptionRequest req) {
        Subscription sub = service.addSubscription(userId, req);
        return ApiResponse.success("Subscription added successfully", sub);
    }

    @GetMapping("/user")
    public ApiResponse<List<Subscription>> getUserSubscriptions(@RequestHeader("X-User-Id") Long userId) {
        List<Subscription> subs = service.getUserSubscriptions(userId);
        return ApiResponse.success("User subscriptions retrieved", subs);
    }

    @GetMapping("/upcoming")
    public ApiResponse<List<Subscription>> getUpcomingRenewals() {
        List<Subscription> subs = service.getUpcomingRenewals();
        return ApiResponse.success("Upcoming renewals retrieved", subs);
    }

    @GetMapping("/plans")
    public ApiResponse<List<java.util.Map<String, Object>>> getPredefinedPlans() {
        return ApiResponse.success("Predefined plans retrieved", service.getPredefinedPlans());
    }

    @GetMapping("/plans/default")
    public ApiResponse<List<java.util.Map<String, Object>>> getPredefinedPlansDefault() {
        return ApiResponse.success("Predefined plans retrieved", service.getPredefinedPlans());
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSubscription(@PathVariable Long id) {
        service.deleteSubscription(id);
        return ApiResponse.success("Subscription deleted successfully", null);
    }
}
