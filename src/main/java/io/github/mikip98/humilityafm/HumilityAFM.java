package io.github.mikip98.humilityafm;

import io.github.mikip98.humilityafm.config.ConfigJSON;
import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.generators.*;
import io.github.mikip98.humilityafm.registries.BlockEntityRegistry;
import io.github.mikip98.humilityafm.registries.BlockRegistry;
import io.github.mikip98.humilityafm.registries.ItemGroupRegistry;
import io.github.mikip98.humilityafm.registries.ItemRegistry;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;

import static net.fabricmc.loader.api.FabricLoader.getInstance;

public class HumilityAFM implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_ID = "humility-afm";
	public static final String MOD_NAME = "Humility AFM";
	public static final String MOD_CAMEL = "HumilityAFM";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_CAMEL);


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		// ------------------------------------ INITIALIZATION ------------------------------------
		LOGGER.info(MOD_NAME + " is initializing!");
		if (Math.random() < 0.05) printPumpkin();

		ConfigJSON.loadConfigFromFile();
		checkForSupportedMods();
		ConfigJSON.checkShimmerSupportConfig();

		CabinetBlockGenerator.init();
		ForcedCornerStairsGenerator.init();
		WoodenMosaicGenerator.init();
		TerracottaTilesGenerator.init();
		ColouredLightsGenerator.init();
		CandlestickGenerator.init();


		// ------------------------------------ REGISTRATION ------------------------------------
		// ............ ITEM GROUPS ............
		ItemGroupRegistry.registerItemGroups();

		// ............ ITEMS ............
		ItemRegistry.register();

		// ............ BLOCKS ............
		BlockRegistry.register();

		// ............ BLOCK ENTITIES ............
		BlockEntityRegistry.register();
	}


	public static void checkForSupportedMods() {
		checkForMod("shimmer", "Shimmer", HumilityAFM::shimmerDetected);
		checkForMod("betternether", HumilityAFM::betterNetherDetected);
	}

	protected static void betterNetherDetected() {
		LOGGER.info("Better Nether mod detected! Enabling Better Nether support.");
		ModConfig.betterNetherDetected = true;
	}
	protected static void shimmerDetected() {
		LOGGER.info("Shimmer mod detected! Disabling LEDs brightening.");
		ModConfig.shimmerDetected = true;
	}

	protected static void checkForMod(String modID, Runnable callback) { checkForMod(modID, modID, callback); }
	protected static void checkForMod(String modID, String searchPhrase, Runnable callback) {
		if (getInstance().isModLoaded(modID)) {
			callback.run();
			return;
		}
		// Go through all mods in the mods folder and check if any of them are the mod of interest
		Path gameDirPath = FabricLoader.getInstance().getGameDir();
		File modsFolder = new File(gameDirPath + "/mods");
		File[] mods = modsFolder.listFiles();
		if (mods != null) {
			if (Arrays.stream(mods).anyMatch((mod) -> mod.getName().startsWith(searchPhrase) && mod.getName().endsWith(".jar"))) {
				callback.run();
			}
		}
	}


	public static Identifier getId(String name) {
		return new Identifier(MOD_ID, name);
	}
	public static Identifier[] getIds(Stream<String> names) {
		return names.map(HumilityAFM::getId).toArray(Identifier[]::new);
	}


	public static void printPumpkin() {
		System.out.println("  ___  ");
		System.out.println(" / _ \\ ");
		System.out.println("| | | |");
		System.out.println("| |_| |");
		System.out.println(" \\___/ ");
	}
}