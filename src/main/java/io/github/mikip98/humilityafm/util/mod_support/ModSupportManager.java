package io.github.mikip98.humilityafm.util.mod_support;

import io.github.mikip98.humilityafm.config.ModConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.util.EnumSet;
import java.util.Set;

import static io.github.mikip98.humilityafm.HumilityAFM.LOGGER;
import static io.github.mikip98.humilityafm.util.FunUtils.getRandomFunSymbol;

public class ModSupportManager {
    protected static Set<SupportedMods> loadedMods;

    public static boolean isModLoaded(SupportedMods mod) {
        return loadedMods.contains(mod);
    }

    public static void init() {
        if (!ModConfig.datagenMode) {
            // Init empty enum set of supported mods
            loadedMods = EnumSet.noneOf(SupportedMods.class);

            LOGGER.info("Checking for supported mods...");
            for (SupportedMods mod : SupportedMods.values()) {
                switch (ModConfig.modSupport.get(mod)) {
                    case AUTO -> checkForMod(mod);
                    case ON -> {
                        loadedMods.add(mod);
                        LOGGER.info("Supported mod '{}' ({}) has been enabled by configuration.", mod.modName, mod.modId);
                    }
                    case OFF -> LOGGER.info("Supported mod '{}' ({}) has been disabled by configuration.", mod.modName, mod.modId);
                }
            }
        } else {
            // Init full enum set of supported mods
            loadedMods = EnumSet.allOf(SupportedMods.class);
            LOGGER.info("Datagen mode is enabled, all supported mods will be marked as loaded.");
        }
    }

    protected static void checkForMod(SupportedMods mod) {
        final FabricLoader fabricLoader = FabricLoader.getInstance();

        // Ask fabric if the mod is loaded
        if (fabricLoader.isModLoaded(mod.modId)) {
            loadedMods.add(mod);
            LOGGER.info("Supported mod '{}' ({}) has been found present {}", mod.modName, mod.modId, getRandomFunSymbol());
        }
    }
}
