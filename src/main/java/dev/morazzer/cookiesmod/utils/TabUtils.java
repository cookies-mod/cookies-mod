package dev.morazzer.cookiesmod.utils;

import net.minecraft.client.network.PlayerListEntry;

public class TabUtils {


	public static boolean isInRange(int column, int start, int end, PlayerListEntry entry) {
		String name = entry.getProfile().getName();

		char first = name.charAt(1);
		if (first != (65 + column)) { // 65 = 'A'
			return false;
		}

		int c = name.charAt(3);
		return (start + 97 <= c) && (c <= end + 97);
	}

	public static int getRow(PlayerListEntry entry) {
		return entry.getProfile().getName().charAt(3) - 97;
	}

	public static int getColumn(PlayerListEntry entry) {
		return entry.getProfile().getName().charAt(1) - 65;
	}

}
