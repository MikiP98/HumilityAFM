package io.github.mikip98.humilityafm.config;

import io.github.mikip98.humilityafm.content.block_entity_renderers.LightStripBlockEntityRenderer;
import io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.FloorIlluminatedCabinetBlockEntityRenderer;
import io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.IlluminatedCabinetBlockEntityRenderer;
import io.github.mikip98.humilityafm.util.mod_support.SupportedMods;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ModConfigScreen {
    public static Screen createScreen(Screen parentScreen) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setSavingRunnable(ConfigJSON::saveConfigToFile)
                .setParentScreen(parentScreen)
                .setTitle(Text.literal("Humility AFM Config")
                );

        // Create a root category
        ConfigCategory rootCategory = builder.getOrCreateCategory(Text.literal("General Settings"));

        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Text.literal("Transparent Cabinet Blocks"), ModConfig.transparentCabinetBlocks)
                .setDefaultValue(ModConfig.defaultTransparentCabinetBlocks)
                .setTooltip(Text.of("Makes the cabinet blocks transparent.\n(Don't use with vanilla compat rp! It will look weird!)"))
                .setSaveConsumer(value -> {
                    // Save the value to your mod's configuration
                    ModConfig.transparentCabinetBlocks = value;
                })
                .build()
        );
        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Text.literal("Enable Illuminated Cabinet Brightening"), ModConfig.illuminatedCabinetBlockBrightening)
                .setDefaultValue(ModConfig.defaultIlluminatedCabinetBlockBrightening)
                .setTooltip(Text.of("Enables Light Strips Brightening, better looking, small performance impact.\n(Auto disabled when Shimmer is installed)"))
                .setSaveConsumer(value -> {
                    ModConfig.illuminatedCabinetBlockBrightening = value;
                    if (value) {
                        IlluminatedCabinetBlockEntityRenderer.enableBrightening();
                        FloorIlluminatedCabinetBlockEntityRenderer.enableBrightening();
                    } else {
                        IlluminatedCabinetBlockEntityRenderer.disableBrightening();
                        FloorIlluminatedCabinetBlockEntityRenderer.disableBrightening();
                    }
                })
                .build()
        );
        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Text.literal("Enable Light Strip Brightening"), ModConfig.enableLightStripBrightening)
                .setDefaultValue(ModConfig.defaultEnableLightStripBrightening)
                .setTooltip(Text.of("Enables Light Strips Brightening, better looking, small performance impact.\n(Auto disabled when Shimmer is installed)"))
                .setSaveConsumer(value -> {
                    ModConfig.enableLightStripBrightening = value;
                    if (value) LightStripBlockEntityRenderer.enableBrightening();
                    else LightStripBlockEntityRenderer.disableBrightening();
                })
                .build()
        );
        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Text.literal("Enable Candlestick Beta"), ModConfig.getRawEnableCandlestickBeta())
                .setDefaultValue(ModConfig.defaultEnableCandlestickBeta)
                .setTooltip(Text.of("Enables Candlestick blocks and items (beta)"))
                .setSaveConsumer(ModConfig::setEnableCandlestickBeta)
                .build()
        );
        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Text.literal("Enable Coloured Feature Set Beta"), ModConfig.getRawEnableColouredFeatureSetBeta())
                .setDefaultValue(ModConfig.defaultEnableColouredFeatureSetBeta)
                .setTooltip(Text.of("Enables coloured feature set (beta)\nThis includes coloured torches, jack o'Lanterns and light strips + a new ingredient 'glowing powder' for crafting them.\n(For all the blocks to emmit coloured light, Shimmer must be installed or a compatible shader pack must be used)"))
                .setSaveConsumer(ModConfig::setEnableColouredFeatureSetBeta)
                .build()
        );
        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Text.literal("Datagen Mode"), ModConfig.datagenMode)
                .setDefaultValue(ModConfig.defaultDatagenMode)
                .setTooltip(Text.of("Enables datagen mode, which marks all supported mods as preset.\nThis is a debug tool used for Fabric Datagen.\n(Requires a restart to take effect)"))
                .setSaveConsumer(value -> ModConfig.datagenMode = value)
                .build()
        );
        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Text.literal("Print Server Client Config Missmatch In Chat"), ModConfig.printInChatServerClientMissmatch)
                .setDefaultValue(ModConfig.defaultPrintInChatServerClientMissmatch)
                .setTooltip(Text.of("If true, when you join a server with different config, the differences will be printed in chat"))
                .setSaveConsumer(value -> ModConfig.printInChatServerClientMissmatch = value)
                .build()
        );

        ConfigCategory modSupportCategory = builder.getOrCreateCategory(Text.literal("Mod Support"));

        List<Map.Entry<SupportedMods, ModSupportState>> sortedModSupport = ModConfig.modSupport.entrySet()
                .stream()
                .sorted(Comparator.comparing(entry -> entry.getKey().modName)).toList();

        for (Map.Entry<SupportedMods, ModSupportState> entry : sortedModSupport) {
            SupportedMods mod = entry.getKey();
            ModSupportState support = entry.getValue();

            modSupportCategory.addEntry(ConfigEntryBuilder.create()
                    .startEnumSelector(Text.literal(mod.modName), ModSupportState.class, support)
                    .setDefaultValue(ModSupportState.AUTO)
                    .setTooltip(Text.of("Enable or disable support for " + mod.modName + ".\n" +
                            "Auto will enable the support if the mod is present."))
                    .setSaveConsumer(value -> ModConfig.modSupport.put(mod, value))
                    .build()
            );
        }

        ConfigCategory customizationCategory = builder.getOrCreateCategory(Text.literal("Blocks Customization"));

        customizationCategory.addEntry(ConfigEntryBuilder.create()
                .startIntSlider(Text.literal("Cabinet Block Burn Time"), ModConfig.cabinetBlockBurnTime, 0, 64)
                .setDefaultValue(ModConfig.defaultCabinetBlockBurnTime)
                .setSaveConsumer(value -> ModConfig.cabinetBlockBurnTime = value)
                .build()
        );
        customizationCategory.addEntry(ConfigEntryBuilder.create()
                .startIntSlider(Text.literal("Cabinet Block Fire Spread"), ModConfig.cabinetBlockFireSpread, 0, 64)
                .setDefaultValue(ModConfig.defaultCabinetBlockFireSpread)
                .setSaveConsumer(value -> ModConfig.cabinetBlockFireSpread = value)
                .build()
        );
        customizationCategory.addEntry(ConfigEntryBuilder.create()
                .startFloatField(Text.literal("Mosaics and Tiles Strength Multiplayer"), ModConfig.mosaicsAndTilesStrengthMultiplayer)
                .setDefaultValue(ModConfig.defaultMosaicsAndTilesStrengthMultiplayer)
                .setSaveConsumer(value -> ModConfig.mosaicsAndTilesStrengthMultiplayer = value)
                .build()
        );

        return builder.build();
    }
}