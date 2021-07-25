package com.exchange.postgres.repository;

import com.exchange.postgres.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = "update \"order\" set status = ?2 where order_id = ?1", nativeQuery = true)
    void updateStatus(String orderId, String status);
}
