package me.orangemonkey68.powertools.common.api.events;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class EventFactory {
    private static final List<ArrayBackedEvent<?>> ARRAY_BACKED_EVENTS = new ArrayList<>();

    /**
     * Invalidates <b>ALL</b> events created by this EventFactory. Don't call this unless you know what you're doing.
     */
    private static void invalidate() {
        ARRAY_BACKED_EVENTS.forEach(ArrayBackedEvent::update);
    }

    /**
     *
     * @param type The invoker class
     * @param invokerFactory A function describing what to do when the listeners of this class are called upon.
     * @param <T> Any class that is or extends the invoker.
     * @return an {@link ArrayBackedEvent} that can be later invalidated.
     */
    public static <T> Event<T> createArrayBacked(Class<? super T> type, Function<T[], T> invokerFactory) {
        ArrayBackedEvent<T> event = new ArrayBackedEvent<>(type, invokerFactory);
        ARRAY_BACKED_EVENTS.add(event);
        return event;
    }


}
