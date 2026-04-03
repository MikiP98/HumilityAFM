package io.github.mikip98.humilityafm;

import io.github.mikip98.humilityafm.config.ConfigJSON;
import io.github.mikip98.humilityafm.registries.*;
import io.github.mikip98.humilityafm.util.generation_data.ActiveGenerationData;
import io.github.mikip98.humilityafm.util.mod_support.ModSupportManager;
import io.github.mikip98.humilityafm.util.mod_support.SupportedMods;
import net.fabricmc.api.ModInitializer;
#if MC_VERSION < 12111
import net.minecraft.resources.ResourceLocation;
#else
import net.minecraft.resources.Identifier;
#endif
import org.jetbrains.annotations.Nullable;
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
		#if MC_VERSION >= 12006
		// ............ NETWORKING ............
		NetworkRegistry.registerPayload();
		#endif


		// ------------------------------------ CLEANUP -------------------------------------------
		ActiveGenerationData.clear();
	}

	/**
	 * Returns a new identifier for the given name, in 'humility-afm' namespace
	 */
	public static #if MC_VERSION < 12111 ResourceLocation #else Identifier #endif getId(String name) {
		return getId(MOD_ID, name);
	}

	/**
	 * Returns a new identifier for the given name, in 'minecraft' namespace
	 */
	public static #if MC_VERSION < 12111 ResourceLocation #else Identifier #endif getVanillaId(String name) {
		return getId("minecraft", name);
	}

	/**
	 * Returns a new identifier for the given name, in the given mod's namespace if not null, or 'minecraft' otherwise
	 */
	public static #if MC_VERSION < 12111 ResourceLocation #else Identifier #endif getVMId(@Nullable SupportedMods mod, String name) {
		return getId(mod != null ? mod.modId : "minecraft", name);
	}

	protected static #if MC_VERSION < 12111 ResourceLocation #else Identifier #endif getId(String namespace, String name) {
		final #if MC_VERSION < 12111 ResourceLocation #else Identifier #endif id =  #if MC_VERSION < 12111 ResourceLocation #else Identifier #endif .tryBuild(namespace, name);
		if (id == null) throw new IllegalArgumentException("Broken block id: " + namespace + ":" + name);
		return id;
	}
}