package io.github.mikip98.humilityafm.datagen;

import io.github.mikip98.humilityafm.content.properties.ModProperties;
import io.github.mikip98.humilityafm.content.properties.enums.CandleColor;
import io.github.mikip98.humilityafm.registries.BlockRegistry;
import io.github.mikip98.humilityafm.registries.ItemRegistry;
import io.github.mikip98.humilityafm.util.Pair;
import io.github.mikip98.humilityafm.util.generation_data.ActiveGenerationData;
import io.github.mikip98.humilityafm.util.generation_data.RawGenerationData;
import io.github.mikip98.humilityafm.util.mod_support.SupportedMods;
import io.github.mikip98.humilityafm.util.generation_data.material_management.material.BlockMaterial;
import io.github.mikip98.humilityafm.util.generation_data.material_management.material.MaterialType;
#if MC_VERSION >= 12104
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
#endif
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
#if MC_VERSION < 12104
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
#endif
import net.minecraft.block.Block;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.StairShape;
#if MC_VERSION < 12104
import net.minecraft.data.client.*;
#else
import net.minecraft.client.data.*;
#endif
#if MC_VERSION >= 12105
import net.minecraft.client.render.model.json.*;
#endif
import net.minecraft.item.Item;
import net.minecraft.state.property.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
#if MC_VERSION >= 12105
import net.minecraft.util.math.AxisRotation;
#endif
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

import static io.github.mikip98.humilityafm.HumilityAFM.*;
#if MC_VERSION >= 12105
import static net.minecraft.client.data.BlockStateModelGenerator.createWeightedVariant;
#endif

public class ModelGenerator extends FabricModelProvider {
    // Cabinet Models
    protected static final Model CABINET_BLOCK_MODEL = getModel("block/cabinet_block");
    protected static final Model CABINET_BLOCK_OPEN_MODEL = getModel("block/cabinet_block_open");
    protected static final Model FLOOR_CABINET_BLOCK_MODEL = getModel("block/floor_cabinet_block");
    protected static final Model FLOOR_CABINET_BLOCK_OPEN_MODEL = getModel("block/floor_cabinet_block_open");
    // Checker 2x2 Model
    protected static final Model CHECKER_2X2_MODEL = getModel("block/checker_2x2");
    // Forced Corner Stairs Models
    protected static final Model INNER_CORNER_STAIRS_MODEL = getModel("block/stairs_inner");
    protected static final Model OUTER_CORNER_STAIRS_MODEL = getModel("block/stairs_outer");
    // Coloured Torch Model
    protected static final Model TORCH_TEMPLATE_MODEL = getModel("block/torch_template");
    protected static final Model TORCH_WALL_TEMPLATE_MODEL = getModel("block/torch_wall_template");
    // Candlestick Models
    protected static final Model CANDLESTICK_STANDING_MODEL = getModel("block/candlestick");
    protected static final Model CANDLESTICK_STANDING_WITH_CANDLE_MODEL = getModel("block/candlestick_candle");
    protected static final Model CANDLESTICK_STANDING_WITH_CANDLE_LIT_MODEL = getModel("block/candlestick_candle_lit");
    protected static final Model CANDLESTICK_WALL_MODEL = getModel("block/candlestick_wall");
    protected static final Model CANDLESTICK_WALL_WITH_CANDLE_MODEL = getModel("block/candlestick_wall_candle");
    protected static final Model CANDLESTICK_WALL_WITH_CANDLE_LIT_MODEL = getModel("block/candlestick_wall_candle_lit");
    // Light Strip Models
    protected static final Model LIGHT_STRIP_STRAIGHT_MODEL = getModel("block/light_strip");
    protected static final Model LIGHT_STRIP_INNER_MODEL = getModel("block/light_strip_inner");
    protected static final Model LIGHT_STRIP_OUTER_MODEL = getModel("block/light_strip_outer");
    // Jack o'Lantern template model
    protected static final Model JACK_O_LANTERN_TEMPLATE_MODEL = getModel("block/jack_o_lantern_template");


    protected BlockStateModelGenerator blockStateModelGenerator;


    public ModelGenerator(FabricDataOutput output) {
        super(output);
    }


    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        this.blockStateModelGenerator = blockStateModelGenerator;

        // ............ TEST BLOCKS & BLOCK ITEMS ............
        // Cabinet Blocks
        registerItemModel(ItemRegistry.CABINET_ITEM, getId("block/cabinet_block"));
        registerItemModel(ItemRegistry.ILLUMINATED_CABINET_ITEM, getId("block/cabinet_block"));
        blockStateModelGenerator.blockStateCollector.accept(getCabinetBlockstate(BlockRegistry.CABINET_BLOCK, getId("block/cabinet_block"), getId("block/cabinet_block_open")));
        blockStateModelGenerator.blockStateCollector.accept(getCabinetBlockstate(BlockRegistry.ILLUMINATED_CABINET_BLOCK, getId("block/cabinet_block"), getId("block/cabinet_block_open")));
        blockStateModelGenerator.blockStateCollector.accept(getFloorCabinetBlockstate(BlockRegistry.FLOOR_CABINET_BLOCK, getId("block/floor_cabinet_block"), getId("block/floor_cabinet_block_open")));
        blockStateModelGenerator.blockStateCollector.accept(getFloorCabinetBlockstate(BlockRegistry.FLOOR_ILLUMINATED_CABINET_BLOCK, getId("block/floor_cabinet_block"), getId("block/floor_cabinet_block_open")));

        // ............ FINAL BLOCKS & BLOCK ITEMS ............
        generateCabinetModelsAndBlockStates(blockStateModelGenerator);
        generateWoodenMosaicModelsAndBlockStates(blockStateModelGenerator);
        generateTerracottaTilesModelsAndBlockStates(blockStateModelGenerator);
        generateForcedCornerStairsModelsAndBlockstates(blockStateModelGenerator);
        // Optional blocks
        generateCandlestickModelsAndBlockStates(blockStateModelGenerator);
        generateColouredTorchModelsAndBlockStates(blockStateModelGenerator);
        generateLightStripModelsAndBlockStates();
        generateColouredJackOLanternModelsAndBlockStates(blockStateModelGenerator);
    }

    protected static void generateColouredJackOLanternModelsAndBlockStates(BlockStateModelGenerator blockStateModelGenerator) {
        int i = 0;
        for (String color : RawGenerationData.vanillaColorPallet) {
            final Identifier colouredJackOLanternTexture = getId("block/coloured_jack_o_lantern/coloured_jack_o_lantern_" + color);
            final TextureMap textureMap = new TextureMap()
                    .register(TextureKey.FRONT, colouredJackOLanternTexture);

            final Identifier jackOLanternModelId = JACK_O_LANTERN_TEMPLATE_MODEL.upload(
                    getId("block/coloured_jack_o_lantern/coloured_jack_o_lantern_" + color),
                    textureMap,
                    blockStateModelGenerator.modelCollector
            );
            blockStateModelGenerator.registerParentedItemModel(BlockRegistry.COLOURED_JACK_O_LANTERNS[i], jackOLanternModelId);
            blockStateModelGenerator.blockStateCollector.accept(getOrientableBlockState(
                    BlockRegistry.COLOURED_JACK_O_LANTERNS[i],
                    jackOLanternModelId
            ));
            ++i;
        }
    }

    protected static #if MC_VERSION < 12105 VariantsBlockStateSupplier #else VariantsBlockModelDefinitionCreator #endif getOrientableBlockState(Block block, Identifier modelId) {
        #if MC_VERSION < 12105
        return VariantsBlockStateSupplier.create(block)
                .coordinate(BlockStateVariantMap.create(Properties.HORIZONTAL_FACING)
        #else
        return VariantsBlockModelDefinitionCreator.of(block)
                .with(BlockStateVariantMap.models(Properties.HORIZONTAL_FACING)
        #endif
                        .register(
                                Direction.NORTH,
                                getVariant(modelId)
                        )
                        .register(
                                Direction.SOUTH,
                                getUVLockedVariantY(modelId, Rotation.R180.get())
                        )
                        .register(
                                Direction.WEST,
                                getUVLockedVariantY(modelId, Rotation.R270.get())
                        )
                        .register(
                                Direction.EAST,
                                getUVLockedVariantY(modelId, Rotation.R90.get())
                        )
                );
    }
    protected static #if MC_VERSION < 12105 VariantsBlockStateSupplier #else VariantsBlockModelDefinitionCreator #endif getTorchOrientableBlockState(Block block, Identifier modelId) {
        #if MC_VERSION < 12105
        return VariantsBlockStateSupplier.create(block)
                .coordinate(BlockStateVariantMap.create(Properties.HORIZONTAL_FACING)
        #else
        return VariantsBlockModelDefinitionCreator.of(block)
                .with(BlockStateVariantMap.models(Properties.HORIZONTAL_FACING)
        #endif
                        .register(
                                Direction.NORTH,
                                getVariantY(modelId, Rotation.R270.get())
                        )
                        .register(
                                Direction.SOUTH,
                                getVariantY(modelId, Rotation.R90.get())
                        )
                        .register(
                                Direction.WEST,
                                getVariantY(modelId, Rotation.R180.get())
                        )
                        .register(
                                Direction.EAST,
                                getVariant(modelId)
                        )
                );
    }

    protected void generateColouredTorchModelsAndBlockStates(BlockStateModelGenerator blockStateModelGenerator) {
        int i = 0;
        for (String color : RawGenerationData.vanillaColorPallet) {
            final Identifier coloured_torch_texture = getId("block/coloured_torch/coloured_torch_" + color);
            final TextureMap textureMap = new TextureMap()
                    .register(TextureKey.TORCH, coloured_torch_texture);

            final Identifier torchModelId = TORCH_TEMPLATE_MODEL.upload(
                    getId("block/coloured_torch/coloured_torch_" + color),
                    textureMap,
                    blockStateModelGenerator.modelCollector
            );
            final Identifier wallTorchModelId = TORCH_WALL_TEMPLATE_MODEL.upload(
                    getId("block/coloured_torch/coloured_torch_wall_" + color),
                    textureMap,
                    blockStateModelGenerator.modelCollector
            );

            registerItemModel(ItemRegistry.COLOURED_TORCH_ITEM_VARIANTS[i], torchModelId);
            blockStateModelGenerator.blockStateCollector.accept(getDefaultBlockstate(BlockRegistry.COLOURED_TORCH_VARIANTS[i], torchModelId));
            blockStateModelGenerator.blockStateCollector.accept(getTorchOrientableBlockState(BlockRegistry.COLOURED_WALL_TORCH_VARIANTS[i], wallTorchModelId));
            ++i;
        }
    }

    protected void generateLightStripModelsAndBlockStates() {
        int i = 0;
        for (BlockMaterial material : ActiveGenerationData.colouredFeatureSetMaterials) {
            final String color = material.layers()[0].name();

            final Identifier coloured_concrete = getVanillaId("block/" + color + "_concrete");
            final TextureMap textureMap = new TextureMap()
                    .register(TextureKey.of("0"), coloured_concrete);

            final String pathPrefix = "block/light_strip/";
            final Identifier lightStripStraightModelId = LIGHT_STRIP_STRAIGHT_MODEL.upload(
                    getId(pathPrefix + "straight/light_strip_" + material.getSafeName()),
                    textureMap,
                    blockStateModelGenerator.modelCollector
            );
            final Identifier lightStripInnerModelId = LIGHT_STRIP_INNER_MODEL.upload(
                    getId(pathPrefix + "inner/light_strip_inner_" + material.getSafeName()),
                    textureMap,
                    blockStateModelGenerator.modelCollector
            );
            final Identifier lightStripOuterModelId = LIGHT_STRIP_OUTER_MODEL.upload(
                    getId(pathPrefix + "outer/light_strip_outer_" + material.getSafeName()),
                    textureMap,
                    blockStateModelGenerator.modelCollector
            );
            blockStateModelGenerator.registerParentedItemModel(BlockRegistry.LIGHT_STRIP_VARIANTS[i], lightStripStraightModelId);
            blockStateModelGenerator.blockStateCollector.accept(getLightStripBlockstate(
                    BlockRegistry.LIGHT_STRIP_VARIANTS[i],
                    lightStripStraightModelId,
                    lightStripInnerModelId,
                    lightStripOuterModelId
            ));
            ++i;
        }
    }

    protected void generateCandlestickModelsAndBlockStates(BlockStateModelGenerator blockStateModelGenerator) {
        generateCandlestickModelsAndBlockstatesForMetals(
                blockStateModelGenerator,
                ActiveGenerationData.simpleCandlestickMaterials,
                ItemRegistry.CANDLESTICK_ITEM_VARIANTS,
                BlockRegistry.SIMPLE_CANDLESTICK_WALL_VARIANTS,
                BlockRegistry.SIMPLE_CANDLESTICK_FLOOR_VARIANTS
        );
        for (int i = 0; i < ItemRegistry.RUSTABLE_CANDLESTICK_ITEM_VARIANTS.length; ++i) {
            generateCandlestickModelsAndBlockstatesForMetals(
                    blockStateModelGenerator,
                    ActiveGenerationData.rustingCandlestickMaterials[i],
                    ItemRegistry.RUSTABLE_CANDLESTICK_ITEM_VARIANTS[i],
                    BlockRegistry.RUSTABLE_CANDLESTICK_WALL_VARIANTS[i],
                    BlockRegistry.RUSTABLE_CANDLESTICK_FLOOR_VARIANTS[i]
            );
        }
    }
    protected void generateCandlestickModelsAndBlockstatesForMetals(
            BlockStateModelGenerator blockStateModelGenerator,
            Iterable<BlockMaterial> materials,
            Item[] items, Block[] wallBlocks, Block[] standingBlocks
    ) {
        int i = 0;
        final Set<String> block_suffix_metals = Set.of("copper", "gold", "iron");
        for (BlockMaterial material : materials) {
            final String metal = material.layers()[0].name();

            final String suffix = block_suffix_metals.contains(metal) ? "_block" : "";
            final TextureMap metalTextureMap = new TextureMap()
                    .register(TextureKey.of("metal"), getVanillaId("block/" + metal + suffix));

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
            for (String color : RawGenerationData.vanillaColorPallet) {
                final CandleColor candleColor = CandleColor.getColor(color);
                final TextureMap candleColorTextureMap = new TextureMap()
                        .register(TextureKey.of("candle"), getVanillaId("block/" + color + "_candle_lit"));

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

            registerItemModel(items[i], candlestickStandingMetalModelId);
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
        for (BlockMaterial stairMaterial : ActiveGenerationData.forcedCornerStairsVariantMaterials) {
            if (stairMaterial.layers().length != 1) throw new IllegalArgumentException("Forced corner stairs material must have exactly one materialLayer, but found: " + stairMaterial.layers().length);
            final BlockMaterial.Layer materialLayer = stairMaterial.layers()[0];

            final SupportedMods sourceMod = materialLayer.metadata().sourceMod();
            final Function<String, Identifier> getBlockId = (name) -> getBlockId(sourceMod, name);

            final MaterialType materialType = materialLayer.metadata().type();
            if (materialType == null) throw new IllegalStateException("Material type is null for forced corner stairs material layer: " + materialLayer.name());

            Identifier topTextureId;
            Identifier sideTextureId;
            Identifier bottomTextureId;

            String textureBlockName;
            switch (materialType) {
                case BURNABLE_WOOD, FIREPROOF_WOOD -> {
                    textureBlockName = materialLayer.name() + "_planks";
                    final Identifier textureId = getBlockId.apply(textureBlockName);
                    topTextureId = textureId;
                    sideTextureId = textureId;
                    bottomTextureId = textureId;
                }
                case STONY -> {
                    // TODO: Pack into separate function?
                    textureBlockName = materialLayer.name();
                    String topTextureName = textureBlockName;
                    String sideTextureName = textureBlockName;
                    String bottomTextureName = textureBlockName;

                    String suffix = "";

                    switch (textureBlockName) {
                        case "quartz" -> suffix = "_block_side";
                        case "sandstone", "red_sandstone" -> {
                            topTextureName += "_top";
                            bottomTextureName += "_bottom";
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
                            bottomTextureName = "quartz_block_bottom";
                            topTextureName = bottomTextureName;
                            sideTextureName = bottomTextureName;
                        }
                        case "smooth_sandstone" -> {
                            topTextureName = "sandstone_top";
                            bottomTextureName = topTextureName;
                            sideTextureName = topTextureName;
                        }
                        case "smooth_red_sandstone" -> {
                            topTextureName = "red_sandstone_top";
                            bottomTextureName = topTextureName;
                            sideTextureName = topTextureName;
                        }
                    }
                    topTextureId = getBlockId.apply(topTextureName + suffix);
                    sideTextureId = getBlockId.apply(sideTextureName + suffix);
                    bottomTextureId = getBlockId.apply(bottomTextureName + suffix);
                }
                default -> throw new IllegalArgumentException("Unsupported material type for forced corner stairs: " + materialType);
            }

            final Identifier innerStairsModelId = INNER_CORNER_STAIRS_MODEL.upload(
                    getId("block/corner_stairs/inner_stairs/inner_stairs_" + stairMaterial.getSafeName()),
                    new TextureMap()
                            .register(TextureKey.TOP, topTextureId)
                            .register(TextureKey.BOTTOM, bottomTextureId)
                            .register(TextureKey.SIDE, sideTextureId),
                    blockStateModelGenerator.modelCollector
            );
            final Identifier outerStairsModelId = OUTER_CORNER_STAIRS_MODEL.upload(
                    getId("block/corner_stairs/outer_stairs/outer_stairs_" + stairMaterial.getSafeName()),
                    new TextureMap()
                            .register(TextureKey.TOP, topTextureId)
                            .register(TextureKey.BOTTOM, bottomTextureId)
                            .register(TextureKey.SIDE, sideTextureId),
                    blockStateModelGenerator.modelCollector
            );

            blockStateModelGenerator.registerParentedItemModel(BlockRegistry.INNER_STAIRS_BLOCK_VARIANTS[i], innerStairsModelId);
            blockStateModelGenerator.registerParentedItemModel(BlockRegistry.OUTER_STAIRS_BLOCK_VARIANTS[i], outerStairsModelId);
            blockStateModelGenerator.blockStateCollector.accept(getForcedCornerStairsBlockstate(BlockRegistry.INNER_STAIRS_BLOCK_VARIANTS[i], innerStairsModelId));
            blockStateModelGenerator.blockStateCollector.accept(getForcedCornerStairsBlockstate(BlockRegistry.OUTER_STAIRS_BLOCK_VARIANTS[i], outerStairsModelId));

            ++i;
        }
    }

    protected static void generateWoodenMosaicModelsAndBlockStates(BlockStateModelGenerator blockStateModelGenerator) {
        int i = 0;
        for (BlockMaterial variantMaterial : ActiveGenerationData.woodenMosaicVariantMaterials) {
            if (variantMaterial.layers().length != 2) {
                throw new IllegalArgumentException("Wooden mosaic material must have exactly two materialLayers, but found: " + variantMaterial.layers().length);
            }
            BlockMaterial.Layer layer1 = variantMaterial.layers()[0];
            BlockMaterial.Layer layer2 = variantMaterial.layers()[1];
            SupportedMods mod1 = layer1.metadata().sourceMod();
            SupportedMods mod2 = layer2.metadata().sourceMod();

            Identifier planks1Id = getBlockId(mod1, layer1.name() + "_planks");
            Identifier planks2Id = getBlockId(mod2, layer2.name() + "_planks");

            String modFolders = "";
            if (mod1 != null) modFolders += mod1.modId;
            if (mod1 != null && mod2 != null) modFolders += "_";
            if (mod2 != null) modFolders += mod2.modId;
            if (!modFolders.isEmpty()) modFolders += '/';

            // TODO: Add the modFolders to other blocks
            Identifier modelLocationId = getId("block/wooden_mosaic/" + modFolders + "wooden_mosaic_" + variantMaterial.getSafeName());

            final Identifier woodenMosaicModelId = CHECKER_2X2_MODEL.upload(
                    modelLocationId,
                    new TextureMap()
                            .register(TextureKey.of("1"), planks1Id)
                            .register(TextureKey.of("2"), planks2Id),
                    blockStateModelGenerator.modelCollector
            );
            blockStateModelGenerator.registerParentedItemModel(BlockRegistry.WOODEN_MOSAIC_VARIANTS[i], woodenMosaicModelId);
            blockStateModelGenerator.blockStateCollector.accept(getDefaultBlockstate(BlockRegistry.WOODEN_MOSAIC_VARIANTS[i], woodenMosaicModelId));
            ++i;
        }
    }
    protected static void generateTerracottaTilesModelsAndBlockStates(BlockStateModelGenerator blockStateModelGenerator) {
        int i = 0;
        for (String color : RawGenerationData.vanillaColorPallet) {
            for (String color2 : RawGenerationData.vanillaColorPallet) {
                if (color.equals(color2)) continue;

                final Identifier terracottaTileModelId = CHECKER_2X2_MODEL.upload(
                        getId("block/terracotta_tiles/terracotta_tiles_" + color + "_" + color2),
                        new TextureMap()
                                .register(TextureKey.of("1"), getVanillaId("block/" + color + "_terracotta"))
                                .register(TextureKey.of("2"), getVanillaId("block/" + color2 + "_terracotta")),
                        blockStateModelGenerator.modelCollector
                );
                blockStateModelGenerator.registerParentedItemModel(BlockRegistry.TERRACOTTA_TILE_VARIANTS[i], terracottaTileModelId);
                blockStateModelGenerator.blockStateCollector.accept(getDefaultBlockstate(BlockRegistry.TERRACOTTA_TILE_VARIANTS[i], terracottaTileModelId));

                ++i;
            }
        }
    }

    protected void generateCabinetModelsAndBlockStates(BlockStateModelGenerator blockStateModelGenerator) {
        Map<Pair<SupportedMods, String>, CabinetModelWoodSet> mod2baseWoodSet = new HashMap<>();
        int i = 0;
        for (BlockMaterial material : ActiveGenerationData.cabinetVariantMaterials) {
            if (material.layers().length != 2) throw new IllegalStateException("Cabinet material must have exactly two materialLayers, but found: " + material.layers().length);
            BlockMaterial.Layer woodLayer = material.layers()[0];
            Pair<SupportedMods, String> baseWoodSetKey = Pair.of(woodLayer.metadata().sourceMod(), woodLayer.name());
            mod2baseWoodSet.computeIfAbsent(baseWoodSetKey, k -> generateCabinetWoodSet(woodLayer, blockStateModelGenerator));
            CabinetModelWoodSet baseWoodSet = mod2baseWoodSet.get(baseWoodSetKey);

            BlockMaterial.Layer colorLayer = material.layers()[1];

            final TextureMap woolTextureMap = new TextureMap().register(
                    TextureKey.of("wool"),
                    getVanillaId("block/" + colorLayer.name() + "_wool")
            );

            final Identifier coloredCabinet = baseWoodSet.wallClosed.upload(
                    getCabinetCompleteModelId("wall", "closed", woodLayer, colorLayer),
                    woolTextureMap,
                    blockStateModelGenerator.modelCollector
            );
            final Identifier coloredCabinetOpen = baseWoodSet.wallOpen.upload(
                    getCabinetCompleteModelId("wall", "open", woodLayer, colorLayer),
                    woolTextureMap,
                    blockStateModelGenerator.modelCollector
            );
            final Identifier coloredFloorCabinet = baseWoodSet.floorClosed.upload(
                    getCabinetCompleteModelId("floor", "closed", woodLayer, colorLayer),
                    woolTextureMap,
                    blockStateModelGenerator.modelCollector
            );
            final Identifier coloredFloorCabinetOpen = baseWoodSet.floorOpen.upload(
                    getCabinetCompleteModelId("floor", "open", woodLayer, colorLayer),
                    woolTextureMap,
                    blockStateModelGenerator.modelCollector
            );

            registerItemModel(ItemRegistry.CABINET_ITEM_VARIANTS[i], coloredCabinet);
            registerItemModel(ItemRegistry.ILLUMINATED_CABINET_ITEM_VARIANTS[i], coloredCabinet);
            blockStateModelGenerator.blockStateCollector.accept(getCabinetBlockstate(BlockRegistry.WALL_CABINET_BLOCK_VARIANTS[i], coloredCabinet, coloredCabinetOpen));
            blockStateModelGenerator.blockStateCollector.accept(getCabinetBlockstate(BlockRegistry.WALL_ILLUMINATED_CABINET_BLOCK_VARIANTS[i], coloredCabinet, coloredCabinetOpen));
            blockStateModelGenerator.blockStateCollector.accept(getFloorCabinetBlockstate(BlockRegistry.FLOOR_CABINET_BLOCK_VARIANTS[i], coloredFloorCabinet, coloredFloorCabinetOpen));
            blockStateModelGenerator.blockStateCollector.accept(getFloorCabinetBlockstate(BlockRegistry.FLOOR_ILLUMINATED_CABINET_BLOCK_VARIANTS[i], coloredFloorCabinet, coloredFloorCabinetOpen));
            ++i;
        }
    }
    protected static CabinetModelWoodSet generateCabinetWoodSet(BlockMaterial.Layer wood, BlockStateModelGenerator blockStateModelGenerator) {
        SupportedMods sourceMod = wood.metadata().sourceMod();
        final TextureMap plankTextureMap = new TextureMap().register(
                TextureKey.of("planks"),
                getBlockId(sourceMod, wood.name() + "_planks")
        );

        final Identifier woodTypeCabinetId = CABINET_BLOCK_MODEL.upload(
                getCabinetBaseWoodModelId(sourceMod, "wall", "closed", wood.name()),
                plankTextureMap,
                blockStateModelGenerator.modelCollector
        );

        final Identifier woodTypeCabinetOpenId = CABINET_BLOCK_OPEN_MODEL.upload(
                getCabinetBaseWoodModelId(sourceMod, "wall", "open", wood.name()),
                plankTextureMap,
                blockStateModelGenerator.modelCollector
        );

        final Identifier woodTypeFloorCabinetId = FLOOR_CABINET_BLOCK_MODEL.upload(
                getCabinetBaseWoodModelId(sourceMod, "floor", "closed", wood.name()),
                plankTextureMap,
                blockStateModelGenerator.modelCollector
        );

        final Identifier woodTypeFloorCabinetOpenId = FLOOR_CABINET_BLOCK_OPEN_MODEL.upload(
                getCabinetBaseWoodModelId(sourceMod, "floor", "open", wood.name()),
                plankTextureMap,
                blockStateModelGenerator.modelCollector
        );

        return new CabinetModelWoodSet(
                getModel(woodTypeCabinetId),
                getModel(woodTypeCabinetOpenId),
                getModel(woodTypeFloorCabinetId),
                getModel(woodTypeFloorCabinetOpenId)
        );
    }
    protected static Identifier getCabinetBaseWoodModelId(SupportedMods sourceMod, String wallFloor, String openClosed, String woodName) {
        final String cabinetPath = "block/cabinet/" + (sourceMod != null ? sourceMod.modId : "vanilla") + "/" + wallFloor + "/" + openClosed;
        final String cabinetName = "cabinet_block_" + (sourceMod != null ? sourceMod.modId + "_" : "") + woodName;
        return getId(cabinetPath + "/" + cabinetName);
    }
    protected static Identifier getCabinetCompleteModelId(String wallFloor, String openClosed, BlockMaterial.Layer woodLayer, BlockMaterial.Layer colorLayer) {
        final SupportedMods woodSourceMod = woodLayer.metadata().sourceMod();
        final String woodName = woodLayer.name();
        final String cabinetPath = "block/cabinet/" + (woodSourceMod != null ? woodSourceMod.modId : "vanilla") + "/" + wallFloor + "/" + openClosed + "/" + woodName;
        final String cabinetName = "cabinet_block_" + (woodSourceMod != null ? woodSourceMod.modId + "_" : "") + woodName + "_" + colorLayer.name();
        return getId(cabinetPath + "/" + cabinetName);
    }
    protected record CabinetModelWoodSet(Model wallClosed, Model wallOpen, Model floorClosed, Model floorOpen) {}


    protected static #if MC_VERSION < 12105 VariantsBlockStateSupplier #else VariantsBlockModelDefinitionCreator #endif getLightStripBlockstate(
            Block block,
            Identifier straightModel,
            Identifier innerModel,
            Identifier outerModel
    ) {
        #if MC_VERSION < 12104 DirectionProperty #else EnumProperty<Direction> #endif FACING = Properties.HORIZONTAL_FACING;
        EnumProperty<BlockHalf> HALF = Properties.BLOCK_HALF;
        EnumProperty<StairShape> SHAPE = Properties.STAIR_SHAPE;

        #if MC_VERSION < 12105
        return VariantsBlockStateSupplier.create(block)
                .coordinate(BlockStateVariantMap.create(FACING, HALF, SHAPE)
        #else
        return VariantsBlockModelDefinitionCreator.of(block)
                .with(BlockStateVariantMap.models(FACING, HALF, SHAPE)
        #endif
                        // East Bottom
                        .register(
                                Direction.EAST, BlockHalf.BOTTOM, StairShape.INNER_LEFT,
                                getUVLockedUpsideDownVariant(innerModel)
                        )
                        .register(
                                Direction.EAST, BlockHalf.BOTTOM, StairShape.INNER_RIGHT,
                                getUVLockedUpsideDownVariantY(innerModel, Rotation.R90.get())
                        )
                        .register(
                                Direction.EAST, BlockHalf.BOTTOM, StairShape.OUTER_LEFT,
                                getUVLockedUpsideDownVariant(outerModel)
                        )
                        .register(
                                Direction.EAST, BlockHalf.BOTTOM, StairShape.OUTER_RIGHT,
                                getUVLockedUpsideDownVariantY(outerModel, Rotation.R90.get())
                        )
                        .register(
                                Direction.EAST, BlockHalf.BOTTOM, StairShape.STRAIGHT,
                                getUVLockedUpsideDownVariantY(straightModel, Rotation.R90.get())
                        )
                        // East Top
                        .register(
                                Direction.EAST, BlockHalf.TOP, StairShape.INNER_LEFT,
                                getUVLockedVariantY(innerModel, Rotation.R270.get())
                        )
                        .register(
                                Direction.EAST, BlockHalf.TOP, StairShape.INNER_RIGHT,
                                getVariant(innerModel)
                        )
                        .register(
                                Direction.EAST, BlockHalf.TOP, StairShape.OUTER_LEFT,
                                getUVLockedVariantY(outerModel, Rotation.R270.get())
                        )
                        .register(
                                Direction.EAST, BlockHalf.TOP, StairShape.OUTER_RIGHT,
                                getVariant(outerModel)
                        )
                        .register(
                                Direction.EAST, BlockHalf.TOP, StairShape.STRAIGHT,
                                getUVLockedVariantY(straightModel, Rotation.R270.get())
                        )
                        // North Bottom
                        .register(
                                Direction.NORTH, BlockHalf.BOTTOM, StairShape.INNER_LEFT,
                                getUVLockedUpsideDownVariantY(innerModel, Rotation.R270.get())
                        )
                        .register(
                                Direction.NORTH, BlockHalf.BOTTOM, StairShape.INNER_RIGHT,
                                getUVLockedUpsideDownVariant(innerModel)
                        )
                        .register(
                                Direction.NORTH, BlockHalf.BOTTOM, StairShape.OUTER_LEFT,
                                getUVLockedUpsideDownVariantY(outerModel, Rotation.R270.get())
                        )
                        .register(
                                Direction.NORTH, BlockHalf.BOTTOM, StairShape.OUTER_RIGHT,
                                getUVLockedUpsideDownVariant(outerModel)
                        )
                        .register(
                                Direction.NORTH, BlockHalf.BOTTOM, StairShape.STRAIGHT,
                                getUVLockedUpsideDownVariant(straightModel)
                        )
                        // North Top
                        .register(
                                Direction.NORTH, BlockHalf.TOP, StairShape.INNER_LEFT,
                                getUVLockedVariantY(innerModel, Rotation.R180.get())
                        )
                        .register(
                                Direction.NORTH, BlockHalf.TOP, StairShape.INNER_RIGHT,
                                getUVLockedVariantY(innerModel, Rotation.R270.get())
                        )
                        .register(
                                Direction.NORTH, BlockHalf.TOP, StairShape.OUTER_LEFT,
                                getUVLockedVariantY(outerModel, Rotation.R180.get())
                        )
                        .register(
                                Direction.NORTH, BlockHalf.TOP, StairShape.OUTER_RIGHT,
                                getUVLockedVariantY(outerModel, Rotation.R270.get())
                        )
                        .register(
                                Direction.NORTH, BlockHalf.TOP, StairShape.STRAIGHT,
                                getUVLockedVariantY(straightModel, Rotation.R180.get())
                        )
                        // South Bottom
                        .register(
                                Direction.SOUTH, BlockHalf.BOTTOM, StairShape.INNER_LEFT,
                                getUVLockedUpsideDownVariantY(innerModel, Rotation.R90.get())
                        )
                        .register(
                                Direction.SOUTH, BlockHalf.BOTTOM, StairShape.INNER_RIGHT,
                                getUVLockedUpsideDownVariantY(innerModel, Rotation.R180.get())
                        )
                        .register(
                                Direction.SOUTH, BlockHalf.BOTTOM, StairShape.OUTER_LEFT,
                                getUVLockedUpsideDownVariantY(outerModel, Rotation.R90.get())
                        )
                        .register(
                                Direction.SOUTH, BlockHalf.BOTTOM, StairShape.OUTER_RIGHT,
                                getUVLockedUpsideDownVariantY(outerModel, Rotation.R180.get())
                        )
                        .register(
                                Direction.SOUTH, BlockHalf.BOTTOM, StairShape.STRAIGHT,
                                getUVLockedUpsideDownVariantY(straightModel, Rotation.R180.get())
                        )
                        // South Top
                        .register(
                                Direction.SOUTH, BlockHalf.TOP, StairShape.INNER_LEFT,
                                getVariant(innerModel)
                        )
                        .register(
                                Direction.SOUTH, BlockHalf.TOP, StairShape.INNER_RIGHT,
                                getUVLockedVariantY(innerModel, Rotation.R90.get())
                        )
                        .register(
                                Direction.SOUTH, BlockHalf.TOP, StairShape.OUTER_LEFT,
                                getVariant(outerModel)
                        )
                        .register(
                                Direction.SOUTH, BlockHalf.TOP, StairShape.OUTER_RIGHT,
                                getUVLockedVariantY(outerModel, Rotation.R90.get())
                        )
                        .register(
                                Direction.SOUTH, BlockHalf.TOP, StairShape.STRAIGHT,
                                getVariant(straightModel)
                        )
                        // West Bottom
                        .register(
                                Direction.WEST, BlockHalf.BOTTOM, StairShape.INNER_LEFT,
                                getUVLockedUpsideDownVariantY(innerModel, Rotation.R180.get())
                        )
                        .register(
                                Direction.WEST, BlockHalf.BOTTOM, StairShape.INNER_RIGHT,
                                getUVLockedUpsideDownVariantY(innerModel, Rotation.R270.get())
                        )
                        .register(
                                Direction.WEST, BlockHalf.BOTTOM, StairShape.OUTER_LEFT,
                                getUVLockedUpsideDownVariantY(outerModel, Rotation.R180.get())
                        )
                        .register(
                                Direction.WEST, BlockHalf.BOTTOM, StairShape.OUTER_RIGHT,
                                getUVLockedUpsideDownVariantY(outerModel, Rotation.R270.get())
                        )
                        .register(
                                Direction.WEST, BlockHalf.BOTTOM, StairShape.STRAIGHT,
                                getUVLockedUpsideDownVariantY(straightModel, Rotation.R270.get())
                        )
                        // West Top
                        .register(
                                Direction.WEST, BlockHalf.TOP, StairShape.INNER_LEFT,
                                getUVLockedVariantY(innerModel, Rotation.R90.get())
                        )
                        .register(
                                Direction.WEST, BlockHalf.TOP, StairShape.INNER_RIGHT,
                                getUVLockedVariantY(innerModel, Rotation.R180.get())
                        )
                        .register(
                                Direction.WEST, BlockHalf.TOP, StairShape.OUTER_LEFT,
                                getUVLockedVariantY(outerModel, Rotation.R90.get())
                        )
                        .register(
                                Direction.WEST, BlockHalf.TOP, StairShape.OUTER_RIGHT,
                                getUVLockedVariantY(outerModel, Rotation.R180.get())
                        )
                        .register(
                                Direction.WEST, BlockHalf.TOP, StairShape.STRAIGHT,
                                getUVLockedVariantY(straightModel, Rotation.R90.get())
                        )
                );
    }

    #if MC_VERSION < 12105
    protected static MultipartBlockStateSupplier getStandingCandlestickBlockstate(
            Block block,
            Identifier emptyModel,
            Identifier plainCandleModel,
            Identifier litPlainCandleModel,
            Map<CandleColor, Identifier> colouredCandleModels,
            Map<CandleColor, Identifier> litColouredCandleModels
    ) {
        BooleanProperty LIT = Properties.LIT;
        EnumProperty<CandleColor> CANDLE_COLOR = ModProperties.CANDLE_COLOR;

        MultipartBlockStateSupplier multipart = MultipartBlockStateSupplier.create(block)
                // NO CANDLE
                .with(
                        When.create().set(CANDLE_COLOR, CandleColor.NONE),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, emptyModel)
                )
                // PLAIN NON-LIT
                .with(
                        When.allOf(
                                When.create().set(CANDLE_COLOR, CandleColor.PLAIN),
                                When.create().set(LIT, false)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, plainCandleModel)
                )
                // PLAIN LIT
                .with(
                        When.allOf(
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
                                    When.create().set(CANDLE_COLOR, candleColor),
                                    When.create().set(LIT, false)
                            ),
                            BlockStateVariant.create()
                                    .put(VariantSettings.MODEL, colouredCandleModels.get(candleColor))
                    )
                    // LIT
                    .with(
                            When.allOf(
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
        #if MC_VERSION < 12104 DirectionProperty #else EnumProperty<Direction> #endif FACING = Properties.HORIZONTAL_FACING;
        BooleanProperty LIT = Properties.LIT;
        EnumProperty<CandleColor> CANDLE_COLOR = ModProperties.CANDLE_COLOR;

        MultipartBlockStateSupplier multipart = MultipartBlockStateSupplier.create(block)
                // NO CANDLE
                .with(
                        When.allOf(
                                When.create().set(CANDLE_COLOR, CandleColor.NONE),
                                When.create().set(FACING, Direction.NORTH)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, emptyModel)
                                .put(VariantSettings.Y, Rotation.R180.get())
                )
                .with(
                        When.allOf(
                                When.create().set(CANDLE_COLOR, CandleColor.NONE),
                                When.create().set(FACING, Direction.SOUTH)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, emptyModel)
                )
                .with(
                        When.allOf(
                                When.create().set(CANDLE_COLOR, CandleColor.NONE),
                                When.create().set(FACING, Direction.EAST)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, emptyModel)
                                .put(VariantSettings.Y, Rotation.R270.get())
                )
                .with(
                        When.allOf(
                                When.create().set(CANDLE_COLOR, CandleColor.NONE),
                                When.create().set(FACING, Direction.WEST)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, emptyModel)
                                .put(VariantSettings.Y, Rotation.R90.get())
                )
                // PLAIN NON LIT
                .with(
                        When.allOf(
                                When.create().set(CANDLE_COLOR, CandleColor.PLAIN),
                                When.create().set(LIT, false),
                                When.create().set(FACING, Direction.NORTH)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, plainCandleModel)
                                .put(VariantSettings.Y, Rotation.R180.get())
                )
                .with(
                        When.allOf(
                                When.create().set(CANDLE_COLOR, CandleColor.PLAIN),
                                When.create().set(LIT, false),
                                When.create().set(FACING, Direction.SOUTH)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, plainCandleModel)
                )
                .with(
                        When.allOf(
                                When.create().set(CANDLE_COLOR, CandleColor.PLAIN),
                                When.create().set(LIT, false),
                                When.create().set(FACING, Direction.EAST)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, plainCandleModel)
                                .put(VariantSettings.Y, Rotation.R270.get())
                )
                .with(
                        When.allOf(
                                When.create().set(CANDLE_COLOR, CandleColor.PLAIN),
                                When.create().set(LIT, false),
                                When.create().set(FACING, Direction.WEST)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, plainCandleModel)
                                .put(VariantSettings.Y, Rotation.R90.get())
                )
                // PLAIN LIT
                .with(
                        When.allOf(
                                When.create().set(CANDLE_COLOR, CandleColor.PLAIN),
                                When.create().set(LIT, true),
                                When.create().set(FACING, Direction.NORTH)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, litPlainCandleModel)
                                .put(VariantSettings.Y, Rotation.R180.get())
                )
                .with(
                        When.allOf(
                                When.create().set(CANDLE_COLOR, CandleColor.PLAIN),
                                When.create().set(LIT, true),
                                When.create().set(FACING, Direction.SOUTH)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, litPlainCandleModel)
                )
                .with(
                        When.allOf(
                                When.create().set(CANDLE_COLOR, CandleColor.PLAIN),
                                When.create().set(LIT, true),
                                When.create().set(FACING, Direction.EAST)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, litPlainCandleModel)
                                .put(VariantSettings.Y, Rotation.R270.get())
                )
                .with(
                        When.allOf(
                                When.create().set(CANDLE_COLOR, CandleColor.PLAIN),
                                When.create().set(LIT, true),
                                When.create().set(FACING, Direction.WEST)
                        ),
                        BlockStateVariant.create()
                                .put(VariantSettings.MODEL, litPlainCandleModel)
                                .put(VariantSettings.Y, Rotation.R90.get())
                );

        for (CandleColor candleColor : colouredCandleModels.keySet()) {
            multipart
                    // NON-LIT
                    .with(
                            When.allOf(
                                    When.create().set(CANDLE_COLOR, candleColor),
                                    When.create().set(LIT, false),
                                    When.create().set(FACING, Direction.NORTH)
                            ),
                            BlockStateVariant.create()
                                    .put(VariantSettings.MODEL, colouredCandleModels.get(candleColor))
                                    .put(VariantSettings.Y, Rotation.R180.get())
                    )
                    .with(
                            When.allOf(
                                    When.create().set(CANDLE_COLOR, candleColor),
                                    When.create().set(LIT, false),
                                    When.create().set(FACING, Direction.SOUTH)
                            ),
                            BlockStateVariant.create()
                                    .put(VariantSettings.MODEL, colouredCandleModels.get(candleColor))
                    )
                    .with(
                            When.allOf(
                                    When.create().set(CANDLE_COLOR, candleColor),
                                    When.create().set(LIT, false),
                                    When.create().set(FACING, Direction.EAST)
                            ),
                            BlockStateVariant.create()
                                    .put(VariantSettings.MODEL, colouredCandleModels.get(candleColor))
                                    .put(VariantSettings.Y, Rotation.R270.get())
                    )
                    .with(
                            When.allOf(
                                    When.create().set(CANDLE_COLOR, candleColor),
                                    When.create().set(LIT, false),
                                    When.create().set(FACING, Direction.WEST)
                            ),
                            BlockStateVariant.create()
                                    .put(VariantSettings.MODEL, colouredCandleModels.get(candleColor))
                                    .put(VariantSettings.Y, Rotation.R90.get())
                    )
                    // LIT
                    .with(
                            When.allOf(
                                    When.create().set(CANDLE_COLOR, candleColor),
                                    When.create().set(LIT, true),
                                    When.create().set(FACING, Direction.NORTH)
                            ),
                            BlockStateVariant.create()
                                    .put(VariantSettings.MODEL, litColouredCandleModels.get(candleColor))
                                    .put(VariantSettings.Y, Rotation.R180.get())
                            )
                    .with(
                            When.allOf(
                                    When.create().set(CANDLE_COLOR, candleColor),
                                    When.create().set(LIT, true),
                                    When.create().set(FACING, Direction.SOUTH)
                            ),
                            BlockStateVariant.create()
                                    .put(VariantSettings.MODEL, litColouredCandleModels.get(candleColor))
                    )
                    .with(
                            When.allOf(
                                    When.create().set(CANDLE_COLOR, candleColor),
                                    When.create().set(LIT, true),
                                    When.create().set(FACING, Direction.EAST)
                            ),
                            BlockStateVariant.create()
                                    .put(VariantSettings.MODEL, litColouredCandleModels.get(candleColor))
                                    .put(VariantSettings.Y, Rotation.R270.get())
                    )
                    .with(
                            When.allOf(
                                    When.create().set(CANDLE_COLOR, candleColor),
                                    When.create().set(LIT, true),
                                    When.create().set(FACING, Direction.WEST)
                            ),
                            BlockStateVariant.create()
                                    .put(VariantSettings.MODEL, litColouredCandleModels.get(candleColor))
                                    .put(VariantSettings.Y, Rotation.R90.get())
                    );
        }
        return multipart;
    }
    #else
    protected static MultipartBlockModelDefinitionCreator getStandingCandlestickBlockstate(
            Block block,
            Identifier emptyModel,
            Identifier plainCandleModel,
            Identifier litPlainCandleModel,
            Map<CandleColor, Identifier> colouredCandleModels,
            Map<CandleColor, Identifier> litColouredCandleModels
    ) {
        BooleanProperty LIT = Properties.LIT;
        EnumProperty<CandleColor> CANDLE_COLOR = ModProperties.CANDLE_COLOR;

        MultipartBlockModelDefinitionCreator multipart = MultipartBlockModelDefinitionCreator.create(block)
                // NO CANDLE
                .with(
                        new MultipartModelConditionBuilder().put(CANDLE_COLOR, CandleColor.NONE),
                        getVariant(emptyModel)
                )
                // PLAIN NON LIT
                .with(
                        new MultipartModelCombinedCondition(
                                MultipartModelCombinedCondition.LogicalOperator.AND,
                                List.of(
                                        new MultipartModelConditionBuilder().put(CANDLE_COLOR, CandleColor.PLAIN).build(),
                                        new MultipartModelConditionBuilder().put(LIT, false).build()
                                )
                        ),
                        getVariant(plainCandleModel)
                )
                // PLAIN LIT
                .with(
                        new MultipartModelCombinedCondition(
                                MultipartModelCombinedCondition.LogicalOperator.AND,
                                List.of(
                                        new MultipartModelConditionBuilder().put(CANDLE_COLOR, CandleColor.PLAIN).build(),
                                        new MultipartModelConditionBuilder().put(LIT, true).build()
                                )
                        ),
                        getVariant(litPlainCandleModel)
                );

        for (CandleColor candleColor : colouredCandleModels.keySet()) {
            multipart
                    // NON-LIT
                    .with(
                            new MultipartModelCombinedCondition(
                                    MultipartModelCombinedCondition.LogicalOperator.AND,
                                    List.of(
                                            new MultipartModelConditionBuilder().put(CANDLE_COLOR, candleColor).build(),
                                            new MultipartModelConditionBuilder().put(LIT, false).build()
                                    )
                            ),
                            getVariant(colouredCandleModels.get(candleColor))
                    )
                    // LIT
                    .with(
                            new MultipartModelCombinedCondition(
                                    MultipartModelCombinedCondition.LogicalOperator.AND,
                                    List.of(
                                            new MultipartModelConditionBuilder().put(CANDLE_COLOR, candleColor).build(),
                                            new MultipartModelConditionBuilder().put(LIT, true).build()
                                    )
                            ),
                            getVariant(litColouredCandleModels.get(candleColor))
                    );
        }

        return multipart;
    }
    protected static MultipartBlockModelDefinitionCreator getWallCandlestickBlockstate(
            Block block,
            Identifier emptyModel,
            Identifier plainCandleModel,
            Identifier litPlainCandleModel,
            Map<CandleColor, Identifier> colouredCandleModels,
            Map<CandleColor, Identifier> litColouredCandleModels
    ) {
        EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;
        BooleanProperty LIT = Properties.LIT;
        EnumProperty<CandleColor> CANDLE_COLOR = ModProperties.CANDLE_COLOR;

        MultipartBlockModelDefinitionCreator multipart = MultipartBlockModelDefinitionCreator.create(block)
                // NO CANDLE
                .with(
                        new MultipartModelCombinedCondition(
                                MultipartModelCombinedCondition.LogicalOperator.AND,
                                List.of(
                                        new MultipartModelConditionBuilder().put(CANDLE_COLOR, CandleColor.NONE).build(),
                                        new MultipartModelConditionBuilder().put(FACING, Direction.NORTH).build()
                                )
                        ),
                        getVariantY(emptyModel, AxisRotation.R180)
                )
                .with(
                        new MultipartModelCombinedCondition(
                                MultipartModelCombinedCondition.LogicalOperator.AND,
                                List.of(
                                        new MultipartModelConditionBuilder().put(CANDLE_COLOR, CandleColor.NONE).build(),
                                        new MultipartModelConditionBuilder().put(FACING, Direction.SOUTH).build()
                                )
                        ),
                        getVariant(emptyModel)
                )
                .with(
                        new MultipartModelCombinedCondition(
                                MultipartModelCombinedCondition.LogicalOperator.AND,
                                List.of(
                                        new MultipartModelConditionBuilder().put(CANDLE_COLOR, CandleColor.NONE).build(),
                                        new MultipartModelConditionBuilder().put(FACING, Direction.EAST).build()
                                )
                        ),
                        getVariantY(emptyModel, AxisRotation.R270)
                )
                .with(
                        new MultipartModelCombinedCondition(
                                MultipartModelCombinedCondition.LogicalOperator.AND,
                                List.of(
                                        new MultipartModelConditionBuilder().put(CANDLE_COLOR, CandleColor.NONE).build(),
                                        new MultipartModelConditionBuilder().put(FACING, Direction.WEST).build()
                                )
                        ),
                        getVariantY(emptyModel, AxisRotation.R90)
                )
                // PLAIN NON LIT
                .with(
                        new MultipartModelCombinedCondition(
                                MultipartModelCombinedCondition.LogicalOperator.AND,
                                List.of(
                                        new MultipartModelConditionBuilder().put(CANDLE_COLOR, CandleColor.PLAIN).build(),
                                        new MultipartModelConditionBuilder().put(LIT, false).build(),
                                        new MultipartModelConditionBuilder().put(FACING, Direction.NORTH).build()
                                )
                        ),
                        getVariantY(plainCandleModel, AxisRotation.R180)
                )
                .with(
                        new MultipartModelCombinedCondition(
                                MultipartModelCombinedCondition.LogicalOperator.AND,
                                List.of(
                                        new MultipartModelConditionBuilder().put(CANDLE_COLOR, CandleColor.PLAIN).build(),
                                        new MultipartModelConditionBuilder().put(LIT, false).build(),
                                        new MultipartModelConditionBuilder().put(FACING, Direction.SOUTH).build()
                                )
                        ),
                        getVariant(plainCandleModel)
                )
                .with(
                        new MultipartModelCombinedCondition(
                                MultipartModelCombinedCondition.LogicalOperator.AND,
                                List.of(
                                        new MultipartModelConditionBuilder().put(CANDLE_COLOR, CandleColor.PLAIN).build(),
                                        new MultipartModelConditionBuilder().put(LIT, false).build(),
                                        new MultipartModelConditionBuilder().put(FACING, Direction.EAST).build()
                                )
                        ),
                        getVariantY(plainCandleModel, AxisRotation.R270)
                )
                .with(
                        new MultipartModelCombinedCondition(
                                MultipartModelCombinedCondition.LogicalOperator.AND,
                                List.of(
                                        new MultipartModelConditionBuilder().put(CANDLE_COLOR, CandleColor.PLAIN).build(),
                                        new MultipartModelConditionBuilder().put(LIT, false).build(),
                                        new MultipartModelConditionBuilder().put(FACING, Direction.WEST).build()
                                )
                        ),
                        getVariantY(plainCandleModel, AxisRotation.R90)
                )
                // PLAIN LIT
                .with(
                        new MultipartModelCombinedCondition(
                                MultipartModelCombinedCondition.LogicalOperator.AND,
                                List.of(
                                        new MultipartModelConditionBuilder().put(CANDLE_COLOR, CandleColor.PLAIN).build(),
                                        new MultipartModelConditionBuilder().put(LIT, true).build(),
                                        new MultipartModelConditionBuilder().put(FACING, Direction.NORTH).build()
                                )
                        ),
                        getVariantY(litPlainCandleModel, AxisRotation.R180)
                )
                .with(
                        new MultipartModelCombinedCondition(
                                MultipartModelCombinedCondition.LogicalOperator.AND,
                                List.of(
                                        new MultipartModelConditionBuilder().put(CANDLE_COLOR, CandleColor.PLAIN).build(),
                                        new MultipartModelConditionBuilder().put(LIT, true).build(),
                                        new MultipartModelConditionBuilder().put(FACING, Direction.SOUTH).build()
                                )
                        ),
                        getVariant(litPlainCandleModel)
                )
                .with(
                        new MultipartModelCombinedCondition(
                                MultipartModelCombinedCondition.LogicalOperator.AND,
                                List.of(
                                        new MultipartModelConditionBuilder().put(CANDLE_COLOR, CandleColor.PLAIN).build(),
                                        new MultipartModelConditionBuilder().put(LIT, true).build(),
                                        new MultipartModelConditionBuilder().put(FACING, Direction.EAST).build()
                                )
                        ),
                        getVariantY(litPlainCandleModel, AxisRotation.R270)
                )
                .with(
                        new MultipartModelCombinedCondition(
                                MultipartModelCombinedCondition.LogicalOperator.AND,
                                List.of(
                                        new MultipartModelConditionBuilder().put(CANDLE_COLOR, CandleColor.PLAIN).build(),
                                        new MultipartModelConditionBuilder().put(LIT, true).build(),
                                        new MultipartModelConditionBuilder().put(FACING, Direction.WEST).build()
                                )
                        ),
                        getVariantY(litPlainCandleModel, AxisRotation.R90)
                );

        for (CandleColor candleColor : colouredCandleModels.keySet()) {
            multipart
                    // NON-LIT
                    .with(
                            new MultipartModelCombinedCondition(
                                    MultipartModelCombinedCondition.LogicalOperator.AND,
                                    List.of(
                                            new MultipartModelConditionBuilder().put(CANDLE_COLOR, candleColor).build(),
                                            new MultipartModelConditionBuilder().put(LIT, false).build(),
                                            new MultipartModelConditionBuilder().put(FACING, Direction.NORTH).build()
                                    )
                            ),
                            getVariantY(colouredCandleModels.get(candleColor), AxisRotation.R180)
                    )
                    .with(
                            new MultipartModelCombinedCondition(
                                    MultipartModelCombinedCondition.LogicalOperator.AND,
                                    List.of(
                                            new MultipartModelConditionBuilder().put(CANDLE_COLOR, candleColor).build(),
                                            new MultipartModelConditionBuilder().put(LIT, false).build(),
                                            new MultipartModelConditionBuilder().put(FACING, Direction.SOUTH).build()
                                    )
                            ),
                            getVariant(colouredCandleModels.get(candleColor))
                    )
                    .with(
                            new MultipartModelCombinedCondition(
                                    MultipartModelCombinedCondition.LogicalOperator.AND,
                                    List.of(
                                            new MultipartModelConditionBuilder().put(CANDLE_COLOR, candleColor).build(),
                                            new MultipartModelConditionBuilder().put(LIT, false).build(),
                                            new MultipartModelConditionBuilder().put(FACING, Direction.EAST).build()
                                    )
                            ),
                            getVariantY(colouredCandleModels.get(candleColor), AxisRotation.R270)
                    )
                    .with(
                            new MultipartModelCombinedCondition(
                                    MultipartModelCombinedCondition.LogicalOperator.AND,
                                    List.of(
                                            new MultipartModelConditionBuilder().put(CANDLE_COLOR, candleColor).build(),
                                            new MultipartModelConditionBuilder().put(LIT, false).build(),
                                            new MultipartModelConditionBuilder().put(FACING, Direction.WEST).build()
                                    )
                            ),
                            getVariantY(colouredCandleModels.get(candleColor), AxisRotation.R90)
                    )
                    // LIT
                    .with(
                            new MultipartModelCombinedCondition(
                                    MultipartModelCombinedCondition.LogicalOperator.AND,
                                    List.of(
                                            new MultipartModelConditionBuilder().put(CANDLE_COLOR, candleColor).build(),
                                            new MultipartModelConditionBuilder().put(LIT, true).build(),
                                            new MultipartModelConditionBuilder().put(FACING, Direction.NORTH).build()
                                    )
                            ),
                            getVariantY(litColouredCandleModels.get(candleColor), AxisRotation.R180)
                    )
                    .with(
                            new MultipartModelCombinedCondition(
                                    MultipartModelCombinedCondition.LogicalOperator.AND,
                                    List.of(
                                            new MultipartModelConditionBuilder().put(CANDLE_COLOR, candleColor).build(),
                                            new MultipartModelConditionBuilder().put(LIT, true).build(),
                                            new MultipartModelConditionBuilder().put(FACING, Direction.SOUTH).build()
                                    )
                            ),
                            getVariant(litColouredCandleModels.get(candleColor))
                    )
                    .with(
                            new MultipartModelCombinedCondition(
                                    MultipartModelCombinedCondition.LogicalOperator.AND,
                                    List.of(
                                            new MultipartModelConditionBuilder().put(CANDLE_COLOR, candleColor).build(),
                                            new MultipartModelConditionBuilder().put(LIT, true).build(),
                                            new MultipartModelConditionBuilder().put(FACING, Direction.EAST).build()
                                    )
                            ),
                            getVariantY(litColouredCandleModels.get(candleColor), AxisRotation.R270)
                    )
                    .with(
                            new MultipartModelCombinedCondition(
                                    MultipartModelCombinedCondition.LogicalOperator.AND,
                                    List.of(
                                            new MultipartModelConditionBuilder().put(CANDLE_COLOR, candleColor).build(),
                                            new MultipartModelConditionBuilder().put(LIT, true).build(),
                                            new MultipartModelConditionBuilder().put(FACING, Direction.WEST).build()
                                    )
                            ),
                            getVariantY(litColouredCandleModels.get(candleColor), AxisRotation.R90)
                    );
        }
        return multipart;
    }
    #endif

    protected static #if MC_VERSION < 12105 VariantsBlockStateSupplier #else VariantsBlockModelDefinitionCreator #endif getForcedCornerStairsBlockstate(Block block, Identifier modelId) {
        #if MC_VERSION < 12105
        return VariantsBlockStateSupplier.create(block)
                .coordinate(BlockStateVariantMap.create(Properties.HORIZONTAL_FACING, Properties.BLOCK_HALF)
        #else
        return VariantsBlockModelDefinitionCreator.of(block)
                .with(BlockStateVariantMap.models(Properties.HORIZONTAL_FACING, Properties.BLOCK_HALF)
        #endif
                        .register(
                                Direction.EAST, BlockHalf.BOTTOM,
                                getUVLockedVariantY(modelId, Rotation.R90.get())
                        )
                        .register(
                                Direction.EAST, BlockHalf.TOP,
                                getUVLockedUpsideDownVariantY(modelId, Rotation.R180.get())
                        )
                        .register(
                                Direction.NORTH, BlockHalf.BOTTOM,
                                getVariant(modelId)
                        )
                        .register(
                                Direction.NORTH, BlockHalf.TOP,
                                getUVLockedUpsideDownVariantY(modelId, Rotation.R90.get())
                        )
                        .register(
                                Direction.SOUTH, BlockHalf.BOTTOM,
                                getUVLockedVariantY(modelId, Rotation.R180.get())
                        )
                        .register(
                                Direction.SOUTH, BlockHalf.TOP,
                                getUVLockedUpsideDownVariantY(modelId, Rotation.R270.get())
                        )
                        .register(
                                Direction.WEST, BlockHalf.BOTTOM,
                                getUVLockedVariantY(modelId, Rotation.R270.get())
                        )
                        .register(
                                Direction.WEST, BlockHalf.TOP,
                                getUVLockedUpsideDownVariant(modelId)
                        )
                );
    }

    protected static #if MC_VERSION < 12105 VariantsBlockStateSupplier #else VariantsBlockModelDefinitionCreator #endif getDefaultBlockstate(Block block, Identifier modelId) {
        #if MC_VERSION < 12105
        return VariantsBlockStateSupplier.create(block, BlockStateVariant.create().put(VariantSettings.MODEL, modelId));
        #else
        return VariantsBlockModelDefinitionCreator.of(block, createWeightedVariant(new ModelVariant(modelId)));
        #endif
    }

    protected static #if MC_VERSION < 12105 VariantsBlockStateSupplier #else VariantsBlockModelDefinitionCreator #endif getCabinetBlockstate(Block cabinetBlock, Identifier cabinetModel, Identifier cabinetOpenModel) {
        #if MC_VERSION < 12105
        return VariantsBlockStateSupplier.create(cabinetBlock)
                .coordinate(BlockStateVariantMap.create(Properties.HORIZONTAL_FACING, Properties.OPEN)
        #else
        return VariantsBlockModelDefinitionCreator.of(cabinetBlock)
                .with(BlockStateVariantMap.models(Properties.HORIZONTAL_FACING, Properties.OPEN)
        #endif
                        .register(
                                Direction.NORTH, true,
                                getVariantY(cabinetOpenModel, Rotation.R180.get())
                        )
                        .register(
                                Direction.NORTH, false,
                                getVariantY(cabinetModel, Rotation.R180.get())
                        )
                        .register(
                                Direction.EAST, true,
                                getVariantY(cabinetOpenModel, Rotation.R270.get())
                        )
                        .register(
                                Direction.EAST, false,
                                getVariantY(cabinetModel, Rotation.R270.get())
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
                                getVariantY(cabinetOpenModel, Rotation.R90.get())
                        )
                        .register(
                                Direction.WEST, false,
                                getVariantY(cabinetModel, Rotation.R90.get())
                        )
                );
    }
    protected static #if MC_VERSION < 12105 VariantsBlockStateSupplier #else VariantsBlockModelDefinitionCreator #endif getFloorCabinetBlockstate(Block cabinetBlock, Identifier cabinetModel, Identifier cabinetOpenModel) {
        #if MC_VERSION < 12105
        return VariantsBlockStateSupplier.create(cabinetBlock)
                .coordinate(BlockStateVariantMap.create(Properties.BLOCK_HALF, Properties.HORIZONTAL_FACING, Properties.OPEN)
        #else
        return VariantsBlockModelDefinitionCreator.of(cabinetBlock)
                .with(BlockStateVariantMap.models(Properties.BLOCK_HALF, Properties.HORIZONTAL_FACING, Properties.OPEN)
        #endif
                        .register(
                                BlockHalf.BOTTOM, Direction.NORTH, true,
                                getVariantY(cabinetOpenModel, Rotation.R180.get())
                        )
                        .register(
                                BlockHalf.BOTTOM, Direction.NORTH, false,
                                getVariantY(cabinetModel, Rotation.R180.get())
                        )
                        .register(
                                BlockHalf.BOTTOM, Direction.EAST, true,
                                getVariantY(cabinetOpenModel, Rotation.R270.get())
                        )
                        .register(
                                BlockHalf.BOTTOM, Direction.EAST, false,
                                getVariantY(cabinetModel, Rotation.R270.get())
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
                                getVariantY(cabinetOpenModel, Rotation.R90.get())
                        )
                        .register(
                                BlockHalf.BOTTOM, Direction.WEST, false,
                                getVariantY(cabinetModel, Rotation.R90.get())
                        )
                        .register(
                                BlockHalf.TOP, Direction.NORTH, true,
                                getUpsideDownVariantY(cabinetOpenModel, Rotation.R180.get())
                        )
                        .register(
                                BlockHalf.TOP, Direction.NORTH, false,
                                getUpsideDownVariantY(cabinetModel, Rotation.R180.get())
                        )
                        .register(
                                BlockHalf.TOP, Direction.EAST, true,
                                getUpsideDownVariantY(cabinetOpenModel, Rotation.R270.get())
                        )
                        .register(
                                BlockHalf.TOP, Direction.EAST, false,
                                getUpsideDownVariantY(cabinetModel, Rotation.R270.get())
                        )
                        .register(
                                BlockHalf.TOP, Direction.SOUTH, true,
                                getUpsideDownVariant(cabinetOpenModel)
                        )
                        .register(
                                BlockHalf.TOP, Direction.SOUTH, false,
                                getUpsideDownVariant(cabinetModel)
                        )
                        .register(
                                BlockHalf.TOP, Direction.WEST, true,
                                getUpsideDownVariantY(cabinetOpenModel, Rotation.R90.get())
                        )
                        .register(
                                BlockHalf.TOP, Direction.WEST, false,
                                getUpsideDownVariantY(cabinetModel, Rotation.R90.get())
                        )
                );
    }


    #if MC_VERSION < 12105
    protected static BlockStateVariant getUVLockedUpsideDownVariantY(Identifier model, VariantSettings.Rotation rotationY) {
        return BlockStateVariant.create()
                .put(VariantSettings.MODEL, model)
                .put(VariantSettings.UVLOCK, true)
                .put(VariantSettings.X, Rotation.R180.get())
                .put(VariantSettings.Y, rotationY);
    }

    protected static BlockStateVariant getUVLockedUpsideDownVariant(Identifier model) {
        return BlockStateVariant.create()
                .put(VariantSettings.MODEL, model)
                .put(VariantSettings.UVLOCK, true)
                .put(VariantSettings.X, Rotation.R180.get());
    }
    protected static BlockStateVariant getUVLockedVariantY(Identifier model, VariantSettings.Rotation rotationY) {
        return BlockStateVariant.create()
                .put(VariantSettings.MODEL, model)
                .put(VariantSettings.UVLOCK, true)
                .put(VariantSettings.Y, rotationY);
    }

    protected static BlockStateVariant getUpsideDownVariantY(Identifier model, VariantSettings.Rotation rotationY) {
        return BlockStateVariant.create()
                .put(VariantSettings.MODEL, model)
                .put(VariantSettings.X, Rotation.R180.get())
                .put(VariantSettings.Y, rotationY);
    }

    protected static BlockStateVariant getUpsideDownVariant(Identifier model) {
        return BlockStateVariant.create()
                .put(VariantSettings.MODEL, model)
                .put(VariantSettings.X, Rotation.R180.get());
    }
    protected static BlockStateVariant getVariantY(Identifier model, VariantSettings.Rotation rotationY) {
        return BlockStateVariant.create()
                .put(VariantSettings.MODEL, model)
                .put(VariantSettings.Y, rotationY);
    }

    protected static BlockStateVariant getVariant(Identifier model) {
        return BlockStateVariant.create().put(VariantSettings.MODEL, model);
    }
    #else
    protected static WeightedVariant getUVLockedUpsideDownVariantY(Identifier model, AxisRotation rotationY) {
        return createWeightedVariant(new ModelVariant(model)
                .with(ModelVariantOperator.UV_LOCK.withValue(true))
                .with(ModelVariantOperator.ROTATION_X.withValue(AxisRotation.R180))
                .with(ModelVariantOperator.ROTATION_Y.withValue(rotationY))
        );
    }

    protected static WeightedVariant getUVLockedUpsideDownVariant(Identifier model) {
        return createWeightedVariant(new ModelVariant(model)
                .with(ModelVariantOperator.UV_LOCK.withValue(true))
                .with(ModelVariantOperator.ROTATION_X.withValue(AxisRotation.R180))
        );
    }
    protected static WeightedVariant getUVLockedVariantY(Identifier model, AxisRotation rotationY) {
        return createWeightedVariant(new ModelVariant(model)
                .with(ModelVariantOperator.UV_LOCK.withValue(true))
                .with(ModelVariantOperator.ROTATION_Y.withValue(rotationY))
        );
    }

    protected static WeightedVariant getUpsideDownVariantY(Identifier model, AxisRotation rotationY) {
        return createWeightedVariant(new ModelVariant(model)
                .with(ModelVariantOperator.ROTATION_X.withValue(AxisRotation.R180))
                .with(ModelVariantOperator.ROTATION_Y.withValue(rotationY))
        );
    }

    protected static WeightedVariant getUpsideDownVariant(Identifier model) {
        return createWeightedVariant(new ModelVariant(model)
                .with(ModelVariantOperator.ROTATION_X.withValue(AxisRotation.R180))
        );
    }
    protected static WeightedVariant getVariantY(Identifier model, AxisRotation rotationY) {
        return createWeightedVariant(new ModelVariant(model)
                .with(ModelVariantOperator.ROTATION_Y.withValue(rotationY))
        );
    }

    protected static WeightedVariant getVariant(Identifier model) {
        return createWeightedVariant(new ModelVariant(model));
    }
    #endif


    protected static Model getModel(String id) {
        return new Model(Optional.of(getId(id)), Optional.empty());
    }
    protected static Model getModel(Identifier id) {
        return new Model(Optional.of(id), Optional.empty());
    }


    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        for (Item item : ItemRegistry.GLOWING_POWDER_VARIANTS) {
            itemModelGenerator.register(item, Models.GENERATED);
        }
    }

    protected void registerItemModel(Item item, Identifier modelId) {
        #if MC_VERSION < 12104
        blockStateModelGenerator.registerParentedItemModel(item, modelId);
        #else
        blockStateModelGenerator.registerItemModel(item, modelId);
        #endif
    }

    protected static Identifier getBlockId(@Nullable SupportedMods mod, String name) {
        return getVMId(mod, "block/" + name);
    }


    #if MC_VERSION < 12105
    protected static enum Rotation {
        R0(VariantSettings.Rotation.R0),
        R90(VariantSettings.Rotation.R90),
        R180(VariantSettings.Rotation.R180),
        R270(VariantSettings.Rotation.R270);
        
        protected final VariantSettings.Rotation rotation;
        public VariantSettings.Rotation get() { return rotation; }
        
        Rotation(AxisRotation rotation) {
            this.rotation = rotation;
        }
    }
    #else
    protected enum Rotation {
        R0(AxisRotation.R0),
        R90(AxisRotation.R90),
        R180(AxisRotation.R180),
        R270(AxisRotation.R270);
        
        protected final AxisRotation rotation;
        public AxisRotation get() { return rotation; }
        
        Rotation(AxisRotation rotation) {
            this.rotation = rotation;
        }
    }
    #endif
}
