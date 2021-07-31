package com.exchange.postgres.service;

import com.exchange.Constants;
import com.exchange.postgres.repository.BankStatementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankStatementService {
    private BankStatementRepository bankStatementRepository;

    public void updateStatus(String transactionId, Constants.STATUS status) {
        bankStatementRepository.updateStatus(transactionId, status.toString());
    };
}
