package dev.morazzer.cookiesmod.utils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import org.jetbrains.annotations.Range;

/**
 * Helper methods to deal with roman numerals.
 */
@Getter
public enum RomanNumerals {
    I(1),
    IV(4),
    V(5),
    IX(9),
    X(10),
    XL(40),
    L(50),
    XC(90),
    C(100),
    CD(400),
    D(500),
    CM(900),
    M(1000);

    private final int value;

    RomanNumerals(int value) {
        this.value = value;
    }

    private static List<RomanNumerals> getReverseSortedValues() {
        return Arrays.stream(values())
            .sorted(Comparator.comparing((RomanNumerals e) -> e.value).reversed())
            .collect(Collectors.toList());
    }

    /**
     * Gets the decimal representation.
     *
     * @param input The roman number.
     * @return The arabic number.
     */
    public static int romanToArabic(String input) {
        String romanNumeral = input.toUpperCase();
        int result = 0;

        List<RomanNumerals> romanNumerals = RomanNumerals.getReverseSortedValues();

        int i = 0;

        while ((!romanNumeral.isEmpty()) && (i < romanNumerals.size())) {
            RomanNumerals symbol = romanNumerals.get(i);
            if (romanNumeral.startsWith(symbol.name())) {
                result += symbol.getValue();
                romanNumeral = romanNumeral.substring(symbol.name().length());
            } else {
                i++;
            }
        }

        if (!romanNumeral.isEmpty()) {
            return -1;
        }

        return result;
    }

    /**
     * Gets the roman number.
     *
     * @param number The arabic number.
     * @return The roman number.
     */
    public static String toRoman(@Range(from = 1, to = 3999) int number) {
        List<RomanNumerals> romanNumerals = RomanNumerals.getReverseSortedValues();

        int i = 0;
        StringBuilder sb = new StringBuilder();

        while ((number > 0) && (i < romanNumerals.size())) {
            RomanNumerals currentSymbol = romanNumerals.get(i);
            if (currentSymbol.getValue() <= number) {
                sb.append(currentSymbol.name());
                number -= currentSymbol.getValue();
            } else {
                i++;
            }
        }

        return sb.toString();
    }
}
