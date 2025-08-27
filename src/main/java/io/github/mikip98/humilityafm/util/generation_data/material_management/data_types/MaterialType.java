package io.github.mikip98.humilityafm.util.generation_data.material_management.data_types;

public enum MaterialType {
    FIREPROOF(true),
    BURNABLE(false),

    BURNABLE_WOOD(false),
    FIREPROOF_WOOD(true),
    STONY(true);


    public final boolean isFireproof;


    MaterialType(boolean isFireproof) {
        this.isFireproof = isFireproof;
    }
}
