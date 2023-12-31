package dev.morazzer.cookiesmod.utils;

/**
 * Various methods related to strings and string manipulation.
 */
public class StringUtils {

    /**
     * Escapes all non ascii characters.
     *
     * @param original The text to escape.
     * @return The escaped text.
     */
    public static String escapeUnicode(String original) {
        StringBuilder escaped = new StringBuilder();

        for (int i = 0; i < original.length(); i++) {
            char c = original.charAt(i);
            String replacement = Character.toString(c);
            if (c > 127) {
                replacement = String.format("\\u%04x", (int) c);
            }
            escaped.append(replacement);
        }

        return escaped.toString();
    }

    /**
     * Returns the bytes as a hex string with lowercase characters.
     *
     * @param bytes The bytes to convert.
     * @return The string.
     */
    public static String hex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte singleByte : bytes) {
            result.append(String.format("%02x", singleByte));
        }
        return result.toString();
    }

}
