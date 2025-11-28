package com.subscriptionservice.demo.repository;

import com.subscriptionservice.demo.model.Subscription;
import com.subscriptionservice.demo.model.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByUserId(Long userId);

    List<Subscription> findByUserIdAndStatus(Long userId, SubscriptionStatus status);

    @Query("""
           SELECT s FROM Subscription s 
           WHERE s.status = com.subscriptionservice.demo.model.SubscriptionStatus.ACTIVE
           AND s.endDate BETWEEN :from AND :to
           """)
    List<Subscription> findExpiringBetween(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
}
