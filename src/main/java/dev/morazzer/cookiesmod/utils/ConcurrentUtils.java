package dev.morazzer.cookiesmod.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ConcurrentUtils {

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor(Executors.defaultThreadFactory());

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(ConcurrentUtils::shutdown));
    }

    public static void execute(Runnable runnable) {
        executorService.execute(runnable);
    }

    public static void shutdown() {
        executorService.shutdownNow().forEach(runnable -> log.warn("Couldn't execute runnable {}", runnable));
    }

}
