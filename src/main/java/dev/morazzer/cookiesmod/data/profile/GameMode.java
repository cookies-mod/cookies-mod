package dev.morazzer.cookiesmod.data.profile;

import java.util.Optional;

/**
 * The game mode of a skyblock profile.
 */
public enum GameMode {

    CLASSIC,
    IRONMAN("♲ Ironman"),
    STRANDED("☀ Stranded"),
    BINGO("Ⓑ Bingo"),
    UNSET,
    UNKNOWN("[^A-Za-z0-9⏣] .*");

    private final String symbol;

    GameMode() {
        this(null);
    }

    GameMode(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Gets the game mode based on the symbol.
     *
     * @param gameMode The symbol.
     * @return The game mode.
     */
    public static GameMode getByString(String gameMode) {
        if (gameMode.matches(IRONMAN.symbol)) {
            return IRONMAN;
        } else if (gameMode.matches(STRANDED.symbol)) {
            return STRANDED;
        } else if (gameMode.matches(BINGO.symbol)) {
            return BINGO;
        }
        return UNKNOWN;
    }

    /**
     * Get the symbol of the game mode.
     *
     * @return The symbol.
     */
    @SuppressWarnings("unused")
    public Optional<String> getSymbol() {
        return Optional.ofNullable(this.symbol);
    }

}
