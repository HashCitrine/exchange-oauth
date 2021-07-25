package com.exchange.postgres.repository;

import com.exchange.postgres.entity.Bankstatement;
import com.exchange.postgres.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BankstatementRepository extends JpaRepository<Bankstatement, Long> {
    Bankstatement findByMemberId(String memberId);

    @Query(value = "update bankstatement set status = ?2 where transactionId = ?1",nativeQuery = true)
    void updateStatus(String transactionId, String status);
}
