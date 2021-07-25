package com.exchange.postgres.service;

import com.exchange.Constants;
import com.exchange.postgres.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public void updateStatus(String orderId, Constants.STATUS status) {
        orderRepository.updateStatus(orderId, status.toString());
    };
}
