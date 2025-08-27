package io.github.mikip98.humilityafm.util.generation_data.material_management.data_types;

import io.github.mikip98.humilityafm.util.mod_support.SupportedMods;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public record BlockMaterial(@NotNull Layer[] layers) {
    // Various constructors to create BlockMaterial instances
    public static BlockMaterial of(@NotNull Layer... layers) {
        return new BlockMaterial(layers);
    }
    public static BlockMaterial of(@NotNull Layer layer) {
        return new BlockMaterial(new Layer[]{layer});
    }
    public static BlockMaterial of(
            @NotNull String name,
            @NotNull Metadata metadata
    ) {
        return new BlockMaterial(new Layer[]{new Layer(name, metadata)});
    }
    public static BlockMaterial of(
            @NotNull String name1,
            @NotNull Metadata metadata1,
            @NotNull String name2,
            @NotNull Metadata metadata2
    ) {
        return new BlockMaterial(new Layer[]{
                new Layer(name1, metadata1),
                new Layer(name2, metadata2)
        });
    }


    /**
     * Returns a raw name of the block material, which is a concatenation of all layer names separated by underscores.
     * @return Raw name of the block material
     */
    public String getRawName() {
        return String.join("_", Arrays.stream(layers).map(Layer::name).toArray(String[]::new));
    }
    /**
     * Returns a safe name of the block material, which includes the source mod if present.
     * Each layer's name is prefixed with its source mod, if it exists.
     * This is useful for resource identifiers or similar purposes where uniqueness is required.
     * @return Safe name of the block material for use as an identifier or similar purpose.
     */
    public String getSafeName() {
        return String.join(
                "_",
                Arrays.stream(layers)
                        .map((layer) -> layer.metadata.sourceMod() != null ? layer.metadata.sourceMod().modId + "_" + layer.name : layer.name)
                        .toArray(String[]::new)
        );
    }


    public record Layer(String name, Metadata metadata) {}

    public record Metadata(@Nullable MaterialType type, @Nullable BlockStrength blockStrength, @Nullable SupportedMods sourceMod) {
        public static Metadata of(
                @Nullable MaterialType type,
                @Nullable BlockStrength blockStrength,
                @Nullable SupportedMods sourceMod
        ) {
            return new Metadata(type, blockStrength, sourceMod);
        }

        public static Metadata of(@Nullable MaterialType type, @Nullable BlockStrength blockStrength) {
            return new Metadata(type, blockStrength, null);
        }
        public static Metadata of(@Nullable MaterialType type, @Nullable SupportedMods sourceMod) {
            return new Metadata(type, null, sourceMod);
        }
        public static Metadata of(@Nullable BlockStrength blockStrength, @Nullable SupportedMods sourceMod) {
            return new Metadata(null, blockStrength, sourceMod);
        }

        public static Metadata of(@Nullable MaterialType type) {
            return new Metadata(type, null, null);
        }
        public static Metadata of(@Nullable BlockStrength blockStrength) {
            return new Metadata(null, blockStrength, null);
        }
        public static Metadata of(@Nullable SupportedMods sourceMod) {
            return new Metadata(null, null, sourceMod);
        }

        public static Metadata empty() { return new Metadata(null, null, null); }
    }
}
