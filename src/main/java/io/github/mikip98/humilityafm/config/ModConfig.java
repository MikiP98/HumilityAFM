package io.github.mikip98.humilityafm.config;

import io.github.mikip98.humilityafm.util.data_types.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModConfig {
    public static boolean TransparentCabinetBlocks = true;
    public static boolean enableLightStrips = false;
    public static boolean enableLightStripBrightening = true;
    public static boolean enableLightStripRadiusColorCompensation = true;
    public static boolean enableCandlesticks = false;

    public static short LightStripColoredLightStrength = 85;
    public static short LightStripColoredLightRadius = 9;
    public static short LightStripRadiusColorCompensationBias = 0;
    // (3 x 20 + 60) / 5 = 24 -> flammability
    // (3 x 5 + 30) / 5 = 9 -> fire spreading speed
    public static int cabinetBlockBurnTime = 24;
    public static int cabinetBlockFireSpread = 9;
    public static float mosaicsAndTilesStrengthMultiplayer = (float) (4.0 / 3.0);  // 1.333f

    public static ArrayList<Color> LightStripColors = new ArrayList<>();
    static {
        LightStripColors.add(new Color("white", 255, 255,255));
        LightStripColors.add(new Color("light_gray", 180, 180, 180));
        LightStripColors.add(new Color("gray", 90, 90, 90));
        LightStripColors.add(new Color("black", 0, 0, 0));
        LightStripColors.add(new Color("brown", 139, 69, 19));
        LightStripColors.add(new Color("red", 255, 0, 0));
        LightStripColors.add(new Color("orange", 255, 165, 0));
        LightStripColors.add(new Color("yellow", 255, 255, 0));
        LightStripColors.add(new Color("lime", 192, 255, 0));
        LightStripColors.add(new Color("green", 0, 255, 0));
        LightStripColors.add(new Color("cyan", 0, 255, 255));
        LightStripColors.add(new Color("light_blue", 30, 144, 255));
        LightStripColors.add(new Color("blue", 0, 0, 255));
        LightStripColors.add(new Color("purple", 128, 0, 128));
        LightStripColors.add(new Color("magenta", 255, 0, 255));
        LightStripColors.add(new Color("pink", 255, 192, 203));
    }
    public static Map<String, Color> pumpkinColors = new HashMap<>(Map.of(
            "red", new Color("red", 255, 0, 0),
            "light_blue", new Color("light_blue", 0, 100, 255)
    ));

    public static boolean shimmerDetected = false;
    public static boolean betterNetherDetected = false;
}

