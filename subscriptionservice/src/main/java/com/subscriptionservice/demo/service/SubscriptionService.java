package com.subscriptionservice.demo.service;

import com.subscriptionservice.demo.dto.CreateSubscriptionRequest;
import com.subscriptionservice.demo.dto.ExpiringSubscriptionResponse;
import com.subscriptionservice.demo.dto.SubscriptionResponse;
import com.subscriptionservice.demo.dto.UpdateSubscriptionRequest;
import com.subscriptionservice.demo.model.Subscription;
import com.subscriptionservice.demo.model.SubscriptionStatus;
import com.subscriptionservice.demo.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class SubscriptionService {

    private final SubscriptionRepository repo;

    public SubscriptionService(SubscriptionRepository repo) {
        this.repo = repo;
    }

    private SubscriptionResponse toResponse(Subscription s) {
        SubscriptionResponse r = new SubscriptionResponse();
        r.setId(s.getId());
        r.setUserId(s.getUserId());
        r.setName(s.getName());
        r.setProvider(s.getProvider());
        r.setPlanName(s.getPlanName());
        r.setAmount(s.getAmount());
        r.setStartDate(s.getStartDate());
        r.setEndDate(s.getEndDate());
        r.setStatus(s.getStatus());
        r.setBillingCycle(s.getBillingCycle());
        return r;
    }

    public SubscriptionResponse create(Long userId, CreateSubscriptionRequest req) {
        Subscription s = new Subscription();
        s.setUserId(userId);
        s.setName(req.getName());
        s.setProvider(req.getProvider());
        s.setPlanName(req.getPlanName());
        s.setAmount(req.getAmount());
        s.setStartDate(req.getStartDate());
        s.setEndDate(req.getEndDate());
        s.setBillingCycle(req.getBillingCycle());
        s.setStatus(SubscriptionStatus.ACTIVE);
        return toResponse(repo.save(s));
    }

    public List<SubscriptionResponse> listForUser(Long userId) {
        return repo.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public SubscriptionResponse update(Long id, Long userId, UpdateSubscriptionRequest req) {
        Subscription s = repo.findById(id)
                .filter(sub -> sub.getUserId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Subscription not found or not allowed"));

        if (req.getName() != null) s.setName(req.getName());
        if (req.getProvider() != null) s.setProvider(req.getProvider());
        if (req.getPlanName() != null) s.setPlanName(req.getPlanName());
        if (req.getAmount() != null) s.setAmount(req.getAmount());
        if (req.getStartDate() != null) s.setStartDate(req.getStartDate());
        if (req.getEndDate() != null) s.setEndDate(req.getEndDate());
        if (req.getBillingCycle() != null) s.setBillingCycle(req.getBillingCycle());

        return toResponse(s);
    }

    public void cancel(Long id, Long userId) {
        Subscription s = repo.findById(id)
                .filter(sub -> sub.getUserId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Subscription not found or not allowed"));
        s.setStatus(SubscriptionStatus.CANCELLED);
        repo.save(s);
    }

    /**
     * Used by Notification Service.
     * Return all ACTIVE subscriptions that are expiring in next N days.
     */
    public List<ExpiringSubscriptionResponse> findExpiring(int days) {
        LocalDate today = LocalDate.now();
        LocalDate to = today.plusDays(days);

        return repo.findExpiringBetween(today, to)
                .stream()
                .map(s -> {
                    ExpiringSubscriptionResponse r = new ExpiringSubscriptionResponse();
                    r.setUserId(s.getUserId());
                    r.setSubscriptionId(s.getId());
                    r.setName(s.getName());
                    r.setEndDate(s.getEndDate());
                    r.setAmount(s.getAmount());
                    return r;
                })
                .toList();
    }

}
