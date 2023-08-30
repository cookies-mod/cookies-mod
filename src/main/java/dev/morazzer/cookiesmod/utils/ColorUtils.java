package dev.morazzer.cookiesmod.utils;

import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class ColorUtils {

    public static final int mainColor = 0xE99DBE;
    public static final int failColor = 0xFF6961;
    public static final int successColor = 0x77DD77;

    private static final Identifier showStacktraceOnGradientHover = DevUtils.createIdentifier("show_stacktrace_on_gradient_hover");

    @SuppressWarnings("unused") // might be useful in the future
    public static MutableText literalWithGradientToAndBack(@NotNull String text, int borderColor, int middleColor) {
        return literalWithGradient3(text, borderColor, middleColor, borderColor);
    }

    public static MutableText literalWithGradient3(@NotNull String text, int startColor, int middleColor, int endColor) {
        MutableText firstHalf;
        MutableText secondHalf;

        String first = text.substring(0, text.length() / 2);
        String second = text.substring(text.length() / 2);

        if (startColor == middleColor) {
            firstHalf = Text.literal(first).setStyle(Style.EMPTY.withColor(middleColor));
        } else {
            firstHalf = literalWithGradient(first, startColor, middleColor);
        }

        if (middleColor == endColor) {
            secondHalf = Text.literal(second).setStyle(Style.EMPTY.withColor(endColor));
        } else {
            secondHalf = literalWithGradient(second, middleColor, endColor);
        }

        return Text.empty().append(firstHalf).append(secondHalf);
    }

    public static MutableText literalWithGradient(@NotNull String text, int startColor, int endColor) {
        if (startColor == endColor) return Text.literal(text).setStyle(Style.EMPTY.withColor(startColor));

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
        if (DevUtils.isEnabled(showStacktraceOnGradientHover)) hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(ExceptionHandler.getStacktrace(new Throwable())).formatted(Formatting.RED));

        for (int i = 0; i < text.length(); i++) {
            int color = 0;
            color |= ((redStart + red * i) & 0xFF) << 16;
            color |= ((greenStart + green * i) & 0xFF) << 8;
            color |= ((blueStart + blue * i) & 0xFF);

            MutableText mutableText = Text.literal(String.valueOf(text.charAt(i))).setStyle(Style.EMPTY.withColor(color));

            if (DevUtils.isEnabled(showStacktraceOnGradientHover)) mutableText.setStyle(mutableText.getStyle().withHoverEvent(hoverEvent));

            gradient.append(mutableText);
        }

        return gradient;
    }

}
