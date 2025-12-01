package com.example.expense.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.expense.dto.ApiResponse;
import com.example.expense.dto.SubscriptionDto;
import com.example.expense.dto.TransactionEvent;
import com.example.expense.model.Notification;
import com.example.expense.service.NotificationService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<List<Notification>> get(HttpServletRequest request) {
        String userIdHeader = request.getHeader("X-User-Id");
        if (userIdHeader == null) throw new RuntimeException("Missing header");
        
        Long userId = Long.valueOf(userIdHeader);
        return ApiResponse.success("Notifications retrieved", service.getUserNotifications(userId));
    }


    @PostMapping("/transaction")
    public ApiResponse<String> tx(@RequestBody TransactionEvent e) {
        service.createTransactionNotification(e);
        return ApiResponse.success("Transaction notification processed", "ok");
    }

    @DeleteMapping("/by-transaction/{id}")
    public ApiResponse<String> del(@PathVariable Long id) {
        service.deleteByTransactionId(id);
        return ApiResponse.success("Deleted by transaction id", "deleted");
    }

    @PostMapping("/subscription-expiring")
    public ApiResponse<String> sub(@RequestBody SubscriptionDto dto) {
        service.createSubscriptionExpiryNotification(dto);
        return ApiResponse.success("Subscription notification processed", "ok");
    }

    @PostMapping("/send")
    public ApiResponse<String> send(@RequestBody Map<String, Object> req) {
        service.createGenericNotification(req);
        return ApiResponse.success("Notification sent", "ok");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> del2(@PathVariable Long id, HttpServletRequest request) {
        service.deleteNotificationById(id);
        return ApiResponse.success("Notification deleted", "deleted");
    }
    
    @DeleteMapping("/delete-all")
    public ApiResponse<String> del3(HttpServletRequest request) {
    	Long userId = Long.valueOf(request.getHeader("X-User-Id"));
        service.deleteAllNotifications(userId);
        return ApiResponse.success("Notification deleted", "deleted");
    }

    @PatchMapping("/read-all")
    public ApiResponse<String> readAll(HttpServletRequest request) {
        Long userId = Long.valueOf(request.getHeader("X-User-Id"));
        service.markAllAsRead(userId);
        return ApiResponse.success("Notifications marked as read", "ok");
    }
}