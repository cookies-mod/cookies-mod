package dev.morazzer.cookiesmod.utils;

@SuppressWarnings("unused")
public class TimeUtils {

    public static long getTime() {
        return System.currentTimeMillis() / 1000L;
    }

    public static long getTimeMillis() {
        return System.currentTimeMillis();
    }

    public static long getTimeNano() {
        return System.nanoTime();
    }

    public static long getTimeMicro() {
        return System.nanoTime() / 1000L;
    }

    public static <T> TimeResult<T> time(Timeable<T> runnable) {
        Timer timer = new Timer();
        T result;
        try {
            result = runnable.run();
        } catch (Exception e) {
            timer.stop();
            return new TimeResult<>(timer, null, e);
        }
        timer.stop();
        return new TimeResult<>(timer, result, null);
    }

    public record TimeResult<T>(Timer timer, T result, Throwable throwable) {
    }

    @FunctionalInterface
    public interface Timeable<T> {
        T run() throws Exception;
    }

    public static class Timer {

        private long start;
        private long end;

        public Timer() {
            this(true);
        }

        public Timer(boolean start) {
            if (start) {
                this.start();
            }
        }

        public void start() {
            this.start = TimeUtils.getTimeNano();
        }

        public void stop() {
            this.end = TimeUtils.getTimeNano();
        }

        public long getNanoTime() {
            return this.end - this.start;
        }

        public long getMicroTime() {
            return this.getNanoTime() / 1000L;
        }

        public long getMillisTime() {
            return this.getNanoTime() / 1000000L;
        }

        public long getSecondsTime() {
            return this.getNanoTime() / 1000000000L;
        }

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
