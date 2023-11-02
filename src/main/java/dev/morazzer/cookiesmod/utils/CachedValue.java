package dev.morazzer.cookiesmod.utils;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import lombok.Getter;

/**
 * Used to temporarily cache values that are frequently request and/or expensive to calculate.
 *
 * @param <T> Type of the cached value
 */
public class CachedValue<T> {

    @Getter
    private final ValueProvider<T> valueProvider;
    private final Duration duration;

    private T value;
    private long lastTimeEvaluated;

    /**
     * Creates a cached value.
     *
     * @param valueProvider A functional interface to provide the value after a certain time has passed.
     * @param time          The time to wait between invocations.
     * @param timeUnit      The time unit of the time argument.
     */
    public CachedValue(ValueProvider<T> valueProvider, long time, TimeUnit timeUnit) {
        this(valueProvider, Duration.ofSeconds(timeUnit.toSeconds(time)));
    }

    /**
     * Creates a cached value.
     *
     * @param valueProvider A functional interface to provide the value after a certain time has passed.
     * @param duration      The duration to wait between invocations.
     */
    public CachedValue(ValueProvider<T> valueProvider, Duration duration) {
        this.valueProvider = valueProvider;
        this.duration = duration;
    }

    /**
     * Forces the cached value to be reevaluated.
     */
    public void updateNow() {
        this.value = this.valueProvider.getValue();
        this.lastTimeEvaluated = System.currentTimeMillis();
    }

    /**
     * Always gets the current value even if the time has passed.
     */
    public T getValueNoUpdate() {
        return this.value;
    }

    /**
     * Gets the cached value or compute a new one if the provided duration has elapsed.
     *
     * @return The cached value.
     */
    public T getValue() {
        if (this.lastTimeEvaluated + this.duration.toMillis() < System.currentTimeMillis()) {
            this.value = this.valueProvider.getValue();
            this.lastTimeEvaluated = System.currentTimeMillis();
        }
        return this.value;
    }

    /**
     * The functional interface for providing the cached value.
     */
    @FunctionalInterface
    public interface ValueProvider<V> {

        V getValue();

    }

}
