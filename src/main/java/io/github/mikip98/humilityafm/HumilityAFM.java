package io.github.mikip98.humilityafm;

import io.github.mikip98.humilityafm.config.ConfigJSON;
import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.content.blockentities.LEDBlockEntity;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.IlluminatedCabinetBlockEntity;
import io.github.mikip98.humilityafm.generators.CabinetBlockGenerator;
import io.github.mikip98.humilityafm.generators.CandlestickGenerator;
import io.github.mikip98.humilityafm.generators.ColouredLightsGenerator;
import io.github.mikip98.humilityafm.generators.ForcedCornerStairsGenerator;
import io.github.mikip98.humilityafm.helpers.*;
import io.github.mikip98.humilityafm.registries.BlockRegistry;
import io.github.mikip98.humilityafm.registries.ItemGroupRegistry;
import io.github.mikip98.humilityafm.registries.ItemRegistry;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.CabinetBlockEntity;

import java.io.File;
import java.nio.file.Path;
import java.util.stream.Stream;

import static io.github.mikip98.humilityafm.registries.BlockRegistry.CABINET_BLOCK;
import static io.github.mikip98.humilityafm.registries.BlockRegistry.ILLUMINATED_CABINET_BLOCK;
import static net.fabricmc.loader.api.FabricLoader.getInstance;

public class HumilityAFM implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_ID = "humility-afm";
	public static final String MOD_NAME = "Humility AFM";
	public static final String MOD_CAMEL = "HumilityAFM";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_CAMEL);

	//Cabinet block entity
	public static BlockEntityType<CabinetBlockEntity> CABINET_BLOCK_ENTITY;
	public static BlockEntityType<IlluminatedCabinetBlockEntity> ILLUMINATED_CABINET_BLOCK_ENTITY;

	// LED block entity
	public static BlockEntityType<LEDBlockEntity> LED_BLOCK_ENTITY;



	// Candlestick
//	public static Block CANDLESTICK = new JustHorizontalFacingBlock(FabricBlockSettings.create().strength(0.5f).nonOpaque());
//	public static Item CANDLESTICK_ITEM = new BlockItem(CANDLESTICK, new FabricItemSettings());

	private Block[] getCabinetBlockVariantsToRegisterBlockEntity() {
		final Block[] cabinetBlockVariants = CabinetBlockGenerator.cabinetBlockVariants;
		Block[] blocks = new Block[cabinetBlockVariants.length + 1];
		blocks[0] = CABINET_BLOCK;
        System.arraycopy(cabinetBlockVariants, 0, blocks, 1, cabinetBlockVariants.length);
		return blocks;
	}
	private Block[] getIlluminatedCabinetBlockVariantsToRegisterBlockEntity() {
		final Block[] illuminatedCabinetBlockVariants = CabinetBlockGenerator.illuminatedCabinetBlockVariants;
		Block[] blocks = new Block[illuminatedCabinetBlockVariants.length + 1];
		blocks[0] = ILLUMINATED_CABINET_BLOCK;
        System.arraycopy(illuminatedCabinetBlockVariants, 0, blocks, 1, illuminatedCabinetBlockVariants.length);
		return blocks;
	}


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		// ------------------------------------ INITIALIZATION ------------------------------------
		LOGGER.info(MOD_NAME + " is initializing!");

		ConfigJSON.loadConfigFromFile();
		ConfigJSON.checkShimmerSupportConfig();
		checkForSupportedMods();

		CabinetBlockGenerator.init();
		ForcedCornerStairsGenerator.init();
		WoodenMosaicHelper.init();
		TerracottaTilesHelper.init();
		ColouredLightsGenerator.init();
		PumpkinHelper.init();
		if (ModConfig.enableCandlesticks) CandlestickGenerator.init();


		// ------------------------------------ REGISTRATION ------------------------------------
		// ............ ITEM GROUPS ............
		ItemGroupRegistry.registerItemGroups();

		// ............ ITEMS ............
		ItemRegistry.register();

		// ............ BLOCKS & BLOCK ITEMS ............
		BlockRegistry.register();

		// ............ FINAL BLOCKS & ITEMS ............
		// Register innerOuter stairs variants
		ForcedCornerStairsGenerator.registerInnerOuterStairsVariants();
		// Register wooden mosaic variants
		WoodenMosaicHelper.registerWoodenMosaicVariants();
		// Register terracotta tiles variants
		TerracottaTilesHelper.registerTerracottaTilesVariants();
		// Register red and blue pumpkins
		PumpkinHelper.registerPumpkins();

		// ............ BLOCK ENTITIES ............
		//Register cabinet block entity
		CABINET_BLOCK_ENTITY = Registry.register(
				Registries.BLOCK_ENTITY_TYPE,
				new Identifier(MOD_ID, "cabinet_block_entity"),
				FabricBlockEntityTypeBuilder.create(CabinetBlockEntity::new, getCabinetBlockVariantsToRegisterBlockEntity()).build()
				//, CABINET_BLOCK_BIRCH_WHITE, CABINET_BLOCK_BIRCH_ORANGE, CABINET_BLOCK_BIRCH_MAGENTA, CABINET_BLOCK_BIRCH_LIGHT_BLUE, CABINET_BLOCK_BIRCH_YELLOW, CABINET_BLOCK_BIRCH_LIME, CABINET_BLOCK_BIRCH_PINK, CABINET_BLOCK_BIRCH_GRAY, CABINET_BLOCK_BIRCH_LIGHT_GRAY, CABINET_BLOCK_BIRCH_CYAN, CABINET_BLOCK_BIRCH_PURPLE, CABINET_BLOCK_BIRCH_BLUE, CABINET_BLOCK_BIRCH_BROWN, CABINET_BLOCK_BIRCH_GREEN, CABINET_BLOCK_BIRCH_RED, CABINET_BLOCK_BIRCH_BLACK
		);
		//Register illuminated cabinet block entity
		ILLUMINATED_CABINET_BLOCK_ENTITY = Registry.register(
				Registries.BLOCK_ENTITY_TYPE,
				new Identifier(MOD_ID, "illuminated_cabinet_block_entity"),
				FabricBlockEntityTypeBuilder.create(IlluminatedCabinetBlockEntity::new, getIlluminatedCabinetBlockVariantsToRegisterBlockEntity()).build()
		);

		//Register LED block entity
		if (ModConfig.enableLEDs && ModConfig.enableLEDsBrightening && !ModConfig.shimmerDetected) {
			LED_BLOCK_ENTITY = Registry.register(
					Registries.BLOCK_ENTITY_TYPE,
					new Identifier(MOD_ID, "led_block_entity"),
					FabricBlockEntityTypeBuilder.create(LEDBlockEntity::new, ColouredLightsGenerator.LEDBlockVariants).build()
			);
		}
	}

	public static void checkForSupportedMods() {
		checkIfShimmerModIsPresent();
		checkForBetterNether();
	}

	private static void checkForBetterNether() {
//		if (getInstance().isModLoaded("betternether")) {
//			ModConfig.betterNetherDetected = true;
//			LOGGER.info("Better Nether mod detected! Enabling Better Nether support.");
//		} else {
//			// Go through all mods in the mods folder and check if any of them are Better Nether
//			Path gameDirPath = FabricLoader.getInstance().getGameDir();
//			File modsFolder = new File(gameDirPath + "/mods");
//			File[] mods = modsFolder.listFiles();
//			if (mods != null) {
//				for (File mod : mods) {
//					if (mod.getName().startsWith("betternether")) {
//						ModConfig.betterNetherDetected = true;
//						LOGGER.info("Better Nether mod detected! Enabling Better Nether support.");
//						break;
//					}
//				}
//			}
//		}
	}

	private static void checkIfShimmerModIsPresent() {
		if (getInstance().isModLoaded("shimmer")) {
			ModConfig.shimmerDetected = true;
			LOGGER.info("Shimmer mod detected! Disabling LEDs brightening.");
		} else {
			// Go through all mods in the mods folder and check if any of them are Shimmer
			Path gameDirPath = FabricLoader.getInstance().getGameDir();
			File modsFolder = new File(gameDirPath + "/mods");
			File[] mods = modsFolder.listFiles();
			if (mods != null) {
				for (File mod : mods) {
					if (mod.getName().startsWith("Shimmer")) {
						ModConfig.shimmerDetected = true;
						LOGGER.info("Shimmer mod detected! Disabling LEDs brightening.");
						break;
					}
				}
			}
		}
	}


	public static Identifier getId(String name) {
		return new Identifier(MOD_ID, name);
	}
	public static Identifier[] getIds(Stream<String> names) {
		return names.map(HumilityAFM::getId).toArray(Identifier[]::new);
	}
}