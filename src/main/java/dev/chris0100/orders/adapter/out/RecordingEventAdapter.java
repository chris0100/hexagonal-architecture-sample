package dev.chris0100.orders.adapter.out;

import dev.chris0100.orders.application.port.out.EventPort;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/** Collects events in memory; an SNS/Kafka adapter would implement the same port. */
public final class RecordingEventAdapter implements EventPort {

    private final List<String> published = new CopyOnWriteArrayList<>();

    @Override
    public void publish(String eventType, String orderId) {
        published.add(eventType + ":" + orderId);
    }

    public List<String> published() {
        return List.copyOf(published);
    }
}
