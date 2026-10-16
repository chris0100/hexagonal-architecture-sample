package dev.chris0100.orders.application.port.out;

public interface EventPort {
    void publish(String eventType, String orderId);
}
