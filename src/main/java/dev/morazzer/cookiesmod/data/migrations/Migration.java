package dev.morazzer.cookiesmod.data.migrations;

/**
 * Generic migration for every type of data.
 *
 * @param <T> The type of the data.
 */
public interface Migration<T> {

    /**
     * Gets the unique number of the migration.
     *
     * @return The number.
     */
    long getNumber();

    /**
     * Applies the migration to the data.
     *
     * @param value The data to apply it to.
     */
    void apply(T value);

}
