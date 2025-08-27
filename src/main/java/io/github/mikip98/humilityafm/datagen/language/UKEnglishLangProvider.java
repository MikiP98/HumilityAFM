package io.github.mikip98.humilityafm.datagen.language;

import io.github.mikip98.humilityafm.datagen.language.util.Translation;
import io.github.mikip98.humilityafm.datagen.language.util.TranslationCategory;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import java.util.Map;

public class UKEnglishLangProvider extends FabricLanguageProvider {
    public UKEnglishLangProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "en_gb");
    }

    @Override
    public void generateTranslations(FabricLanguageProvider.TranslationBuilder translationBuilder) {
        // Replace:
        // - "Gray" -> "Grey"

        for (Map.Entry<TranslationCategory, Map<String, String>> entry : USEnglishLangProvider.generateBaseUSEnglishTranslations().entrySet()) {
            for (Map.Entry<String, String> subEntry : entry.getValue().entrySet()) {
                final String key = subEntry.getKey();
                final Translation valueTranslation = new Translation(subEntry.getValue())
                        .translate("Gray", "Grey");
                translationBuilder.add(key, valueTranslation.getString());
            }
        }
    }
}
