package dev.chris0100.orders.application.port.out;

import dev.chris0100.orders.domain.Money;

import java.util.Optional;

public interface PaymentPort {
    /** @return payment reference if approved, empty if declined. */
    Optional<String> charge(String customerId, Money amount);
}
