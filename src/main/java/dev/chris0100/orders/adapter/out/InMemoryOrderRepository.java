package dev.chris0100.orders.adapter.out;

import dev.chris0100.orders.application.port.out.OrderRepository;
import dev.chris0100.orders.domain.Order;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/** Swap for a JPA/Mongo adapter in production; the application does not change. */
public final class InMemoryOrderRepository implements OrderRepository {

    private final Map<String, Order> store = new ConcurrentHashMap<>();

    @Override
    public void save(Order order) {
        store.put(order.id(), order);
    }

    @Override
    public Optional<Order> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }
}
