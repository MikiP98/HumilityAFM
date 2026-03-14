package io.github.mikip98.humilityafm.datagen.language;

import io.github.mikip98.humilityafm.datagen.language.util.Translation;
import io.github.mikip98.humilityafm.datagen.language.util.TranslationCategory;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
#if MC_VERSION >= 12006
import net.minecraft.registry.RegistryWrapper;
#endif

import java.util.Map;
#if MC_VERSION >= 12006
import java.util.concurrent.CompletableFuture;
#endif

public class UKEnglishLangProvider extends FabricLanguageProvider {
    #if MC_VERSION < 12006
    public UKEnglishLangProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "en_gb");
    }
    #else
    public UKEnglishLangProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "en_gb", registryLookup);
    }
    #endif

    @Override
    #if MC_VERSION < 12006
    public void generateTranslations(FabricLanguageProvider.TranslationBuilder translationBuilder) {
    #else
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, FabricLanguageProvider.TranslationBuilder translationBuilder) {
    #endif
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
