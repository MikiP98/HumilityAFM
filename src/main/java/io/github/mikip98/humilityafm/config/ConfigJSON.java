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
        configJson.addProperty("enableLEDs", ModConfig.enableLEDs);
        configJson.addProperty("enableLEDsBrightening", ModConfig.enableLEDsBrightening);
        configJson.addProperty("enableLEDRadiusColorCompensation", ModConfig.enableLEDRadiusColorCompensation);
        configJson.addProperty("enableCandlesticks", ModConfig.enableCandlesticks);

        configJson.addProperty("LEDColoredLightStrength", ModConfig.LEDColoredLightStrength);
        configJson.addProperty("LEDColoredLightRadius", ModConfig.LEDColoredLightRadius);
        configJson.addProperty("LEDRadiusColorCompensationBias", ModConfig.LEDRadiusColorCompensationBias);
        configJson.addProperty("cabinetBlockBurnTime", ModConfig.cabinetBlockBurnTime);
        configJson.addProperty("cabinetBlockFireSpread", ModConfig.cabinetBlockFireSpread);
        configJson.addProperty("mosaicsAndTilesStrengthMultiplayer", ModConfig.mosaicsAndTilesStrengthMultiplayer);

        JsonArray LEDColors = new JsonArray();
        for (Color color : ModConfig.LEDColors) {
            JsonObject colorJson = new JsonObject();
            colorJson.addProperty("name", color.name);
            colorJson.addProperty("r", color.r);
            colorJson.addProperty("g", color.g);
            colorJson.addProperty("b", color.b);
            LEDColors.add(colorJson);
        }
        configJson.add("LEDColors", LEDColors);

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
            e.printStackTrace();
        }

        saveShimmerSupportConfigLED();
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
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsBoolean, "enableLEDs");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsBoolean, "enableLEDsBrightening");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsBoolean, "enableLEDRadiusColorCompensation");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsBoolean, "enableCandlesticks");

                    needsUpdating |= tryLoad(configJson, JsonElement::getAsShort, "LEDColoredLightStrength");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsShort, "LEDColoredLightRadius");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsShort, "LEDRadiusColorCompensationBias");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsInt, "cabinetBlockBurnTime");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsInt, "cabinetBlockFireSpread");
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsFloat, "mosaicsAndTilesStrengthMultiplayer");

//                    needsUpdating |= tryLoad(configJson, JsonElement::getAsJsonArray, "LEDColors");
                    try {
                        JsonArray LEDColors = configJson.getAsJsonArray("LEDColors");
                        for (int i = 0; i < LEDColors.size(); i++) {
                            JsonObject colorJson = LEDColors.get(i).getAsJsonObject();
                            Color color = ModConfig.LEDColors.get(i);
                            color.r = colorJson.get("r").getAsShort();
                            color.g = colorJson.get("g").getAsShort();
                            color.b = colorJson.get("b").getAsShort();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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
                        e.printStackTrace();
                        needsUpdating = true;
                    }
                }
                if (needsUpdating) {
                    LOGGER.info("Updating config file to include new values");
                    saveConfigToFile();  // Update the config file to include new values
                }
            } catch (IOException e) {
                e.printStackTrace();
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
            e.printStackTrace();
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
            saveShimmerSupportConfigLED();
        }

        configFile = new File(configDir, "shimmer/humility-other.json");
        if (!configFile.exists()) {
            // Create the config file
            saveShimmerSupportConfigOther();
        }
    }

    public static void saveShimmerSupportConfigLED() {
        File configDir = FabricLoader.getInstance().getConfigDir().toFile();
        File configFile = new File(configDir, "shimmer/humility-led.json");

        // Create the config file
        String JSON = """
                {
                \t"ColorReference": {
                    """;
        for (Color color : ModConfig.LEDColors) {
            if (color.name.equals("pink")) {
                JSON += """
                        \t\t\"""" + color.name + """ 
                        ": {
                        \t\t\t"r":\s""" + color.r + """
                        ,
                        \t\t\t"g":\s""" + color.g + """
                        ,
                        \t\t\t"b":\s""" + color.b + """
                        ,
                        \t\t\t"a":\s""" + ModConfig.LEDColoredLightStrength + """
                            
                        \t\t}
                        """;
            } else {
                JSON += """
                        \t\t\"""" + color.name + """ 
                        ": {
                        \t\t\t"r":\s""" + color.r + """
                        ,
                        \t\t\t"g":\s""" + color.g + """
                        ,
                        \t\t\t"b":\s""" + color.b + """
                        ,
                        \t\t\t"a":\s""" + ModConfig.LEDColoredLightStrength + """
                            
                        \t\t},
                        """;
            }
        }
        JSON += """
        \t},
        
        \t"LightBlock": [
        """;

        for (Color color : ModConfig.LEDColors) {
            if (color.name.equals("pink")) {
                JSON += """
                        \t\t{
                        \t\t\t"block": "humility-afm:led_""" + color.name + """
                        ",
                        \t\t\t"color": "#""" + color.name + """
                        ",
                        \t\t\t"radius":\s""" + (ModConfig.LEDColoredLightRadius + bias(color)) + """
                        
                        \t\t}
                        """;
            } else {
                JSON += """
                        \t\t{
                        \t\t\t"block": "humility-afm:led_""" + color.name + """
                        ",
                        \t\t\t"color": "#""" + color.name + """
                        ",
                        \t\t\t"radius":\s""" + (ModConfig.LEDColoredLightRadius + bias(color)) + """
                        
                        \t\t},
                        """;
            }
        }
        JSON +="""
    \t]
    }
        """;
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write(JSON);
        } catch (IOException e) {
            LOGGER.error("Error writing to file: " + e.getMessage());
        }
    }

    public static void saveShimmerSupportConfigOther() {
        File configDir = FabricLoader.getInstance().getConfigDir().toFile();
        if (!Files.exists(configDir.toPath().resolve("shimmer"))) {
            return;
        }
        File configFile = new File(configDir, "shimmer/humility-other.json");

        Map<String, Color> colors = ModConfig.pumpkinColors;

        // Create the config file
        String JSON = """
                {
                \t"ColorReference": {
                \t\t\"""" + colors.get("red").name + """
                    " : {
                    \t\t\t"r":\s""" + colors.get("red").r + """
                    ,
                    \t\t\t"g":\s""" + colors.get("red").g + """
                    ,
                    \t\t\t"b":\s""" + colors.get("red").b + """
                    ,
                    \t\t\t"a": 255
                    \t\t},
                    \t\t\"""" + colors.get("light_blue").name + """
                    " : {
                    \t\t\t"r":\s""" + colors.get("light_blue").r + """
                    ,
                    \t\t\t"g":\s""" + colors.get("light_blue").g + """
                    ,
                    \t\t\t"b":\s""" + colors.get("light_blue").b + """
                    ,
                    \t\t\t"a": 255
                    \t\t}
                    \t},
                    
                    \t"LightBlock": [
                    \t\t{
                    \t\t\t"block": "humility-afm:jack_o_lantern_redstone",
                    \t\t\t"color": "#red",
                    \t\t\t"radius": "9"
                    \t\t},
                    \t\t{
                    \t\t\t"block": "humility-afm:jack_o_lantern_soul",
                    \t\t\t"color": "#light_blue",
                    \t\t\t"radius": "10"
                    \t\t}
                    \t]
                    }
                    """;

        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write(JSON);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @BM
    private static short bias(Color color) {
        if (ModConfig.enableLEDRadiusColorCompensation) {
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
