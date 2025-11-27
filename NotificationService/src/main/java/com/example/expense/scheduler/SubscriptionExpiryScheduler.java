package com.example.expense.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.expense.service.NotificationService;

@Component
public class SubscriptionExpiryScheduler {

    private final NotificationService service;

    public SubscriptionExpiryScheduler(NotificationService service) {
        this.service = service;
    }

    @Scheduled(cron = "${notification.scheduler.cron}")
    public void run() {
        service.checkAndCreateExpiryNotifications(1);
    }
}
