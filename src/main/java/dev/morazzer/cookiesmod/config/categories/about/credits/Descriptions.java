package dev.morazzer.cookiesmod.config.categories.about.credits;

import net.minecraft.text.Text;

import java.util.List;

public record Descriptions(List<Text> maintainer, List<Text> discordStaff, List<Text> contributionsCode, List<Text> contributionsArt,
                           List<Text> ideas, List<Text> libraries) {
}
