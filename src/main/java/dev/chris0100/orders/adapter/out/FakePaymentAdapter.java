package dev.chris0100.orders.adapter.out;

import dev.chris0100.orders.application.port.out.PaymentPort;
import dev.chris0100.orders.domain.Money;

import java.math.BigDecimal;
import java.util.Optional;

/** Approves payments up to a limit. A real adapter would call Stripe/PayU and map their errors. */
public final class FakePaymentAdapter implements PaymentPort {

    private final BigDecimal limit;

    public FakePaymentAdapter(String limit) {
        this.limit = new BigDecimal(limit);
    }

    @Override
    public Optional<String> charge(String customerId, Money amount) {
        if (amount.amount().compareTo(limit) > 0) {
            return Optional.empty();
        }
        return Optional.of("PAY-" + customerId + "-" + amount.amount().toPlainString());
    }
}
