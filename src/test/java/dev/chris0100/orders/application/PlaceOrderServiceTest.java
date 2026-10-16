package dev.chris0100.orders.application;

import dev.chris0100.orders.adapter.out.FakePaymentAdapter;
import dev.chris0100.orders.adapter.out.InMemoryOrderRepository;
import dev.chris0100.orders.adapter.out.RecordingEventAdapter;
import dev.chris0100.orders.application.port.in.PlaceOrderUseCase.PlaceOrderCommand;
import dev.chris0100.orders.application.port.in.PlaceOrderUseCase.PlaceOrderResult;
import dev.chris0100.orders.domain.Money;
import dev.chris0100.orders.domain.OrderLine;
import dev.chris0100.orders.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlaceOrderServiceTest {

    private InMemoryOrderRepository repository;
    private RecordingEventAdapter events;
    private PlaceOrderService service;

    @BeforeEach
    void setUp() {
        repository = new InMemoryOrderRepository();
        events = new RecordingEventAdapter();
        service = new PlaceOrderService(repository, new FakePaymentAdapter("100.00"), events);
    }

    @Test
    void approvedPaymentMarksTheOrderPaidAndPublishesAnEvent() {
        PlaceOrderResult result = service.place(new PlaceOrderCommand("c-1",
                List.of(new OrderLine("sku-1", 3, Money.of("20.00")))));

        assertEquals("PAID", result.status());
        assertEquals(OrderStatus.PAID, repository.findById(result.orderId()).orElseThrow().status());
        assertEquals(List.of("OrderPaid:" + result.orderId()), events.published());
    }

    @Test
    void declinedPaymentCancelsTheOrder() {
        PlaceOrderResult result = service.place(new PlaceOrderCommand("c-2",
                List.of(new OrderLine("sku-9", 1, Money.of("500.00")))));

        assertEquals("CANCELLED", result.status());
        assertEquals(List.of("OrderCancelled:" + result.orderId()), events.published());
    }
}
