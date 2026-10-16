package dev.chris0100.orders.domain;

import java.util.List;
import java.util.UUID;

/** Aggregate root: all state changes go through its methods, which enforce the rules. */
public final class Order {

    private final String id;
    private final String customerId;
    private final List<OrderLine> lines;
    private OrderStatus status;
    private String paymentReference;

    private Order(String id, String customerId, List<OrderLine> lines) {
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("an order needs at least one line");
        }
        this.id = id;
        this.customerId = customerId;
        this.lines = List.copyOf(lines);
        this.status = OrderStatus.PENDING_PAYMENT;
    }

    public static Order place(String customerId, List<OrderLine> lines) {
        return new Order(UUID.randomUUID().toString(), customerId, lines);
    }

    public Money total() {
        return lines.stream().map(OrderLine::total).reduce(Money.of("0"), Money::plus);
    }

    public void markPaid(String reference) {
        if (status != OrderStatus.PENDING_PAYMENT) {
            throw new IllegalStateException("only pending orders can be paid, status=" + status);
        }
        this.status = OrderStatus.PAID;
        this.paymentReference = reference;
    }

    public void cancel() {
        if (status == OrderStatus.PAID) {
            throw new IllegalStateException("paid orders must be refunded, not cancelled");
        }
        this.status = OrderStatus.CANCELLED;
    }

    public String id() { return id; }
    public String customerId() { return customerId; }
    public List<OrderLine> lines() { return lines; }
    public OrderStatus status() { return status; }
    public String paymentReference() { return paymentReference; }
}
