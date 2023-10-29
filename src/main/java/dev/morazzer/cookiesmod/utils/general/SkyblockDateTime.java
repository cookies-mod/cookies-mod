package dev.morazzer.cookiesmod.utils.general;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;

/**
 * Class to represent skyblock date and time.
 */
@Getter
public class SkyblockDateTime {

    private final static Instant SKYBLOCK_EPOCH = Instant.ofEpochMilli(1_560_275_700_000L);
    private static final long SB_HOUR = 50;
    private static final long SB_DAY = SB_HOUR * 24;
    private static final long SB_MONTH = SB_DAY * 31;
    private static final long SB_SEASON = SB_MONTH * 3;
    private static final long SB_YEAR = SB_SEASON * 4;
    private static final long DATE_OFFSET = SB_YEAR + SB_DAY;
    private final Instant instant;
    private final Instant skyblockInstant;

    /**
     * Get the skyblock time instance of an {@linkplain java.time.Instant}.
     *
     * @param instant The instant.
     */
    public SkyblockDateTime(Instant instant) {
        this.instant = instant;
        this.skyblockInstant = this.instant.minus(Duration.ofSeconds(SKYBLOCK_EPOCH.getEpochSecond()));
    }

    /**
     * Get the skyblock time of a skyblock instant.
     *
     * @param instant The instant.
     * @return The corresponding skyblock time.
     */
    public static SkyblockDateTime ofSkyblockInstant(Instant instant) {
        return new SkyblockDateTime(instant.plus(Duration.ofSeconds(SKYBLOCK_EPOCH.getEpochSecond())));
    }

    /**
     * Get the current skyblock time.
     *
     * @return The current time.
     */
    public static SkyblockDateTime now() {
        return new SkyblockDateTime(Instant.now());
    }

    /**
     * Get the skyblock time corresponding to an epoch instant.
     *
     * @param second The seconds.
     * @return The skyblock time.
     */
    public static SkyblockDateTime ofEpochSecond(long second) {
        return new SkyblockDateTime(Instant.ofEpochSecond(second));
    }

    /**
     * If the time is in the future.
     *
     * @return If it is in the future.
     */
    public boolean isInFuture() {
        SkyblockDateTime now = now();
        return now.getElapsedSkyblockDays() < this.getElapsedSkyblockDays();
    }

    /**
     * If the date is the currently active skyblock day.
     *
     * @return If it is the current day.
     */
    public boolean isCurrentDay() {
        SkyblockDateTime now = now();
        return now.getElapsedSkyblockDays() == this.getElapsedSkyblockDays();
    }

    /**
     * If the date is in the past.
     *
     * @return If it is in the past.
     */
    public boolean isInPast() {
        SkyblockDateTime now = now();
        return now.getElapsedSkyblockDays() > this.getElapsedSkyblockDays();
    }

    /**
     * Return the instance as a string with all active events.
     *
     * @return The string.
     */
    public String toStringWithEvents() {
        StringBuilder stringBuilder = new StringBuilder();
        for (SkyblockEvents value : SkyblockEvents.values()) {
            if (this.isActive(value)) {
                stringBuilder.append(value).append(", ");
            }
        }
        String suffix = "";
        if (!stringBuilder.isEmpty()) {
            suffix = "[%s]".formatted(stringBuilder.substring(0, stringBuilder.length() - 2));
        }
        return "%s %s".formatted(
                toString(),
                suffix
        );
    }

    /**
     * Get the date with a new year.
     *
     * @param year The new year.
     * @return The new date.
     */
    public SkyblockDateTime withYear(int year) {
        return new SkyblockDateTime(Instant.ofEpochSecond((this.getInstant()
                .getEpochSecond() - ((this.getSkyblockYear()) * SB_YEAR)) + (year * SB_YEAR)));
    }

    /**
     * Get the date with a new month.
     *
     * @param month The new month.
     * @return The new year.
     */
    public SkyblockDateTime withMonth(int month) {
        return new SkyblockDateTime(Instant.ofEpochSecond((this.getInstant()
                .getEpochSecond() - ((this.getCurrentSkyblockMonth() - 1) * SB_MONTH)) + (month * SB_MONTH)));
    }

    /**
     * Get the date with a new day.
     *
     * @param day The new day.
     * @return The new date.
     */
    public SkyblockDateTime withDay(int day) {
        return new SkyblockDateTime(Instant.ofEpochSecond((this.getInstant()
                .getEpochSecond() - ((this.getCurrentSkyblockDay() - 1) * SB_DAY)) + (day * SB_DAY)));
    }

    /**
     * Get the date with a different minute.
     *
     * @param minute The new minute.
     * @return The new date.
     */
    public SkyblockDateTime withMinute(int minute) {
        return new SkyblockDateTime(Instant.ofEpochMilli((long) (this.getInstant()
                .getEpochSecond() - this.getInstant().getEpochSecond() % 50 + minute * 0.83) * 1000));
    }

    /**
     * Get the date with the minute set to 0.
     *
     * @return The new date.
     */
    public SkyblockDateTime withMinuteZero() {
        return new SkyblockDateTime(Instant.ofEpochSecond(this.getInstant()
                .getEpochSecond() - this.getInstant().getEpochSecond() % 50));
    }

    /**
     * Get the date with a different hour.
     *
     * @param hour The new hour.
     * @return The new date.
     */
    public SkyblockDateTime withHour(int hour) {
        return new SkyblockDateTime(Instant.ofEpochSecond(((this.getInstant()
                .getEpochSecond() - (this.getInstant().getEpochSecond() % SB_DAY)) + (hour * SB_HOUR))));
    }

    /**
     * Check if a specific skyblock even is currently active.
     *
     * @param events The skyblock event to check for.
     * @return If it is active.
     */
    public boolean isActive(SkyblockEvents events) {
        return switch (events) {
            case FARMING_CONTEST -> (getElapsedSkyblockDays() % 3) == 1;
            case DARK_AUCTION -> (getElapsedSkyblockDays() % 3) == 0;
            case STAR_CULT -> (getCurrentSkyblockDay() != 0) && ((getCurrentSkyblockDay() % 7) == 0);
            case NEW_YEAR -> (getCurrentSkyblockMonth() == 12) && (getCurrentSkyblockDay() >= 29);
            case TRAVELING_ZOO -> ((getCurrentSkyblockMonth() == 4) && (getCurrentSkyblockDay() <= 3))
                    || ((getCurrentSkyblockMonth() == 10) && (getCurrentSkyblockDay() <= 3));
            case SPOOKY_FESTIVAL -> getCurrentSkyblockMonth() == 8 && getCurrentSkyblockDay() >= 29;
            case WINTER_ISLAND -> getCurrentSkyblockMonth() == 12;
            case JERRY_WORKSHOP -> getCurrentSkyblockMonth() == 12 && getCurrentSkyblockDay() >= 24
                    && getCurrentSkyblockDay() <= 26;
            case ELECTION_START -> this.getCurrentSkyblockMonth() == 6 && this.getCurrentSkyblockDay() == 27;
            case ELECTION_CLOSE -> this.getCurrentSkyblockMonth() == 3 && this.getCurrentSkyblockDay() == 27;
            case ELECTION_OPEN ->
                    (this.getCurrentSkyblockMonth() < 3) || ((this.getCurrentSkyblockMonth() == 3) && (this.getCurrentSkyblockDay() < 27))
                            || (this.getCurrentSkyblockMonth() > 6) || ((this.getCurrentSkyblockMonth() == 6) && (this.getCurrentSkyblockDay() >= 27));
            default -> false;
        };
    }

    /**
     * Get the next time a skyblock event is active.
     *
     * @param skyblockEvents The skyblock event.
     * @return The next time.
     */
    public SkyblockDateTime getNext(SkyblockEvents skyblockEvents) {
        return switch (skyblockEvents) {
            case FARMING_CONTEST -> this.with(
                    0,
                    0,
                    (this.getCurrentSkyblockDay() + 3) - ((this.getCurrentSkyblockDay() % 3)) - 1,
                    getCurrentSkyblockMonth() - 1,
                    getSkyblockYear() - 1
            );
            case DARK_AUCTION -> this.with(
                    0,
                    0,
                    (this.getCurrentSkyblockDay() + 3) - ((this.getCurrentSkyblockDay() % 3)) - 2,
                    getCurrentSkyblockMonth() - 1,
                    getSkyblockYear() - 1
            );
            case STAR_CULT -> {
                if (this.getCurrentSkyblockDay() < 27) {
                    yield this.with(
                            0,
                            0,
                            this.getCurrentSkyblockDay() - (this.getCurrentSkyblockDay() % 7) + 6,
                            getCurrentSkyblockMonth() - 1,
                            getSkyblockYear() - 1
                    );
                } else {
                    yield this.with(
                            0,
                            0,
                            6,
                            getCurrentSkyblockMonth(),
                            getSkyblockYear() - 1
                    );
                }
            }
            case NEW_YEAR -> {
                if (this.isActive(SkyblockEvents.NEW_YEAR)) {
                    yield this.with(0, 0, 28, 11, this.getSkyblockYear());
                } else {
                    yield this.with(0, 0, 28, 11, this.getSkyblockYear() - 1);
                }
            }
            case TRAVELING_ZOO -> {
                if (this.getCurrentSkyblockMonth() == 10 && this.getCurrentSkyblockDay() > 3
                        || this.getCurrentSkyblockMonth() > 10 || this.getCurrentSkyblockMonth() < 3) {
                    yield this.with(0, 0, 0, 2, this.getSkyblockYear());
                } else {
                    yield this.with(0, 0, 0, 9, this.getSkyblockYear());
                }
            }
            case WINTER_ISLAND -> {
                if (getCurrentSkyblockMonth() == 12) {
                    yield this.with(0, 0, 0, 11, this.getSkyblockYear());
                } else {
                    yield this.with(0, 0, 0, 11, this.getSkyblockYear() - 1);
                }
            }
            case ELECTION_CLOSE -> {
                if (this.getCurrentSkyblockMonth() < 3 || this.getCurrentSkyblockMonth() == 3 && getCurrentSkyblockDay() < 27) {
                    yield this.with(0, 0, 26, 2, this.getSkyblockYear() - 1);
                } else {
                    yield this.with(0, 0, 26, 2, this.getSkyblockYear());
                }
            }
            case ELECTION_START, ELECTION_OPEN -> {
                if (this.getCurrentSkyblockMonth() < 6 || this.getCurrentSkyblockMonth() == 6 && this.getCurrentSkyblockDay() < 27) {
                    yield this.with(0, 0, 26, 5, this.getSkyblockYear() - 1);
                } else {
                    yield this.with(0, 0, 26, 5, this.getSkyblockYear());
                }
            }
            case JERRY_WORKSHOP -> {
                if (this.getCurrentSkyblockMonth() == 12 && this.getCurrentSkyblockDay() >= 24) {
                    yield this.with(0, 0, 23, 11, this.getSkyblockYear());
                } else {
                    yield this.with(0, 0, 23, 11, this.getSkyblockYear() - 1);
                }
            }
            case SPOOKY_FESTIVAL -> {
                if (this.getCurrentSkyblockMonth() > 8 || this.getCurrentSkyblockMonth() == 8 && this.getCurrentSkyblockDay() >= 29) {
                    yield this.with(0, 0, 28, 7, this.getSkyblockYear());
                } else {
                    yield this.with(0, 0, 28, 7, this.getSkyblockYear() - 1);
                }
            }
            default -> this;
        };
    }

    /**
     * Get the date as string.
     *
     * @return The date.
     */
    @Override
    public String toString() {
        return "%s:%S %s/%s/%s".formatted(
                getCurrentSkyblockHour(),
                getCurrentSkyblockMinute(),
                getCurrentSkyblockDay(),
                getCurrentSkyblockMonth(),
                getSkyblockYear()
        );
    }

    /**
     * Get the skyblock instant.
     *
     * @return The skyblock instant.
     */
    private long getSkyblockInstant() {
        return this.skyblockInstant.getEpochSecond();
    }

    /**
     * Get all skyblock minutes elapsed.
     *
     * @return The skyblock minutes.
     */
    private int getElapsedSkyblockMinutes() {
        return (int) Math.floor((this.getSkyblockInstant() / 0.83));
    }

    /**
     * Get the current skyblock minute.
     *
     * @return The skyblock minute.
     */
    private int getCurrentSkyblockMinute() {
        return (int) Math.floor((this.getSkyblockInstant() % 50) / 0.83);
    }

    /**
     * Get all skyblock hours elapsed.
     *
     * @return The skyblock hours.
     */
    private int getElapsedSkyblockHours() {
        return (int) Math.floor((this.getSkyblockInstant() / 50f));
    }

    /**
     * Get the current skyblock hour.
     *
     * @return The skyblock hour.
     */
    private int getCurrentSkyblockHour() {
        return (int) Math.floor((this.getSkyblockInstant() % (float) SB_DAY) / SB_HOUR);
    }

    /**
     * Get all skyblock days elapsed.
     *
     * @return The skyblock days.
     */
    private int getElapsedSkyblockDays() {
        return (int) Math.floor(this.getSkyblockInstant() / (float) SB_DAY);
    }

    /**
     * Get the current skyblock day.
     *
     * @return The skyblock day.
     */
    private int getCurrentSkyblockDay() {
        return (int) Math.floor((this.getSkyblockInstant() % (float) SB_MONTH) / SB_DAY) + 1;
    }

    /**
     * Get all skyblock months elapsed.
     *
     * @return The skyblock months.
     */
    private int getElapsedSkyblockMonths() {
        return (int) Math.floor(this.getSkyblockInstant() / (float) SB_MONTH);
    }

    /**
     * Get the current skyblock month.
     *
     * @return The skyblock month.
     */
    private int getCurrentSkyblockMonth() {
        return (int) Math.floor((this.getSkyblockInstant() % (float) SB_YEAR) / SB_MONTH) + 1;
    }

    /**
     * Get the current skyblock year.
     *
     * @return The current skyblock year.
     */
    private int getSkyblockYear() {
        return (int) Math.floor(this.getSkyblockInstant() / (float) SB_YEAR) + 1;
    }

    /**
     * Get the skyblock date with different values.
     *
     * @param minute The new minutes.
     * @param hour   The new hour.
     * @param day    The new day.
     * @param month  The new month.
     * @param year   The new year.
     * @return The new date.
     */
    @SuppressWarnings("SameParameterValue")
    private SkyblockDateTime with(int minute, int hour, int day, int month, int year) {
        return SkyblockDateTime.ofSkyblockInstant(Instant.ofEpochMilli((long) ((minute * 0.83 + hour * SB_HOUR + day * SB_DAY + month * SB_MONTH + year * SB_YEAR) * 1000)));
    }

    /**
     * All skyblock events supported.
     */
    public enum SkyblockEvents {
        FARMING_CONTEST,
        TRAVELING_ZOO,
        ELECTION_START,
        SPOOKY_FESTIVAL,
        ELECTION_OPEN,
        ELECTION_CLOSE,
        THUNDER,
        RAIN,
        JERRY_WORKSHOP,
        WINTER_ISLAND,
        DARK_AUCTION,
        NEW_YEAR,
        STAR_CULT
    }

}
