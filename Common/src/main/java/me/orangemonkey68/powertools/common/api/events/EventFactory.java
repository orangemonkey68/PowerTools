package me.orangemonkey68.powertools.common.api.events;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class EventFactory {
    private static final List<ArrayBackedEvent<?>> ARRAY_BACKED_EVENTS = new ArrayList<>();

    private static void invalidate() {
        ARRAY_BACKED_EVENTS.forEach(ArrayBackedEvent::update);
    }

    public static <T> Event<T> createArrayBacked(Class<? super T> type, Function<T[], T> invokerFactory) {
        ArrayBackedEvent<T> event = new ArrayBackedEvent<>(type, invokerFactory);
        ARRAY_BACKED_EVENTS.add(event);
        return event;
    }


}
