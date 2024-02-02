package io.github.mikip98.config;

import io.github.mikip98.helpers.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class ModConfig {
    public static boolean TransparentCabinetBlocks = true;
    public static boolean enableLEDs = false;
    public static boolean enableLEDsBrightening = true;
    public static boolean enableLEDRadiusColorCompensation = true;
    public static boolean enableCandlesticks = false;

    public static short LEDColoredLightStrength = 85;
    public static short LEDColoredLightRadius = 9;
    public static short LEDRadiusColorCompensationBias = 0;
    public static int cabinetBlockBurnTime = 24;
    public static int cabinetBlockFireSpread = 9;
    public static float mosaicsAndTilesStrengthMultiplayer = 1.5f;

    public static ArrayList<Color> LEDColors = new ArrayList<>();
    static {
        LEDColors.add(new Color("white", 255, 255,255));
        LEDColors.add(new Color("light_gray", 180, 180, 180));
        LEDColors.add(new Color("gray", 90, 90, 90));
        LEDColors.add(new Color("black", 0, 0, 0));
        LEDColors.add(new Color("brown", 139, 69, 19));
        LEDColors.add(new Color("red", 255, 0, 0));
        LEDColors.add(new Color("orange", 255, 165, 0));
        LEDColors.add(new Color("yellow", 255, 255, 0));
        LEDColors.add(new Color("lime", 192, 255, 0));
        LEDColors.add(new Color("green", 0, 255, 0));
        LEDColors.add(new Color("cyan", 0, 255, 255));
        LEDColors.add(new Color("light_blue", 30, 144, 255));
        LEDColors.add(new Color("blue", 0, 0, 255));
        LEDColors.add(new Color("purple", 128, 0, 128));
        LEDColors.add(new Color("magenta", 255, 0, 255));
        LEDColors.add(new Color("pink", 255, 192, 203));
    }
    public static Map<String, Color> pumpkinColors = new HashMap<>();
    static {
        pumpkinColors.put("red", new Color("red", 255, 0, 0));
        pumpkinColors.put("light_blue", new Color("light_blue", 0, 100, 255));
    }

    public static boolean shimmerDetected = false;
    public static boolean betterNetherDetected = false;
}

