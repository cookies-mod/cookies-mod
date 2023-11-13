package dev.morazzer.cookiesmod.utils;

import java.time.temporal.ChronoUnit;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * Various constants and methods related to colors.
 */
public class ColorUtils {

    public static final int mainColor = 0xFFE99DBE;
    public static final int failColor = 0xFFFF6961;
    public static final int successColor = 0xFF77DD77;

    private static final Identifier showStacktraceOnGradientHover = DevUtils.createIdentifier(
        "show_stacktrace_on_gradient_hover");


    /**
     * Gets a color between red and green based on the time. The default timespan is one day.
     *
     * @param seconds The missing seconds.
     * @return The color as rgb int.
     */
    public static int getColor(long seconds) {
        return getColor(seconds, ChronoUnit.DAYS.getDuration().getSeconds());
    }

    /**
     * Gets a color between red and green based on the time.
     *
     * @param seconds   The missing seconds.
     * @param maxAmount The total time.
     * @return The color as rgb int.
     */
    public static int getColor(long seconds, float maxAmount) {
        return getColor(seconds, maxAmount, 0xFF5757, 0x8DF3B1);
    }

    /**
     * Gets a color between two supplied colors based on the time.
     *
     * @param seconds    The missing seconds.
     * @param maxAmount  The total time.
     * @param startColor The start color of the time.
     * @param endColor   The end color of the time.
     * @return The color as rgb int.
     */
    public static int getColor(long seconds, float maxAmount, int startColor, int endColor) {
        float percentage = seconds / maxAmount;

        int redStart = (startColor >> 16) & 0xFF;
        int greenStart = (startColor >> 8) & 0xFF;
        int blueStart = (startColor) & 0xFF;

        int red = (int) ((((endColor >> 16) & 0xFF) - redStart) * percentage);
        int green = (int) ((((endColor >> 8) & 0xFF) - greenStart) * percentage);
        int blue = (int) ((((endColor) & 0xFF) - blueStart) * percentage);

        return (redStart + red) << 16 | (greenStart + green) << 8 | (blueStart + blue);
    }


    /**
     * Gets a text with an applied gradient.
     *
     * @param text       The text.
     * @param startColor The start color of the gradient.
     * @param endColor   The end color of the gradient.
     * @return The text with an applied gradient.
     */
    public static MutableText literalWithGradient(@NotNull String text, int startColor, int endColor) {
        if (startColor == endColor) {
            return Text.literal(text).setStyle(Style.EMPTY.withColor(startColor));
        }

        int redStart = (startColor >> 16) & 0xFF;
        int greenStart = (startColor >> 8) & 0xFF;
        int blueStart = (startColor) & 0xFF;

        int redDifference = ((endColor >> 16) & 0xFF) - redStart;
        int greenDifference = ((endColor >> 8) & 0xFF) - greenStart;
        int blueDifference = ((endColor) & 0xFF) - blueStart;

        int length = text.length();

        int red = redDifference / length;
        int green = greenDifference / length;
        int blue = blueDifference / length;

        MutableText gradient = Text.empty().setStyle(Style.EMPTY.withColor(endColor));

        HoverEvent hoverEvent = null;
        if (DevUtils.isEnabled(showStacktraceOnGradientHover)) {
            hoverEvent = new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                Text.literal(ExceptionHandler.getStacktrace(new Throwable())).formatted(Formatting.RED)
            );
        }

        for (int i = 0; i < text.length(); i++) {
            int color = 0;
            color |= ((redStart + red * i) & 0xFF) << 16;
            color |= ((greenStart + green * i) & 0xFF) << 8;
            color |= ((blueStart + blue * i) & 0xFF);

            MutableText mutableText = Text
                .literal(String.valueOf(text.charAt(i)))
                .setStyle(Style.EMPTY.withColor(color));

            if (DevUtils.isEnabled(showStacktraceOnGradientHover)) {
                mutableText.setStyle(mutableText.getStyle().withHoverEvent(hoverEvent));
            }

            gradient.append(mutableText);
        }

        return gradient;
    }

}
