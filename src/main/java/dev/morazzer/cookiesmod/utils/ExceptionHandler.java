package dev.morazzer.cookiesmod.utils;

import dev.morazzer.cookiesmod.CookiesMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;

public class ExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger("cookies-exception-handler");


    public static void handleException(Throwable exception) {
        LOGGER.error("An exception occurred", exception);
        if (MinecraftClient.getInstance().player != null) {
            String stackTrace = getStacktrace(exception);
            String copy = """
                    ```
                    Version: %s
                    VM: %s
                    Mod: %s
                    Exception type: %s
                                        
                    --------------------------
                                        
                    Stacktrace:
                    %s
                    ```
                    """.formatted(
                    MinecraftClient.getInstance().getGameVersion(),
                    ManagementFactory.getRuntimeMXBean().getVmVendor() + " " + ManagementFactory.getRuntimeMXBean().getVmName() + " " + ManagementFactory.getRuntimeMXBean().getVmVersion(),
                    "Cookies mod",
                    exception.getClass().getSimpleName(),
                    stackTrace
            );
            MinecraftClient.getInstance().player.sendMessage(
                    CookiesMod.createPrefix(ColorUtils.failColor)
                            .append(Text.literal("An internal error occurred please report this on our discord. (Click to copy)")
                                    .styled(style -> style.withColor(ColorUtils.failColor)
                                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("%s: %s".formatted(exception.getClass().getName(), exception.getMessage()))))
                                            .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, copy))
                                    )
                            )
            );
        }


    }

    public static String getStacktrace(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }

    @FunctionalInterface
    public interface ThrowableFunction<T> {
        T run() throws Throwable;
    }

    public static boolean tryCatch(ThrowableFunction<?> throwableFunction) {
        try {
            throwableFunction.run();
            return true;
        } catch (Throwable exception){
            handleException(exception);
            return false;
        }
    }

    public static <T> T removeThrows(ThrowableFunction<T> throwableFunction) {
        return removeThrows(throwableFunction, null);
    }
    public static <T> T removeThrows(ThrowableFunction<T> throwableFunction, T defaultObject) {
        try {
            return throwableFunction.run();
        } catch (Throwable e) {
            handleException(e);
            return defaultObject;
        }
    }
}