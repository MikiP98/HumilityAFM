package io.github.mikip98.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.mikip98.helpers.Color;
import net.fabricmc.loader.api.FabricLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.function.Function;

import static io.github.mikip98.HumilityAFM.LOGGER;

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

        configJson.addProperty("LEDColoredLightStrength", ModConfig.LEDColoredLightStrength);
        configJson.addProperty("LEDColoredLightRadius", ModConfig.LEDColoredLightRadius);
        configJson.addProperty("LEDRadiusColorCompensationBias", ModConfig.LEDRadiusColorCompensationBias);
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
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsBoolean, ModConfig.class.getField("TransparentCabinetBlocks"));
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsBoolean, ModConfig.class.getField("enableLEDs"));
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsBoolean, ModConfig.class.getField("enableLEDsBrightening"));
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsBoolean, ModConfig.class.getField("enableLEDRadiusColorCompensation"));

                    needsUpdating |= tryLoad(configJson, JsonElement::getAsShort, ModConfig.class.getField("LEDColoredLightStrength"));
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsShort, ModConfig.class.getField("LEDColoredLightRadius"));
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsShort, ModConfig.class.getField("LEDRadiusColorCompensationBias"));
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsInt, ModConfig.class.getField("cabinetBlockBurnTime"));
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsInt, ModConfig.class.getField("cabinetBlockFireSpread"));
                    needsUpdating |= tryLoad(configJson, JsonElement::getAsFloat, ModConfig.class.getField("mosaicsAndTilesStrengthMultiplayer"));

//                    needsUpdating |= tryLoad(configJson, JsonElement::getAsMap, ModConfig.class.getField("customColorReferences"));
                }
                if (needsUpdating) {
                    saveConfigToFile();  // Update the config file to include new values
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        } else {
            saveConfigToFile();  // Create the config file
        }
    }
    private static <T> boolean tryLoad(JsonObject configJson, Function<JsonElement, T> getter, Field field) {
        try {
            T value = getter.apply(configJson.get(field.getName()));
            field.set(ModConfig.class, value);
        } catch (Exception e) {
            return true;
        }
        return false;
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

        ArrayList<Color> colors = new ArrayList<>();
        colors.add(new Color("white", 255, 255,255));
        colors.add(new Color("light_gray", 180, 180, 180));
        colors.add(new Color("gray", 90, 90, 90));
        colors.add(new Color("black", 0, 0, 0));
        colors.add(new Color("brown", 139, 69, 19));
        colors.add(new Color("red", 255, 0, 0));
        colors.add(new Color("orange", 255, 165, 0));
        colors.add(new Color("yellow", 255, 255, 0));
        colors.add(new Color("lime", 192, 255, 0));
        colors.add(new Color("green", 0, 255, 0));
        colors.add(new Color("cyan", 0, 255, 255));
        colors.add(new Color("light_blue", 30, 144, 255));
        colors.add(new Color("blue", 0, 0, 255));
        colors.add(new Color("purple", 128, 0, 128));
        colors.add(new Color("magenta", 255, 0, 255));
        colors.add(new Color("pink", 255, 192, 203));
//        LOGGER.info("Colors: ");
//        for (Color color : colors) {
//            LOGGER.info(color.toString());
//        }

        // Create the config file
        String JSON = """
                {
                    "ColorReference": {
                    """;
        for (Color color : colors) {
            if (color.name.equals("pink")) {
                JSON += """
                        \t\t\"""" + color.name + """ 
                        " : {
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
                        " : {
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
            },
                    
            "LightBlock": [
        """;

        for (Color color : colors) {
            if (color.name.equals("pink")) {
                JSON += """
            \t\t{
                \t\t"block": "humility-afm:led_""" + color.name + """
                ",
                \t\t\t"color": "#""" + color.name + """
                ",
                \t\t\t"radius":\s""" + (ModConfig.LEDColoredLightRadius + bias(color)) + """
    
            \t\t}
                """;
            } else {
                JSON += """
                        \t\t{
                            \t\t"block": "humility-afm:led_""" + color.name + """
                        ",
                        \t\t\t"color": "#""" + color.name + """
                        ",
                        \t\t\t"radius":\s""" + (ModConfig.LEDColoredLightRadius + bias(color)) + """
                        
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
