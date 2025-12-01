package com.transactionservice.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.transactionservice.demo.model.Transaction;
import java.time.Instant;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserId(Long userId);

    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId " +
            "AND (:categoryId IS NULL OR t.categoryId = :categoryId) " +
            "AND (:categoryIds IS NULL OR t.categoryId IN :categoryIds) " +
            "AND (:start IS NULL OR t.createdAt >= :start) " +
            "AND (:end IS NULL OR t.createdAt <= :end) " +
            "AND (:minAmount IS NULL OR t.amount >= :minAmount) " +
            "AND (:maxAmount IS NULL OR t.amount <= :maxAmount)")
    Page<Transaction> filterTransactions(
            @Param("userId") Long userId,
            @Param("categoryId") Long categoryId,
            @Param("categoryIds") List<Long> categoryIds,
            @Param("start") Instant start,
            @Param("end") Instant end,
            @Param("minAmount") Double minAmount,
            @Param("maxAmount") Double maxAmount,
            Pageable pageable
    );

    @Query("SELECT MONTH(t.createdAt) as month, " +
           "SUM(CASE WHEN t.amount >= 0 THEN t.amount ELSE 0 END) as income, " +
           "SUM(CASE WHEN t.amount < 0 THEN ABS(t.amount) ELSE 0 END) as expense " +
           "FROM Transaction t WHERE t.userId = :userId " +
           "AND YEAR(t.createdAt) = :year " +
           "GROUP BY MONTH(t.createdAt)")
    List<Object[]> getMonthlySummary(@Param("userId") Long userId, @Param("year") int year);

    @Query("SELECT t.categoryId, SUM(t.amount), COUNT(t) " +
           "FROM Transaction t WHERE t.userId = :userId " +
           "AND t.createdAt >= :start " +
           "AND t.createdAt <= :end " +
           "GROUP BY t.categoryId")
    List<Object[]> getCategorySummary(
            @Param("userId") Long userId,
            @Param("start") Instant start,
            @Param("end") Instant end
    );

    @Query("SELECT MONTH(t.createdAt) as month, SUM(t.amount) as total " +
            "FROM Transaction t WHERE t.userId = :userId " +
            "GROUP BY MONTH(t.createdAt)")
    List<Object[]> monthlyStats(@Param("userId") Long userId);

    @Query("SELECT t.categoryId, SUM(t.amount) " +
            "FROM Transaction t WHERE t.userId = :userId " +
            "GROUP BY t.categoryId")
    List<Object[]> categoryStats(@Param("userId") Long userId);
}
