package io.github.mikip98.humilityafm.util;

import io.github.mikip98.humilityafm.config.ModConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static io.github.mikip98.humilityafm.HumilityAFM.LOGGER;
import static io.github.mikip98.humilityafm.util.FunUtils.getRandomFunSymbol;

public class ModSupportManager {
    public static Set<SupportedMods> loadedMods = new HashSet<>();

    public static boolean isModLoaded(SupportedMods mod) {
        return loadedMods.contains(mod);
    }

    public static void init() {
        if (!ModConfig.datagenMode) {
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
            LOGGER.info("Datagen mode is enabled, all supported mods will be marked as loaded.");
            loadedMods.addAll(Set.of(SupportedMods.values()));
        }
    }

    protected static void checkForMod(SupportedMods mod) {
        final FabricLoader fabricLoader = FabricLoader.getInstance();

        if (fabricLoader.isModLoaded(mod.modId)) {
            loadedMods.add(mod);
            LOGGER.info("Supported mod '{}' ({}) has been found loaded {}", mod.modName, mod.modId, getRandomFunSymbol());
            return;
        }

        Collection<ModContainer> mods = fabricLoader.getAllMods();
        for (ModContainer modContainer : mods) {
            if (modContainer.getMetadata().getId().equals(mod.modId)) {
                loadedMods.add(mod);
                LOGGER.info("Supported mod '{}' ({}) has been found present {}", mod.modName, mod.modId, getRandomFunSymbol());
                return;
            }
        }
    }
}
