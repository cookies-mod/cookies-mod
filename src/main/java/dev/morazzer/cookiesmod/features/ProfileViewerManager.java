package dev.morazzer.cookiesmod.features;

import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class ProfileViewerManager {

    private static final CopyOnWriteArrayList<UUID> lastSearches = new CopyOnWriteArrayList<>();

    public static void setLastSearch(UUID uuid) {
        lastSearches.remove(uuid);
        lastSearches.add(0, uuid);
        if (lastSearches.size() > 10) lastSearches.remove(lastSearches.size() - 1);
    }

    public static UUID getLastSearch() {
        if (lastSearches.isEmpty()) {
            return null;
        } else {
            return lastSearches.get(0);
        }
    }

}
