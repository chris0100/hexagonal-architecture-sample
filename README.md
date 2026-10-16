# Hexagonal Architecture Sample (Ports & Adapters)

An order service where the **domain has zero framework dependencies**. Everything external (database, payments, events) plugs in through ports.

```
            ┌──────────────── application ────────────────┐
 adapter ─► │  PlaceOrderUseCase (input port)             │
 (REST,     │      │                                      │
  queue)    │      ▼                                      │
            │  domain: Order, Money, OrderStatus          │
            │      │                                      │
            │      ▼ output ports                         │
            │  OrderRepository · PaymentPort · EventPort  │ ─► adapters (JPA/Mongo, Stripe, SNS)
            └─────────────────────────────────────────────┘
```

| Layer | Package | Depends on |
|-------|---------|------------|
| Domain | `domain` | nothing |
| Application (use cases + ports) | `application` | domain |
| Adapters | `adapter` | application + domain |

## Run

```bash
mvn test
```

Tests are split by layer: domain rules (pure unit tests), use case with in-memory adapters, and an adapter test.

## Interview notes

- Business rules live in the domain (`Order.pay()`, `Order.cancel()`), not in services or controllers.
- The use case orchestrates: load, call domain methods, save, publish. It talks to **interfaces** (ports).
- Swapping Mongo for Postgres or SNS for Kafka means writing a new adapter. The domain and use cases don't change.
- Testing is fast: in-memory adapters replace infrastructure without mocks or Docker.
- Trade-off: more interfaces and mapping code. Worth it for core domains, overkill for a simple CRUD.
