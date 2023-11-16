package io.github.mikip98.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ModConfigScreen {
    public static Screen createScreen(Screen parentScreen) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setSavingRunnable(ConfigToJSON::saveConfigToFile)
                .setParentScreen(parentScreen)
                .setTitle(Text.literal("Humility AFM Config")
                );

        // Create a root category
        ConfigCategory rootCategory = builder.getOrCreateCategory(Text.literal("General Settings"));

        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Text.literal("Enable LEDs"), ModConfig.enableLEDs)
                .setSaveConsumer(value -> {
                    // Save the value to your mod's configuration
                    ModConfig.enableLEDs = value;
                })
                .build()
        );

        ConfigCategory customizationCategory = builder.getOrCreateCategory(Text.literal("Blocks Customization"));

        customizationCategory.addEntry(ConfigEntryBuilder.create()
                .startIntSlider(Text.literal("Cabinet Block Burn Time"), ModConfig.cabinetBlockBurnTime, 0, 64)
                .setDefaultValue(24)
                .setSaveConsumer(value -> {
                    ModConfig.cabinetBlockBurnTime = value;
                })
                .build()
        );
        customizationCategory.addEntry(ConfigEntryBuilder.create()
                .startIntSlider(Text.literal("Cabinet Block Fire Spread"), ModConfig.cabinetBlockFireSpread, 0, 64)
                .setDefaultValue(9)
                .setSaveConsumer(value -> {
                    ModConfig.cabinetBlockFireSpread = value;
                })
                .build()
        );
        customizationCategory.addEntry(ConfigEntryBuilder.create()
                .startFloatField(Text.literal("Mosaics and Tiles Strength Multiplayer"), ModConfig.mosaicsAndTilesStrengthMultiplayer)
                .setDefaultValue(1.5f)
                .setSaveConsumer(value -> {
                    ModConfig.mosaicsAndTilesStrengthMultiplayer = value;
                })
                .build()
        );

        return builder.build();
    }
}