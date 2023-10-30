package dev.morazzer.cookiesmod.mixin;

import net.minecraft.util.Identifier;

public interface ItemModelOverrides {

    Identifier ITEM_MODEL_PREDICATE_SKYBLOCK_ID = new Identifier("cookiesmod", "skyblock_id");
    Identifier ITEM_MODEL_PREDICATE_REFORGE = new Identifier("cookiesmod", "reforge");
    Identifier ITEM_MODEL_PREDICATE_DISPLAY_NAME = new Identifier("cookiesmod", "display_name");
    Identifier ITEM_MODEL_PREDICATE_DISABLE_RESPECT_FOR_OTHER = new Identifier(
            "cookiesmod",
            "disable_respecting_others"
    );

    Identifier[] ITEM_MODEL_PRECISE_MATCHES = new Identifier[] {ITEM_MODEL_PREDICATE_SKYBLOCK_ID, ITEM_MODEL_PREDICATE_REFORGE, ITEM_MODEL_PREDICATE_DISPLAY_NAME};
    Identifier[] ITEM_MODEL_SHOULD_RESPECT_OTHER_PREDICATES = new Identifier[] {ITEM_MODEL_PREDICATE_SKYBLOCK_ID, ITEM_MODEL_PREDICATE_REFORGE, ITEM_MODEL_PREDICATE_DISPLAY_NAME};

    /**
     * Whether the item model override should respect the other overrides.
     *
     * @param identifier The override identifier.
     * @return Whether it should respect others.
     */
    static boolean shouldRespectOthers(Identifier identifier) {
        for (Identifier itemModelPreciseMatch : ITEM_MODEL_SHOULD_RESPECT_OTHER_PREDICATES) {
            if (identifier.equals(itemModelPreciseMatch)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Whether the override should match the precise value instead of exact and higher.
     *
     * @param identifier The identifier.
     * @return Whether the value should match precise.
     */
    static boolean shouldMatchPrecise(Identifier identifier) {
        for (Identifier itemModelPreciseMatch : ITEM_MODEL_PRECISE_MATCHES) {
            if (identifier.equals(itemModelPreciseMatch)) {
                return true;
            }
        }
        return false;
    }

}
