package dev.morazzer.cookiesmod.utils;

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

}
