package me.orangemonkey68.powertools.common.api.events;

public abstract class Event<T> {

    protected volatile T invoker;

    public final T invoker() {
        return invoker;
    }

    public abstract void register(T listener);
}
