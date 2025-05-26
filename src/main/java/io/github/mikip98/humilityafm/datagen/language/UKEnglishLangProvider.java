package io.github.mikip98.humilityafm.datagen.language;

import io.github.mikip98.humilityafm.datagen.language.util.TranslationCategory;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class UKEnglishLangProvider extends FabricLanguageProvider {
    public UKEnglishLangProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "en_gb", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, FabricLanguageProvider.TranslationBuilder translationBuilder) {
        // Replace:
        // - "Gray" -> "Grey"
        // - "gray" -> "grey"

        for (Map.Entry<TranslationCategory, Map<String, String>> entry : USEnglishLangProvider.generateBaseUSEnglishTranslations().entrySet()) {
            for (Map.Entry<String, String> subEntry : entry.getValue().entrySet()) {
                String key = subEntry.getKey();
                String value = subEntry.getValue()
                        .replace("Gray", "Grey")
                        .replace("gray", "grey");
                translationBuilder.add(key, value);
            }
        }
    }
}
