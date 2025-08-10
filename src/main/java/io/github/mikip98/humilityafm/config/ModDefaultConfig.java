package io.github.mikip98.humilityafm.config;

import io.github.mikip98.humilityafm.util.SupportedMods;
import io.github.mikip98.humilityafm.util.data_types.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ModDefaultConfig {
    public static final boolean defaultTransparentCabinetBlocks = true;
    public static final boolean defaultEnableLightStripBrightening = true;
    public static final boolean defaultEnableLightStripRadiusColorCompensation = true;
    public static final boolean defaultEnableCandlestickBeta = false;
    public static final boolean defaultEnableColouredFeatureSetBeta = false;

    public static final float defaultMosaicsAndTilesStrengthMultiplayer = (float) (4.0 / 3.0);  // 1.333f
    // (3 x 20 + 60) / 5 = 24 -> flammability
    // (3 x 5 + 30) / 5 = 9 -> fire spreading speed
    public static final int defaultCabinetBlockBurnTime = 24;
    public static final int defaultCabinetBlockFireSpread = 9;

    public static final boolean defaultDatagenMode = false;

    public static final short defaultLightStripColoredLightStrength = 85;
    public static final short defaultLightStripColoredLightRadius = 9;
    public static final short defaultLightStripRadiusColorCompensationBias = 0;

    public static final ArrayList<Color> defaultLightStripColors = new ArrayList<>();
    static {
        defaultLightStripColors.add(new Color("white", 255, 255,255));
        defaultLightStripColors.add(new Color("light_gray", 180, 180, 180));
        defaultLightStripColors.add(new Color("gray", 90, 90, 90));
        defaultLightStripColors.add(new Color("black", 0, 0, 0));
        defaultLightStripColors.add(new Color("brown", 139, 69, 19));
        defaultLightStripColors.add(new Color("red", 255, 0, 0));
        defaultLightStripColors.add(new Color("orange", 255, 165, 0));
        defaultLightStripColors.add(new Color("yellow", 255, 255, 0));
        defaultLightStripColors.add(new Color("lime", 192, 255, 0));
        defaultLightStripColors.add(new Color("green", 0, 255, 0));
        defaultLightStripColors.add(new Color("cyan", 0, 255, 255));
        defaultLightStripColors.add(new Color("light_blue", 30, 144, 255));
        defaultLightStripColors.add(new Color("blue", 0, 0, 255));
        defaultLightStripColors.add(new Color("purple", 128, 0, 128));
        defaultLightStripColors.add(new Color("magenta", 255, 0, 255));
        defaultLightStripColors.add(new Color("pink", 255, 192, 203));
    }
    public final static Map<String, Color> defaultPumpkinColors = new HashMap<>(Map.of(
            "red", new Color("red", 255, 0, 0),
            "light_blue", new Color("light_blue", 0, 100, 255)
    ));

    public static final Map<SupportedMods, ModSupport> defaultModSupport = Arrays.stream(SupportedMods.values()).map(
            mod -> Map.entry(mod, ModSupport.AUTO)
    ).collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), HashMap::putAll);
}
