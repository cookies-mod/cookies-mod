package dev.morazzer.cookiesmod.data.profile;

import java.util.Optional;

public enum GameMode {

    CLASSIC, IRONMAN("\u2672 Ironman"), STRANDED("\u2600 Stranded"), BINGO("\u24B7 Bingo"), UNSET, UNKNOWN("[^A-Za-z0-9\u23E3] .*");

    private final String symbol;

    GameMode() {
        this.symbol = "";
    }

    GameMode(String symbol) {
        this.symbol = symbol;
    }

    @SuppressWarnings("unused")
    public Optional<String> getSymbol() {
        return Optional.ofNullable(this.symbol);
    }

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

}
