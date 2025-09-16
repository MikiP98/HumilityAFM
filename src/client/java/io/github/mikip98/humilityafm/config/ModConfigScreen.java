package io.github.mikip98.humilityafm.config;

import io.github.mikip98.humilityafm.content.block_entity_renderers.LightStripBlockEntityRenderer;
import io.github.mikip98.humilityafm.util.mod_support.SupportedMods;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
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
                .startBooleanToggle(Text.literal("Enable Light Strip Radius Color Compensation"), ModConfig.enableLightStripRadiusColorCompensation)
                .setDefaultValue(ModConfig.defaultEnableLightStripRadiusColorCompensation)
                .setTooltip(Text.of("Enables Light Strip Radius Color Compensation\nTries to increase brightness without losing out on saturation\n(Requires Shimmer to be installed)"))
                .setSaveConsumer(value -> ModConfig.enableLightStripRadiusColorCompensation = value)
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

        List<Map.Entry<SupportedMods, ModSupport>> sortedModSupport = ModConfig.modSupport.entrySet()
                .stream()
                .sorted(Comparator.comparing(entry -> entry.getKey().modName)).toList();

        for (Map.Entry<SupportedMods, ModSupport> entry : sortedModSupport) {
            SupportedMods mod = entry.getKey();
            ModSupport support = entry.getValue();

            modSupportCategory.addEntry(ConfigEntryBuilder.create()
                    .startEnumSelector(Text.literal(mod.modName), ModSupport.class, support)
                    .setDefaultValue(ModSupport.AUTO)
                    .setTooltip(Text.of("Enable or disable support for " + mod.modName + ".\n" +
                            "Auto will enable the support if the mod is present."))
                    .setSaveConsumer(value -> ModConfig.modSupport.put(mod, value))
                    .build()
            );
        }

        ConfigCategory customizationCategory = builder.getOrCreateCategory(Text.literal("Blocks Customization"));

        customizationCategory.addEntry(ConfigEntryBuilder.create()
                .startIntSlider(Text.literal("Light Strip Colored Light Strength"), ModConfig.lightStripColoredLightStrength, 0, 255)
                .setDefaultValue(85)
                .setTooltip(Text.of("The strength of the Light Strip Colored Light.\n(Requires Shimmer to be installed)"))
                .setSaveConsumer(value -> ModConfig.lightStripColoredLightStrength = value.shortValue())
                .build()
        );
        customizationCategory.addEntry(ConfigEntryBuilder.create()
                .startIntSlider(Text.literal("Light Strip Colored Light Radius"), ModConfig.lightStripColoredLightRadius, 0, 63)
                .setDefaultValue(9)
                .setTooltip(Text.of("The radius of the Light Strip Colored Light.\n(Requires Shimmer to be installed)"))
                .setSaveConsumer(value -> ModConfig.lightStripColoredLightRadius = value.shortValue())
                .build()
        );
        customizationCategory.addEntry(ConfigEntryBuilder.create()
                .startIntSlider(Text.literal("Light Strip Radius Color Compensation Bias"), ModConfig.lightStripRadiusColorCompensationBias, -1, 1)
                .setDefaultValue(0)
                .setTooltip(Text.of("The bias of the Light Strip Radius Color Compensation.\n(Requires Shimmer to be installed)"))
                .setSaveConsumer(value -> ModConfig.lightStripRadiusColorCompensationBias = value.shortValue())
                .build()
        );
        customizationCategory.addEntry(ConfigEntryBuilder.create()
                .startIntSlider(Text.literal("Cabinet Block Burn Time"), ModConfig.cabinetBlockBurnTime, 0, 64)
                .setDefaultValue(24)
                .setSaveConsumer(value -> ModConfig.cabinetBlockBurnTime = value)
                .build()
        );
        customizationCategory.addEntry(ConfigEntryBuilder.create()
                .startIntSlider(Text.literal("Cabinet Block Fire Spread"), ModConfig.cabinetBlockFireSpread, 0, 64)
                .setDefaultValue(9)
                .setSaveConsumer(value -> ModConfig.cabinetBlockFireSpread = value)
                .build()
        );
        customizationCategory.addEntry(ConfigEntryBuilder.create()
                .startFloatField(Text.literal("Mosaics and Tiles Strength Multiplayer"), ModConfig.mosaicsAndTilesStrengthMultiplayer)
                .setDefaultValue(1.5f)
                .setSaveConsumer(value -> ModConfig.mosaicsAndTilesStrengthMultiplayer = value)
                .build()
        );

        ConfigCategory lightStripColorsCategory = builder.getOrCreateCategory(Text.literal("Light Strip Colors"));

        for (Color color : ModConfig.lightStripColors) {
            lightStripColorsCategory.addEntry(ConfigEntryBuilder.create()
                    .startSubCategory(Text.of(color.name), createColorEntries(color))
                    .build());
        }

        ConfigCategory pumpkinColorsCategory = builder.getOrCreateCategory(Text.literal("Pumpkin Colors"));

        for (Color color : ModConfig.pumpkinColors.values()) {
            pumpkinColorsCategory.addEntry(ConfigEntryBuilder.create()
                    .startSubCategory(Text.of(color.name), createColorEntries(color))
                    .build());
        }

        return builder.build();
    }

    @SuppressWarnings("rawtypes")
    private static List<AbstractConfigListEntry> createColorEntries(Color color) {
        List<AbstractConfigListEntry> entries = new ArrayList<>();
        entries.add(ConfigEntryBuilder.create()
                .startIntSlider(Text.literal("Red"), color.r, 0, 255)
                .setDefaultValue(color.defaultR)
                .setSaveConsumer(value -> color.r = value.shortValue())
                .build()
        );
        entries.add(ConfigEntryBuilder.create()
                .startIntSlider(Text.literal("Green"), color.g, 0, 255)
                .setDefaultValue(color.defaultG)
                .setSaveConsumer(value -> color.g = value.shortValue())
                .build()
        );
        entries.add(ConfigEntryBuilder.create()
                .startIntSlider(Text.literal("Blue"), color.b, 0, 255)
                .setDefaultValue(color.defaultB)
                .setSaveConsumer(value -> color.b = value.shortValue())
                .build()
        );
        return entries;
    }
}