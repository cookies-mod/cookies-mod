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

	public static long fromString(String number) {
		char lastCharacter = number.charAt(number.length() - 1);
		if (lastCharacter != 'k' && lastCharacter != 'm' && lastCharacter != 'b' && lastCharacter != 't') {
			return Long.parseLong(number);
		}

		double v = Double.parseDouble(number.trim().substring(0, number.length() - 1));
		return (long) switch (number.charAt(number.length() - 1)) {
			case 'k' -> v * 1000;
			case 'm' -> v * 1000000;
			case 'b' -> v * 1000000000;
			case 't' -> v * 1000000000000L;
			default -> throw new IllegalStateException("Unexpected value: " + number.charAt(number.length() - 1));
		};
	}
}