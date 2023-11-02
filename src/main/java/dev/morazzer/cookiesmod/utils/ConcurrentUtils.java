package dev.morazzer.cookiesmod.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * Various methods related to concurrent code execution.
 */
@Slf4j
public class ConcurrentUtils {

    private static final ExecutorService executorService =
        Executors.newSingleThreadExecutor(Executors.defaultThreadFactory());
    private static final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(
        Executors.defaultThreadFactory());

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(ConcurrentUtils::shutdown));
    }

    /**
     * Runs a runnable async.
     *
     * @param runnable The runnable.
     */
    public static void execute(Runnable runnable) {
        executorService.execute(runnable);
    }

    /**
     * Runs a runnable at a fixed rate.
     *
     * @param runnable The runnable
     * @param time     The time.
     * @param timeUnit The unit of the time.
     */
    public static void scheduleAtFixedRate(Runnable runnable, long time, TimeUnit timeUnit) {
        scheduledExecutorService.scheduleAtFixedRate(runnable, 0, time, timeUnit);
    }

    /**
     * Runs a runnable after a fixed time.
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
