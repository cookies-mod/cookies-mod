package dev.morazzer.cookiesmod.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Methods to format numbers.
 */
public class NumberFormat {

    private static final DecimalFormat numberFormatter = new DecimalFormat(
        "###,###.###",
        DecimalFormatSymbols.getInstance(
            Locale.ENGLISH)
    );

    /**
     * Formats a number to a string.
     *
     * @param number The number.
     * @return The string.
     */
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

    /**
     * Formats a number to a formatted string.
     *
     * @param number The number.
     * @return The string.
     */
    public static String toFormattedString(int number) {
        return numberFormatter.format(number);
    }

    /**
     * Parses a number from a string.
     *
     * @param number The number as string.
     * @return The number.
     */
    public static long fromString(String number) {
        char lastCharacter = number.charAt(number.length() - 1);
        if (lastCharacter != 'k' && lastCharacter != 'm' && lastCharacter != 'b' && lastCharacter != 't') {
            return Long.parseLong(number);
        }

        double v = Double.parseDouble(number.trim().substring(0, number.length() - 1));
        return (long) switch (number.toLowerCase().charAt(number.length() - 1)) {
            case 'k' -> v * 1000;
            case 'm' -> v * 1000000;
            case 'b' -> v * 1000000000;
            case 't' -> v * 1000000000000L;
            default -> throw new IllegalStateException("Unexpected value: " + number.charAt(number.length() - 1));
        };
    }

}
