package io.github.mikip98.config;

import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import static com.mojang.text2speech.Narrator.LOGGER;

public class ConfigToJSON {

    // Save the configuration to a JSON file in the Minecraft configuration folder
    public static void saveConfigToFile() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File configDir = FabricLoader.getInstance().getConfigDir().toFile();
        File configFile = new File(configDir, "humility-afm.json");

        // Create a JSON object to store the configuration
        JsonObject configJson = new JsonObject();
        configJson.addProperty("enableLEDs", ModConfig.enableLEDs);
        configJson.addProperty("cabinetBlockBurnTime", ModConfig.cabinetBlockBurnTime);
        configJson.addProperty("cabinetBlockFireSpread", ModConfig.cabinetBlockFireSpread);

        // Save the JSON object to a file
        try (FileWriter writer = new FileWriter(configFile)) {
            gson.toJson(configJson, writer);
        } catch (IOException e) {
            e.printStackTrace();
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
                if (configJson != null) {
                    // Load the static fields from the JSON object
                    ModConfig.enableLEDs = configJson.get("enableLEDs").getAsBoolean();
                    ModConfig.cabinetBlockBurnTime = configJson.get("cabinetBlockBurnTime").getAsInt();
                    LOGGER.info("Cabinet block burn time: " + ModConfig.cabinetBlockBurnTime);
                    ModConfig.cabinetBlockFireSpread = configJson.get("cabinetBlockFireSpread").getAsInt();
                    LOGGER.info("Cabinet block fire spread: " + ModConfig.cabinetBlockFireSpread);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
