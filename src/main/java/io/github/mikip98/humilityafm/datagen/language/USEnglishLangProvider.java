package io.github.mikip98.humilityafm.datagen.language;

import io.github.mikip98.humilityafm.datagen.language.util.TranslationCategory;
import io.github.mikip98.humilityafm.datagen.language.util.TranslationHashMap;
import io.github.mikip98.humilityafm.datagen.language.util.TranslatorBase;
import io.github.mikip98.humilityafm.registries.BlockRegistry;
import io.github.mikip98.humilityafm.registries.ItemRegistry;
import io.github.mikip98.humilityafm.util.generation_data.ActiveGenerationData;
import io.github.mikip98.humilityafm.util.generation_data.material_management.data_types.BlockMaterial;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import static io.github.mikip98.humilityafm.HumilityAFM.MOD_ID;

public class USEnglishLangProvider extends FabricLanguageProvider {
    public USEnglishLangProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "en_us");
    }

    @Override
    public void generateTranslations(FabricLanguageProvider.TranslationBuilder translationBuilder) {
        for (Map.Entry<TranslationCategory, Map<String, String>> entry : generateBaseUSEnglishTranslations().entrySet()) {
            for (Map.Entry<String, String> subEntry : entry.getValue().entrySet()) {
                translationBuilder.add(subEntry.getKey(), subEntry.getValue());
            }
        }
    }

    public static Map<TranslationCategory, Map<String, String>> generateBaseUSEnglishTranslations() {
        Map<TranslationCategory, Map<String, String>> categoryTranslations = new EnumMap<>(TranslationCategory.class);

        // Item groups
        categoryTranslations.put(TranslationCategory.ITEM_GROUPS, Map.of(
                "itemGroup.cabinets", "Cabinets",
                "itemGroup.innerOuterStairs", "Inner and Outer Stairs",
                "itemGroup.woodenMosaics", "Wooden Mosaics",
                "itemGroup.terracottaTiles", "Terracotta Tiles",
                "itemGroup.humilityMisc", "Miscellaneous (Humility)",
                // Optional item groups
                "itemGroup.leds", "LEDs",
                "itemGroup.candlesticks", "Candlesticks"
        ));

        // Submit the block translations
        categoryTranslations.put(TranslationCategory.BLOCKS, new BlockTranslator().getBlockTranslations());

        // Submit the item translations
        categoryTranslations.put(TranslationCategory.ITEMS, new ItemTranslator().getItemTranslations());

        return categoryTranslations;
    }

    protected static class BlockTranslator extends TranslatorBase {
        public BlockTranslator() {
            super(new TranslationHashMap("block." + MOD_ID + "."));
        }

        public TranslationHashMap getBlockTranslations() {
            // GENERAL
            generateCabinetTranslations();
            generateWoodenMosaicTranslations();
            generateTerracottaTilesTranslations();
            generateForcedCornerStairsTranslations();
            generateSpecialJackOLanternTranslations();

            // CANDLESTICK BETA
            generateCandlestickTranslations();

            // COLOURED FEATURE SET BETA
            generateColouredTorchesTranslations();
            generateLightStripTranslations();
            generateColouredJackOLanternTranslations();

            return translations;
        }

        public void generateCabinetTranslations() {
            translations.put(ItemRegistry.CABINET_ITEM, "Test Cabinet Block");  // Manual testing block
            translations.put(ItemRegistry.ILLUMINATED_CABINET_ITEM, "Test Illuminated Cabinet Block");  // Manual testing block
            final String idPrefix1 = "cabinet_block_";
            final String idPrefix2 = "illuminated_cabinet_block_";
            final String nameSuffix1 = " cabinet";
            final String nameSuffix2 = " illuminated cabinet";
            enterTranslations(ActiveGenerationData.cabinetVariantMaterials, idPrefix1, nameSuffix1);
            enterTranslations(ActiveGenerationData.cabinetVariantMaterials, idPrefix2, nameSuffix2);
        }

        public void generateWoodenMosaicTranslations() {
            final String idPrefix = "wooden_mosaic_";
            final String nameSuffix = " wooden mosaic";
            enterTranslations(ActiveGenerationData.colouredFeatureSetMaterials, idPrefix, nameSuffix);
        }

        public void generateTerracottaTilesTranslations() {
            final String idPrefix = "terracotta_tiles_";
            final String nameSuffix = " terracotta tiles";
            enterTranslations(ActiveGenerationData.colouredFeatureSetMaterials, idPrefix, nameSuffix);
        }

        public void generateForcedCornerStairsTranslations() {
            translations.put(BlockRegistry.INNER_STAIRS, "Test Inner Stairs");  // Manual testing block
            translations.put(BlockRegistry.OUTER_STAIRS, "Test Outer Stairs");  // Manual testing block
            final String idPrefix1 = "inner_stairs_";
            final String idPrefix2 = "outer_stairs_";
            final String nameSuffix1 = " inner stairs";
            final String nameSuffix2 = " outer stairs";
            enterTranslations(ActiveGenerationData.colouredFeatureSetMaterials, idPrefix1, nameSuffix1);
            enterTranslations(ActiveGenerationData.colouredFeatureSetMaterials, idPrefix2, nameSuffix2);
        }

        public void generateSpecialJackOLanternTranslations() {
            translations.put("jack_o_lantern_redstone", "Redstone Jack o'Lantern");
            translations.put("jack_o_lantern_soul", "Soul Jack o'Lantern");
        }
        public void generateColouredJackOLanternTranslations() {
            final String idPrefix = "jack_o_lantern_";
            final String nameSuffix = " Jack o'Lantern";
            enterTranslations(ActiveGenerationData.colouredFeatureSetMaterials, idPrefix, nameSuffix);
        }

        // TODO: Figure out how to simplify this effectively
        public void generateColouredTorchesTranslations() {
            for (BlockMaterial material : ActiveGenerationData.colouredFeatureSetMaterials) {
                String idPrefix = "coloured_torch_" + material.getSafeName();
                String name = formatName(material.getRawName()).toLowerCase() + " torch";
                translations.put(idPrefix + "_weak", "Weak " + name.toLowerCase());
                translations.put(idPrefix, name);
                translations.put(idPrefix + "_strong", "Strong " + name.toLowerCase());
            }
        }
        public void generateLightStripTranslations() {
            final String idPrefix = "light_strip_";
            final String nameSuffix = " Light Strip";
            enterTranslations(ActiveGenerationData.colouredFeatureSetMaterials, idPrefix, nameSuffix);
        }

        public void generateCandlestickTranslations() {
            final String idPrefix = "candlestick_";
            final String nameSuffix = " candlestick";
            enterTranslations(ActiveGenerationData.simpleCandlestickMaterials, idPrefix, nameSuffix);
            Arrays.stream(ActiveGenerationData.rustingCandlestickMaterials).forEach(
                    (materialSet) -> enterTranslations(materialSet, idPrefix, nameSuffix)
            );
        }
    }

    protected static class ItemTranslator extends TranslatorBase {
        public ItemTranslator() {
            super(new TranslationHashMap("item." + MOD_ID + "."));
        }

        public TranslationHashMap getItemTranslations() {
            // LED Powders
            generateGlowingPowderTranslations();

            return translations;
        }

        public void generateGlowingPowderTranslations() {
            final String idPrefix = "glowing_powder_";
            final String nameSuffix = " glowing powder";
            enterTranslations(ActiveGenerationData.colouredFeatureSetMaterials, idPrefix, nameSuffix);
        }
    }
}
