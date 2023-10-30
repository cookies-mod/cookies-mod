package dev.morazzer.cookiesmod.utils;

@SuppressWarnings("unused")
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
        hours = hours % 60;

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

    @FunctionalInterface
    public interface Timeable<T> {

        T run() throws Throwable;

    }

    public record TimeResult<T>(
            Timer timer,
            T result,
            Throwable throwable
    ) {}

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
         * @return The nano time elapsed.
         */
        public long getNanoTime() {
            return this.end - this.start;
        }

        /**
         * @return The micro time elapsed.
         */
        public long getMicroTime() {
            return this.getNanoTime() / 1000L;
        }

        /**
         * @return The milli time elapsed.
         */
        public long getMillisTime() {
            return this.getNanoTime() / 1000000L;
        }

        /**
         * @return The seconds elapsed.
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
