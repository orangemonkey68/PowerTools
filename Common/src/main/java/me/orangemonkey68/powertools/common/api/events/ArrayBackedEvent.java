package me.orangemonkey68.powertools.common.api.events;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

public class ArrayBackedEvent<T> extends Event<T> {
    private final Function<T[], T> invokerFactory;
    private final Lock lock = new ReentrantLock();
    private T[] handlers;

    @SuppressWarnings("unchecked")
    ArrayBackedEvent(Class<? super T> type, Function<T[], T> invokerFactory) {
        this.invokerFactory = invokerFactory;
        this.handlers = (T[]) Array.newInstance(type, 0);
        update();
    }

    void update() {
        this.invoker = invokerFactory.apply(handlers);
    }


    @Override
    public void register(T listener) {
        Objects.requireNonNull(listener, "Tried to register a null listener");

        lock.lock();

        try {
            handlers = Arrays.copyOf(handlers, handlers.length + 1);
            handlers[handlers.length - 1] = listener;
            update();
        } finally {
            lock.unlock();
        }
    }
}
