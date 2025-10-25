package io.github.mikip98.humilityafm.config;

import io.github.mikip98.humilityafm.config.enums.CreativeItemGroupCategorization;
import io.github.mikip98.humilityafm.config.enums.ModSupportState;
import io.github.mikip98.humilityafm.util.mod_support.SupportedMods;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ModDefaultConfig {
    public static final boolean defaultTransparentCabinetBlocks = true;
    public static final boolean defaultIlluminatedCabinetBlockBrightening = true;
    public static final boolean defaultEnableLightStripBrightening = true;
    public static final boolean defaultEnableLightStripRadiusColorCompensation = true;
    public static final boolean defaultEnableCandlestickBeta = false;
    public static final boolean defaultEnableColouredFeatureSetBeta = false;

    public static final CreativeItemGroupCategorization dCreativeItemGroupCategorization = CreativeItemGroupCategorization.SEPARATE;
    public static final boolean dPlaceHumilityBlocksInVanillaCreativeCategories = true;

    public static final float defaultMosaicsAndTilesStrengthMultiplayer = (float) (4.0 / 3.0);  // 1.333f
    // (3 x 20 + 60) / 5 = 24 -> flammability
    // (3 x 5 + 30) / 5 = 9 -> fire spreading speed
    public static final int defaultCabinetBlockBurnTime = 24;
    public static final int defaultCabinetBlockFireSpread = 9;

    public static final boolean defaultDatagenMode = false;
    public static final boolean defaultPrintInChatServerClientMissmatch = true;

    public static final Map<SupportedMods, ModSupportState> defaultModSupport = Arrays.stream(SupportedMods.values()).map(
            mod -> Map.entry(mod, ModSupportState.AUTO)
    ).collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), HashMap::putAll);
}
