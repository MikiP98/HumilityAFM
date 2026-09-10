package io.github.mikip98.humilityafm.config;

import io.github.mikip98.humilityafm.config.enums.CreativeItemGroupCategorization;
import io.github.mikip98.humilityafm.config.enums.ModSupportState;
import io.github.mikip98.humilityafm.content.block_entity_renderers.LightStripBlockEntityRenderer;
import io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.FloorIlluminatedCabinetBlockEntityRenderer;
import io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.IlluminatedCabinetBlockEntityRenderer;
import io.github.mikip98.humilityafm.util.mod_support.SupportedMods;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ModConfigScreen {
    public static Screen createScreen(Screen parentScreen) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setSavingRunnable(ConfigJSON::saveConfigToFile)
                .setParentScreen(parentScreen)
                .setTitle(Component.literal("Humility AFM Config")
                );

        // Create a root category
        ConfigCategory rootCategory = builder.getOrCreateCategory(Component.literal("General Settings"));

        #if MC_VERSION < 260000
        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Component.literal("Transparent Cabinet Blocks"), ModConfig.transparentCabinetBlocks)
                .setDefaultValue(ModConfig.defaultTransparentCabinetBlocks)
                .setTooltip(Component.literal("Makes the cabinet blocks transparent.\n(Don't use with vanilla compat rp! It will look weird!)"))
                .setSaveConsumer(value -> {
                    // Save the value to your mod's configuration
                    ModConfig.transparentCabinetBlocks = value;
                })
                .build()
        );
        #endif
        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Component.literal("Enable Illuminated Cabinet Brightening"), ModConfig.illuminatedCabinetBlockBrightening)
                .setDefaultValue(ModConfig.defaultIlluminatedCabinetBlockBrightening)
                .setTooltip(Component.literal("Enables Light Strips Brightening, better looking, small performance impact.\n(Auto disabled when Shimmer is installed)"))
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
                .startBooleanToggle(Component.literal("Enable Light Strip Brightening"), ModConfig.enableLightStripBrightening)
                .setDefaultValue(ModConfig.defaultEnableLightStripBrightening)
                .setTooltip(Component.literal("Enables Light Strips Brightening, better looking, small performance impact.\n(Auto disabled when Shimmer is installed)"))
                .setSaveConsumer(value -> {
                    ModConfig.enableLightStripBrightening = value;
                    if (value) LightStripBlockEntityRenderer.enableBrightening();
                    else LightStripBlockEntityRenderer.disableBrightening();
                })
                .build()
        );
        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Component.literal("Enable Candlestick Beta"), ModConfig.getRawEnableCandlestickBeta())
                .setDefaultValue(ModConfig.defaultEnableCandlestickBeta)
                .setTooltip(Component.literal("Enables Candlestick blocks and items (beta)"))
                .setSaveConsumer(ModConfig::setEnableCandlestickBeta)
                .build()
        );
        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Component.literal("Enable Coloured Feature Set Beta"), ModConfig.getRawEnableColouredFeatureSetBeta())
                .setDefaultValue(ModConfig.defaultEnableColouredFeatureSetBeta)
                .setTooltip(Component.literal("Enables coloured feature set (beta)\nThis includes coloured torches, jack o'Lanterns and light strips + a new ingredient 'glowing powder' for crafting them.\n(For all the blocks to emmit coloured light, Shimmer must be installed or a compatible shader pack must be used)"))
                .setSaveConsumer(ModConfig::setEnableColouredFeatureSetBeta)
                .build()
        );

        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startEnumSelector(Component.literal("Cretive Inventory Tab Separation"), CreativeItemGroupCategorization.class, ModConfig.creativeItemGroupCategorization)
                .setDefaultValue(ModConfig.dCreativeItemGroupCategorization)
                .setTooltip(Component.literal("""
                        Controls how the Humility AFM block and items are separated in the creative inventory tabs.
                        - SEPARATE -> Default separation based on block types and their variant amounts
                        - BLOCKS_AND_ITEMS -> Only 2 additional tabs: 'Humility Blocks' and 'Humility Items'
                        - SINGLE -> Only 1 additional tab with all the Humility content
                        - NONE -> No additional tabs are created
                        """
                ))
                .setSaveConsumer(value -> ModConfig.creativeItemGroupCategorization = value)
                .build()
        );
        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Component.literal("Put Humility Blocks In Vanilla Creative Tabs"), ModConfig.placeHumilityBlocksInVanillaCreativeCategories)
                .setDefaultValue(ModConfig.dPlaceHumilityBlocksInVanillaCreativeCategories)
                .setTooltip(Component.literal("Weather to put Humility AFM content inside the vanilla creative inventopry tabs"))
                .setSaveConsumer(value -> ModConfig.placeHumilityBlocksInVanillaCreativeCategories = value)
                .build()
        );

        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Component.literal("Datagen Mode"), ModConfig.datagenMode)
                .setDefaultValue(ModConfig.defaultDatagenMode)
                .setTooltip(Component.literal("Enables datagen mode, which marks all supported mods as preset.\nThis is a debug tool used for Fabric Datagen.\n(Requires a restart to take effect)"))
                .setSaveConsumer(value -> ModConfig.datagenMode = value)
                .build()
        );
        rootCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(Component.literal("Print Server Client Config Missmatch In Chat"), ModConfig.printInChatServerClientMissmatch)
                .setDefaultValue(ModConfig.defaultPrintInChatServerClientMissmatch)
                .setTooltip(Component.literal("If true, when you join a server with different config, the differences will be printed in chat"))
                .setSaveConsumer(value -> ModConfig.printInChatServerClientMissmatch = value)
                .build()
        );

        ConfigCategory modSupportCategory = builder.getOrCreateCategory(Component.literal("Mod Support"));

        List<Map.Entry<SupportedMods, ModSupportState>> sortedModSupport = ModConfig.modSupport.entrySet()
                .stream()
                .sorted(Comparator.comparing(entry -> entry.getKey().modName)).toList();

        for (Map.Entry<SupportedMods, ModSupportState> entry : sortedModSupport) {
            SupportedMods mod = entry.getKey();
            ModSupportState support = entry.getValue();

            modSupportCategory.addEntry(ConfigEntryBuilder.create()
                    .startEnumSelector(Component.literal(mod.modName), ModSupportState.class, support)
                    .setDefaultValue(ModSupportState.AUTO)
                    .setTooltip(Component.literal("Enable or disable support for " + mod.modName + ".\n" +
                            "Auto will enable the support if the mod is present."))
                    .setSaveConsumer(value -> ModConfig.modSupport.put(mod, value))
                    .build()
            );
        }

        #if POLYMER
        modSupportCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(
                        Component.literal("Polymer Allow Optimised Mosaics & Tiles"),
                        ModConfig.polymerAllowOptimisedMosaicsAndTiles
                )
                .setDefaultValue(ModConfig.defaultPolymerAllowOptimisedMosaicsAndTiles)
                .setTooltip(Component.literal("")) // TODO
                .setSaveConsumer(value -> ModConfig.polymerAllowOptimisedMosaicsAndTiles = value)
                .build()
        );
        modSupportCategory.addEntry(ConfigEntryBuilder.create()
                .startBooleanToggle(
                        Component.literal("Polymer Allow Optimised Jack O'Lanterns"),
                        ModConfig.polymerAllowOptimisedJackOLanterns
                )
                .setDefaultValue(ModConfig.defaultPolymerAllowOptimisedJackOLanterns)
                .setTooltip(Component.literal("")) // TODO
                .setSaveConsumer(value -> ModConfig.polymerAllowOptimisedJackOLanterns = value)
                .build()
        );
        #endif

        ConfigCategory customizationCategory = builder.getOrCreateCategory(Component.literal("Blocks Customization"));

        customizationCategory.addEntry(ConfigEntryBuilder.create()
                .startIntSlider(Component.literal("Cabinet Block Burn Time"), ModConfig.cabinetBlockBurnTime, 0, 64)
                .setDefaultValue(ModConfig.defaultCabinetBlockBurnTime)
                .setSaveConsumer(value -> ModConfig.cabinetBlockBurnTime = value)
                .build()
        );
        customizationCategory.addEntry(ConfigEntryBuilder.create()
                .startIntSlider(Component.literal("Cabinet Block Fire Spread"), ModConfig.cabinetBlockFireSpread, 0, 64)
                .setDefaultValue(ModConfig.defaultCabinetBlockFireSpread)
                .setSaveConsumer(value -> ModConfig.cabinetBlockFireSpread = value)
                .build()
        );
        customizationCategory.addEntry(ConfigEntryBuilder.create()
                .startFloatField(Component.literal("Mosaics and Tiles Strength Multiplayer"), ModConfig.mosaicsAndTilesStrengthMultiplayer)
                .setDefaultValue(ModConfig.defaultMosaicsAndTilesStrengthMultiplayer)
                .setSaveConsumer(value -> ModConfig.mosaicsAndTilesStrengthMultiplayer = value)
                .build()
        );

        return builder.build();
    }
}