package dev.morazzer.cookiesmod.utils;

@SuppressWarnings("unused")
public class TimeUtils {

    /**
     * Get the seconds formatted as string.
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
     * Get the current time as epoch second.
     *
     * @return The time as epoch.
     */
    public static long getTime() {
        return System.currentTimeMillis() / 1000L;
    }

    /**
     * Get the current time millis.
     *
     * @return The time millis.
     */
    public static long getTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * Get the current nano time.
     *
     * @return The nano time
     */
    public static long getTimeNano() {
        return System.nanoTime();
    }

    /**
     * Get the current micro time.
     *
     * @return The micro time.
     */
    public static long getTimeMicro() {
        return System.nanoTime() / 1000L;
    }

    /**
     * Time the runtime of a runnable.
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
         * Create a time that automatically starts.
         */
        public Timer() {
            this(true);
        }

        /**
         * Create a timer.
         *
         * @param start If the timer should start.
         */
        public Timer(boolean start) {
            if (start) {
                this.start();
            }
        }

        /**
         * Start the timer.
         */
        public void start() {
            this.start = TimeUtils.getTimeNano();
        }

        /**
         * End the timer.
         */
        public void stop() {
            this.end = TimeUtils.getTimeNano();
        }

        /**
         * Get the nano time elapsed.
         *
         * @return The nano time.
         */
        public long getNanoTime() {
            return this.end - this.start;
        }

        /**
         * Get the micro time elapsed.
         *
         * @return The micro time.
         */
        public long getMicroTime() {
            return this.getNanoTime() / 1000L;
        }

        /**
         * Get the milli time elapsed.
         *
         * @return The milli time.
         */
        public long getMillisTime() {
            return this.getNanoTime() / 1000000L;
        }

        /**
         * Get the seconds elapsed.
         *
         * @return The seconds.
         */
        public long getSecondsTime() {
            return this.getNanoTime() / 1000000000L;
        }

        /**
         * Get the elapsed time as formatted string.
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
