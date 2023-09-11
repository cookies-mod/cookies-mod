package dev.morazzer.cookiesmod.config.categories.about.credits;

import net.minecraft.text.Text;

import java.util.List;

public record Library(String name, List<Text> description, String url) {
}
