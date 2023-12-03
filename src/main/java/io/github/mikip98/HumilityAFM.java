package io.github.mikip98;

import io.github.mikip98.config.ConfigJSON;
import io.github.mikip98.config.ModConfig;
import io.github.mikip98.content.blockentities.LEDBlockEntity;
import io.github.mikip98.content.blockentities.cabinetBlock.IlluminatedCabinetBlockEntity;
import io.github.mikip98.content.blocks.leds.LEDBlock;
import io.github.mikip98.content.blocks.stairs.InnerStairs;
import io.github.mikip98.content.blocks.stairs.OuterStairs;
import io.github.mikip98.content.blocks.cabinet.CabinetBlock;
import io.github.mikip98.content.blocks.cabinet.IlluminatedCabinetBlock;
import io.github.mikip98.helpers.*;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.ResourceManager;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.mikip98.content.blockentities.cabinetBlock.CabinetBlockEntity;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;

import static net.fabricmc.loader.api.FabricLoader.getInstance;

public class HumilityAFM implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_ID = "humility-afm";
	public static final String MOD_NAME = "Humility AFM";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


	//Cabinet block
	private static final float CabinetBlockStrength = 2.0f;
	private static final FabricBlockSettings CabinetBlockSettings = FabricBlockSettings.create().strength(CabinetBlockStrength).requiresTool().nonOpaque();
	public static final CabinetBlock CABINET_BLOCK = new CabinetBlock(CabinetBlockSettings);
	public static final Item CABINET_BLOCK_ITEM = new BlockItem(CABINET_BLOCK, new FabricItemSettings());
	private static final FabricBlockSettings IlluminatedCabinetBlockSettings = CabinetBlockSettings.luminance(2);
	public static final IlluminatedCabinetBlock ILLUMINATED_CABINET_BLOCK = new IlluminatedCabinetBlock(IlluminatedCabinetBlockSettings);

	//Cabinet block entity
	public static BlockEntityType<CabinetBlockEntity> CABINET_BLOCK_ENTITY;
	public static BlockEntityType<IlluminatedCabinetBlockEntity> ILLUMINATED_CABINET_BLOCK_ENTITY;

	// LED block entity
	public static BlockEntityType<LEDBlockEntity> LED_BLOCK_ENTITY;

	// Stairs
	private static final float WoodenStairsBlockStrength = 2.0f;
	private static final FabricBlockSettings StairsBlockSettings = FabricBlockSettings.create().strength(WoodenStairsBlockStrength).requiresTool();
	public static final Block OUTER_STAIRS = new OuterStairs(StairsBlockSettings);
	public static final Block INNER_STAIRS = new InnerStairs(StairsBlockSettings);
	public static final Item OUTER_STAIRS_ITEM = new BlockItem(OUTER_STAIRS, new FabricItemSettings());
	public static final Item INNER_STAIRS_ITEM = new BlockItem(INNER_STAIRS, new FabricItemSettings());

	// Wooden mosaic
	private static final float WoodenMosaicStrength = 3.0f * 1.5f;
	private static final FabricBlockSettings WoodenMosaicSettings = FabricBlockSettings.create().strength(WoodenMosaicStrength).requiresTool().sounds(BlockSoundGroup.WOOD);
	public static final Block WOODEN_MOSAIC = new Block(WoodenMosaicSettings);
	public static final Item WOODEN_MOSAIC_ITEM = new BlockItem(WOODEN_MOSAIC, new FabricItemSettings());

	// LED
	public static Block LED_BLOCK;
	public static Item LED_ITEM;


	private Block[] getCabinetBlockVariantsToRegisterBlockEntity() {
		final Block[] cabinetBlockVariants = CabinetBlockHelper.cabinetBlockVariants;
		Block[] blocks = new Block[cabinetBlockVariants.length + 1];
		blocks[0] = CABINET_BLOCK;
        System.arraycopy(cabinetBlockVariants, 0, blocks, 1, cabinetBlockVariants.length);
		return blocks;
	}
	private Block[] getIlluminatedCabinetBlockVariantsToRegisterBlockEntity() {
		final Block[] illuminatedCabinetBlockVariants = CabinetBlockHelper.illuminatedCabinetBlockVariants;
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

		CabinetBlockHelper.init();
		InnerOuterStairsHelper.init();
		WoodenMosaicHelper.init();
		TerracottaTilesHelper.init();
		if (ModConfig.enableLEDs) {
			LEDHelper.init();
		}


		// ------------------------------------ REGISTRATION ------------------------------------
		// ............ ITEM GROUPS ............
		// Register Cabinet item group
		final ItemGroup CABINETS_ITEM_GROUP = FabricItemGroup.builder()
				.icon(() -> new ItemStack(HumilityAFM.CABINET_BLOCK))
				.displayName(Text.translatable("itemGroup.cabinets"))
				.entries((displayContext, entries) -> {
					for (int i = 0; i < CabinetBlockHelper.cabinetBlockVariants.length; i++) {
						entries.add(new ItemStack(CabinetBlockHelper.cabinetBlockVariants[i]));
					}
					for (int i = 0; i < CabinetBlockHelper.illuminatedCabinetBlockVariants.length; i++) {
						entries.add(new ItemStack(CabinetBlockHelper.illuminatedCabinetBlockVariants[i]));
					}
				})
				.build();
		Registry.register(Registries.ITEM_GROUP, new Identifier(MOD_ID, "cabinets_group"), CABINETS_ITEM_GROUP);

		// Register InnerOuterStairs item group
		final ItemGroup INNER_OUTER_STAIRS_ITEM_GROUP = FabricItemGroup.builder()
				.icon(() -> new ItemStack(HumilityAFM.OUTER_STAIRS))
				.displayName(Text.translatable("itemGroup.innerOuterStairs"))
				.entries((displayContext, entries) -> {
					for (int i = 0; i < InnerOuterStairsHelper.innerStairsBlockVariants.length; i++) {
						entries.add(new ItemStack(InnerOuterStairsHelper.innerStairsBlockVariants[i]));
					}
					for (int i = 0; i < InnerOuterStairsHelper.outerStairsBlockVariants.length; i++) {
						entries.add(new ItemStack(InnerOuterStairsHelper.outerStairsBlockVariants[i]));
					}
				})
				.build();
		Registry.register(Registries.ITEM_GROUP, new Identifier(MOD_ID, "inner_outer_stairs_group"), INNER_OUTER_STAIRS_ITEM_GROUP);

		// Register WoodenMosaic item group
		final ItemGroup WOODEN_MOSAIC_ITEM_GROUP = FabricItemGroup.builder()
				.icon(() -> new ItemStack(WoodenMosaicHelper.woodenMosaicVariants[0]))
				.displayName(Text.translatable("itemGroup.woodenMosaics"))
				.entries((displayContext, entries) -> {
					for (int i = 0; i < WoodenMosaicHelper.woodenMosaicVariants.length; i++) {
//						LOGGER.info("Adding wooden mosaic variant to item group: " + WoodenMosaicHelper.woodenMosaicVariants[i]);
						entries.add(new ItemStack(WoodenMosaicHelper.woodenMosaicVariants[i])); // TODO: Fix this
					}
				})
				.build();
		Registry.register(Registries.ITEM_GROUP, new Identifier(MOD_ID, "wooden_mosaics_group"), WOODEN_MOSAIC_ITEM_GROUP);

		// Register TerracottaTiles item group
		final ItemGroup TERRACOTTA_TILES_ITEM_GROUP = FabricItemGroup.builder()
				.icon(() -> new ItemStack(TerracottaTilesHelper.terracottaTilesVariants[0]))
				.displayName(Text.translatable("itemGroup.terracottaTiles"))
				.entries((displayContext, entries) -> {
					for (int i = 0; i < TerracottaTilesHelper.terracottaTilesVariants.length; i++) {
//						LOGGER.info("Adding terracotta tiles variant to item group: " + TerracottaTilesHelper.terracottaTilesVariants[i]);
						entries.add(new ItemStack(TerracottaTilesHelper.terracottaTilesVariants[i]));
					}
				})
				.build();
		Registry.register(Registries.ITEM_GROUP, new Identifier(MOD_ID, "terracotta_tiles_group"), TERRACOTTA_TILES_ITEM_GROUP);

		// Register LED item group
		if (ModConfig.enableLEDs) {
			final ItemGroup LED_ITEM_GROUP = FabricItemGroup.builder()
					.icon(() -> new ItemStack(LEDHelper.LEDBlockVariants[0]))
					.displayName(Text.translatable("itemGroup.leds"))
					.entries((displayContext, entries) -> {
						for (int i = 0; i < LEDHelper.LEDBlockVariants.length; i++) {
//							LOGGER.info("Adding LED variant to item group: " + LEDHelper.LEDBlockVariants[i]);
							entries.add(new ItemStack(LEDHelper.LEDBlockVariants[i]));
						}
					})
					.build();
			Registry.register(Registries.ITEM_GROUP, new Identifier(MOD_ID, "leds_group"), LED_ITEM_GROUP);
		}

		// Register Miscellaneous (Humility Misc) item group
		if (ModConfig.enableLEDs) {
			final ItemGroup HUMILITY_MISCELLANEOUS_GROUP = FabricItemGroup.builder()
					.icon(() -> new ItemStack(LEDHelper.LEDPowderVariants[0]))
					.displayName(Text.translatable("itemGroup.humilityMisc"))
					.entries((displayContext, entries) -> {
						for (int i = 0; i < LEDHelper.LEDPowderVariants.length; i++) {
							entries.add(new ItemStack(LEDHelper.LEDPowderVariants[i]));
						}
					})
					.build();
			Registry.register(Registries.ITEM_GROUP, new Identifier(MOD_ID, "humility_misc_group"), HUMILITY_MISCELLANEOUS_GROUP);
		}


		// ............ TEST BLOCKS & ITEMS ............
		// Register cabinet
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "cabinet_block"), CABINET_BLOCK);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "cabinet_block"), CABINET_BLOCK_ITEM);
		// Register illuminated cabinet
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "illuminated_cabinet_block"), ILLUMINATED_CABINET_BLOCK);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "illuminated_cabinet_block"), new BlockItem(ILLUMINATED_CABINET_BLOCK, new FabricItemSettings()));
		// Register always corner stair
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "outer_stairs"), OUTER_STAIRS);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "outer_stairs"), OUTER_STAIRS_ITEM);
		// Register always full stair
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "inner_stairs"), INNER_STAIRS);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "inner_stairs"), INNER_STAIRS_ITEM);
		// Register wooden mosaic
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "wooden_mosaic"), WOODEN_MOSAIC);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "wooden_mosaic"), WOODEN_MOSAIC_ITEM);
		// Register candlestick TODO: Finish this
		// Register LED
		if (ModConfig.enableLEDs) {
			LED_BLOCK = new LEDBlock(FabricBlockSettings.create().strength(0.5f).nonOpaque().sounds(BlockSoundGroup.GLASS).luminance(10));
			Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "led"), LED_BLOCK);

			LED_ITEM = new BlockItem(LED_BLOCK, new FabricItemSettings());
			Registry.register(Registries.ITEM, new Identifier(MOD_ID, "led"), LED_ITEM);
		}


		// ............ FINAL BLOCKS & ITEMS ............
		// Register cabinet block variants
		CabinetBlockHelper.registerCabinetBlockVariants();
		// Register innerOuter stairs variants
		InnerOuterStairsHelper.registerInnerOuterStairsVariants();
		// Register wooden mosaic variants
		WoodenMosaicHelper.registerWoodenMosaicVariants();
		// Register terracotta tiles variants
		TerracottaTilesHelper.registerTerracottaTilesVariants();
		// Register LED block variants
		if (ModConfig.enableLEDs) {
			LEDHelper.registerLEDBlockVariants();
		}


		// ............ MAKE THINGS FLAMMABLE ............
		// No test blocks are currently flammable


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
		if (ModConfig.enableLEDs) {
			LED_BLOCK_ENTITY = Registry.register(
					Registries.BLOCK_ENTITY_TYPE,
					new Identifier(MOD_ID, "led_block_entity"),
					FabricBlockEntityTypeBuilder.create(LEDBlockEntity::new, LEDHelper.LEDBlockVariants).build()
			);
		}
	}

	private static void checkForSupportedMods() {
		checkIfShimmerModIsPresent();
		checkForBetterNether();
	}

	private static void checkForBetterNether() {
		if (getInstance().isModLoaded("betternether")) {
			ModConfig.betterNetherDetected = true;
			LOGGER.info("Better Nether mod detected! Enabling Better Nether support.");
		} else {
			// Go through all mods in the mods folder and check if any of them are Better Nether
			Path gameDirPath = FabricLoader.getInstance().getGameDir();
			File modsFolder = new File(gameDirPath + "/mods");
			File[] mods = modsFolder.listFiles();
			if (mods != null) {
				for (File mod : mods) {
					if (mod.getName().startsWith("betternether")) {
						ModConfig.betterNetherDetected = true;
						LOGGER.info("Better Nether mod detected! Enabling Better Nether support.");
						break;
					}
				}
			}
		}
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
}