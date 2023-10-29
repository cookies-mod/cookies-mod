package dev.morazzer.cookiesmod.utils.maths;

/**
 * A small collection of math functions.
 */
public class MathUtils {

    private static final float sigmoidA = -1 / (sigmoid(-4) - sigmoid(4));

    /**
     * Sigmoid between zero and one.
     *
     * @param value The argument.
     * @return The value.
     */
    public static float sigmoidZeroOne(float value) {
        return sigmoidA * sigmoid(8 * (Math.max(0, Math.min(value, 1)) - 0.5f));
    }

    /**
     * Sigmoid function.
     *
     * @param value The argument.
     * @return The value.
     */
    public static float sigmoid(float value) {
        return (float) (1 / (1 + Math.exp(-value)));
    }

}
