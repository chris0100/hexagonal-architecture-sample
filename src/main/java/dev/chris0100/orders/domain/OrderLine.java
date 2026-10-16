package dev.chris0100.orders.domain;

public record OrderLine(String sku, int quantity, Money unitPrice) {
    public OrderLine {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be > 0");
        }
    }

    public Money total() {
        return unitPrice.times(quantity);
    }
}
