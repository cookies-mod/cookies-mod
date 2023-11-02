package dev.morazzer.cookiesmod.utils;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

/**
 * Various methods related to time and timing.
 */
public class TimeUtils {

    /**
     * Gets the seconds formatted as string.
     *
     * @param seconds The seconds.
     * @return The string.
     */
    public static String toFormattedTime(long seconds) {
        long minutes = seconds / 60;
        seconds = seconds % 60;
        long hours = minutes / 60;
        minutes = minutes % 60;
        long days = hours / 24;
        hours = hours % 24;

        StringBuilder stringBuilder = new StringBuilder();
        boolean didWritePrevious = false;
        if (days > 0) {
            stringBuilder.append(days).append("d ");
            didWritePrevious = true;
        }
        if (hours > 0 || didWritePrevious) {
            stringBuilder.append(hours).append("h ");
            didWritePrevious = true;
        }
        if (minutes > 0 || didWritePrevious) {
            stringBuilder.append(minutes).append("min ");
            didWritePrevious = true;
        }
        if (seconds > 0 || didWritePrevious) {
            stringBuilder.append(seconds).append("s");
        }
        return stringBuilder.toString();
    }

    /**
     * Gets the current time as epoch second.
     *
     * @return The time as epoch.
     */
    public static long getTime() {
        return System.currentTimeMillis() / 1000L;
    }

    public static MutableText toFormattedTimeText(long seconds) {
        return Text.literal(toFormattedTime(seconds));
    }

    /**
     * Gets the current time millis.
     *
     * @return The time millis.
     */
    public static long getTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * Gets the current nano time.
     *
     * @return The nano time
     */
    public static long getTimeNano() {
        return System.nanoTime();
    }

    /**
     * Gets the current micro time.
     *
     * @return The micro time.
     */
    public static long getTimeMicro() {
        return System.nanoTime() / 1000L;
    }

    /**
     * Times the runtime of a runnable.
     *
     * @param runnable The runnable to time
     * @param <T>      The return value of the runnable.
     * @return The time result.
     */
    public static <T> TimeResult<T> time(Timeable<T> runnable) {
        Timer timer = new Timer();
        T result;
        try {
            result = runnable.run();
        } catch (Throwable e) {
            timer.stop();
            return new TimeResult<>(timer, null, e);
        }
        timer.stop();
        return new TimeResult<>(timer, result, null);
    }

    /**
     * Time the execution time of a runnable.
     *
     * @param runnable The runnable to time.
     * @return The result.
     */
    public static TimeResult<Void> time(Runnable runnable) {
        return time(() -> {
            runnable.run();
            return null;
        });
    }

    /**
     * Functional interface to time the execution of a function.
     *
     * @param <T> The return type.
     */
    @FunctionalInterface
    public interface Timeable<T> {

        T run() throws Throwable;

    }

    /**
     * The result of a timer.
     *
     * @param timer     The timer the result is from.
     * @param result    The return value of the function.
     * @param throwable The throwable if any.
     * @param <T>       The type of the return value.
     */
    public record TimeResult<T>(
        Timer timer,
        T result,
        Throwable throwable
    ) {
    }

    /**
     * Class to create timings and format them as string.
     */
    public static class Timer {

        private long start;
        private long end;

        /**
         * Creates a timer that automatically starts.
         */
        public Timer() {
            this(true);
        }

        /**
         * Creates a timer.
         *
         * @param start If the timer should start.
         */
        public Timer(boolean start) {
            if (start) {
                this.start();
            }
        }

        /**
         * Starts the timer.
         */
        public void start() {
            this.start = TimeUtils.getTimeNano();
        }

        /**
         * Stops the timer.
         */
        public void stop() {
            this.end = TimeUtils.getTimeNano();
        }

        /**
         * Returns the nano time elapsed.
         */
        public long getNanoTime() {
            return this.end - this.start;
        }

        /**
         * Returns the micro time elapsed.
         */
        public long getMicroTime() {
            return this.getNanoTime() / 1000L;
        }

        /**
         * Returns the milli time elapsed.
         */
        public long getMillisTime() {
            return this.getNanoTime() / 1000000L;
        }

        /**
         * Returns the seconds elapsed.
         */
        public long getSecondsTime() {
            return this.getNanoTime() / 1000000000L;
        }

        /**
         * Gets the elapsed time as formatted string.
         *
         * @return The elapsed time.
         */
        public String elapsed() {
            if (getSecondsTime() >= 1) {
                return "%.2fs".formatted(getSecondsTime() + (getMillisTime() - (getSecondsTime() * 1000)) / 1000.0);
            } else if (getMillisTime() >= 1) {
                return "%.2fms".formatted(getMillisTime() + (getMicroTime() - (getMillisTime() * 1000)) / 1000.0);
            } else if (getMicroTime() >= 1) {
                return "%.2fµs".formatted(getMicroTime() + (getNanoTime() - (getMicroTime() * 1000)) / 1000.0);
            } else {
                return "%dns".formatted(getNanoTime());
            }
        }

    }

}
