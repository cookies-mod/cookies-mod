package dev.morazzer.cookiesmod.features.repository.items.tags;

import net.minecraft.util.Identifier;

import java.util.List;

public record Tag(List<Identifier> identifiers, Identifier key) {
}
