package com.subscriptionservice.service;

import com.subscriptionservice.client.NotificationClient;
import com.subscriptionservice.dto.SubscriptionRequest;
import com.subscriptionservice.model.Subscription;
import com.subscriptionservice.repository.SubscriptionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
        Subscription saved = repository.save(subscription);
        enrichSubscriptionData(saved);
        return saved;
    }

    public List<Subscription> getUserSubscriptions(Long userId) {
        List<Subscription> list = repository.findByUserId(userId);
        list.forEach(this::enrichSubscriptionData);
        return list;
    }

    public List<Subscription> getUpcomingRenewals() {
        LocalDate today = LocalDate.now();
        LocalDate tenDaysLater = today.plusDays(10);
        List<Subscription> list = repository.findByNextDueDateBetween(today, tenDaysLater);
        list.forEach(this::enrichSubscriptionData);
        return list;
    }

    private void enrichSubscriptionData(Subscription s) {
        long days = ChronoUnit.DAYS.between(LocalDate.now(), s.getNextDueDate());
        s.setDaysUntilRenewal(days);
        if (days < 0) s.setStatus("OVERDUE");
        else if (days <= 3) s.setStatus("DUE_SOON");
        else s.setStatus("ACTIVE");
    }

    public void deleteSubscription(Long id) {
        repository.deleteById(id);
    }

    public List<Map<String, Object>> getPredefinedPlans() {
        List<Map<String, Object>> plans = new java.util.ArrayList<>();
        
        plans.add(createPlan("Netflix", "Basic", 199, "MONTHLY"));
        plans.add(createPlan("Netflix", "Standard", 499, "MONTHLY"));
        plans.add(createPlan("Netflix", "Premium", 649, "MONTHLY"));
        plans.add(createPlan("Amazon Prime", "Monthly", 179, "MONTHLY"));
        plans.add(createPlan("Amazon Prime", "Yearly", 1499, "YEARLY"));
        plans.add(createPlan("Spotify", "Individual", 119, "MONTHLY"));
        plans.add(createPlan("Spotify", "Duo", 149, "MONTHLY"));
        plans.add(createPlan("Spotify", "Family", 179, "MONTHLY"));
        plans.add(createPlan("Disney+", "Monthly", 299, "MONTHLY"));
        plans.add(createPlan("Disney+", "Annual", 1499, "YEARLY"));
        
        return plans;
    }

    private Map<String, Object> createPlan(String platform, String plan, int amount, String cycle) {
        Map<String, Object> map = new java.util.HashMap<>();
        map.put("platform", platform);
        map.put("plan", plan);
        map.put("amount", amount);
        map.put("cycle", cycle);
        return map;
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
