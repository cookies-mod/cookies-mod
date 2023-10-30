package dev.morazzer.cookiesmod.config.categories.about;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.morazzer.cookiesmod.CookiesMod;
import dev.morazzer.cookiesmod.config.categories.about.credits.Contributors;
import dev.morazzer.cookiesmod.config.categories.about.credits.Credits;
import dev.morazzer.cookiesmod.config.categories.about.credits.Descriptions;
import dev.morazzer.cookiesmod.config.categories.about.credits.Library;
import dev.morazzer.cookiesmod.config.categories.about.credits.Person;
import dev.morazzer.cookiesmod.config.system.editor.ConfigOptionEditor;
import dev.morazzer.cookiesmod.utils.ColorUtils;
import dev.morazzer.cookiesmod.utils.ExceptionHandler;
import dev.morazzer.cookiesmod.utils.GsonUtils;
import dev.morazzer.cookiesmod.utils.render.RenderUtils;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * Editor to render the {@linkplain dev.morazzer.cookiesmod.config.categories.about.credits.Credits}.
 */
public class AboutEditor extends ConfigOptionEditor<Object, AboutOption> {

    final int personHeadSize = 40;
    private Credits credits;

    /**
     * Creates a new editor.
     *
     * @param option The option that uses this editor.
     */
    public AboutEditor(AboutOption option) {
        super(option);

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES)
                .registerReloadListener(new SimpleSynchronousResourceReloadListener() {
                    @Override
                    public Identifier getFabricId() {
                        return new Identifier("cookiesmod", "credits");
                    }

                    @Override
                    public void reload(ResourceManager manager) {
                        List<Resource> cookiesmodCredits = MinecraftClient.getInstance().getResourceManager()
                                .getAllResources(new Identifier("cookiesmod", "credits.json"));
                        if (cookiesmodCredits.isEmpty()) {
                            return;
                        }
                        Optional<Resource> fabric = cookiesmodCredits.stream()
                                .filter(resource -> resource.getPack().getName().equals("cookiesmod")).findFirst();

                        if (fabric.isEmpty()) {
                            return;
                        }

                        Resource resource = fabric.get();
                        byte[] bytes = ExceptionHandler.removeThrows(() -> resource.getInputStream().readAllBytes());
                        if (bytes == null) {
                            return;
                        }

                        JsonObject jsonObject = GsonUtils.gsonClean.fromJson(
                                new String(bytes, StandardCharsets.UTF_8),
                                JsonObject.class
                        );

                        List<Person> maintainers = createPersons(jsonObject.getAsJsonArray("maintainers"));
                        List<Library> libraries = createLibraries(jsonObject.getAsJsonArray("used_libraries"));
                        List<Person> discordStaff = createPersons(jsonObject.getAsJsonArray("discord_staff"));
                        discordStaff.sort(Comparator.comparingLong(o -> o.getUuid().getMostSignificantBits()));
                        discordStaff = Lists.reverse(discordStaff);
                        List<Person> contributorsCoding = createPersons(jsonObject.getAsJsonObject("contributors")
                                .getAsJsonArray("coding"));
                        contributorsCoding.sort(Comparator.comparingInt(Person::getNumber));
                        List<Person> contributorsArt = createPersons(jsonObject.getAsJsonObject("contributors")
                                .getAsJsonArray("art"));
                        Contributors contributors = new Contributors(contributorsCoding, contributorsArt);
                        List<Person> ideas = createPersons(jsonObject.getAsJsonArray("ideas"));
                        ideas.sort(Comparator.comparingInt(Person::getNumber));

                        JsonObject descriptionsObject = jsonObject.getAsJsonObject("descriptions");
                        Descriptions descriptions = new Descriptions(
                                getText(descriptionsObject.getAsJsonArray("maintainers")),
                                getText(descriptionsObject.getAsJsonArray("discord_staff")),
                                getText(descriptionsObject.getAsJsonArray("contributors_coding")),
                                getText(descriptionsObject.getAsJsonArray("contributors_art")),
                                getText(descriptionsObject.getAsJsonArray("ideas")),
                                getText(descriptionsObject.getAsJsonArray("libraries"))
                        );

                        credits = new Credits(maintainers, libraries, discordStaff, contributors, ideas, descriptions);
                    }
                });
    }

    @Override
    public int getHeight(int optionWidth) {
        int currentY = 40;
        currentY += getAllGroupHeightSum(optionWidth);
        currentY += 45;
        currentY += this.credits.libraries().size() * 12;

        return currentY + 5;
    }

    @Override
    public void render(@NotNull DrawContext drawContext, int mouseX, int mouseY, float tickDelta, int optionWidth) {
        super.render(drawContext, mouseX, mouseY, tickDelta, optionWidth);
        int currentY = 0;
        RenderUtils.renderTextCenteredScaled(
                drawContext,
                CookiesMod.createColor().append("Cookies Mod"),
                3,
                optionWidth / 2,
                currentY + 15,
                -1
        );

        currentY += 40;
        currentY = renderGroup(
                drawContext,
                CookiesMod.createColor().append("Maintainers"),
                this.credits.maintainers(),
                currentY,
                optionWidth
        );
        currentY = renderGroup(
                drawContext,
                CookiesMod.createColor().append("Discord Staff"),
                this.credits.discordStaff(),
                currentY,
                optionWidth
        );
        currentY = renderGroup(
                drawContext,
                CookiesMod.createColor().append("Contributors - Development"),
                this.credits.contributors().code(),
                currentY,
                optionWidth
        );
        currentY = renderGroup(
                drawContext,
                CookiesMod.createColor().append("Contributors - Art"),
                this.credits.contributors().art(),
                currentY,
                optionWidth
        );
        currentY = renderGroup(
                drawContext,
                CookiesMod.createColor().append("Ideas"),
                this.credits.ideas(),
                currentY,
                optionWidth
        );

        RenderUtils.renderTextCenteredScaled(
                drawContext,
                Text.literal("Used Libraries").setStyle(Style.EMPTY.withColor(ColorUtils.successColor)),
                2,
                optionWidth / 2,
                currentY + 15,
                -1
        );

        currentY += 45;

        for (Library library : this.credits.libraries()) {
            RenderUtils.renderTextScaled(
                    drawContext,
                    Text.literal(library.name()).formatted(Formatting.UNDERLINE, Formatting.BLUE),
                    1.2f,
                    14,
                    currentY,
                    -1,
                    true
            );
            currentY += 12;
        }
    }

    @Override
    public boolean doesMatchSearch(@NotNull String search) {
        return super.doesMatchSearch(search);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button, int optionWidth) {
        if (button != 0) {
            return false;
        }
        int currentY = 40;
        currentY += getAllGroupHeightSum(optionWidth);
        currentY += 45;
        for (Library library : this.credits.libraries()) {
            if (mouseY >= currentY && mouseY < currentY + 12) {
                Util.getOperatingSystem().open(library.url());
                return true;
            }
            currentY += 12;
        }

        return super.mouseClicked(mouseX, mouseY, button, optionWidth);
    }

    @Override
    public void renderOverlay(DrawContext drawContext, int mouseX, int mouseY, float tickDelta, int optionWidth) {
        ScreenRect peek = drawContext.scissorStack.stack.peek();
        drawContext.disableScissor();
        int currentY = 40;
        currentY = renderGroupOverlay(
                drawContext,
                this.credits.maintainers(),
                this.credits.descriptions().maintainer(),
                mouseX,
                mouseY,
                currentY,
                optionWidth
        );
        currentY = renderGroupOverlay(
                drawContext,
                this.credits.discordStaff(),
                this.credits.descriptions().discordStaff(),
                mouseX,
                mouseY,
                currentY,
                optionWidth
        );
        currentY = renderGroupOverlay(
                drawContext,
                this.credits.contributors().code(),
                this.credits.descriptions().contributionCode(), mouseX,
                mouseY,
                currentY,
                optionWidth
        );
        currentY = renderGroupOverlay(
                drawContext,
                this.credits.contributors().art(),
                this.credits.descriptions().contributionsArt(), mouseX,
                mouseY,
                currentY,
                optionWidth
        );
        currentY = renderGroupOverlay(
                drawContext,
                this.credits.ideas(),
                this.credits.descriptions().ideas(),
                mouseX,
                mouseY,
                currentY,
                optionWidth
        );
        if (mouseY >= currentY && mouseY < currentY + 45
                && mouseX > 0 && mouseX < optionWidth) {
            drawContext.drawTooltip(
                    getTextRenderer(),
                    Lists.transform(credits.descriptions().libraries(), Text::asOrderedText),
                    AboutTooltipPositioner.INSTANCE,
                    mouseX,
                    mouseY
            );
        }
        currentY += 45;
        for (Library library : this.credits.libraries()) {
            if (mouseX > 14
                    && mouseX < getTextRenderer().getWidth(library.name()) * 1.2f
                    && mouseY > currentY
                    && mouseY < currentY + 12
            ) {
                drawContext.drawTooltip(
                        getTextRenderer(),
                        Lists.transform(library.description(), Text::asOrderedText),
                        AboutTooltipPositioner.INSTANCE,
                        mouseX,
                        mouseY
                );
            }
            currentY += 12;
        }
        drawContext.scissorStack.push(peek);
        drawContext.setScissor(peek);
    }

    /**
     * Helper method
     * to create a list of {@linkplain dev.morazzer.cookiesmod.config.categories.about.credits.Person} from a {@linkplain com.google.gson.JsonArray}.
     *
     * @param jsonArray A json array containing people.
     * @return A list of people.
     */
    private List<Person> createPersons(JsonArray jsonArray) {
        LinkedList<Person> list = new LinkedList<>();


        for (JsonElement jsonElement : jsonArray) {
            if (!jsonElement.isJsonObject()) {
                continue;
            }

            list.add(Person.fromJson(jsonElement.getAsJsonObject()));
        }

        return list;
    }

    /**
     * Helper method
     * to create a list of {@linkplain dev.morazzer.cookiesmod.config.categories.about.credits.Library} from a {@linkplain com.google.gson.JsonArray}.
     *
     * @param jsonArray A json array containing libraries.
     * @return A list of libraries.
     */
    private List<Library> createLibraries(JsonArray jsonArray) {
        LinkedList<Library> list = new LinkedList<>();


        for (JsonElement jsonElement : jsonArray) {
            if (!jsonElement.isJsonObject()) {
                continue;
            }

            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String name = jsonObject.get("name").getAsString();
            List<Text> description = StreamSupport.stream(
                            jsonObject.getAsJsonArray("description").spliterator(),
                            false
                    )
                    .map(Text.Serializer::fromJson).map(Text.class::cast).toList();
            String url = jsonObject.get("url").getAsString();

            list.add(new Library(name, description, url));
        }

        return list;
    }

    /**
     * Helper method
     * to parse a {@linkplain com.google.gson.JsonArray} into a list of {@linkplain net.minecraft.text.Text}.
     *
     * @param jsonArray A json array with text.
     * @return A list of text.
     */
    private List<Text> getText(JsonArray jsonArray) {
        return StreamSupport.stream(
                        jsonArray.spliterator(),
                        false
                )
                .map(Text.Serializer::fromJson).map(Text.class::cast).toList();
    }

    /**
     * Render the overlay for a group.
     *
     * @param drawContext The current draw context instance.
     * @param people      A list of people in the group.
     * @param description A description of the group.
     * @param mouseX      The current x position of the mouse.
     * @param mouseY      The current y position of the mouse.
     * @param currentY    The current y offset.
     * @param optionWidth The width that should be rendered in.
     * @return The height that was used.
     */
    private int renderGroupOverlay(
            DrawContext drawContext,
            List<Person> people,
            List<Text> description,
            int mouseX,
            int mouseY,
            int currentY,
            int optionWidth
    ) {
        if (mouseY > currentY && mouseY < currentY + 45
                && mouseX > 0 && mouseX < optionWidth) {
            drawContext.drawTooltip(
                    getTextRenderer(),
                    Lists.transform(description, Text::asOrderedText),
                    AboutTooltipPositioner.INSTANCE,
                    mouseX,
                    mouseY
            );
        }
        currentY += 45;
        int personHeadSize = 40;
        int peopleInOneRow = (int) Math.max(1, Math.nextDown((float) (optionWidth - 10) / personHeadSize));
        int columnIndex = 0;

        for (Person person : people) {
            int x = 14 + columnIndex * 40;

            if (mouseX > x && mouseX < x + 40 && mouseY > currentY && mouseY < currentY + 40) {
                drawContext.drawTooltip(
                        getTextRenderer(),
                        Lists.transform(person.getDescription(), Text::asOrderedText),
                        AboutTooltipPositioner.INSTANCE,
                        mouseX,
                        mouseY
                );
            }

            columnIndex++;
            if (columnIndex > peopleInOneRow) {
                currentY += personHeadSize;
            }
        }
        return currentY + personHeadSize;
    }

    /**
     * Helper method to render the people in a group.
     *
     * @param drawContext The current draw context.
     * @param name        The name of the group.
     * @param people      The people in the group.
     * @param currentY    The current y offset.
     * @param optionWidth The width that should be rendered in.
     * @return The height that was used.
     */
    private int renderGroup(DrawContext drawContext, Text name, List<Person> people, int currentY, int optionWidth) {
        RenderUtils.renderTextCenteredScaled(drawContext, name, 2, optionWidth / 2, currentY + 15, -1);

        currentY += 45;

        int personHeadSize = 40;
        int peopleInOneRow = (int) Math.max(1, Math.nextDown((float) (optionWidth - 10) / personHeadSize));
        int columnIndex = 0;

        for (Person person : people) {
            drawContext.getMatrices().push();
            drawContext.getMatrices().scale(2, 2, 1);
            drawContext.drawItem(person.getItemStack(), (14 + columnIndex * 40) / 2, (currentY) / 2, 0, 10);
            drawContext.getMatrices().pop();
            RenderUtils.renderCenteredTextWithMaxWidth(
                    drawContext,
                    person.getName(),
                    40,
                    14 + 20 + columnIndex * 40,
                    currentY + 36,
                    -1,
                    true
            );
            columnIndex++;
            if (columnIndex > peopleInOneRow) {
                currentY += personHeadSize;
            }
        }
        return currentY + personHeadSize;
    }

    /**
     * Utility method the get the height of all groups combined.
     *
     * @param optionWidth The width the groups have to be rendered at.
     * @return The total height of all groups.
     */
    private int getAllGroupHeightSum(int optionWidth) {
        int currentY = 0;
        currentY += getGroupHeight(this.credits.maintainers(), optionWidth);
        currentY += getGroupHeight(this.credits.discordStaff(), optionWidth);
        currentY += getGroupHeight(this.credits.contributors().code(), optionWidth);
        currentY += getGroupHeight(this.credits.contributors().art(), optionWidth);
        currentY += getGroupHeight(this.credits.ideas(), optionWidth);
        return currentY;
    }

    /**
     * Utility method to get the height of a group.
     *
     * @param people      A list of people who will be displayed in the group.
     * @param optionWidth The width that the group will be rendered at.
     * @return The height that the group will use.
     */
    private int getGroupHeight(List<Person> people, int optionWidth) {
        int height = 45;
        int peopleInOneRow = (int) Math.max(1, Math.nextDown((float) (optionWidth - 10) / personHeadSize));

        return height + personHeadSize + (people.size() / peopleInOneRow) * personHeadSize;
    }

}
