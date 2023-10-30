package dev.morazzer.cookiesmod.utils;

import net.minecraft.client.network.PlayerListEntry;

public class TabUtils {

    /**
     * Whether the entry is in a specific range.
     *
     * @param column The column.
     * @param start  The start in the column.
     * @param end    The end in the column.
     * @param entry  The entry.
     * @return Whether the entry is in the range.
     */
    public static boolean isInRange(int column, int start, int end, PlayerListEntry entry) {
        String name = entry.getProfile().getName();
        // name = !A-a (!column-row)

        char first = name.charAt(1);
        if (first != (65 + column)) { // 65 = 'A'
            return false;
        }

        int c = name.charAt(3);
        return (start + 97 <= c) && (c <= end + 97); // 97 = 'a'
    }

    /**
     * Gets the row of the entry.
     *
     * @param entry The entry.
     * @return The row.
     */
    public static int getRow(PlayerListEntry entry) {
        return entry.getProfile().getName().charAt(3) - 97;
    }

    /**
     * Gets the column of the entry.
     *
     * @param entry The entry.
     * @return The column.
     */
    public static int getColumn(PlayerListEntry entry) {
        return entry.getProfile().getName().charAt(1) - 65;
    }

}
