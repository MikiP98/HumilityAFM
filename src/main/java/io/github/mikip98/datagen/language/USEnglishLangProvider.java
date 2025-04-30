package io.github.mikip98.datagen.language;

import io.github.mikip98.helpers.*;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import java.util.HashMap;
import java.util.Map;

import static io.github.mikip98.HumilityAFM.MOD_ID;

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
        Map<TranslationCategory, Map<String, String>> categoryTranslations = new HashMap<>();

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

        // Blocks
        Map<String, String> blockTranslations = new PrefixedHashMap<>("block." + MOD_ID + ".");
        // Cabinet blocks
        blockTranslations.put("cabinet_block", "Test Cabinet Block");  // Manual testing block
        blockTranslations.put("illuminated_cabinet_block", "Test Illuminated Cabinet Block");  // Manual testing block
        blockTranslations.putAll(generateCabinetTranslations());
        // Wooden Mosaics
        blockTranslations.put("wooden_mosaic", "Test Wooden Mosaic");  // Manual testing block
        blockTranslations.putAll(generateWoodenMosaicTranslations());
        // Terracotta Tiles
        blockTranslations.putAll(generateTerracottaTilesTranslations());
        // Inner and outer stairs
        blockTranslations.put("inner_stairs", "Test Inner Stairs");  // Manual testing block
        blockTranslations.put("outer_stairs", "Test Outer Stairs");  // Manual testing block
        blockTranslations.putAll(generateInnerOuterStairsTranslations());
        // Miscellaneous blocks
        blockTranslations.putAll(generateMiscTranslations());
        // LEDs
        blockTranslations.put("led", "Test LED");  // Manual testing block
        blockTranslations.putAll(generateLEDTranslations());
        // Candlesticks
        blockTranslations.putAll(generateCandlestickTranslations());
        // Submit the block translations
        categoryTranslations.put(TranslationCategory.BLOCKS, blockTranslations);

        // Items
        Map<String, String> itemTranslations = new PrefixedHashMap<>("item." + MOD_ID + ".");
        // LED Powders
        itemTranslations.putAll(generateLEDPowderTranslations());
        // Submit the item translations
        categoryTranslations.put(TranslationCategory.ITEMS, itemTranslations);

        return categoryTranslations;
    }
//    @SuppressWarnings("unchecked")
//    protected static class BlockHashMap<K, V> extends HashMap<K, V>{
//        public BlockHashMap() { super(); }
//        @Override
//        public V put(K key,V value) {
//            return super.put((K) ("block." + MOD_ID + "." + key), value);
//        }
//        @Override
//        public void putAll(Map<? extends K, ? extends V> m) {
//            for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
//                put(entry.getKey(), entry.getValue());
//            }
//        }
//    }

    public static Map<String, String> generateCabinetTranslations() {
        Map<String, String> translations = new HashMap<>();
        for (String variant : CabinetBlockHelper.cabinetBlockVariantsNames) {;
            String formattedName = formatName(variant);
            translations.put("cabinet_block_" + variant, formattedName + " Cabinet");
            translations.put("illuminated_cabinet_block_" + variant, formattedName + " Illuminated Cabinet");
        }
        return translations;
    }

    public static Map<String, String> generateWoodenMosaicTranslations() {
        Map<String, String> translations = new HashMap<>();
        for (String variant : WoodenMosaicHelper.woodenMosaicVariantsNames) {
            String key = "wooden_mosaic_" + variant;
            String value = formatName(variant) + " Wooden Mosaic";
            translations.put(key, value);
        }
        return translations;
    }

    public static Map<String, String> generateTerracottaTilesTranslations() {
        Map<String, String> translations = new HashMap<>();
        for (String variant : TerracottaTilesHelper.terracottaTilesVariantsNames) {
            String key = "terracotta_tiles_" + variant;
            String value = formatName(variant) + " Terracotta Tiles";
            translations.put(key, value);
        }
        return translations;
    }

    public static Map<String, String> generateInnerOuterStairsTranslations() {
        Map<String, String> translations = new HashMap<>();
        for (String variant : InnerOuterStairsHelper.innerOuterStairsBlockVariantsNames) {
            // Inner Stairs
            String key = "inner_stairs_" + variant;
            String value = formatName(variant) + " Inner Stairs";
            translations.put(key, value);
            // Outer Stairs
            key = "outer_stairs_" + variant;
            value = formatName(variant) + " Outer Stairs";
            translations.put(key, value);
        }
        return translations;
    }

    public static Map<String, String> generateMiscTranslations() {
        Map<String, String> translations = new HashMap<>();
        translations.put("jack_o_lantern_redstone", "Redstone Jack o'Lantern");
        translations.put("jack_o_lantern_soul", "Soul Jack o'Lantern");
        return translations;
    }

    public static Map<String, String> generateLEDTranslations() {
        Map<String, String> translations = new HashMap<>();
        for (String color : LEDHelper.colors) {
            String key = "led_" + color;
            String value = formatName(color) + " LED";
            translations.put(key, value);
        }
        return translations;
    }
    public static Map<String, String> generateLEDPowderTranslations() {
        Map<String, String> translations = new HashMap<>();
        for (String color : LEDHelper.colors) {
            String key = "led_powder_" + color;
            String value = formatName(color) + " LED Powder";
            translations.put(key, value);
        }
        return translations;
    }

    public static Map<String, String> generateCandlestickTranslations() {
        Map<String, String> translations = new HashMap<>();
        if (CandlestickHelper.candlestickVariantsNames == null) {
            LOGGER.error("Unable to generate candlestick translations: candlestickVariantsNames is null");
            return translations;
        }
        for (String variant : CandlestickHelper.candlestickVariantsNames) {
            String key = "candlestick_" + variant;
            String value = formatName(variant) + " Candlestick";
            translations.put(key, value);
        }
        return translations;
    }

    protected static String formatName(String name) {
        String[] words = name.split("_");
        StringBuilder formattedName = new StringBuilder();
        for (String word : words) {
            formattedName.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1).toLowerCase())
                    .append(" ");
        }
        return formattedName.toString().trim();
    }
}
