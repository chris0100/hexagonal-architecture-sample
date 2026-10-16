package dev.chris0100.orders.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Order sampleOrder() {
        return Order.place("c-1", List.of(
                new OrderLine("sku-1", 2, Money.of("10.00")),
                new OrderLine("sku-2", 1, Money.of("5.50"))));
    }

    @Test
    void totalIsTheSumOfLines() {
        assertEquals(Money.of("25.50"), sampleOrder().total());
    }

    @Test
    void paidOrdersCannotBeCancelled() {
        Order order = sampleOrder();
        order.markPaid("ref-1");
        assertThrows(IllegalStateException.class, order::cancel);
    }

    @Test
    void orderNeedsLinesAndPositiveQuantities() {
        assertThrows(IllegalArgumentException.class, () -> Order.place("c-1", List.of()));
        assertThrows(IllegalArgumentException.class, () -> new OrderLine("sku", 0, Money.of("1")));
    }
}
