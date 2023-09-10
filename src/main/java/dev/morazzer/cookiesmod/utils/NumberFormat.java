package dev.morazzer.cookiesmod.utils;

public class NumberFormat {

    public static String toString(double number) {
        if (number >= 1_000_000_000_000d) {
            return "%.2fT".formatted(number / 1_000_000_000_000d);
        } else if (number >= 1_000_000_000) {
            return "%.2fB".formatted(number / 1_000_000_000d);
        } else if (number >= 1_000_000) {
            return "%.2fm".formatted(number / 1_000_000d);
        } else if (number >= 1_000) {
            return "%.2fk".formatted(number / 1_000d);
        }

        return String.valueOf((int) number);
    }
}
