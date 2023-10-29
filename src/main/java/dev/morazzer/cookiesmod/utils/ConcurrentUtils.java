package dev.morazzer.cookiesmod.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ConcurrentUtils {

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor(Executors.defaultThreadFactory());
    private static final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(
            Executors.defaultThreadFactory());

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(ConcurrentUtils::shutdown));
    }

    /**
     * Run a runnable async.
     *
     * @param runnable The runnable.
     */
    public static void execute(Runnable runnable) {
        executorService.execute(runnable);
    }

    /**
     * Run a runnable at a fixed rate.
     *
     * @param runnable The runnable
     * @param time     The time.
     * @param timeUnit The unit of the time.
     */
    public static void scheduleAtFixedRate(Runnable runnable, long time, TimeUnit timeUnit) {
        scheduledExecutorService.scheduleAtFixedRate(runnable, 0, time, timeUnit);
    }

    /**
     * Run a runnable after a fixed time.
     *
     * @param runnable The runnable.
     * @param time     The time.
     * @param timeUnit The unit of the time.
     */
    public static void schedule(Runnable runnable, long time, TimeUnit timeUnit) {
        scheduledExecutorService.schedule(runnable, time, timeUnit);
    }

    /**
     * Shutdown the executor.
     */
    public static void shutdown() {
        executorService.shutdownNow().forEach(runnable -> log.warn("Couldn't execute runnable {}", runnable));
    }

}
