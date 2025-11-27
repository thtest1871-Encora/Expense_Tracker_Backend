package com.example.expense.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.expense.dto.SubscriptionDto;
import com.example.expense.dto.TransactionEvent;
import com.example.expense.model.Notification;
import com.example.expense.service.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    public List<Notification> get(@PathVariable Long userId) {
        return service.getUserNotifications(userId);
    }

    @PostMapping("/transaction")
    public String tx(@RequestBody TransactionEvent e) {
        service.createTransactionNotification(e);
        return "ok";
    }

    @DeleteMapping("/by-transaction/{id}")
    public String del(@PathVariable Long id) {
        service.deleteByTransactionId(id);
        return "deleted";
    }

    @PostMapping("/subscription-expiring")
    public String sub(@RequestBody SubscriptionDto dto) {
        service.createSubscriptionExpiryNotification(dto);
        return "ok";
    }

    @DeleteMapping("/{id}")
    public String del2(@PathVariable Long id) {
        service.deleteNotificationById(id);
        return "deleted";
    }
}