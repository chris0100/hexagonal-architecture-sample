package dev.chris0100.orders.application.port.out;

import dev.chris0100.orders.domain.Order;

import java.util.Optional;

public interface OrderRepository {
    void save(Order order);
    Optional<Order> findById(String id);
}
