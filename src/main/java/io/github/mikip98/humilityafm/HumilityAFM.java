package io.github.mikip98.humilityafm;

import io.github.mikip98.humilityafm.config.ConfigJSON;
import io.github.mikip98.humilityafm.registries.*;
import io.github.mikip98.humilityafm.util.generation_data.ActiveGenerationData;
import io.github.mikip98.humilityafm.util.mod_support.ModSupportManager;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.mikip98.humilityafm.util.FunUtils.*;

public class HumilityAFM implements ModInitializer {
	public static final String MOD_ID = "humility-afm";
	public static final String MOD_NAME = "Humility AFM";
	public static final String MOD_CAMEL = "HumilityAFM";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_CAMEL);

	@Override
	public void onInitialize() {
		// ------------------------------------ INITIALIZATION ------------------------------------
		LOGGER.info(MOD_NAME + " is initializing! {}", getRandomFunSymbol());
		if (Math.random() < 0.05) printPumpkin();
		// Ensure correct loading order of the first crucial static classes
		// Those 3 have to be loaded in this order before anything else
		ConfigJSON.loadConfigFromFile();  // Load the config file
		ModSupportManager.init();  // Check for supported mods (if datagen mode is enabled, all will be marked as preset)
		ActiveGenerationData.init();  // Initialize active generation data according to which of the supported mods are loaded


		// ------------------------------------ REGISTRATION --------------------------------------
		// ............ BLOCKS ............
		BlockRegistry.init();
		// ............ BLOCK ENTITIES ............
		BlockEntityRegistry.register();
		// ............ ITEMS ............
		ItemRegistry.register();
		// ............ ITEM GROUPS ............
		ItemGroupRegistry.registerItemGroups();
		// ............ NETWORKING ............
		NetworkRegistry.registerPayload();


		// ------------------------------------ CLEANUP -------------------------------------------
		ActiveGenerationData.clear();
	}

	/**
	 * Returns a new identifier for the given name, in 'humility-afm' namespace
	 */
	public static Identifier getId(String name) {
		return Identifier.of(MOD_ID, name);
	}
}