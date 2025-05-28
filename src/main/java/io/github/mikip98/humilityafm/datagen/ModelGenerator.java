package io.github.mikip98.humilityafm.datagen;

import io.github.mikip98.humilityafm.content.ModProperties;
import io.github.mikip98.humilityafm.generators.CabinetBlockGenerator;
import io.github.mikip98.humilityafm.generators.ColouredFeatureSetGenerator;
import io.github.mikip98.humilityafm.generators.ForcedCornerStairsGenerator;
import io.github.mikip98.humilityafm.generators.CandlestickGenerator;
import io.github.mikip98.humilityafm.generators.TerracottaTilesGenerator;
import io.github.mikip98.humilityafm.generators.WoodenMosaicGenerator;
import io.github.mikip98.humilityafm.registries.BlockRegistry;
import io.github.mikip98.humilityafm.registries.ItemRegistry;
import io.github.mikip98.humilityafm.util.GenerationData;
import io.github.mikip98.humilityafm.util.data_types.BlockStrength;
import io.github.mikip98.humilityafm.util.data_types.CandleColor;
import io.github.mikip98.humilityafm.util.data_types.Pair;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.StairShape;
import net.minecraft.data.client.*;
import net.minecraft.item.Item;
import net.minecraft.state.property.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static io.github.mikip98.humilityafm.HumilityAFM.getId;

public class ModelGenerator extends FabricModelProvider {
    // Cabinet Models
    protected static final Model CABINET_BLOCK_MODEL = new Model(
            Optional.of(getId("block/cabinet_block")),
            Optional.empty()
    );
    protected static final Model CABINET_BLOCK_OPEN_MODEL = new Model(
            Optional.of(getId("block/cabinet_block_open")),
            Optional.empty()
    );
    protected static final Model FLOOR_CABINET_BLOCK_MODEL = new Model(
            Optional.of(getId("block/floor_cabinet_block")),
            Optional.empty()
    );
    protected static final Model FLOOR_CABINET_BLOCK_OPEN_MODEL = new Model(
            Optional.of(getId("block/floor_cabinet_block_open")),
            Optional.empty()
    );
    // Checker 2x2 Model
    protected static final Model CHECKER_2X2_MODEL = new Model(
            Optional.of(getId("block/checker_2x2")),
            Optional.empty()
    );
    // Forced Corner Stairs Models
    protected static final Model INNER_CORNER_STAIRS_MODEL = new Model(
            Optional.of(getId("block/stairs_inner")),
            Optional.empty()
    );
    protected static final Model OUTER_CORNER_STAIRS_MODEL = new Model(
            Optional.of(getId("block/stairs_outer")),
            Optional.empty()
    );
    // Coloured Torch Model
    protected static final Model TORCH_TEMPLATE_MODEL = new Model(
            Optional.of(getId("block/torch_template")),
            Optional.empty()
    );
    // Candlestick Models
    protected static final Model CANDLESTICK_STANDING_MODEL = new Model(
            Optional.of(getId("block/candlestick")),
            Optional.empty()
    );
    protected static final Model CANDLESTICK_STANDING_WITH_CANDLE_MODEL = new Model(
            Optional.of(getId("block/candlestick_candle")),
            Optional.empty()
    );
    protected static final Model CANDLESTICK_STANDING_WITH_CANDLE_LIT_MODEL = new Model(
            Optional.of(getId("block/candlestick_candle_lit")),
            Optional.empty()
    );
    protected static final Model CANDLESTICK_WALL_MODEL = new Model(
            Optional.of(getId("block/candlestick_wall")),
            Optional.empty()
    );
    protected static final Model CANDLESTICK_WALL_WITH_CANDLE_MODEL = new Model(
            Optional.of(getId("block/candlestick_wall_candle")),
            Optional.empty()
    );
    protected static final Model CANDLESTICK_WALL_WITH_CANDLE_LIT_MODEL = new Model(
            Optional.of(getId("block/candlestick_wall_candle_lit")),
            Optional.empty()
    );
    // Light Strip Models
    protected static final Model LIGHT_STRIP_STRAIGHT_MODEL = new Model(
            Optional.of(getId("block/light_strip")),
            Optional.empty()
    );
    protected static final Model LIGHT_STRIP_INNER_MODEL = new Model(
            Optional.of(getId("block/light_strip_inner")),
            Optional.empty()
    );
    protected static final Model LIGHT_STRIP_OUTER_MODEL = new Model(
            Optional.of(getId("block/light_strip_outer")),
            Optional.empty()
    );


    public ModelGenerator(FabricDataOutput output) {
        super(output);
    }


    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        // ............ TEST BLOCKS & BLOCK ITEMS ............
        // Cabinet Blocks
        blockStateModelGenerator.registerParentedItemModel(ItemRegistry.CABINET_ITEM, getId("block/cabinet_block"));
        blockStateModelGenerator.registerParentedItemModel(ItemRegistry.ILLUMINATED_CABINET_ITEM, getId("block/cabinet_block"));
        blockStateModelGenerator.blockStateCollector.accept(getCabinetBlockstate(BlockRegistry.CABINET_BLOCK, getId("block/cabinet_block"), getId("block/cabinet_block_open")));
        blockStateModelGenerator.blockStateCollector.accept(getCabinetBlockstate(BlockRegistry.ILLUMINATED_CABINET_BLOCK, getId("block/cabinet_block"), getId("block/cabinet_block_open")));
        blockStateModelGenerator.blockStateCollector.accept(getFloorCabinetBlockstate(BlockRegistry.FLOOR_CABINET_BLOCK, getId("block/floor_cabinet_block"), getId("block/floor_cabinet_block_open")));
        blockStateModelGenerator.blockStateCollector.accept(getFloorCabinetBlockstate(BlockRegistry.FLOOR_ILLUMINATED_CABINET_BLOCK, getId("block/floor_cabinet_block"), getId("block/floor_cabinet_block_open")));
        // Wooden Mosaic
        blockStateModelGenerator.registerParentedItemModel(BlockRegistry.WOODEN_MOSAIC, getId("block/checker_2x2"));
        blockStateModelGenerator.blockStateCollector.accept(getDefaultBlockstate(BlockRegistry.WOODEN_MOSAIC, getId("block/checker_2x2")));
        // Forced Corner Stairs
        blockStateModelGenerator.registerParentedItemModel(BlockRegistry.INNER_STAIRS, getId("block/stairs_inner"));
        blockStateModelGenerator.registerParentedItemModel(BlockRegistry.OUTER_STAIRS, getId("block/stairs_outer"));
        blockStateModelGenerator.blockStateCollector.accept(getForcedCornerStairsBlockstate(BlockRegistry.INNER_STAIRS, getId("block/stairs_inner")));
        blockStateModelGenerator.blockStateCollector.accept(getForcedCornerStairsBlockstate(BlockRegistry.OUTER_STAIRS, getId("block/stairs_outer")));

        // ............ FINAL BLOCKS & BLOCK ITEMS ............
        generateCabinetModelsAndBlockStates(blockStateModelGenerator);
        generateWoodenMosaicModelsAndBlockStates(blockStateModelGenerator);
        generateTerracottaTilesModelsAndBlockStates(blockStateModelGenerator);
        generateForcedCornerStairsModelsAndBlockstates(blockStateModelGenerator);
        // Optional blocks
        generateCandlestickModelsAndBlockStates(blockStateModelGenerator);
        generateColouredTorchModelsAndBlockStates(blockStateModelGenerator);
        generateLightStripModelsAndBlockStates(blockStateModelGenerator);
    }

    protected static void generateColouredTorchModelsAndBlockStates(BlockStateModelGenerator blockStateModelGenerator) {
        int i = 0;
        for (String color : GenerationData.vanillaColorPallet) {
            final Identifier coloured_torch_texture = getId("block/coloured_torch/coloured_torch_" + color);
            final TextureMap textureMap = new TextureMap()
                    .register(TextureKey.TORCH, coloured_torch_texture);

            final Identifier torchModelId = TORCH_TEMPLATE_MODEL.upload(
                    getId("block/coloured_torch/coloured_torch_" + color),
                    textureMap,
                    blockStateModelGenerator.modelCollector
            );
            blockStateModelGenerator.registerParentedItemModel(ColouredFeatureSetGenerator.colouredTorchWeakVariants[i], torchModelId);
            blockStateModelGenerator.registerParentedItemModel(ColouredFeatureSetGenerator.colouredTorchVariants[i], torchModelId);
            blockStateModelGenerator.registerParentedItemModel(ColouredFeatureSetGenerator.colouredTorchStrongVariants[i], torchModelId);
            blockStateModelGenerator.blockStateCollector.accept(getDefaultBlockstate(ColouredFeatureSetGenerator.colouredTorchWeakVariants[i], torchModelId));
            blockStateModelGenerator.blockStateCollector.accept(getDefaultBlockstate(ColouredFeatureSetGenerator.colouredTorchVariants[i], torchModelId));
            blockStateModelGenerator.blockStateCollector.accept(getDefaultBlockstate(ColouredFeatureSetGenerator.colouredTorchStrongVariants[i], torchModelId));
            ++i;
        }
    }

    protected static void generateLightStripModelsAndBlockStates(BlockStateModelGenerator blockStateModelGenerator) {
        int i = 0;
        for (String color : GenerationData.vanillaColorPallet) {
            final Identifier coloured_concrete = new Identifier("block/" + color + "_concrete");
            final TextureMap textureMap = new TextureMap()
                    .register(TextureKey.of("0"), coloured_concrete);

            final Identifier lightStripStraightModelId = LIGHT_STRIP_STRAIGHT_MODEL.upload(
                    getId("block/light_strip/straight/light_strip_" + color),
                    textureMap,
                    blockStateModelGenerator.modelCollector
            );
            final Identifier lightStripInnerModelId = LIGHT_STRIP_INNER_MODEL.upload(
                    getId("block/light_strip/inner/light_strip_inner_" + color),
                    textureMap,
                    blockStateModelGenerator.modelCollector
            );
            final Identifier lightStripOuterModelId = LIGHT_STRIP_OUTER_MODEL.upload(
                    getId("block/light_strip/outer/light_strip_outer_" + color),
                    textureMap,
                    blockStateModelGenerator.modelCollector
            );
            blockStateModelGenerator.registerParentedItemModel(ColouredFeatureSetGenerator.LightStripBlockVariants[i], lightStripStraightModelId);
            blockStateModelGenerator.blockStateCollector.accept(getLightStripBlockstate(
                    ColouredFeatureSetGenerator.LightStripBlockVariants[i],
                    lightStripStraightModelId,
                    lightStripInnerModelId,
                    lightStripOuterModelId
            ));
            ++i;
        }
    }

    protected static void generateCandlestickModelsAndBlockStates(BlockStateModelGenerator blockStateModelGenerator) {
        generateCandlestickModelsAndBlockstatesForMetals(
                blockStateModelGenerator,
                GenerationData.vanillaCandlestickMetals,
                ItemRegistry.CANDLESTICK_ITEM_VARIANTS,
                CandlestickGenerator.candlestickClassicWallVariants,
                CandlestickGenerator.candlestickClassicStandingVariants
        );
        for (int i = 0; i < ItemRegistry.RUSTABLE_CANDLESTICK_ITEM_VARIANTS.size(); ++i) {
            generateCandlestickModelsAndBlockstatesForMetals(
                    blockStateModelGenerator,
                    GenerationData.vanillaRustableCandlestickMetals.get(i),
                    ItemRegistry.RUSTABLE_CANDLESTICK_ITEM_VARIANTS.get(i),
                    CandlestickGenerator.candlestickRustableWallVariants.get(i),
                    CandlestickGenerator.candlestickRustableStandingVariants.get(i)
            );
        }
    }
    protected static void generateCandlestickModelsAndBlockstatesForMetals(BlockStateModelGenerator blockStateModelGenerator, String[] metals, Item[] items, Block[] wallBlocks, Block[] standingBlocks) {
        int i = 0;
        Set<String> block_suffix_metals = Set.of("copper", "gold", "iron");
        for (String metal : metals) {
            String suffix = "";
            if (block_suffix_metals.contains(metal)) suffix = "_block";

            final TextureMap metalTextureMap = new TextureMap()
                    .register(TextureKey.of("metal"), new Identifier("block/" + metal + suffix));

            final Identifier candlestickStandingMetalModelId = CANDLESTICK_STANDING_MODEL.upload(
                    getId("block/candlestick/standing/" + metal + "/candlestick_" + metal),
                    metalTextureMap,
                    blockStateModelGenerator.modelCollector
            );
            final Identifier candlestickWallMetalModelId = CANDLESTICK_WALL_MODEL.upload(
                    getId("block/candlestick/wall/" + metal + "/candlestick_wall_" + metal),
                    metalTextureMap,
                    blockStateModelGenerator.modelCollector
            );

            final Identifier candlestickWithCandleStandingMetalModelId = CANDLESTICK_STANDING_WITH_CANDLE_MODEL.upload(
                    getId("block/candlestick/standing/" + metal + "/candlestick_" + metal + "_candle"),
                    metalTextureMap,
                    blockStateModelGenerator.modelCollector
            );
            final Identifier candlestickWithCandleLitStandingMetalModelId = CANDLESTICK_STANDING_WITH_CANDLE_LIT_MODEL.upload(
                    getId("block/candlestick/standing/" + metal + "/candlestick_" + metal + "_candle_lit"),
                    metalTextureMap,
                    blockStateModelGenerator.modelCollector
            );
            final Identifier candlestickWithCandleWallMetalModelId = CANDLESTICK_WALL_WITH_CANDLE_MODEL.upload(
                    getId("block/candlestick/wall/" + metal + "/candlestick_wall_" + metal + "_candle"),
                    metalTextureMap,
                    blockStateModelGenerator.modelCollector
            );
            final Identifier candlestickWithCandleLitWallMetalModelId = CANDLESTICK_WALL_WITH_CANDLE_LIT_MODEL.upload(
                    getId("block/candlestick/wall/" + metal + "/candlestick_wall_" + metal + "_candle_lit"),
                    metalTextureMap,
                    blockStateModelGenerator.modelCollector
            );

            final Model candlestickWithCandleStandingMetalModel = new Model(
                    Optional.of(candlestickWithCandleStandingMetalModelId),
                    Optional.empty()
            );
            final Model candlestickWithCandleLitStandingMetalModel = new Model(
                    Optional.of(candlestickWithCandleLitStandingMetalModelId),
                    Optional.empty()
            );
            final Model candlestickWithCandleWallMetalModel = new Model(
                    Optional.of(candlestickWithCandleWallMetalModelId),
                    Optional.empty()
            );
            final Model candlestickWithCandleLitWallMetalModel = new Model(
                    Optional.of(candlestickWithCandleLitWallMetalModelId),
                    Optional.empty()
            );

            Map<CandleColor, Identifier> wallCandleColorModelMap = new HashMap<>();
            Map<CandleColor, Identifier> wallLitCandleColorModelMap = new HashMap<>();
            Map<CandleColor, Identifier> standingCandleColorModelMap = new HashMap<>();
            Map<CandleColor, Identifier> standingLitCandleColorModelMap = new HashMap<>();
            for (String color : GenerationData.vanillaColorPallet) {
                final CandleColor candleColor = CandleColor.getColor(color);
                final TextureMap candleColorTextureMap = new TextureMap()
                        .register(TextureKey.of("candle"), new Identifier("block/" + color + "_candle_lit"));

                String id = "block/candlestick/standing/" + metal + "/candlestick_" + metal + "_" + color;
                final Identifier standingCandlestickColoredModelId = candlestickWithCandleStandingMetalModel.upload(
                        getId(id),
                        candleColorTextureMap,
                        blockStateModelGenerator.modelCollector
                );
                standingCandleColorModelMap.put(candleColor, standingCandlestickColoredModelId);
                final Identifier standingCandlestickLitColoredModelId = candlestickWithCandleLitStandingMetalModel.upload(
                        getId(id + "_lit"),
                        candleColorTextureMap,
                        blockStateModelGenerator.modelCollector
                );
                standingLitCandleColorModelMap.put(candleColor, standingCandlestickLitColoredModelId);

                id = "block/candlestick/wall/" + metal + "/candlestick_wall_" + metal + "_" + color;
                final Identifier wallCandlestickColoredModelId = candlestickWithCandleWallMetalModel.upload(
                        getId(id),
                        candleColorTextureMap,
                        blockStateModelGenerator.modelCollector
                );
                wallCandleColorModelMap.put(candleColor, wallCandlestickColoredModelId);
                final Identifier wallCandlestickLitColoredModelId = candlestickWithCandleLitWallMetalModel.upload(
                        getId(id + "_lit"),
                        candleColorTextureMap,
                        blockStateModelGenerator.modelCollector
                );
                wallLitCandleColorModelMap.put(candleColor, wallCandlestickLitColoredModelId);
            }

            blockStateModelGenerator.registerParentedItemModel(items[i], candlestickStandingMetalModelId);
            blockStateModelGenerator.blockStateCollector.accept(getWallCandlestickBlockstate(
                    wallBlocks[i],
                    candlestickWallMetalModelId,
                    candlestickWithCandleWallMetalModelId,
                    candlestickWithCandleLitWallMetalModelId,
                    wallCandleColorModelMap,
                    wallLitCandleColorModelMap
            ));
            blockStateModelGenerator.blockStateCollector.accept(getStandingCandlestickBlockstate(
                    standingBlocks[i],
                    candlestickStandingMetalModelId,
                    candlestickWithCandleStandingMetalModelId,
                    candlestickWithCandleLitStandingMetalModelId,
                    standingCandleColorModelMap,
                    standingLitCandleColorModelMap
            ));
            ++i;
        }
    }

    protected static void generateForcedCornerStairsModelsAndBlockstates(BlockStateModelGenerator blockStateModelGenerator) {
        int i = 0;
        for (String woodType : GenerationData.vanillaWoodTypes) {
            final Identifier innerStairsModelId = INNER_CORNER_STAIRS_MODEL.upload(
                    getId("block/corner_stairs/inner_stairs/inner_stairs_" + woodType),
                    new TextureMap()
                            .register(TextureKey.TOP, new Identifier("block/" + woodType + "_planks"))
                            .register(TextureKey.BOTTOM, new Identifier("block/" + woodType + "_planks"))
                            .register(TextureKey.SIDE, new Identifier("block/" + woodType + "_planks")),
                    blockStateModelGenerator.modelCollector
            );
            final Identifier outerStairsModelId = OUTER_CORNER_STAIRS_MODEL.upload(
                    getId("block/corner_stairs/outer_stairs/outer_stairs_" + woodType),
                    new TextureMap()
                            .register(TextureKey.TOP, new Identifier("block/" + woodType + "_planks"))
                            .register(TextureKey.BOTTOM, new Identifier("block/" + woodType + "_planks"))
                            .register(TextureKey.SIDE, new Identifier("block/" + woodType + "_planks")),
                    blockStateModelGenerator.modelCollector
            );

            blockStateModelGenerator.registerParentedItemModel(ForcedCornerStairsGenerator.innerStairsBlockVariants[i], innerStairsModelId);
            blockStateModelGenerator.registerParentedItemModel(ForcedCornerStairsGenerator.outerStairsBlockVariants[i], outerStairsModelId);
            blockStateModelGenerator.blockStateCollector.accept(getForcedCornerStairsBlockstate(ForcedCornerStairsGenerator.innerStairsBlockVariants[i], innerStairsModelId));
            blockStateModelGenerator.blockStateCollector.accept(getForcedCornerStairsBlockstate(ForcedCornerStairsGenerator.outerStairsBlockVariants[i], outerStairsModelId));

            ++i;
        }
        for (Pair<BlockStrength, String[]> entry : GenerationData.vanillaStonyMaterialsPerStrength) {
            for (String material : entry.second()) {
                String topTexture = material;
                String sideTexture = material;
                String bottomTexture = material;

                String suffix = "";

                switch (material) {
                    case "quartz" -> suffix = "_block_side";
                    case "sandstone", "red_sandstone" -> {
                        topTexture += "_top";
                        bottomTexture += "_bottom";
                    }
                    case "polished_blackstone_brick",
                         "stone_brick",
                         "mossy_stone_brick",
                         "brick",
                         "nether_brick",
                         "red_nether_brick",
                         "end_stone_brick",
                         "deepslate_brick",
                         "deepslate_tile" -> suffix = "s";
                    case "purpur" -> suffix = "_block";
                    case "smooth_quartz" -> {
                        bottomTexture = "quartz_block_bottom";
                        topTexture = bottomTexture;
                        sideTexture = bottomTexture;
                    }
                    case "smooth_sandstone" -> {
                        topTexture = "sandstone_top";
                        bottomTexture = topTexture;
                        sideTexture = topTexture;
                    }
                    case "smooth_red_sandstone" -> {
                        topTexture = "red_sandstone_top";
                        bottomTexture = topTexture;
                        sideTexture = topTexture;
                    }
                }

                topTexture = "block/" + topTexture + suffix;
                sideTexture = "block/" + sideTexture + suffix;
                bottomTexture = "block/" + bottomTexture + suffix;

                final Identifier innerStairsModelId = INNER_CORNER_STAIRS_MODEL.upload(
                        getId("block/corner_stairs/inner_stairs/inner_stairs_" + material),
                        new TextureMap()
                                .register(TextureKey.TOP, new Identifier(topTexture))
                                .register(TextureKey.BOTTOM, new Identifier(bottomTexture))
                                .register(TextureKey.SIDE, new Identifier(sideTexture)),
                        blockStateModelGenerator.modelCollector
                );
                final Identifier outerStairsModelId = OUTER_CORNER_STAIRS_MODEL.upload(
                        getId("block/corner_stairs/outer_stairs/outer_stairs_" + material),
                        new TextureMap()
                                .register(TextureKey.TOP, new Identifier(topTexture))
                                .register(TextureKey.BOTTOM, new Identifier(bottomTexture))
                                .register(TextureKey.SIDE, new Identifier(sideTexture)),
                        blockStateModelGenerator.modelCollector
                );

                blockStateModelGenerator.registerParentedItemModel(ForcedCornerStairsGenerator.innerStairsBlockVariants[i], innerStairsModelId);
                blockStateModelGenerator.registerParentedItemModel(ForcedCornerStairsGenerator.outerStairsBlockVariants[i], outerStairsModelId);
                blockStateModelGenerator.blockStateCollector.accept(getForcedCornerStairsBlockstate(ForcedCornerStairsGenerator.innerStairsBlockVariants[i], innerStairsModelId));
                blockStateModelGenerator.blockStateCollector.accept(getForcedCornerStairsBlockstate(ForcedCornerStairsGenerator.outerStairsBlockVariants[i], outerStairsModelId));

                ++i;
            }
        }
    }

    protected static void generateWoodenMosaicModelsAndBlockStates(BlockStateModelGenerator blockStateModelGenerator) {
        int i = 0;
        for (String woodType : GenerationData.vanillaWoodTypes) {
            for (String woodType2 : GenerationData.vanillaWoodTypes) {
                if (woodType.equals(woodType2)) continue;

                final Identifier woodenMosaicModelId = CHECKER_2X2_MODEL.upload(
                        getId("block/wooden_mosaic/wooden_mosaic_" + woodType + "_" + woodType2),
                        new TextureMap()
                                .register(TextureKey.of("1"), new Identifier("block/" + woodType + "_planks"))
                                .register(TextureKey.of("2"), new Identifier("block/" + woodType2 + "_planks")),
                        blockStateModelGenerator.modelCollector
                );
                blockStateModelGenerator.registerParentedItemModel(WoodenMosaicGenerator.woodenMosaicVariants[i], woodenMosaicModelId);
                blockStateModelGenerator.blockStateCollector.accept(getDefaultBlockstate(WoodenMosaicGenerator.woodenMosaicVariants[i], woodenMosaicModelId));

                ++i;
            }
        }
    }
    protected static void generateTerracottaTilesModelsAndBlockStates(BlockStateModelGenerator blockStateModelGenerator) {
        int i = 0;
        for (String color : GenerationData.vanillaColorPallet) {
            for (String color2 : GenerationData.vanillaColorPallet) {
                if (color.equals(color2)) continue;

                final Identifier terracottaTileModelId = CHECKER_2X2_MODEL.upload(
                        getId("block/terracotta_tiles/terracotta_tiles_" + color + "_" + color2),
                        new TextureMap()
                                .register(TextureKey.of("1"), new Identifier("block/" + color + "_terracotta"))
                                .register(TextureKey.of("2"), new Identifier("block/" + color2 + "_terracotta")),
                        blockStateModelGenerator.modelCollector
                );
                blockStateModelGenerator.registerParentedItemModel(TerracottaTilesGenerator.terracottaTilesVariants[i], terracottaTileModelId);
                blockStateModelGenerator.blockStateCollector.accept(getDefaultBlockstate(TerracottaTilesGenerator.terracottaTilesVariants[i], terracottaTileModelId));

                ++i;
            }
        }
    }

    protected static void generateCabinetModelsAndBlockStates(BlockStateModelGenerator blockStateModelGenerator) {
        int i = 0;
        for (String woodType : GenerationData.vanillaWoodTypes) {
            final TextureMap plankTextureMap = new TextureMap().register(
                    TextureKey.of("planks"),
                    new Identifier("block/" + woodType + "_planks")
            );

            final Identifier woodTypeCabinetId = CABINET_BLOCK_MODEL.upload(
                    getId("block/cabinet/closed/cabinet_block_" + woodType),
                    plankTextureMap,
                    blockStateModelGenerator.modelCollector
            );
            final Model woodTypeCabinetModel = new Model(
                    Optional.of(woodTypeCabinetId),
                    Optional.empty()
            );

            final Identifier woodTypeCabinetOpenId = CABINET_BLOCK_OPEN_MODEL.upload(
                    getId("block/cabinet/open/cabinet_block_open_" + woodType),
                    plankTextureMap,
                    blockStateModelGenerator.modelCollector
            );
            final Model woodTypeCabinetOpenModel = new Model(
                    Optional.of(woodTypeCabinetOpenId),
                    Optional.empty()
            );

            final Identifier woodTypeFloorCabinetId = FLOOR_CABINET_BLOCK_MODEL.upload(
                    getId("block/cabinet/floor/closed/floor_cabinet_block_" + woodType),
                    plankTextureMap,
                    blockStateModelGenerator.modelCollector
            );
            final Model woodTypeFloorCabinetModel = new Model(
                    Optional.of(woodTypeFloorCabinetId),
                    Optional.empty()
            );

            final Identifier woodTypeFloorCabinetOpenId = FLOOR_CABINET_BLOCK_OPEN_MODEL.upload(
                    getId("block/cabinet/floor/open/floor_cabinet_block_open_" + woodType),
                    plankTextureMap,
                    blockStateModelGenerator.modelCollector
            );
            final Model woodTypeFloorCabinetOpenModel = new Model(
                    Optional.of(woodTypeFloorCabinetOpenId),
                    Optional.empty()
            );

            for (String color : GenerationData.vanillaColorPallet) {
                final TextureMap woolTextureMap = new TextureMap().register(
                        TextureKey.of("wool"),
                        new Identifier("block/" + color + "_wool")
                );
                final Identifier coloredCabinet = woodTypeCabinetModel.upload(
                        getId("block/cabinet/closed/" + woodType + "/cabinet_block_" + woodType + "_" + color),
                        woolTextureMap,
                        blockStateModelGenerator.modelCollector
                );
                final Identifier coloredCabinetOpen = woodTypeCabinetOpenModel.upload(
                        getId("block/cabinet/open/" + woodType + "/cabinet_block_open_" + woodType + "_" + color),
                        woolTextureMap,
                        blockStateModelGenerator.modelCollector
                );
                final Identifier coloredFloorCabinet = woodTypeFloorCabinetModel.upload(
                        getId("block/cabinet/floor/closed/" + woodType + "/floor_cabinet_block_" + woodType + "_" + color),
                        woolTextureMap,
                        blockStateModelGenerator.modelCollector
                );
                final Identifier coloredFloorCabinetOpen = woodTypeFloorCabinetOpenModel.upload(
                        getId("block/cabinet/floor/open/" + woodType + "/floor_cabinet_block_open_" + woodType + "_" + color),
                        woolTextureMap,
                        blockStateModelGenerator.modelCollector
                );

                blockStateModelGenerator.registerParentedItemModel(ItemRegistry.CABINET_ITEM_VARIANTS[i], coloredCabinet);
                blockStateModelGenerator.registerParentedItemModel(ItemRegistry.ILLUMINATED_CABINET_ITEM_VARIANTS[i], coloredCabinet);
                blockStateModelGenerator.blockStateCollector.accept(getCabinetBlockstate(CabinetBlockGenerator.cabinetBlockVariants[i], coloredCabinet, coloredCabinetOpen));
                blockStateModelGenerator.blockStateCollector.accept(getCabinetBlockstate(CabinetBlockGenerator.illuminatedCabinetBlockVariants[i], coloredCabinet, coloredCabinetOpen));
                blockStateModelGenerator.blockStateCollector.accept(getFloorCabinetBlockstate(CabinetBlockGenerator.floorCabinetBlockVariants[i], coloredFloorCabinet, coloredFloorCabinetOpen));
                blockStateModelGenerator.blockStateCollector.accept(getFloorCabinetBlockstate(CabinetBlockGenerator.floorIlluminatedCabinetBlockVariants[i], coloredFloorCabinet, coloredFloorCabinetOpen));
                ++i;
            }
        }
    }


    protected static VariantsBlockStateSupplier getLightStripBlockstate(
            Block block,
            Identifier straightModel,
            Identifier innerModel,
            Identifier outerModel
    ) {
        DirectionProperty FACING = Properties.HORIZONTAL_FACING;
        EnumProperty<BlockHalf> HALF = Properties.BLOCK_HALF;
        EnumProperty<StairShape> SHAPE = Properties.STAIR_SHAPE;

        return VariantsBlockStateSupplier.create(block)
                .coordinate(BlockStateVariantMap.create(FACING, HALF, SHAPE)
                        // East Bottom
                        .register(
                                Direction.EAST, BlockHalf.BOTTOM, StairShape.INNER_LEFT,
                                getUVLockedVariantX(innerModel, VariantSettings.Rotation.R180)
                        )
                        .register(
                                Direction.EAST, BlockHalf.BOTTOM, StairShape.INNER_RIGHT,
                                getUVLockedVariantXY(innerModel, VariantSettings.Rotation.R180, VariantSettings.Rotation.R90)
                        )
                        .register(
                                Direction.EAST, BlockHalf.BOTTOM, StairShape.OUTER_LEFT,
                                getUVLockedVariantX(outerModel, VariantSettings.Rotation.R180)
                        )
                        .register(
                                Direction.EAST, BlockHalf.BOTTOM, StairShape.OUTER_RIGHT,
                                getUVLockedVariantXY(outerModel, VariantSettings.Rotation.R180, VariantSettings.Rotation.R90)
                        )
                        .register(
                                Direction.EAST, BlockHalf.BOTTOM, StairShape.STRAIGHT,
                                getUVLockedVariantXY(straightModel, VariantSettings.Rotation.R180, VariantSettings.Rotation.R90)
                        )
                        // East Top
                        .register(
                                Direction.EAST, BlockHalf.TOP, StairShape.INNER_LEFT,
                                getUVLockedVariantY(innerModel, VariantSettings.Rotation.R270)
                        )
                        .register(
                                Direction.EAST, BlockHalf.TOP, StairShape.INNER_RIGHT,
                                getVariant(innerModel)
                        )
                        .register(
                                Direction.EAST, BlockHalf.TOP, StairShape.OUTER_LEFT,
                                getUVLockedVariantY(outerModel, VariantSettings.Rotation.R270)
                        )
                        .register(
                                Direction.EAST, BlockHalf.TOP, StairShape.OUTER_RIGHT,
                                getVariant(outerModel)
                        )
                        .register(
                                Direction.EAST, BlockHalf.TOP, StairShape.STRAIGHT,
                                getUVLockedVariantY(straightModel, VariantSettings.Rotation.R270)
                        )
                        // North Bottom
                        .register(
                                Direction.NORTH, BlockHalf.BOTTOM, StairShape.INNER_LEFT,
                                getUVLockedVariantXY(innerModel, VariantSettings.Rotation.R180, VariantSettings.Rotation.R270)
                        )
                        .register(
                                Direction.NORTH, BlockHalf.BOTTOM, StairShape.INNER_RIGHT,
                                getUVLockedVariantX(innerModel, VariantSettings.Rotation.R180)
                        )
                        .register(
                                Direction.NORTH, BlockHalf.BOTTOM, StairShape.OUTER_LEFT,
                                getUVLockedVariantXY(outerModel, VariantSettings.Rotation.R180, VariantSettings.Rotation.R270)
                        )
                        .register(
                                Direction.NORTH, BlockHalf.BOTTOM, StairShape.OUTER_RIGHT,
                                getUVLockedVariantX(outerModel, VariantSettings.Rotation.R180)
                        )
                        .register(
                                Direction.NORTH, BlockHalf.BOTTOM, StairShape.STRAIGHT,
                                getUVLockedVariantX(straightModel, VariantSettings.Rotation.R180)
                        )
                        // North Top
                        .register(
                                Direction.NORTH, BlockHalf.TOP, StairShape.INNER_LEFT,
                                getUVLockedVariantY(innerModel, VariantSettings.Rotation.R180)
                        )
                        .register(
                                Direction.NORTH, BlockHalf.TOP, StairShape.INNER_RIGHT,
                                getUVLockedVariantY(innerModel, VariantSettings.Rotation.R270)
                        )
                        .register(
                                Direction.NORTH, BlockHalf.TOP, StairShape.OUTER_LEFT,
                                getUVLockedVariantY(outerModel, VariantSettings.Rotation.R180)
                        )
                        .register(
                                Direction.NORTH, BlockHalf.TOP, StairShape.OUTER_RIGHT,
                                getUVLockedVariantY(outerModel, VariantSettings.Rotation.R270)
                        )
                        .register(
                                Direction.NORTH, BlockHalf.TOP, StairShape.STRAIGHT,
                                getUVLockedVariantY(straightModel, VariantSettings.Rotation.R180)
                        )
                        // South Bottom
                        .register(
                                Direction.SOUTH, BlockHalf.BOTTOM, StairShape.INNER_LEFT,
                                getUVLockedVariantXY(innerModel, VariantSettings.Rotation.R180, VariantSettings.Rotation.R90)
                        )
                        .register(
                                Direction.SOUTH, BlockHalf.BOTTOM, StairShape.INNER_RIGHT,
                                getUVLockedVariantXY(innerModel, VariantSettings.Rotation.R180, VariantSettings.Rotation.R180)
                        )
                        .register(
                                Direction.SOUTH, BlockHalf.BOTTOM, StairShape.OUTER_LEFT,
                                getUVLockedVariantXY(outerModel, VariantSettings.Rotation.R180, VariantSettings.Rotation.R90)
                        )
                        .register(
                                Direction.SOUTH, BlockHalf.BOTTOM, StairShape.OUTER_RIGHT,
                                getUVLockedVariantXY(outerModel, VariantSettings.Rotation.R180, VariantSettings.Rotation.R180)
                        )
                        .register(
                                Direction.SOUTH, BlockHalf.BOTTOM, StairShape.STRAIGHT,
                                getUVLockedVariantXY(straightModel, VariantSettings.Rotation.R180, VariantSettings.Rotation.R180)
                        )
                        // South Top
                        .register(
                                Direction.SOUTH, BlockHalf.TOP, StairShape.INNER_LEFT,
                                getVariant(innerModel)
                        )
                        .register(
                                Direction.SOUTH, BlockHalf.TOP, StairShape.INNER_RIGHT,
                                getUVLockedVariantY(innerModel, VariantSettings.Rotation.R90)
                        )
                        .register(
                                Direction.SOUTH, BlockHalf.TOP, StairShape.OUTER_LEFT,
                                getVariant(outerModel)
                        )
                        .register(
                                Direction.SOUTH, BlockHalf.TOP, StairShape.OUTER_RIGHT,
                                getUVLockedVariantY(outerModel, VariantSettings.Rotation.R90)
                        )
                        .register(
                                Direction.SOUTH, BlockHalf.TOP, StairShape.STRAIGHT,
                                getVariant(straightModel)
                        )
                        // West Bottom
                        .register(
                                Direction.WEST, BlockHalf.BOTTOM, StairShape.INNER_LEFT,
                                getUVLockedVariantXY(innerModel, VariantSettings.Rotation.R180, VariantSettings.Rotation.R180)
                        )
                        .register(
                                Direction.WEST, BlockHalf.BOTTOM, StairShape.INNER_RIGHT,
                                getUVLockedVariantXY(innerModel, VariantSettings.Rotation.R180, VariantSettings.Rotation.R270)
                        )
                        .register(
                                Direction.WEST, BlockHalf.BOTTOM, StairShape.OUTER_LEFT,
                                getUVLockedVariantXY(outerModel, VariantSettings.Rotation.R180, VariantSettings.Rotation.R180)
                        )
                        .register(
                                Direction.WEST, BlockHalf.BOTTOM, StairShape.OUTER_RIGHT,
                                getUVLockedVariantXY(outerModel, VariantSettings.Rotation.R180, VariantSettings.Rotation.R270)
                        )
                        .register(
                                Direction.WEST, BlockHalf.BOTTOM, StairShape.STRAIGHT,
                                getUVLockedVariantXY(straightModel, VariantSettings.Rotation.R180, VariantSettings.Rotation.R270)
                        )
                        // West Top
                        .register(
                                Direction.WEST, BlockHalf.TOP, StairShape.INNER_LEFT,
                                getUVLockedVariantY(innerModel, VariantSettings.Rotation.R90)
                        )
                        .register(
                                Direction.WEST, BlockHalf.TOP, StairShape.INNER_RIGHT,
                                getUVLockedVariantY(innerModel, VariantSettings.Rotation.R180)
                        )
                        .register(
                                Direction.WEST, BlockHalf.TOP, StairShape.OUTER_LEFT,
                                getUVLockedVariantY(outerModel, VariantSettings.Rotation.R90)
                        )
                        .register(
                                Direction.WEST, BlockHalf.TOP, StairShape.OUTER_RIGHT,
                                getUVLockedVariantY(outerModel, VariantSettings.Rotation.R180)
                        )
                        .register(
                                Direction.WEST, BlockHalf.TOP, StairShape.STRAIGHT,
                                getUVLockedVariantY(straightModel, VariantSettings.Rotation.R90)
                        )
                );
    }

    protected static MultipartBlockStateSupplier getStandingCandlestickBlockstate(
            Block block,
            Identifier emptyModel,
            Identifier plainCandleModel,
            Identifier litPlainCandleModel,
            Map<CandleColor, Identifier> colouredCandleModels,
            Map<CandleColor, Identifier> litColouredCandleModels
    ) {
        BooleanProperty CANDLE = ModProperties.CANDLE;
        BooleanProperty LIT = Properties.LIT;
        EnumProperty<CandleColor> CANDLE_COLOR = ModProperties.CANDLE_COLOR;

        MultipartBlockStateSupplier multipart = MultipartBlockStateSupplier.create(block)
                // NO CANDLE
                .with(
                        When.create().set(CANDLE, false),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, emptyModel)
                )
                // PLAIN NON LIT
                .with(
                        When.allOf(
                                When.create().set(CANDLE, true),
                                When.create().set(CANDLE_COLOR, CandleColor.PLAIN),
                                When.create().set(LIT, false)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, plainCandleModel)
                )
                // PLAIN LIT
                .with(
                        When.allOf(
                                When.create().set(CANDLE, true),
                                When.create().set(CANDLE_COLOR, CandleColor.PLAIN),
                                When.create().set(LIT, true)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, litPlainCandleModel)
                );

        for (CandleColor candleColor : colouredCandleModels.keySet()) {
            multipart
                    // NON-LIT
                    .with(
                            When.allOf(
                                    When.create().set(CANDLE, true),
                                    When.create().set(CANDLE_COLOR, candleColor),
                                    When.create().set(LIT, false)
                            ),
                            BlockStateVariant.create()
                                    .put(VariantSettings.MODEL, colouredCandleModels.get(candleColor))
                    )
                    // LIT
                    .with(
                            When.allOf(
                                    When.create().set(CANDLE, true),
                                    When.create().set(CANDLE_COLOR, candleColor),
                                    When.create().set(LIT, true)
                            ),
                            BlockStateVariant.create()
                                    .put(VariantSettings.MODEL, litColouredCandleModels.get(candleColor))
                    );
        }

        return multipart;
    }
    protected static MultipartBlockStateSupplier getWallCandlestickBlockstate(
            Block block,
            Identifier emptyModel,
            Identifier plainCandleModel,
            Identifier litPlainCandleModel,
            Map<CandleColor, Identifier> colouredCandleModels,
            Map<CandleColor, Identifier> litColouredCandleModels
    ) {
        DirectionProperty FACING = Properties.HORIZONTAL_FACING;
        BooleanProperty CANDLE = ModProperties.CANDLE;
        BooleanProperty LIT = Properties.LIT;
        EnumProperty<CandleColor> CANDLE_COLOR = ModProperties.CANDLE_COLOR;

        MultipartBlockStateSupplier multipart = MultipartBlockStateSupplier.create(block)
                // NO CANDLE
                .with(
                        When.allOf(
                                When.create().set(CANDLE, false),
                                When.create().set(FACING, Direction.NORTH)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, emptyModel)
                                .put(VariantSettings.Y, VariantSettings.Rotation.R180)
                )
                .with(
                        When.allOf(
                                When.create().set(CANDLE, false),
                                When.create().set(FACING, Direction.SOUTH)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, emptyModel)
                )
                .with(
                        When.allOf(
                                When.create().set(CANDLE, false),
                                When.create().set(FACING, Direction.EAST)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, emptyModel)
                                .put(VariantSettings.Y, VariantSettings.Rotation.R270)
                )
                .with(
                        When.allOf(
                                When.create().set(CANDLE, false),
                                When.create().set(FACING, Direction.WEST)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, emptyModel)
                                .put(VariantSettings.Y, VariantSettings.Rotation.R90)
                )
                // PLAIN NON LIT
                .with(
                        When.allOf(
                                When.create().set(CANDLE, true),
                                When.create().set(CANDLE_COLOR, CandleColor.PLAIN),
                                When.create().set(LIT, false),
                                When.create().set(FACING, Direction.NORTH)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, plainCandleModel)
                                .put(VariantSettings.Y, VariantSettings.Rotation.R180)
                )
                .with(
                        When.allOf(
                                When.create().set(CANDLE, true),
                                When.create().set(CANDLE_COLOR, CandleColor.PLAIN),
                                When.create().set(LIT, false),
                                When.create().set(FACING, Direction.SOUTH)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, plainCandleModel)
                )
                .with(
                        When.allOf(
                                When.create().set(CANDLE, true),
                                When.create().set(CANDLE_COLOR, CandleColor.PLAIN),
                                When.create().set(LIT, false),
                                When.create().set(FACING, Direction.EAST)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, plainCandleModel)
                                .put(VariantSettings.Y, VariantSettings.Rotation.R270)
                )
                .with(
                        When.allOf(
                                When.create().set(CANDLE, true),
                                When.create().set(CANDLE_COLOR, CandleColor.PLAIN),
                                When.create().set(LIT, false),
                                When.create().set(FACING, Direction.WEST)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, plainCandleModel)
                                .put(VariantSettings.Y, VariantSettings.Rotation.R90)
                )
                // PLAIN LIT
                .with(
                        When.allOf(
                                When.create().set(CANDLE, true),
                                When.create().set(CANDLE_COLOR, CandleColor.PLAIN),
                                When.create().set(LIT, true),
                                When.create().set(FACING, Direction.NORTH)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, litPlainCandleModel)
                                .put(VariantSettings.Y, VariantSettings.Rotation.R180)
                )
                .with(
                        When.allOf(
                                When.create().set(CANDLE, true),
                                When.create().set(CANDLE_COLOR, CandleColor.PLAIN),
                                When.create().set(LIT, true),
                                When.create().set(FACING, Direction.SOUTH)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, litPlainCandleModel)
                )
                .with(
                        When.allOf(
                                When.create().set(CANDLE, true),
                                When.create().set(CANDLE_COLOR, CandleColor.PLAIN),
                                When.create().set(LIT, true),
                                When.create().set(FACING, Direction.EAST)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, litPlainCandleModel)
                                .put(VariantSettings.Y, VariantSettings.Rotation.R270)
                )
                .with(
                        When.allOf(
                                When.create().set(CANDLE, true),
                                When.create().set(CANDLE_COLOR, CandleColor.PLAIN),
                                When.create().set(LIT, true),
                                When.create().set(FACING, Direction.WEST)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, litPlainCandleModel)
                                .put(VariantSettings.Y, VariantSettings.Rotation.R90)
                );

        for (CandleColor candleColor : colouredCandleModels.keySet()) {
            multipart
                    // NON-LIT
                    .with(
                            When.allOf(
                                    When.create().set(CANDLE, true),
                                    When.create().set(CANDLE_COLOR, candleColor),
                                    When.create().set(LIT, false),
                                    When.create().set(FACING, Direction.NORTH)
                            ),
                            BlockStateVariant.create()
                                    .put(VariantSettings.MODEL, colouredCandleModels.get(candleColor))
                                    .put(VariantSettings.Y, VariantSettings.Rotation.R180)
                    )
                    .with(
                            When.allOf(
                                    When.create().set(CANDLE, true),
                                    When.create().set(CANDLE_COLOR, candleColor),
                                    When.create().set(LIT, false),
                                    When.create().set(FACING, Direction.SOUTH)
                            ),
                            BlockStateVariant.create()
                                    .put(VariantSettings.MODEL, colouredCandleModels.get(candleColor))
                    )
                    .with(
                            When.allOf(
                                    When.create().set(CANDLE, true),
                                    When.create().set(CANDLE_COLOR, candleColor),
                                    When.create().set(LIT, false),
                                    When.create().set(FACING, Direction.EAST)
                            ),
                            BlockStateVariant.create()
                                    .put(VariantSettings.MODEL, colouredCandleModels.get(candleColor))
                                    .put(VariantSettings.Y, VariantSettings.Rotation.R270)
                    )
                    .with(
                            When.allOf(
                                    When.create().set(CANDLE, true),
                                    When.create().set(CANDLE_COLOR, candleColor),
                                    When.create().set(LIT, false),
                                    When.create().set(FACING, Direction.WEST)
                            ),
                            BlockStateVariant.create()
                                    .put(VariantSettings.MODEL, colouredCandleModels.get(candleColor))
                                    .put(VariantSettings.Y, VariantSettings.Rotation.R90)
                    )
                    // LIT
                    .with(
                            When.allOf(
                                    When.create().set(CANDLE, true),
                                    When.create().set(CANDLE_COLOR, candleColor),
                                    When.create().set(LIT, true),
                                    When.create().set(FACING, Direction.NORTH)
                            ),
                            BlockStateVariant.create()
                                    .put(VariantSettings.MODEL, litColouredCandleModels.get(candleColor))
                                    .put(VariantSettings.Y, VariantSettings.Rotation.R180)
                            )
                    .with(
                            When.allOf(
                                    When.create().set(CANDLE, true),
                                    When.create().set(CANDLE_COLOR, candleColor),
                                    When.create().set(LIT, true),
                                    When.create().set(FACING, Direction.SOUTH)
                            ),
                            BlockStateVariant.create()
                                    .put(VariantSettings.MODEL, litColouredCandleModels.get(candleColor))
                    )
                    .with(
                            When.allOf(
                                    When.create().set(CANDLE, true),
                                    When.create().set(CANDLE_COLOR, candleColor),
                                    When.create().set(LIT, true),
                                    When.create().set(FACING, Direction.EAST)
                            ),
                            BlockStateVariant.create()
                                    .put(VariantSettings.MODEL, litColouredCandleModels.get(candleColor))
                                    .put(VariantSettings.Y, VariantSettings.Rotation.R270)
                    )
                    .with(
                            When.allOf(
                                    When.create().set(CANDLE, true),
                                    When.create().set(CANDLE_COLOR, candleColor),
                                    When.create().set(LIT, true),
                                    When.create().set(FACING, Direction.WEST)
                            ),
                            BlockStateVariant.create()
                                    .put(VariantSettings.MODEL, litColouredCandleModels.get(candleColor))
                                    .put(VariantSettings.Y, VariantSettings.Rotation.R90)
                    );
        }

        return multipart;
    }

    protected static VariantsBlockStateSupplier getForcedCornerStairsBlockstate(Block block, Identifier modelId) {
        return VariantsBlockStateSupplier
                .create(block)
                .coordinate(BlockStateVariantMap.create(Properties.HORIZONTAL_FACING, Properties.BLOCK_HALF)
                        .register(
                                Direction.EAST, BlockHalf.BOTTOM,
                                getUVLockedVariantY(modelId, VariantSettings.Rotation.R90)
                        )
                        .register(
                                Direction.EAST, BlockHalf.TOP,
                                getUVLockedVariantXY(modelId, VariantSettings.Rotation.R180, VariantSettings.Rotation.R180)
                        )
                        .register(
                                Direction.NORTH, BlockHalf.BOTTOM,
                                getVariant(modelId)
                        )
                        .register(
                                Direction.NORTH, BlockHalf.TOP,
                                getUVLockedVariantXY(modelId, VariantSettings.Rotation.R180, VariantSettings.Rotation.R90)
                        )
                        .register(
                                Direction.SOUTH, BlockHalf.BOTTOM,
                                getUVLockedVariantY(modelId, VariantSettings.Rotation.R180)
                        )
                        .register(
                                Direction.SOUTH, BlockHalf.TOP,
                                getUVLockedVariantXY(modelId, VariantSettings.Rotation.R180, VariantSettings.Rotation.R270)
                        )
                        .register(
                                Direction.WEST, BlockHalf.BOTTOM,
                                getUVLockedVariantY(modelId, VariantSettings.Rotation.R270)
                        )
                        .register(
                                Direction.WEST, BlockHalf.TOP,
                                getUVLockedVariantX(modelId, VariantSettings.Rotation.R180)
                        )
                );
    }

    protected static VariantsBlockStateSupplier getDefaultBlockstate(Block block, Identifier modelId) {
        return VariantsBlockStateSupplier.create(block, BlockStateVariant.create().put(VariantSettings.MODEL, modelId));
    }

    protected static VariantsBlockStateSupplier getCabinetBlockstate(Block cabinetBlock, Identifier cabinetModel, Identifier cabinetOpenModel) {
        return VariantsBlockStateSupplier.create(cabinetBlock)
                .coordinate(BlockStateVariantMap.create(Properties.HORIZONTAL_FACING, Properties.OPEN)
                    .register(
                            Direction.NORTH, true,
                            getVariantY(cabinetOpenModel, VariantSettings.Rotation.R180)
                    )
                    .register(
                            Direction.NORTH, false,
                            getVariantY(cabinetModel, VariantSettings.Rotation.R180)
                    )
                    .register(
                            Direction.EAST, true,
                            getVariantY(cabinetOpenModel, VariantSettings.Rotation.R270)
                    )
                    .register(
                            Direction.EAST, false,
                            getVariantY(cabinetModel, VariantSettings.Rotation.R270)
                    )
                    .register(
                            Direction.SOUTH, true,
                            getVariant(cabinetOpenModel)
                    )
                    .register(
                            Direction.SOUTH, false,
                            getVariant(cabinetModel)
                    )
                    .register(
                            Direction.WEST, true,
                            getVariantY(cabinetOpenModel, VariantSettings.Rotation.R90)
                    )
                    .register(
                            Direction.WEST, false,
                            getVariantY(cabinetModel, VariantSettings.Rotation.R90)
                    )
                );
    }
    protected static VariantsBlockStateSupplier getFloorCabinetBlockstate(Block cabinetBlock, Identifier cabinetModel, Identifier cabinetOpenModel) {
        return VariantsBlockStateSupplier.create(cabinetBlock)
                .coordinate(BlockStateVariantMap.create(Properties.BLOCK_HALF, Properties.HORIZONTAL_FACING, Properties.OPEN)
                        .register(
                                BlockHalf.BOTTOM, Direction.NORTH, true,
                                getVariantY(cabinetOpenModel, VariantSettings.Rotation.R180)
                        )
                        .register(
                                BlockHalf.BOTTOM, Direction.NORTH, false,
                                getVariantY(cabinetModel, VariantSettings.Rotation.R180)
                        )
                        .register(
                                BlockHalf.BOTTOM, Direction.EAST, true,
                                getVariantY(cabinetOpenModel, VariantSettings.Rotation.R270)
                        )
                        .register(
                                BlockHalf.BOTTOM, Direction.EAST, false,
                                getVariantY(cabinetModel, VariantSettings.Rotation.R270)
                        )
                        .register(
                                BlockHalf.BOTTOM, Direction.SOUTH, true,
                                getVariant(cabinetOpenModel)
                        )
                        .register(
                                BlockHalf.BOTTOM, Direction.SOUTH, false,
                                getVariant(cabinetModel)
                        )
                        .register(
                                BlockHalf.BOTTOM, Direction.WEST, true,
                                getVariantY(cabinetOpenModel, VariantSettings.Rotation.R90)
                        )
                        .register(
                                BlockHalf.BOTTOM, Direction.WEST, false,
                                getVariantY(cabinetModel, VariantSettings.Rotation.R90)
                        )
                        .register(
                                BlockHalf.TOP, Direction.NORTH, true,
                                getVariantXY(cabinetOpenModel, VariantSettings.Rotation.R180, VariantSettings.Rotation.R180)
                        )
                        .register(
                                BlockHalf.TOP, Direction.NORTH, false,
                                getVariantXY(cabinetModel, VariantSettings.Rotation.R180, VariantSettings.Rotation.R180)
                        )
                        .register(
                                BlockHalf.TOP, Direction.EAST, true,
                                getVariantXY(cabinetOpenModel, VariantSettings.Rotation.R180, VariantSettings.Rotation.R270)
                        )
                        .register(
                                BlockHalf.TOP, Direction.EAST, false,
                                getVariantXY(cabinetModel, VariantSettings.Rotation.R180, VariantSettings.Rotation.R270)
                        )
                        .register(
                                BlockHalf.TOP, Direction.SOUTH, true,
                                getVariantX(cabinetOpenModel, VariantSettings.Rotation.R180)
                        )
                        .register(
                                BlockHalf.TOP, Direction.SOUTH, false,
                                getVariantX(cabinetModel, VariantSettings.Rotation.R180)
                        )
                        .register(
                                BlockHalf.TOP, Direction.WEST, true,
                                getVariantXY(cabinetOpenModel, VariantSettings.Rotation.R180, VariantSettings.Rotation.R90)
                        )
                        .register(
                                BlockHalf.TOP, Direction.WEST, false,
                                getVariantXY(cabinetModel, VariantSettings.Rotation.R180, VariantSettings.Rotation.R90)
                        )
                );
    }


    protected static BlockStateVariant getUVLockedVariantXY(Identifier model, VariantSettings.Rotation rotationX, VariantSettings.Rotation rotationY) {
        return BlockStateVariant.create()
                .put(VariantSettings.MODEL, model)
                .put(VariantSettings.UVLOCK, true)
                .put(VariantSettings.X, rotationX)
                .put(VariantSettings.Y, rotationY);
    }

    protected static BlockStateVariant getUVLockedVariantX(Identifier model, VariantSettings.Rotation rotationX) {
        return BlockStateVariant.create()
                .put(VariantSettings.MODEL, model)
                .put(VariantSettings.UVLOCK, true)
                .put(VariantSettings.X, rotationX);
    }
    protected static BlockStateVariant getUVLockedVariantY(Identifier model, VariantSettings.Rotation rotationY) {
        return BlockStateVariant.create()
                .put(VariantSettings.MODEL, model)
                .put(VariantSettings.UVLOCK, true)
                .put(VariantSettings.Y, rotationY);
    }

    protected static BlockStateVariant getVariantXY(Identifier model, VariantSettings.Rotation rotationX, VariantSettings.Rotation rotationY) {
        return BlockStateVariant.create()
                .put(VariantSettings.MODEL, model)
                .put(VariantSettings.X, rotationX)
                .put(VariantSettings.Y, rotationY);
    }

    protected static BlockStateVariant getVariantX(Identifier model, VariantSettings.Rotation rotationX) {
        return BlockStateVariant.create()
                .put(VariantSettings.MODEL, model)
                .put(VariantSettings.X, rotationX);
    }
    protected static BlockStateVariant getVariantY(Identifier model, VariantSettings.Rotation rotationY) {
        return BlockStateVariant.create()
                .put(VariantSettings.MODEL, model)
                .put(VariantSettings.Y, rotationY);
    }

    protected static BlockStateVariant getVariant(Identifier model) {
        return BlockStateVariant.create().put(VariantSettings.MODEL, model);
    }


    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        for (Item item : ItemRegistry.GLOWING_POWDER_VARIANTS) {
            itemModelGenerator.register(item, Models.GENERATED);
        }
    }
}
