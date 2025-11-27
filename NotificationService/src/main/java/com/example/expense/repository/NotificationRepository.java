package com.example.expense.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.expense.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    boolean existsByTxnIdAndUserId(Long txnId, Long userId);

    boolean existsBySubscriptionIdAndUserId(Long subscriptionId, Long userId);

    List<Notification> findByUserId(Long userId);

    void deleteByTxnId(Long txnId);
    void deleteBySubscriptionId(Long subscriptionId);
}
