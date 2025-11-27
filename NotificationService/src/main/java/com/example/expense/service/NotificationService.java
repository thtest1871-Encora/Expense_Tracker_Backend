package com.example.expense.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.expense.dto.SubscriptionDto;
import com.example.expense.dto.TransactionEvent;
import com.example.expense.model.Notification;
import com.example.expense.repository.NotificationRepository;


@Service
public class NotificationService {

    private final NotificationRepository repo;
    private final SubscriptionClient subscriptionClient;

    public NotificationService(NotificationRepository repo, SubscriptionClient subscriptionClient) {
        this.repo = repo;
        this.subscriptionClient = subscriptionClient;
    }

    @Transactional
    public void createTransactionNotification(TransactionEvent e) {
        if (e == null || e.getId() == null) return;

        if (repo.existsByTxnIdAndUserId(e.getId(), e.getUserId())) return;

        String type = e.getType().equalsIgnoreCase("Income") ? "Income" : "Expense";
        
        String message;
        
        if(type == "Income") {
        	message = "₹" + e.getAmount() + " recieved from " + e.getCategory();
        }
        else {
        	message = "₹" + e.getAmount() + " spent on " + e.getCategory();
        }

        Notification n = new Notification(e.getUserId(), "TRANSACTION", message, e.getId(), null);

        repo.save(n);
    }

    @Transactional
    public void createSubscriptionExpiryNotification(SubscriptionDto s) {
        if (repo.existsBySubscriptionIdAndUserId(s.getId(), s.getUserId())) return;

        String message = "Your " + s.getName() + " subscription expires on " + s.getExpiryDate();

        Notification n = new Notification(s.getUserId(), "SUBSCRIPTION", message, null, s.getId());

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

    public void checkAndCreateExpiryNotifications(int days) {
        List<SubscriptionDto> subs = subscriptionClient.fetchExpiring(days);
        if (subs != null) {
            for (SubscriptionDto s : subs) createSubscriptionExpiryNotification(s);
        }
    }
}
