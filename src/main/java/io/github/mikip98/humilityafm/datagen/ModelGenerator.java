package io.github.mikip98.humilityafm.datagen;

import io.github.mikip98.humilityafm.content.ModProperties;
import io.github.mikip98.humilityafm.generators.CabinetBlockGenerator;
import io.github.mikip98.humilityafm.generators.ColouredLightsGenerator;
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
    protected static final Model COLOURED_TORCH_MODEL = new Model(
            Optional.of(getId("block/coloured_torch")),
            Optional.empty()
    );
    // Optional
    // Candlestick Models
    protected static final Model CANDLESTICK_MODEL = new Model(
            Optional.of(getId("block/candlestick")),
            Optional.empty()
    );
    protected static final Model CANDLESTICK_WITH_CANDLE_MODEL = new Model(
            Optional.of(getId("block/candlestick_candle")),
            Optional.empty()
    );
    protected static final Model CANDLESTICK_WITH_CANDLE_LIT_MODEL = new Model(
            Optional.of(getId("block/candlestick_candle_lit")),
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
        blockStateModelGenerator.registerParentedItemModel(BlockRegistry.CABINET_BLOCK, getId("block/cabinet_block"));
        blockStateModelGenerator.registerParentedItemModel(BlockRegistry.ILLUMINATED_CABINET_BLOCK, getId("block/cabinet_block"));
        blockStateModelGenerator.blockStateCollector.accept(getCabinetBlockstate(BlockRegistry.CABINET_BLOCK, getId("block/cabinet_block"), getId("block/cabinet_block_open")));
        blockStateModelGenerator.blockStateCollector.accept(getCabinetBlockstate(BlockRegistry.ILLUMINATED_CABINET_BLOCK, getId("block/cabinet_block"), getId("block/cabinet_block_open")));
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
        generateColouredTorchModelsAndBlockStates(blockStateModelGenerator);
        // Optional blocks
        generateCandlestickModelsAndBlockStates(blockStateModelGenerator);
        generateLightStripModelsAndBlockStates(blockStateModelGenerator);
    }

    protected static void generateColouredTorchModelsAndBlockStates(BlockStateModelGenerator blockStateModelGenerator) {
        int i = 0;
        for (String color : GenerationData.vanillaColorPallet) {
            final Identifier coloured_torch_texture = getId("block/coloured_torch/coloured_torch_" + color);
            final TextureMap textureMap = new TextureMap()
                    .register(TextureKey.TORCH, coloured_torch_texture);

            final Identifier torchModelId = COLOURED_TORCH_MODEL.upload(
                    getId("block/coloured_torch/coloured_torch_" + color),
                    textureMap,
                    blockStateModelGenerator.modelCollector
            );
            blockStateModelGenerator.registerParentedItemModel(ColouredLightsGenerator.colouredTorchesVariants[i], torchModelId);
            blockStateModelGenerator.blockStateCollector.accept(getDefaultBlockstate(ColouredLightsGenerator.colouredTorchesVariants[i], torchModelId));
            ++i;
        }
    }

    protected static void generateLightStripModelsAndBlockStates(BlockStateModelGenerator blockStateModelGenerator) {
        int i = 0;
        for (String color : GenerationData.vanillaColorPallet) {
            final Identifier coloured_concrete = new Identifier("block/" + color + "_concrete");
            final TextureMap textureMap = new TextureMap()
                    .register(TextureKey.PARTICLE, coloured_concrete)
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
            blockStateModelGenerator.registerParentedItemModel(ColouredLightsGenerator.LightStripBlockVariants[i], lightStripStraightModelId);
            blockStateModelGenerator.blockStateCollector.accept(getLightStripBlockstate(
                    ColouredLightsGenerator.LightStripBlockVariants[i],
                    lightStripStraightModelId,
                    lightStripInnerModelId,
                    lightStripOuterModelId
            ));
            ++i;
        }
    }

    protected static void generateCandlestickModelsAndBlockStates(BlockStateModelGenerator blockStateModelGenerator) {
        generateCandlestickMABForMetals(blockStateModelGenerator, GenerationData.vanillaCandlestickMetals, CandlestickGenerator.candlestickClassicVariants);
        int i = 0;
        for (String[] metals : GenerationData.vanillaRustableCandlestickMetals) {
            generateCandlestickMABForMetals(blockStateModelGenerator, metals, CandlestickGenerator.candlestickRustableVariants.get(i));
            ++i;
        }
    }
    protected static void generateCandlestickMABForMetals(BlockStateModelGenerator blockStateModelGenerator, String[] metals, Block[] candlesticks) {
        int i = 0;
        Set<String> block_suffix_metals = Set.of("copper", "gold");
        for (String metal : metals) {
            String suffix = "";
            if (block_suffix_metals.contains(metal)) suffix = "_block";

            final Identifier candlestickModelId = CANDLESTICK_MODEL.upload(
                    getId("block/candlestick/" + metal + "/candlestick_" + metal),
                    new TextureMap()
                            .register(TextureKey.PARTICLE, new Identifier("block/" + metal + suffix))
                            .register(TextureKey.of("0"), new Identifier("block/" + metal + suffix)),
                    blockStateModelGenerator.modelCollector
            );

            final Identifier candlestickWithCandleModelId = CANDLESTICK_WITH_CANDLE_MODEL.upload(
                    getId("block/candlestick/" + metal + "/candlestick_" + metal + "_candle"),
                    new TextureMap()
                            .register(TextureKey.PARTICLE, new Identifier("block/" + metal + suffix))
                            .register(TextureKey.of("0"), new Identifier("block/" + metal + suffix)),
                    blockStateModelGenerator.modelCollector
            );
            final Identifier candlestickWithCandleLitModelId = CANDLESTICK_WITH_CANDLE_LIT_MODEL.upload(
                    getId("block/candlestick/" + metal + "/candlestick_" + metal + "_candle_lit"),
                    new TextureMap()
                            .register(TextureKey.PARTICLE, new Identifier("block/" + metal + suffix))
                            .register(TextureKey.of("0"), new Identifier("block/" + metal + suffix)),
                    blockStateModelGenerator.modelCollector
            );

            final Model candlestickWithCandleMetalModel = new Model(
                    Optional.of(candlestickWithCandleModelId),
                    Optional.empty()
            );
            final Model candlestickWithCandleLitMetalModel = new Model(
                    Optional.of(candlestickWithCandleLitModelId),
                    Optional.empty()
            );

            Map<CandleColor, Identifier> candleColorModelMap = new HashMap<>();
            Map<CandleColor, Identifier> litCandleColorModelMap = new HashMap<>();
            for (String color : GenerationData.vanillaColorPallet) {
                CandleColor candleColor = CandleColor.getColor(color);
                final Identifier candlestickColoredModelId = candlestickWithCandleMetalModel.upload(
                        getId("block/candlestick/" + metal + "/candlestick_" + metal + "_" + color),
                        new TextureMap()
                                .register(TextureKey.of("2"), new Identifier("block/" + color + "_candle")),
                        blockStateModelGenerator.modelCollector
                );
                candleColorModelMap.put(candleColor, candlestickColoredModelId);
                final Identifier candlestickLitColoredModelId = candlestickWithCandleLitMetalModel.upload(
                        getId("block/candlestick/" + metal + "/candlestick_" + metal + "_" + color + "_lit"),
                        new TextureMap()
                                .register(TextureKey.of("2"), new Identifier("block/" + color + "_candle_lit")),
                        blockStateModelGenerator.modelCollector
                );
                litCandleColorModelMap.put(candleColor, candlestickLitColoredModelId);
            }

            blockStateModelGenerator.registerParentedItemModel(candlesticks[i], candlestickModelId);
            blockStateModelGenerator.blockStateCollector.accept(getCandlestickBlockstate(
                    candlesticks[i],
                    candlestickModelId,
                    candlestickWithCandleModelId,
                    candlestickWithCandleLitModelId,
                    candleColorModelMap,
                    litCandleColorModelMap
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
                            .register(TextureKey.PARTICLE, new Identifier("block/" + woodType + "_planks"))
                            .register(TextureKey.TOP, new Identifier("block/" + woodType + "_planks"))
                            .register(TextureKey.BOTTOM, new Identifier("block/" + woodType + "_planks"))
                            .register(TextureKey.SIDE, new Identifier("block/" + woodType + "_planks")),
                    blockStateModelGenerator.modelCollector
            );
            final Identifier outerStairsModelId = OUTER_CORNER_STAIRS_MODEL.upload(
                    getId("block/corner_stairs/outer_stairs/outer_stairs_" + woodType),
                    new TextureMap()
                            .register(TextureKey.PARTICLE, new Identifier("block/" + woodType + "_planks"))
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
                                .register(TextureKey.PARTICLE, new Identifier(topTexture))
                                .register(TextureKey.TOP, new Identifier(topTexture))
                                .register(TextureKey.BOTTOM, new Identifier(bottomTexture))
                                .register(TextureKey.SIDE, new Identifier(sideTexture)),
                        blockStateModelGenerator.modelCollector
                );
                final Identifier outerStairsModelId = OUTER_CORNER_STAIRS_MODEL.upload(
                        getId("block/corner_stairs/outer_stairs/outer_stairs_" + material),
                        new TextureMap()
                                .register(TextureKey.PARTICLE, new Identifier(topTexture))
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
                                .register(TextureKey.of("2"), new Identifier("block/" + woodType2 + "_planks"))
                                .register(TextureKey.PARTICLE, new Identifier("block/" + woodType + "_planks")),
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
                                .register(TextureKey.of("2"), new Identifier("block/" + color2 + "_terracotta"))
                                .register(TextureKey.PARTICLE, new Identifier("block/" + color + "_terracotta")),
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
            final Identifier woodTypeCabinetId = CABINET_BLOCK_MODEL.upload(
                    getId("block/cabinet/closed/cabinet_block_" + woodType),
                    new TextureMap()
                            .register(TextureKey.of("2"), new Identifier("block/" + woodType + "_planks"))
                            .register(TextureKey.PARTICLE, new Identifier("block/" + woodType + "_planks")),
                    blockStateModelGenerator.modelCollector
            );
            final Model woodTypeCabinetModel = new Model(
                    Optional.of(woodTypeCabinetId),
                    Optional.empty()
            );

            final Identifier woodTypeCabinetOpenId = CABINET_BLOCK_OPEN_MODEL.upload(
                    getId("block/cabinet/open/cabinet_block_open_" + woodType),
                    new TextureMap()
                            .register(TextureKey.of("2"), new Identifier("block/" + woodType + "_planks"))
                            .register(TextureKey.PARTICLE, new Identifier("block/" + woodType + "_planks")),
                    blockStateModelGenerator.modelCollector
            );
            final Model woodTypeCabinetOpenModel = new Model(
                    Optional.of(woodTypeCabinetOpenId),
                    Optional.empty()
            );

            for (String color : GenerationData.vanillaColorPallet) {
                final Identifier coloredCabinet = woodTypeCabinetModel.upload(
                        getId("block/cabinet/closed/" + woodType + "/cabinet_block_" + woodType + "_" + color),
                        new TextureMap().register(TextureKey.of("1"), new Identifier("block/" + color + "_wool")),
                        blockStateModelGenerator.modelCollector
                );
                final Identifier coloredCabinetOpen = woodTypeCabinetOpenModel.upload(
                        getId("block/cabinet/open/" + woodType + "/cabinet_block_open_" + woodType + "_" + color),
                        new TextureMap().register(TextureKey.of("1"), new Identifier("block/" + color + "_wool")),
                        blockStateModelGenerator.modelCollector
                );

                blockStateModelGenerator.registerParentedItemModel(CabinetBlockGenerator.cabinetBlockVariants[i], coloredCabinet);
                blockStateModelGenerator.registerParentedItemModel(CabinetBlockGenerator.illuminatedCabinetBlockVariants[i], coloredCabinet);
                blockStateModelGenerator.blockStateCollector.accept(getCabinetBlockstate(CabinetBlockGenerator.cabinetBlockVariants[i], coloredCabinet, coloredCabinetOpen));
                blockStateModelGenerator.blockStateCollector.accept(getCabinetBlockstate(CabinetBlockGenerator.illuminatedCabinetBlockVariants[i], coloredCabinet, coloredCabinetOpen));
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

    protected static MultipartBlockStateSupplier getCandlestickBlockstate(
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
                    // NON LIT
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

    protected static MultipartBlockStateSupplier getCabinetBlockstate(Block cabinetBlock, Identifier cabinetModel, Identifier cabinetOpenModel) {
        return MultipartBlockStateSupplier
                .create(cabinetBlock)
                .with(
                        When.allOf(
                                When.create().set(Properties.HORIZONTAL_FACING, Direction.NORTH),
                                When.create().set(Properties.OPEN, true)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, cabinetOpenModel)
                                .put(VariantSettings.Y, VariantSettings.Rotation.R180)
                )
                .with(
                        When.allOf(
                                When.create().set(Properties.HORIZONTAL_FACING, Direction.NORTH),
                                When.create().set(Properties.OPEN, false)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, cabinetModel)
                                .put(VariantSettings.Y, VariantSettings.Rotation.R180)
                )
                .with(
                        When.allOf(
                                When.create().set(Properties.HORIZONTAL_FACING, Direction.EAST),
                                When.create().set(Properties.OPEN, true)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, cabinetOpenModel)
                                .put(VariantSettings.Y, VariantSettings.Rotation.R270)
                )
                .with(
                        When.allOf(
                                When.create().set(Properties.HORIZONTAL_FACING, Direction.EAST),
                                When.create().set(Properties.OPEN, false)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, cabinetModel)
                                .put(VariantSettings.Y, VariantSettings.Rotation.R270)
                )
                .with(
                        When.allOf(
                                When.create().set(Properties.HORIZONTAL_FACING, Direction.SOUTH),
                                When.create().set(Properties.OPEN, true)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, cabinetOpenModel)
                                .put(VariantSettings.Y, VariantSettings.Rotation.R0)
                )
                .with(
                        When.allOf(
                                When.create().set(Properties.HORIZONTAL_FACING, Direction.SOUTH),
                                When.create().set(Properties.OPEN, false)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, cabinetModel)
                                .put(VariantSettings.Y, VariantSettings.Rotation.R0)
                )
                .with(
                        When.allOf(
                                When.create().set(Properties.HORIZONTAL_FACING, Direction.WEST),
                                When.create().set(Properties.OPEN, true)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, cabinetOpenModel)
                                .put(VariantSettings.Y, VariantSettings.Rotation.R90)
                )
                .with(
                        When.allOf(
                                When.create().set(Properties.HORIZONTAL_FACING, Direction.WEST),
                                When.create().set(Properties.OPEN, false)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, cabinetModel)
                                .put(VariantSettings.Y, VariantSettings.Rotation.R90)
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
    protected static BlockStateVariant getVariant(Identifier model) {
        return BlockStateVariant.create().put(VariantSettings.MODEL, model);
    }


    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        for (Item item : ItemRegistry.glowingPowderVariants) {
            itemModelGenerator.register(item, Models.GENERATED);
        }
    }
}
