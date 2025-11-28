package com.subscriptionservice.service;

import com.subscriptionservice.client.NotificationClient;
import com.subscriptionservice.dto.SubscriptionRequest;
import com.subscriptionservice.model.Subscription;
import com.subscriptionservice.repository.SubscriptionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class SubscriptionService {

    private final SubscriptionRepository repository;
    private final NotificationClient notificationClient;

    public SubscriptionService(SubscriptionRepository repository, NotificationClient notificationClient) {
        this.repository = repository;
        this.notificationClient = notificationClient;
    }

    public Subscription addSubscription(Long userId, SubscriptionRequest req) {
        LocalDate nextDueDate = calculateNextDueDate(req.getBillingCycle());
        Subscription subscription = new Subscription(
                userId,
                req.getPlatformName(),
                req.getPlanName(),
                req.getAmount(),
                req.getBillingCycle(),
                nextDueDate
        );
        return repository.save(subscription);
    }

    public List<Subscription> getUserSubscriptions(Long userId) {
        return repository.findByUserId(userId);
    }

    public List<Subscription> getUpcomingRenewals() {
        LocalDate today = LocalDate.now();
        LocalDate tenDaysLater = today.plusDays(10);
        return repository.findByNextDueDateBetween(today, tenDaysLater);
    }

    public void deleteSubscription(Long id) {
        repository.deleteById(id);
    }

    private LocalDate calculateNextDueDate(String cycle) {
        LocalDate today = LocalDate.now();
        if ("WEEKLY".equalsIgnoreCase(cycle)) return today.plusWeeks(1);
        if ("MONTHLY".equalsIgnoreCase(cycle)) return today.plusMonths(1);
        if ("YEARLY".equalsIgnoreCase(cycle)) return today.plusYears(1);
        return today.plusMonths(1); // Default
    }

    // Scheduled task to check for renewals and send notifications
    @Scheduled(cron = "0 0 9 * * ?") // Every day at 9 AM
    public void checkRenewals() {
        List<Subscription> upcoming = getUpcomingRenewals();
        for (Subscription sub : upcoming) {
            String message = "Your subscription for " + sub.getPlatformName() + " is due on " + sub.getNextDueDate();
            try {
                notificationClient.sendNotification(Map.of(
                        "userId", sub.getUserId(),
                        "message", message,
                        "type", "RENEWAL_ALERT"
                ));
            } catch (Exception e) {
                System.err.println("Failed to send notification for subscription " + sub.getId());
            }
        }
    }
}
