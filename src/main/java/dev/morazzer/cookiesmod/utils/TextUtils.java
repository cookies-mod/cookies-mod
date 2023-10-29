package dev.morazzer.cookiesmod.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.StringUtils;

public class TextUtils {

    /**
     * Pretty print a json element.
     *
     * @param jsonElement The json element to pretty print.
     * @return The text.
     */
    public static Text prettyPrintJson(JsonElement jsonElement) {
        MutableText text = Text.empty();
        text.append(toText(jsonElement, 1));
        return text;
    }

    /**
     * Turn a json element into text.
     *
     * @param jsonElement The json element.
     * @param depth       The depth of the recursive call.
     * @return The text.
     */
    private static Text toText(JsonElement jsonElement, int depth) {
        if (jsonElement instanceof JsonObject jsonObject) {
            MutableText object = Text.empty().append(Text.literal("{").formatted(Formatting.GOLD)).append("\n");
            int index = 0;
            for (String key : jsonObject.keySet()) {
                object.append(StringUtils.repeat(' ', depth));
                object.append("\"");
                object.append(Text.literal(key).formatted(Formatting.GREEN));
                object.append("\"").append(": ");
                object.append(toText(((JsonObject) jsonElement).get(key), depth + 1));
                if (index + 1 < jsonObject.keySet().size()) {
                    object.append(",");
                }
                object.append("\n");
                index++;
            }
            object.append(StringUtils.repeat(' ', depth - 1));
            object.append(Text.literal("}").formatted(Formatting.GOLD));
            return object;
        } else if (jsonElement instanceof JsonArray jsonArray) {
            MutableText array = Text.empty().append(Text.literal("[").formatted(Formatting.GOLD)).append("\n");
            int index = 0;
            for (JsonElement element : jsonArray) {
                array.append(StringUtils.repeat(' ', depth));
                array.append(toText(element, depth + 1));
                if (index + 1 < jsonArray.size()) {
                    array.append(",");
                }
                array.append("\n");
                index++;
            }
        } else if (jsonElement instanceof JsonPrimitive jsonPrimitive) {
            if (jsonPrimitive.isBoolean()) {
                return Text.literal(jsonPrimitive.getAsString())
                        .formatted(jsonPrimitive.getAsBoolean() ? Formatting.GREEN : Formatting.RED);
            } else if (jsonPrimitive.isNumber()) {
                return Text.literal(jsonPrimitive.getAsString()).formatted(Formatting.DARK_PURPLE);
            } else if (jsonPrimitive.isJsonNull()) {
                return Text.literal("null").formatted(Formatting.DARK_RED);
            } else if (jsonPrimitive.isString()) {
                String content = jsonPrimitive.getAsString();
                MutableText literal = Text.literal(content);
                if (content.startsWith("http")) {
                    literal.setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, content)));
                }
                return Text.literal("\"").append(literal.formatted(Formatting.GREEN)).append("\"");
            }
        }
        return Text.empty();
    }

}
