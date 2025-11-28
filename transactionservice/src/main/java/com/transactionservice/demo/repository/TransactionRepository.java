package com.transactionservice.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.transactionservice.demo.model.Transaction;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserId(Long userId);

    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId " +
            "AND (:category IS NULL OR t.category = :category) " +
            "AND (:start IS NULL OR t.createdAt >= :start) " +
            "AND (:end IS NULL OR t.createdAt <= :end)")
    List<Transaction> filterTransactions(
            @Param("userId") Long userId,
            @Param("category") String category,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    @Query("SELECT MONTH(t.createdAt) as month, SUM(t.amount) as total " +
            "FROM Transaction t WHERE t.userId = :userId " +
            "GROUP BY MONTH(t.createdAt)")
    List<Object[]> monthlyStats(@Param("userId") Long userId);

    @Query("SELECT t.category, SUM(t.amount) " +
            "FROM Transaction t WHERE t.userId = :userId " +
            "GROUP BY t.category")
    List<Object[]> categoryStats(@Param("userId") Long userId);
}
