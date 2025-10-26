package io.github.mikip98.humilityafm.config;

import com.google.gson.*;
import io.github.mikip98.humilityafm.config.enums.CreativeItemGroupCategorization;
import io.github.mikip98.humilityafm.config.enums.ModSupportState;
import io.github.mikip98.humilityafm.util.mod_support.SupportedMods;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.function.Function;

import static io.github.mikip98.humilityafm.HumilityAFM.LOGGER;

// TODO: Move to TOML
public class ConfigJSON {
    // Save the configuration to a JSON file in the Minecraft configuration folder
    public static void saveConfigToFile() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File configDir = FabricLoader.getInstance().getConfigDir().toFile();
        File configFile = new File(configDir, "humility-afm.json");

        // Create a JSON object to store the configuration
        JsonObject configJson = new JsonObject();
        configJson.addProperty("transparentCabinetBlocks", ModConfig.transparentCabinetBlocks);
        configJson.addProperty("illuminatedCabinetBlockBrightening", ModConfig.illuminatedCabinetBlockBrightening);
        configJson.addProperty("enableLightStripBrightening", ModConfig.enableLightStripBrightening);
        configJson.addProperty("enableLightStripRadiusColorCompensation", ModConfig.enableLightStripRadiusColorCompensation);
        configJson.addProperty("enableCandlestickBeta", ModConfig.getRawEnableCandlestickBeta());
        configJson.addProperty("enableColouredFeatureSetBeta", ModConfig.getRawEnableColouredFeatureSetBeta());

        configJson.addProperty("creativeItemGroupCategorization", ModConfig.creativeItemGroupCategorization.toString());
        configJson.addProperty("placeHumilityBlocksInVanillaCreativeCategories", ModConfig.placeHumilityBlocksInVanillaCreativeCategories);

        configJson.addProperty("mosaicsAndTilesStrengthMultiplayer", ModConfig.mosaicsAndTilesStrengthMultiplayer);
        configJson.addProperty("cabinetBlockBurnTime", ModConfig.cabinetBlockBurnTime);
        configJson.addProperty("cabinetBlockFireSpread", ModConfig.cabinetBlockFireSpread);

        configJson.addProperty("datagenMode", ModConfig.datagenMode);
        configJson.addProperty("printInChatServerClientMissmatch", ModConfig.printInChatServerClientMissmatch);

        // Add the mod support configuration
        JsonObject modSupportJson = new JsonObject();
        for (Map.Entry<SupportedMods, ModSupportState> entry : ModConfig.modSupport.entrySet()) {
            modSupportJson.addProperty(entry.getKey().modId, entry.getValue().toString());
        }
        configJson.add("modSupport", modSupportJson);

        // Save the JSON object to a file
        try (FileWriter writer = new FileWriter(configFile)) {
            gson.toJson(configJson, writer);
        } catch (IOException e) {
            configSaveError(configFile, e);
        }
    }

    // Load the configuration from the JSON file in the Minecraft configuration folder
    public static void loadConfigFromFile() {
        File configDir = FabricLoader.getInstance().getConfigDir().toFile();
        File configFile = new File(configDir, "humility-afm.json");

        if (configFile.exists()) {
            Gson gson = new Gson();
            try (FileReader reader = new FileReader(configFile)) {
                JsonObject configJson = gson.fromJson(reader, JsonObject.class);
                boolean needsUpdating = false;
                if (configJson != null) {
                    // Load the static fields from the JSON object
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsBoolean, "transparentCabinetBlocks");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsBoolean, "illuminatedCabinetBlockBrightening");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsBoolean, "enableLightStripBrightening");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsBoolean, "enableLightStripRadiusColorCompensation");
                    needsUpdating |= tryLoadViaSetter(configJson, JsonElement::getAsBoolean, "enableCandlestickBeta", boolean.class);
                    needsUpdating |= tryLoadViaSetter(configJson, JsonElement::getAsBoolean, "enableColouredFeatureSetBeta", boolean.class);

                    needsUpdating |= tryLoadEnum(configJson, "creativeItemGroupCategorization", CreativeItemGroupCategorization::valueOf);
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsBoolean, "placeHumilityBlocksInVanillaCreativeCategories");

                    needsUpdating |= tryLoad(configJson, JsonElement::getAsFloat, "mosaicsAndTilesStrengthMultiplayer");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsInt, "cabinetBlockBurnTime");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsInt, "cabinetBlockFireSpread");

                    needsUpdating |= tryLoad(configJson, JsonElement::getAsBoolean, "datagenMode");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsBoolean, "printInChatServerClientMissmatch");

                    // Load the mod support configuration
                    try {
                        JsonObject modSupportJson = configJson.getAsJsonObject("modSupport");
                        for (Map.Entry<String, JsonElement> entry : modSupportJson.entrySet()) {
                            SupportedMods mod = SupportedMods.fromModId(entry.getKey());
                            ModSupportState support = ModSupportState.valueOf(entry.getValue().getAsString());
                            ModConfig.modSupport.put(mod, support);
                        }
                    } catch (Exception e) {
                        LOGGER.error(
                                "Failed to load 'modSupport' from config file: {}\nError: {}\nStacktrace: {}\nMarking the config file for update",
                                configFile.getAbsolutePath(), e.getMessage(), e.getStackTrace()
                        );
                        needsUpdating = true;
                    }
                }
                if (needsUpdating) {
                    LOGGER.warn("Updating config file to include new values and/or fix broken ones");
                    saveConfigToFile();  // Update the config file to include new values
                }
            } catch (IOException e) {
                LOGGER.error(
                        "Failed to load config file: {}\nError: {}\nStacktrace: {}",
                        configFile.getAbsolutePath(), e.getMessage(), e.getStackTrace()
                );
            }
        } else {
            saveConfigToFile();  // Create the config file
        }
    }

    // Setting loaders
    private static <T> boolean tryLoad(JsonObject configJson, Function<JsonElement, T> getter, String fieldName) {
        try {
            T value = getter.apply(configJson.get(fieldName));
            ModConfig.class.getField(fieldName).set(ModConfig.class, value);
        } catch (Exception e) {
            printLoadError(fieldName, configJson, e);
            return true;
        }
        return false;
    }
    private static <V> boolean tryLoadEnum(JsonObject configJson, String fieldName, Function<String, V> setter) {
        try {
            String value = configJson.get(fieldName).getAsString();
            ModConfig.class.getField(fieldName).set(ModConfig.class, setter.apply(value));
        } catch (Exception e) {
            printLoadError(fieldName, configJson, e);
            return true;
        }
        return false;
    }
    private static <T> boolean tryLoadViaSetter(JsonObject configJson, Function<JsonElement, T> getter, String fieldName, Class<T> clazz) {
        final String setterName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        try {
            T value = getter.apply(configJson.get(fieldName));
            ModConfig.class.getMethod(setterName, clazz).invoke(null, value);
        } catch (Exception e) {
            printLoadError(fieldName, configJson, e);
            return true;
        }
        return false;
    }

    // Error logging
    private static void printLoadError(String fieldName, JsonObject configJson, Exception e) {
        LOGGER.error(
                "Failed to load '{}' from config file: {}\n\tError: {}\n\tStacktrace: {}",
                fieldName, configJson, e.getMessage(), e.getStackTrace()
        );
    }
    protected static void configSaveError(File configFile, IOException e) {
        LOGGER.error(
                "Failed to save config file: {}\nError: {}\nStacktrace: {}",
                configFile.getAbsolutePath(), e.getMessage(), e.getStackTrace()
        );
    }
}
