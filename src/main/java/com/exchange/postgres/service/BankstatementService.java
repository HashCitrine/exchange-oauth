package com.exchange.postgres.service;

import com.exchange.Constants;
import com.exchange.postgres.repository.BankstatementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankstatementService {
    private BankstatementRepository bankstatementRepository;

    public void updateStatus(String transactionId, Constants.STATUS status) {
        bankstatementRepository.updateStatus(transactionId, status.toString());
    };
}
