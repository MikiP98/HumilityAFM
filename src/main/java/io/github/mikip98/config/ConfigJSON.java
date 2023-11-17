package io.github.mikip98.config;

import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static com.mojang.text2speech.Narrator.LOGGER;

public class ConfigJSON {

    // Save the configuration to a JSON file in the Minecraft configuration folder
    public static void saveConfigToFile() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File configDir = FabricLoader.getInstance().getConfigDir().toFile();
        File configFile = new File(configDir, "humility-afm.json");

        // Create a JSON object to store the configuration
        JsonObject configJson = new JsonObject();
        configJson.addProperty("enableLEDs", ModConfig.enableLEDs);
        configJson.addProperty("enableLEDsBrightening", ModConfig.enableLEDsBrightening);
        configJson.addProperty("enableIlluminatedCabinetBlockBrightening", ModConfig.enableIlluminatedCabinetBlockBrightening);
        configJson.addProperty("forceCabinetBlockResourcePackCompatibility", ModConfig.forceCabinetBlockResourcePackCompatibility);
        configJson.addProperty("LEDColoredLightStrength", ModConfig.LEDColoredLightStrength);
        configJson.addProperty("LEDColoredLightRadius", ModConfig.LEDColoredLightRadius);

        configJson.addProperty("cabinetBlockBurnTime", ModConfig.cabinetBlockBurnTime);
        configJson.addProperty("cabinetBlockFireSpread", ModConfig.cabinetBlockFireSpread);
        configJson.addProperty("mosaicsAndTilesStrengthMultiplayer", ModConfig.mosaicsAndTilesStrengthMultiplayer);

        configJson.addProperty("customColorReferences", ModConfig.customColorReferences.toString());

        // Save the JSON object to a file
        try (FileWriter writer = new FileWriter(configFile)) {
            gson.toJson(configJson, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        saveShimmerSupportConfig();
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
                    try {
                        ModConfig.enableLEDs = configJson.get("enableLEDs").getAsBoolean();
                    } catch (Exception e) {
                        needsUpdating = true;
                    }
                    try {
                        ModConfig.enableLEDsBrightening = configJson.get("enableLEDsBrightening").getAsBoolean();
                    } catch (Exception e) {
                        needsUpdating = true;
                    }
                    try {
                        ModConfig.enableIlluminatedCabinetBlockBrightening = configJson.get("enableIlluminatedCabinetBlockBrightening").getAsBoolean();
                    } catch (Exception e) {
                        needsUpdating = true;
                    }
                    try {
                        ModConfig.forceCabinetBlockResourcePackCompatibility = configJson.get("forceCabinetBlockResourcePackCompatibility").getAsBoolean();
                    } catch (Exception e) {
                        needsUpdating = true;
                    }
                    try {
                        ModConfig.LEDColoredLightStrength = configJson.get("LEDColoredLightStrength").getAsShort();
                    } catch (Exception e) {
                        needsUpdating = true;
                    }
                    try {
                        ModConfig.LEDColoredLightRadius = configJson.get("LEDColoredLightRadius").getAsShort();
                    } catch (Exception e) {
                        needsUpdating = true;
                    }

                    try {
                        ModConfig.cabinetBlockBurnTime = configJson.get("cabinetBlockBurnTime").getAsInt();
                    } catch (Exception e) {
                        needsUpdating = true;
                    }
                    try {
                        ModConfig.cabinetBlockFireSpread = configJson.get("cabinetBlockFireSpread").getAsInt();
                    } catch (Exception e) {
                        needsUpdating = true;
                    }
                    try {
                        ModConfig.mosaicsAndTilesStrengthMultiplayer = configJson.get("mosaicsAndTilesStrengthMultiplayer").getAsFloat();
                    } catch (Exception e) {
                        needsUpdating = true;
                    }

//                    try {
//                        ModConfig.customColorReferences = configJson.get("customColorReferences").getAsMap();
//                    } catch (Exception e) {
//                        needsUpdating = true;
//                    }
                }
                if (needsUpdating) {
                    saveConfigToFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Create the config file
            saveConfigToFile();
        }
    }

    // Create the shimmer support configuration file if it does not exist
    public static void checkShimmerSupportConfig() {
        File configDir = FabricLoader.getInstance().getConfigDir().toFile();
        File configFile = new File(configDir, "shimmer/humility.json");

        if (!configFile.exists()) {
            // Create the config file
            saveShimmerSupportConfig();
        }
    }

    public static void saveShimmerSupportConfig() {
        File configDir = FabricLoader.getInstance().getConfigDir().toFile();
        File configFile = new File(configDir, "shimmer/humility.json");

        // Create the config file
        String JSON = """
                {
                    "ColorReference": {
                        "white" : {
                            "r": 255,
                            "g": 255,
                            "b": 255,
                            "a":\s""" + ModConfig.LEDColoredLightStrength + """
                        
                        \t\t},
                        \t\t"light_gray" : {
                            \t\t"r": 200,
                            \t\t"g": 200,
                            \t\t"b": 200,
                            \t\t"a":\s""" + ModConfig.LEDColoredLightStrength + """
                        
                    \t\t},
                        \t"gray" : {
                            \t"r": 100,
                            \t"g": 100,
                            \t"b": 100,
                            \t"a":\s""" + ModConfig.LEDColoredLightStrength + """
                        
                    \t\t},
                        \t"black" : {
                            \t"r": 0,
                            \t"g": 0,
                            \t"b": 0,
                            \t"a":\s""" + ModConfig.LEDColoredLightStrength + """
                        
                    \t\t},
                        \t"brown" : {
                            \t"r": 139,
                            \t"g": 69,
                            \t"b": 19,
                            \t"a":\s""" + ModConfig.LEDColoredLightStrength + """
                        
                    \t\t},
                        \t"red" : {
                            \t"r": 255,
                            \t"g": 0,
                            \t"b": 0,
                            \t"a":\s""" + ModConfig.LEDColoredLightStrength + """
                        
                    \t\t},
                        \t"orange" : {
                            \t"r": 255,
                            \t"g": 165,
                            \t"b": 0,
                            \t"a":\s""" + ModConfig.LEDColoredLightStrength + """
                        
                    \t\t},
                        \t"yellow" : {
                            \t"r": 255,
                            \t"g": 255,
                            \t"b": 0,
                            \t"a":\s""" + ModConfig.LEDColoredLightStrength + """
                        
                    \t\t},
                        \t"lime" : {
                            \t"r": 0,
                            \t"g": 255,
                            \t"b": 0,
                            \t"a":\s""" + ModConfig.LEDColoredLightStrength + """
                        
                    \t\t},
                        \t"green" : {
                            \t"r": 0,
                            \t"g": 128,
                            \t"b": 0,
                            \t"a":\s""" + ModConfig.LEDColoredLightStrength + """
                        
                    \t\t},
                        \t"cyan" : {
                            \t"r": 0,
                            \t"g": 255,
                            \t"b": 255,
                            \t"a":\s""" + ModConfig.LEDColoredLightStrength + """
                        
                    \t\t},
                        \t"light_blue" : {
                            \t"r": 0,
                            \t"g": 0,
                            \t"b": 255,
                            \t"a":\s""" + ModConfig.LEDColoredLightStrength + """
                        
                    \t\t},
                        \t"blue" : {
                            \t"r": 0,
                            \t"g": 0,
                            \t"b": 128,
                            \t"a":\s""" + ModConfig.LEDColoredLightStrength + """
                        
                    \t\t},
                        \t"purple" : {
                            \t"r": 128,
                            \t"g": 0,
                            \t"b": 128,
                            \t"a":\s""" + ModConfig.LEDColoredLightStrength + """
                        
                    \t\t},
                        \t"magenta" : {
                            \t"r": 255,
                            \t"g": 0,
                            \t"b": 255,
                            \t"a":\s""" + ModConfig.LEDColoredLightStrength + """
                        
                    \t\t},
                        \t"pink" : {
                            \t"r": 255,
                            \t"g": 192,
                            \t"b": 203,
                            \t"a":\s""" + ModConfig.LEDColoredLightStrength + """
                        
                }
            },
                    
            "LightBlock": [
        """;

        String[] colors = {"white", "light_gray", "gray", "black", "brown", "red", "orange", "yellow", "lime", "green", "cyan", "light_blue", "blue", "purple", "magenta", "pink"};

        for (String color : colors) {
            if (color.equals("pink")) {
                JSON += """
            \t\t{
                \t\t"block": "humility-afm:led_""" + color + """
                ",
                \t\t\t"color": "#""" + color + """
                ",
                \t\t\t"radius":\s""" + ModConfig.LEDColoredLightRadius + """
    
            \t\t}
                """;
            } else {
                JSON += """
                        \t\t{
                            \t\t"block": "humility-afm:led_""" + color + """
                        ",
                        \t\t\t"color": "#""" + color + """
                        ",
                        \t\t\t"radius":\s""" + ModConfig.LEDColoredLightRadius + """
                        
                        },
                """;
            }
        }

        JSON +="""
        ]
    }
        """;
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write(JSON);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
