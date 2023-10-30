package dev.morazzer.cookiesmod.features.repository.items.item.attributes;

import dev.morazzer.cookiesmod.data.dungeons.EssenceType;
import net.minecraft.util.Identifier;

/**
 * The types of salvage results.
 */
@SuppressWarnings({"unused", "InstantiationOfUtilityClass"})
public class SalvageType<T> {

    public final static SalvageType<EssenceType> ESSENCE = new SalvageType<>();
    public final static SalvageType<Identifier> ITEM = new SalvageType<>();

}
