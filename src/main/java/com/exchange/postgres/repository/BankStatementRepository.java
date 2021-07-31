package com.exchange.postgres.repository;

import com.exchange.postgres.entity.BankStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BankStatementRepository extends JpaRepository<BankStatement, Long> {
    BankStatement findByMemberId(String memberId);

    @Query(value = "update bank_statement set status = ?2 where transactionId = ?1",nativeQuery = true)
    void updateStatus(String transactionId, String status);
}
