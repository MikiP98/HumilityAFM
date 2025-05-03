package io.github.mikip98.humilityafm.datagen.language;

import io.github.mikip98.humilityafm.datagen.language.util.PrefixedHashMap;
import io.github.mikip98.humilityafm.datagen.language.util.TranslationCategory;
import io.github.mikip98.humilityafm.generators.CabinetBlockGenerator;
import io.github.mikip98.humilityafm.helpers.*;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import java.util.HashMap;
import java.util.Iterator;
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
        for (String variant : CabinetBlockGenerator.cabinetBlockVariantsNames) {;
            String formattedName = formatName(variant);
            translations.put("cabinet_block_" + variant, formattedName + " cabinet");
            translations.put("illuminated_cabinet_block_" + variant, formattedName + " illuminated cabinet");
        }
        return translations;
    }

    public static Map<String, String> generateWoodenMosaicTranslations() {
        Map<String, String> translations = new HashMap<>();
        for (String variant : WoodenMosaicHelper.woodenMosaicVariantsNames) {
            String key = "wooden_mosaic_" + variant;
            String value = formatName(variant) + " wooden mosaic";
            translations.put(key, value);
        }
        return translations;
    }

    public static Map<String, String> generateTerracottaTilesTranslations() {
        Map<String, String> translations = new HashMap<>();
        for (String variant : TerracottaTilesHelper.terracottaTilesVariantsNames) {
            String key = "terracotta_tiles_" + variant;
            String value = formatName(variant) + " terracotta tiles";
            translations.put(key, value);
        }
        return translations;
    }

    public static Map<String, String> generateInnerOuterStairsTranslations() {
        Map<String, String> translations = new HashMap<>();
        for (String variant : InnerOuterStairsHelper.innerOuterStairsBlockVariantsNames) {
            // Inner Stairs
            String key = "inner_stairs_" + variant;
            String value = formatName(variant) + " inner stairs";
            translations.put(key, value);
            // Outer Stairs
            key = "outer_stairs_" + variant;
            value = formatName(variant) + " outer stairs";
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
            String value = formatName(color) + " LED powder";
            translations.put(key, value);
        }
        return translations;
    }

    public static Map<String, String> generateCandlestickTranslations() {
        Map<String, String> translations = new HashMap<>();
        if (CandlestickHelper.candlestickVariantsNames == null) {
            throw new IllegalStateException("Candlestick variants names are not initialized. Remember to uncomment the datagen lines in HumilityAFM.java");
        }
        for (String metal : CandlestickHelper.metals) {
            String key = "candlestick_" + metal;
            String value = formatName(metal) + " candlestick";
            translations.put(key, value);
            translations.put(key + "_candle", value + " with candle");

            for (String color : MainHelper.vanillaWoolTypes) {
                translations.put(key + "_candle_" + color, value + " with " + formatName(color).toLowerCase() + " candle");
            }
        }
        return translations;
    }

    protected static String formatName(String name) {
        String[] words = name.split("_");
        Iterator<String> it = java.util.Arrays.stream(words).iterator();

        StringBuilder formattedName = new StringBuilder();
        String firstWord = it.next();
        formattedName.append(Character.toUpperCase(firstWord.charAt(0)))
                .append(firstWord.substring(1).toLowerCase())
                .append(" ");

        for (Iterator<String> iter = it; iter.hasNext(); ) {
            String word = iter.next();
            formattedName.append(word.toLowerCase()).append(" ");
        }
        return formattedName.toString().trim();
    }
}
