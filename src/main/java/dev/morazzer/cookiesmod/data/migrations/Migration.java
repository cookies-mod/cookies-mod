package dev.morazzer.cookiesmod.data.migrations;

/**
 * Generic migration for every type of data.
 *
 * @param <T> The type of the data.
 */
public interface Migration<T> {

    /**
     * Gets the unique number of the migration.
     * This number incrementally counts up with each migration
     * and is used to track whether a migration is already applied or not.
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
