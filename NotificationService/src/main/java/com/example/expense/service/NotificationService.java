package com.example.expense.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.expense.dto.SubscriptionDto;
import com.example.expense.dto.TransactionEvent;
import com.example.expense.model.Notification;
import com.example.expense.repository.NotificationRepository;

@Service
public class NotificationService {

    private final NotificationRepository repo;
    // private final SubscriptionClient subscriptionClient; // Removed as Subscription Service pushes notifications now

    public NotificationService(NotificationRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public void createTransactionNotification(TransactionEvent e) {
        if (e == null || e.getId() == null) return;

        // Prevent duplicate notifications
        if (repo.existsByTxnIdAndUserId(e.getId(), e.getUserId())) return;

        String message;

        if ("Income".equalsIgnoreCase(e.getType())) {
            message = "₹" + e.getAmount() + " received from " + e.getCategory();
        } else {
            message = "₹" + e.getAmount() + " spent on " + e.getCategory();
        }

        Notification n = new Notification(
                e.getUserId(),
                "TRANSACTION",
                message,
                e.getId(),
                null
        );

        repo.save(n);
    }

    @Transactional
    public void createSubscriptionExpiryNotification(SubscriptionDto s) {
        if (repo.existsBySubscriptionIdAndUserId(s.getId(), s.getUserId())) return;

        String message = "Your " + s.getName() + " subscription expires on " + s.getExpiryDate();

        Notification n = new Notification(
                s.getUserId(),
                "SUBSCRIPTION",
                message,
                null,
                s.getId()
        );

        repo.save(n);
    }

    @Transactional
    public void createGenericNotification(Map<String, Object> req) {
        Long userId = ((Number) req.get("userId")).longValue();
        String message = (String) req.get("message");
        String type = (String) req.get("type");

        Notification n = new Notification(
                userId,
                type,
                message,
                null,
                null
        );
        repo.save(n);
    }

    public List<Notification> getUserNotifications(Long userId) {
        return repo.findByUserId(userId);
    }

    public void deleteNotificationById(Long id) {
        repo.deleteById(id);
    }

    public void deleteByTransactionId(Long txnId) {
        repo.deleteByTxnId(txnId);
    }

    public void deleteBySubscriptionId(Long subId) {
        repo.deleteBySubscriptionId(subId);
    }
}
