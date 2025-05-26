package io.github.mikip98.humilityafm.config;

import io.github.mikip98.humilityafm.util.data_types.Color;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

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
                .startBooleanToggle(Text.literal("Transparent Cabinet Blocks"), ModConfig.TransparentCabinetBlocks)
                .setDefaultValue(true)
                .setTooltip(Text.of("Makes the cabinet blocks transparent.\n(Don't use with vanilla compat rp! It will look weird!)"))
                .setSaveConsumer(value -> {
                    // Save the value to your mod's configuration
                    ModConfig.TransparentCabinetBlocks = value;
                })
                .build()
        );
        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Text.literal("Enable Light Strip Brightening"), ModConfig.enableLightStripBrightening)
                .setDefaultValue(true)
                .setTooltip(Text.of("Enables Light Strips Brightening, better looking, small performance impact.\n(Auto disabled when Shimmer is installed)"))
                .setSaveConsumer(value -> ModConfig.enableLightStripBrightening = value)
                .build()
        );
        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Text.literal("Enable Light Strip Radius Color Compensation"), ModConfig.enableLightStripRadiusColorCompensation)
                .setDefaultValue(true)
                .setTooltip(Text.of("Enables Light Strip Radius Color Compensation\nTries to increase brightness without losing out on saturation\n(Requires Shimmer to be installed)"))
                .setSaveConsumer(value -> ModConfig.enableLightStripRadiusColorCompensation = value)
                .build()
        );
        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Text.literal("Enable Candlestick Beta"), ModConfig.enableCandlestickBeta)
                .setDefaultValue(false)
                .setTooltip(Text.of("Enables Candlestick blocks and items (beta)"))
                .setSaveConsumer(value -> ModConfig.enableCandlestickBeta = value)
                .build()
        );
        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Text.literal("Enable Coloured Feature Set Beta"), ModConfig.enableColouredFeatureSetBeta)
                .setDefaultValue(false)
                .setTooltip(Text.of("Enables coloured feature set (beta)\nThis includes coloured torches, jack o'Lanterns and light strips + a new ingredient 'glowing powder' for crafting them.\n(For all the blocks to emmit coloured light, Shimmer must be installed or a compatible shader pack must be used)"))
                .setSaveConsumer(value -> ModConfig.enableColouredFeatureSetBeta = value)
                .build()
        );

        ConfigCategory customizationCategory = builder.getOrCreateCategory(Text.literal("Blocks Customization"));

        customizationCategory.addEntry(ConfigEntryBuilder.create()
                .startIntSlider(Text.literal("Light Strip Colored Light Strength"), ModConfig.LightStripColoredLightStrength, 0, 255)
                .setDefaultValue(85)
                .setTooltip(Text.of("The strength of the Light Strip Colored Light.\n(Requires Shimmer to be installed)"))
                .setSaveConsumer(value -> ModConfig.LightStripColoredLightStrength = value.shortValue())
                .build()
        );
        customizationCategory.addEntry(ConfigEntryBuilder.create()
                .startIntSlider(Text.literal("Light Strip Colored Light Radius"), ModConfig.LightStripColoredLightRadius, 0, 63)
                .setDefaultValue(9)
                .setTooltip(Text.of("The radius of the Light Strip Colored Light.\n(Requires Shimmer to be installed)"))
                .setSaveConsumer(value -> ModConfig.LightStripColoredLightRadius = value.shortValue())
                .build()
        );
        customizationCategory.addEntry(ConfigEntryBuilder.create()
                .startIntSlider(Text.literal("Light Strip Radius Color Compensation Bias"), ModConfig.LightStripRadiusColorCompensationBias, -1, 1)
                .setDefaultValue(0)
                .setTooltip(Text.of("The bias of the Light Strip Radius Color Compensation.\n(Requires Shimmer to be installed)"))
                .setSaveConsumer(value -> ModConfig.LightStripRadiusColorCompensationBias = value.shortValue())
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

        for (Color color : ModConfig.LightStripColors) {
            @SuppressWarnings("rawtypes")
            List<AbstractConfigListEntry> entries = new ArrayList<>();
            entries.add(ConfigEntryBuilder.create()
                    .startIntSlider(Text.literal("Red"), color.r, 0, 255)
                    .setDefaultValue(color.getDefaultR())
                    .setSaveConsumer(value -> color.r = value.shortValue())
                    .build()
            );
            entries.add(ConfigEntryBuilder.create()
                    .startIntSlider(Text.literal("Green"), color.g, 0, 255)
                    .setDefaultValue(color.getDefaultG())
                    .setSaveConsumer(value -> color.g = value.shortValue())
                    .build()
            );
            entries.add(ConfigEntryBuilder.create()
                    .startIntSlider(Text.literal("Blue"), color.b, 0, 255)
                    .setDefaultValue(color.getDefaultB())
                    .setSaveConsumer(value -> color.b = value.shortValue())
                    .build()
            );
            lightStripColorsCategory.addEntry(ConfigEntryBuilder.create()
                    .startSubCategory(Text.of(color.name), entries).build());
        }

        ConfigCategory pumpkinColorsCategory = builder.getOrCreateCategory(Text.literal("Pumpkin Colors"));

        for (Color color : ModConfig.pumpkinColors.values()) {
            @SuppressWarnings("rawtypes")
            List<AbstractConfigListEntry> entries = new ArrayList<>();
            entries.add(ConfigEntryBuilder.create()
                    .startIntSlider(Text.literal("Red"), color.r, 0, 255)
                    .setDefaultValue(color.getDefaultR())
                    .setSaveConsumer(value -> color.r = value.shortValue())
                    .build()
            );
            entries.add(ConfigEntryBuilder.create()
                    .startIntSlider(Text.literal("Green"), color.g, 0, 255)
                    .setDefaultValue(color.getDefaultG())
                    .setSaveConsumer(value -> color.g = value.shortValue())
                    .build()
            );
            entries.add(ConfigEntryBuilder.create()
                    .startIntSlider(Text.literal("Blue"), color.b, 0, 255)
                    .setDefaultValue(color.getDefaultB())
                    .setSaveConsumer(value -> color.b = value.shortValue())
                    .build()
            );
            pumpkinColorsCategory.addEntry(ConfigEntryBuilder.create()
                    .startSubCategory(Text.of(color.name), entries).build());
        }

        return builder.build();
    }
}