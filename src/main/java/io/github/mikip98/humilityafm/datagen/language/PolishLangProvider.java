package io.github.mikip98.humilityafm.datagen.language;

import io.github.mikip98.humilityafm.datagen.language.util.TranslationCategory;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import java.util.Hashtable;
import java.util.Map;

public class PolishLangProvider extends FabricLanguageProvider {
    public PolishLangProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "pl_pl");
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        for (Map.Entry<TranslationCategory, Map<String, String>> entry : generatePolishTranslations().entrySet()) {
            for (Map.Entry<String, String> subEntry : entry.getValue().entrySet()) {
                translationBuilder.add(subEntry.getKey(), subEntry.getValue());
            }
        }
    }

    public static Map<TranslationCategory, Map<String, String>> generatePolishTranslations() {
        LOGGER.info("Generating Polish translations for HumilityAFM");
        Map<TranslationCategory, Map<String, String>> categoryTranslations = USEnglishLangProvider.generateBaseUSEnglishTranslations();

        // Item groups
        LOGGER.info(" Replacing item groups translations EN -> PL");
        categoryTranslations.put(TranslationCategory.ITEM_GROUPS, Map.of(
                "itemGroup.cabinets", "Gabloty",
                "itemGroup.innerOuterStairs", "Schody wewnętrzne i zewnętrzne",
                "itemGroup.woodenMosaics", "Drewniane Mozaiki",
                "itemGroup.terracottaTiles", "Płytki z terakoty",
                "itemGroup.humilityMisc", "Różne (Humility)",
                // Optional item groups
                "itemGroup.leds", "LED-y",
                "itemGroup.candlesticks", "Świeczniki"
        ));

        // Blocks
        LOGGER.info(" Replacing block translations EN -> PL");
        Map<String, String> blockTranslations = categoryTranslations.get(TranslationCategory.BLOCKS);
        blockTranslations = new Hashtable<>(blockTranslations); // Without this line, fabric throws 'java.util.ConcurrentModificationException' exception
        for (Map.Entry<String, String> entry : blockTranslations.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            blockTranslations.put(
                    key,
                    value
                            // Test blocks
                            .replace("Test", "Testowy")
                            .replace("test", "testowy")
                            // Cabinet blocks
                            .replace("Illuminated Cabinet", "Podświetlana Gablota")
                            .replace("illuminated cabinet", "podświetlana gablota")
                            .replace("Cabinet", "Gablota")
                            .replace("cabinet", "gablota")
                            // Wooden Mosaics
                            .replace("Wooden Mosaic", "Drewniana Mozaika")
                            .replace("wooden mosaic", "drewniana mozaika")
                            // Terracotta Tiles
                            .replace("Terracotta Tiles", "Płytki z Terakoty")
                            .replace("terracotta tiles", "płytki z terakoty")
                            // Inner and outer stairs
                            .replace("Inner Stairs", "Schody Wewnętrzne")
                            .replace("inner stairs", "schody wewnętrzne")
                            .replace("Outer Stairs", "Schody Zewnętrzne")
                            .replace("outer stairs", "schody zewnętrzne")
                            // LEDs
                            .replace("LED Powder", "Proszek LED")
                            .replace("LED powder", "proszek LED")
                            // Candlesticks
                            .replace("Candlestick", "Świecznik")
                            .replace("candlestick", "świecznik")
                            .replace("With", "Z")
                            .replace("with", "z")
                            .replace("Candle", "Świeczką")
                            .replace("candle", "świeczką")
                    // TODO: Translate wood types, colours and 'block'
                    //  For cabinet blocks e.g. "Oak" -> "Dębowa", "Birch" -> "Brzozowa", etc.
                    //  For wooden mosaics the first wood type should end with 'owo' instead of 'owa'
                    //  As for colours
                    //  For cabinet blocks e.g. "White" -> "Biała", "Black" -> "Czarna", etc.
                    //  For terracotta tiles the first colour should end with 'o' , and the second one with 'e', instead of 'a'
            );
        }
        // Submit the block translations
        categoryTranslations.put(TranslationCategory.BLOCKS, blockTranslations);  // required because of line 43

        return categoryTranslations;
    }
}
