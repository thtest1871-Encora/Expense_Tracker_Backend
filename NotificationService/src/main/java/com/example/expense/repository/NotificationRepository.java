package com.example.expense.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.expense.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    boolean existsByTxnIdAndUserId(Long txnId, Long userId);

    boolean existsBySubscriptionIdAndUserId(Long subscriptionId, Long userId);

    List<Notification> findByUserId(Long userId);

    void deleteByTxnId(Long txnId);
    void deleteBySubscriptionId(Long subscriptionId);
    void deleteByUserId(Long userId);

    @Modifying
    @Query("UPDATE Notification n SET n.status = 'READ' WHERE n.userId = :userId")
    void markAllAsRead(@Param("userId") Long userId);
}
