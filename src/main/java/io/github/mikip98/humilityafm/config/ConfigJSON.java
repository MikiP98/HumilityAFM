package io.github.mikip98.humilityafm.config;

import com.google.gson.*;
import io.github.mikip98.humilityafm.util.data_types.Color;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.function.Function;

import static io.github.mikip98.humilityafm.HumilityAFM.LOGGER;

public class ConfigJSON {

    // Save the configuration to a JSON file in the Minecraft configuration folder
    public static void saveConfigToFile() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File configDir = FabricLoader.getInstance().getConfigDir().toFile();
        File configFile = new File(configDir, "humility-afm.json");

        // Create a JSON object to store the configuration
        JsonObject configJson = new JsonObject();
        configJson.addProperty("TransparentCabinetBlocks", ModConfig.TransparentCabinetBlocks);
        configJson.addProperty("enableLightStripBrightening", ModConfig.enableLightStripBrightening);
        configJson.addProperty("enableLightStripRadiusColorCompensation", ModConfig.enableLightStripRadiusColorCompensation);
        configJson.addProperty("enableCandlestickBeta", ModConfig.enableCandlestickBeta);
        configJson.addProperty("enableColouredFeatureSetBeta", ModConfig.enableColouredFeatureSetBeta);

        configJson.addProperty("LightStripColoredLightStrength", ModConfig.LightStripColoredLightStrength);
        configJson.addProperty("LightStripColoredLightRadius", ModConfig.LightStripColoredLightRadius);
        configJson.addProperty("LightStripRadiusColorCompensationBias", ModConfig.LightStripRadiusColorCompensationBias);
        configJson.addProperty("cabinetBlockBurnTime", ModConfig.cabinetBlockBurnTime);
        configJson.addProperty("cabinetBlockFireSpread", ModConfig.cabinetBlockFireSpread);
        configJson.addProperty("mosaicsAndTilesStrengthMultiplayer", ModConfig.mosaicsAndTilesStrengthMultiplayer);

        JsonArray LightStripColors = new JsonArray();
        for (Color color : ModConfig.LightStripColors) {
            JsonObject colorJson = new JsonObject();
            colorJson.addProperty("name", color.name);
            colorJson.addProperty("r", color.r);
            colorJson.addProperty("g", color.g);
            colorJson.addProperty("b", color.b);
            LightStripColors.add(colorJson);
        }
        configJson.add("LightStripColors", LightStripColors);

        JsonObject pumpkinColors = new JsonObject();
        for (Map.Entry<String, Color> entry : ModConfig.pumpkinColors.entrySet()) {
            JsonObject colorJson = new JsonObject();
            colorJson.addProperty("r", entry.getValue().r);
            colorJson.addProperty("g", entry.getValue().g);
            colorJson.addProperty("b", entry.getValue().b);
            pumpkinColors.add(entry.getKey(), colorJson);
        }
        configJson.add("pumpkinColors", pumpkinColors);

        // Save the JSON object to a file
        try (FileWriter writer = new FileWriter(configFile)) {
            gson.toJson(configJson, writer);
        } catch (IOException e) {
            configSaveError(configFile, e);
        }

        saveShimmerSupportConfigLightStrips();
        saveShimmerSupportConfigOther();
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
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsBoolean, "TransparentCabinetBlocks");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsBoolean, "enableLightStripBrightening");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsBoolean, "enableLightStripRadiusColorCompensation");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsBoolean, "enableCandlestickBeta");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsBoolean, "enableColouredFeatureSetBeta");

                    needsUpdating |= tryLoad(configJson, JsonElement::getAsShort, "LightStripColoredLightStrength");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsShort, "LightStripColoredLightRadius");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsShort, "LightStripRadiusColorCompensationBias");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsInt, "cabinetBlockBurnTime");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsInt, "cabinetBlockFireSpread");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsFloat, "mosaicsAndTilesStrengthMultiplayer");

//                    needsUpdating |= tryLoad(configJson, JsonElement::getAsJsonArray, "LightStripColors");
                    try {
                        JsonArray LEDColors = configJson.getAsJsonArray("LightStripColors");
                        for (int i = 0; i < LEDColors.size(); i++) {
                            JsonObject colorJson = LEDColors.get(i).getAsJsonObject();
                            Color color = ModConfig.LightStripColors.get(i);
                            color.r = colorJson.get("r").getAsShort();
                            color.g = colorJson.get("g").getAsShort();
                            color.b = colorJson.get("b").getAsShort();
                        }
                    } catch (Exception e) {
                        LOGGER.error(
                                "Failed to load 'LightStripColors' from config file: {}\nError: {}\nStacktrace: {}\nMarking the config file for update",
                                configFile.getAbsolutePath(), e.getMessage(), e.getStackTrace()
                        );
                        needsUpdating = true;
                    }
//                    needsUpdating |= tryLoad(configJson, JsonElement::getAsJsonObject, "pumpkinColors");
                    try {
                        JsonObject pumpkinColors = configJson.getAsJsonObject("pumpkinColors");
                        for (Map.Entry<String, JsonElement> entry : pumpkinColors.entrySet()) {
                            JsonObject colorJson = entry.getValue().getAsJsonObject();
                            Color color = ModConfig.pumpkinColors.get(entry.getKey());
                            color.r = colorJson.get("r").getAsShort();
                            color.g = colorJson.get("g").getAsShort();
                            color.b = colorJson.get("b").getAsShort();
                        }
                    } catch (Exception e) {
                        LOGGER.error(
                                "Failed to load 'pumpkinColors' from config file: {}\nError: {}\nStacktrace: {}\nMarking the config file for update",
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
    private static <T> boolean tryLoad(JsonObject configJson, Function<JsonElement, T> getter, String fieldName) {
        try {
            T value = getter.apply(configJson.get(fieldName));
            ModConfig.class.getField(fieldName).set(ModConfig.class, value);
        } catch (Exception e) {
            LOGGER.error(
                    "Failed to load '{}' from config file: {}\nError: {}\nStacktrace: {}",
                    fieldName, configJson, e.getMessage(), e.getStackTrace()
            );
            return true;
        }
        return false;
    }

    // Create the shimmer support configuration file if it does not exist
    public static void checkShimmerSupportConfig() {
        File configDir = FabricLoader.getInstance().getConfigDir().toFile();
        File configFile = new File(configDir, "shimmer/humility-led.json");

        if (!configFile.exists()) {
            // Create the config file
            saveShimmerSupportConfigLightStrips();
        }

        configFile = new File(configDir, "shimmer/humility-other.json");
        if (!configFile.exists()) {
            // Create the config file
            saveShimmerSupportConfigOther();
        }
    }

    public static void saveShimmerSupportConfigLightStrips() {
        File configDir = FabricLoader.getInstance().getConfigDir().toFile();
        File configFile = new File(configDir, "shimmer/humility-light_strips.json");

        JsonObject configJson = new JsonObject();

        JsonObject colorReference = getColorReference(ModConfig.LightStripColors, ModConfig.LightStripColoredLightStrength);
        configJson.add("ColorReference", colorReference);

        JsonArray lightBlock = new JsonArray();
        for (Color color : ModConfig.LightStripColors) {
            JsonObject lightBlockJson = new JsonObject();
            lightBlockJson.addProperty("block", "humility-afm:light_strip_" + color.name);
            lightBlockJson.addProperty("color", "#" + color.name);
            lightBlockJson.addProperty("radius", ModConfig.LightStripColoredLightRadius + bias(color));
            lightBlock.add(lightBlockJson);
        }
        configJson.add("LightBlock", lightBlock);

        // Save the JSON object to a file
        try (FileWriter writer = new FileWriter(configFile)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(configJson, writer);
        } catch (IOException e) {
            configSaveError(configFile, e);
        }
    }

    public static void saveShimmerSupportConfigOther() {
        File configDir = FabricLoader.getInstance().getConfigDir().toFile();
        if (!Files.exists(configDir.toPath().resolve("shimmer"))) {
            return;
        }
        File configFile = new File(configDir, "shimmer/humility-other.json");

        JsonObject configJson = new JsonObject();

        JsonObject colorReference = getColorReference(ModConfig.pumpkinColors.values(), 255);
        configJson.add("ColorReference", colorReference);

        JsonArray lightBlock = new JsonArray();

        JsonObject lightBlockJson = new JsonObject();
        lightBlockJson.addProperty("block", "humility-afm:jack_o_lantern_redstone");
        lightBlockJson.addProperty("color", "#" + ModConfig.pumpkinColors.get("red").name);
        lightBlockJson.addProperty("radius", 9);
        lightBlock.add(lightBlockJson);
        lightBlockJson = new JsonObject();
        lightBlockJson.addProperty("block", "humility-afm:jack_o_lantern_soul");
        lightBlockJson.addProperty("color", "#" + ModConfig.pumpkinColors.get("light_blue").name);
        lightBlockJson.addProperty("radius", 10);
        lightBlock.add(lightBlockJson);
        configJson.add("LightBlock", lightBlock);

        // Save the JSON object to a file
        try (FileWriter writer = new FileWriter(configFile)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(configJson, writer);
        } catch (IOException e) {
            configSaveError(configFile, e);
        }
    }

    protected static JsonObject getColorReference(Iterable<Color> colors, int alpha) {
        JsonObject colorReference = new JsonObject();
        for (Color color : colors) {
            JsonObject colorJson = new JsonObject();
            colorJson.addProperty("r", color.r);
            colorJson.addProperty("g", color.g);
            colorJson.addProperty("b", color.b);
            colorJson.addProperty("a", alpha);
            colorReference.add(color.name, colorJson);
        }
        return colorReference;
    }

    protected static void configSaveError(File configFile, IOException e) {
        LOGGER.error(
                "Failed to save config file: {}\nError: {}\nStacktrace: {}",
                configFile.getAbsolutePath(), e.getMessage(), e.getStackTrace()
        );
    }

    private static short bias(Color color) {
        if (ModConfig.enableLightStripRadiusColorCompensation) {
            short sum = (short) (color.r + color.g + color.b);
            if (sum <= 255) {
                return 1;
            } else if (sum > 510) {
                return -1;
            }
        }
        return 0;
    }
}
