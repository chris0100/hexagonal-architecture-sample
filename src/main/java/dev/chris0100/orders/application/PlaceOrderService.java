package dev.chris0100.orders.application;

import dev.chris0100.orders.application.port.in.PlaceOrderUseCase;
import dev.chris0100.orders.application.port.out.EventPort;
import dev.chris0100.orders.application.port.out.OrderRepository;
import dev.chris0100.orders.application.port.out.PaymentPort;
import dev.chris0100.orders.domain.Order;

/** Use case: orchestrates domain + ports. No business rules here, no framework imports. */
public final class PlaceOrderService implements PlaceOrderUseCase {

    private final OrderRepository orders;
    private final PaymentPort payments;
    private final EventPort events;

    public PlaceOrderService(OrderRepository orders, PaymentPort payments, EventPort events) {
        this.orders = orders;
        this.payments = payments;
        this.events = events;
    }

    @Override
    public PlaceOrderResult place(PlaceOrderCommand command) {
        Order order = Order.place(command.customerId(), command.lines());

        payments.charge(order.customerId(), order.total())
                .ifPresentOrElse(order::markPaid, order::cancel);

        orders.save(order);
        events.publish(order.status() == dev.chris0100.orders.domain.OrderStatus.PAID ? "OrderPaid" : "OrderCancelled",
                order.id());
        return new PlaceOrderResult(order.id(), order.status().name());
    }
}
