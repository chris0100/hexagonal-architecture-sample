package dev.chris0100.orders.application.port.in;

import dev.chris0100.orders.domain.OrderLine;

import java.util.List;

/** Input port: what the outside world can ask the application to do. */
public interface PlaceOrderUseCase {

    record PlaceOrderCommand(String customerId, List<OrderLine> lines) {
    }

    record PlaceOrderResult(String orderId, String status) {
    }

    PlaceOrderResult place(PlaceOrderCommand command);
}
