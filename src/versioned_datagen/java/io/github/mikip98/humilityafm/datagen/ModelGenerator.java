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
import net.minecraft.core.Direction;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

import static io.github.mikip98.humilityafm.HumilityAFM.*;

public class ModelGenerator extends FabricModelProvider {
    // Cabinet Models
    protected static final ModelTemplate CABINET_BLOCK_MODEL = getModel("block/cabinet_block");
    protected static final ModelTemplate CABINET_BLOCK_OPEN_MODEL = getModel("block/cabinet_block_open");
    // Checker 2x2 Model
    protected static final ModelTemplate CHECKER_2X2_MODEL = getModel("block/checker_2x2");
    // Forced Corner Stairs Models
    protected static final ModelTemplate INNER_CORNER_STAIRS_MODEL = getModel("block/stairs_inner");
    protected static final ModelTemplate OUTER_CORNER_STAIRS_MODEL = getModel("block/stairs_outer");
    // Coloured Torch Model
    protected static final ModelTemplate TORCH_TEMPLATE_MODEL = getModel("block/torch_template");
    protected static final ModelTemplate TORCH_WALL_TEMPLATE_MODEL = getModel("block/torch_wall_template");
    // Candlestick Models
    protected static final ModelTemplate CANDLESTICK_STANDING_MODEL = getModel("block/candlestick");
    protected static final ModelTemplate CANDLESTICK_STANDING_WITH_CANDLE_MODEL = getModel("block/candlestick_candle");
    protected static final ModelTemplate CANDLESTICK_STANDING_WITH_CANDLE_LIT_MODEL = getModel("block/candlestick_candle_lit");
    protected static final ModelTemplate CANDLESTICK_WALL_MODEL = getModel("block/candlestick_wall");
    protected static final ModelTemplate CANDLESTICK_WALL_WITH_CANDLE_MODEL = getModel("block/candlestick_wall_candle");
    protected static final ModelTemplate CANDLESTICK_WALL_WITH_CANDLE_LIT_MODEL = getModel("block/candlestick_wall_candle_lit");
    // Light Strip Models
    protected static final ModelTemplate LIGHT_STRIP_STRAIGHT_MODEL = getModel("block/light_strip");
    protected static final ModelTemplate LIGHT_STRIP_INNER_MODEL = getModel("block/light_strip_inner");
    protected static final ModelTemplate LIGHT_STRIP_OUTER_MODEL = getModel("block/light_strip_outer");
    // Jack o'Lantern template model
    protected static final ModelTemplate JACK_O_LANTERN_TEMPLATE_MODEL = getModel("block/jack_o_lantern_template");


    protected BlockModelGenerators blockStateModelGenerator;


    public ModelGenerator(FabricDataOutput output) {
        super(output);
    }


    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        this.blockStateModelGenerator = blockStateModelGenerator;

        // ............ TEST BLOCKS & BLOCK ITEMS ............
        // Cabinet Blocks
        registerItemModel(ItemRegistry.CABINET_ITEM, getId("block/cabinet_block"));
        registerItemModel(ItemRegistry.ILLUMINATED_CABINET_ITEM, getId("block/cabinet_block"));
        blockStateModelGenerator.blockStateOutput.accept(getCabinetBlockstate(BlockRegistry.CABINET_BLOCK, getId("block/cabinet_block"), getId("block/cabinet_block_open")));
        blockStateModelGenerator.blockStateOutput.accept(getCabinetBlockstate(BlockRegistry.ILLUMINATED_CABINET_BLOCK, getId("block/cabinet_block"), getId("block/cabinet_block_open")));
        blockStateModelGenerator.blockStateOutput.accept(getFloorCabinetBlockstate(BlockRegistry.FLOOR_CABINET_BLOCK, getId("block/floor_cabinet_block"), getId("block/floor_cabinet_block_open")));
        blockStateModelGenerator.blockStateOutput.accept(getFloorCabinetBlockstate(BlockRegistry.FLOOR_ILLUMINATED_CABINET_BLOCK, getId("block/floor_cabinet_block"), getId("block/floor_cabinet_block_open")));

        // ............ FINAL BLOCKS & BLOCK ITEMS ............
        generateCabinetModelsAndBlockStates();
        generateWoodenMosaicModelsAndBlockStates();
        generateTerracottaTilesModelsAndBlockStates();
        generateForcedCornerStairsModelsAndBlockstates();
        // Optional blocks
        generateCandlestickModelsAndBlockStates();
        generateColouredTorchModelsAndBlockStates();
        generateLightStripModelsAndBlockStates();
        generateColouredJackOLanternModelsAndBlockStates();
    }

    protected void generateColouredJackOLanternModelsAndBlockStates() {
        int i = 0;
        for (String color : RawGenerationData.vanillaColorPallet) {
            final ResourceLocation colouredJackOLanternTexture = getId("block/coloured_jack_o_lantern/coloured_jack_o_lantern_" + color);
            final TextureMapping textureMap = new TextureMapping()
                    .putForced(TextureSlot.FRONT, colouredJackOLanternTexture);

            final ResourceLocation jackOLanternModelId = JACK_O_LANTERN_TEMPLATE_MODEL.create(
                    getId("block/coloured_jack_o_lantern/coloured_jack_o_lantern_" + color),
                    textureMap,
                    blockStateModelGenerator.modelOutput
            );
            blockStateModelGenerator.delegateItemModel(BlockRegistry.COLOURED_JACK_O_LANTERNS[i], jackOLanternModelId);
            blockStateModelGenerator.blockStateOutput.accept(getOrientableBlockState(
                    BlockRegistry.COLOURED_JACK_O_LANTERNS[i],
                    jackOLanternModelId
            ));
            ++i;
        }
    }

    protected static MultiVariantGenerator getOrientableBlockState(Block block, ResourceLocation modelId) {
        return MultiVariantGenerator.multiVariant(block)
                .with(PropertyDispatch.property(BlockStateProperties.HORIZONTAL_FACING)
                        .select(
                                Direction.NORTH,
                                getVariant(modelId)
                        )
                        .select(
                                Direction.SOUTH,
                                getUVLockedVariantY(modelId, Rotation.R180)
                        )
                        .select(
                                Direction.WEST,
                                getUVLockedVariantY(modelId, Rotation.R270)
                        )
                        .select(
                                Direction.EAST,
                                getUVLockedVariantY(modelId, Rotation.R90)
                        )
                );
    }
    protected static MultiVariantGenerator getTorchOrientableBlockState(Block block, ResourceLocation modelId) {
        return MultiVariantGenerator.multiVariant(block)
                .with(PropertyDispatch.property(BlockStateProperties.HORIZONTAL_FACING)
                        .select(
                                Direction.NORTH,
                                getVariantY(modelId, Rotation.R270)
                        )
                        .select(
                                Direction.SOUTH,
                                getVariantY(modelId, Rotation.R90)
                        )
                        .select(
                                Direction.WEST,
                                getVariantY(modelId, Rotation.R180)
                        )
                        .select(
                                Direction.EAST,
                                getVariant(modelId)
                        )
                );
    }

    protected void generateColouredTorchModelsAndBlockStates() {
        int i = 0;
        for (String color : RawGenerationData.vanillaColorPallet) {
            final ResourceLocation coloured_torch_texture = getId("block/coloured_torch/coloured_torch_" + color);
            final TextureMapping textureMap = new TextureMapping()
                    .putForced(TextureSlot.TORCH, coloured_torch_texture);

            final ResourceLocation torchModelId = TORCH_TEMPLATE_MODEL.create(
                    getId("block/coloured_torch/coloured_torch_" + color),
                    textureMap,
                    blockStateModelGenerator.modelOutput
            );
            final ResourceLocation wallTorchModelId = TORCH_WALL_TEMPLATE_MODEL.create(
                    getId("block/coloured_torch/coloured_torch_wall_" + color),
                    textureMap,
                    blockStateModelGenerator.modelOutput
            );

            registerItemModel(ItemRegistry.COLOURED_TORCH_ITEM_VARIANTS[i], torchModelId);
            blockStateModelGenerator.blockStateOutput.accept(getDefaultBlockstate(BlockRegistry.COLOURED_TORCH_VARIANTS[i], torchModelId));
            blockStateModelGenerator.blockStateOutput.accept(getTorchOrientableBlockState(BlockRegistry.COLOURED_WALL_TORCH_VARIANTS[i], wallTorchModelId));
            ++i;
        }
    }

    protected void generateLightStripModelsAndBlockStates() {
        int i = 0;
        for (BlockMaterial material : ActiveGenerationData.colouredFeatureSetMaterials) {
            final String color = material.layers()[0].name();

            final ResourceLocation coloured_concrete = getVanillaId("block/" + color + "_concrete");
            final TextureMapping textureMap = new TextureMapping()
                    .putForced(TextureSlot.create("0"), coloured_concrete);

            final String pathPrefix = "block/light_strip/";
            final ResourceLocation lightStripStraightModelId = LIGHT_STRIP_STRAIGHT_MODEL.create(
                    getId(pathPrefix + "straight/light_strip_" + material.getSafeName()),
                    textureMap,
                    blockStateModelGenerator.modelOutput
            );
            final ResourceLocation lightStripInnerModelId = LIGHT_STRIP_INNER_MODEL.create(
                    getId(pathPrefix + "inner/light_strip_inner_" + material.getSafeName()),
                    textureMap,
                    blockStateModelGenerator.modelOutput
            );
            final ResourceLocation lightStripOuterModelId = LIGHT_STRIP_OUTER_MODEL.create(
                    getId(pathPrefix + "outer/light_strip_outer_" + material.getSafeName()),
                    textureMap,
                    blockStateModelGenerator.modelOutput
            );
            blockStateModelGenerator.delegateItemModel(BlockRegistry.LIGHT_STRIP_VARIANTS[i], lightStripStraightModelId);
            blockStateModelGenerator.blockStateOutput.accept(getLightStripBlockstate(
                    BlockRegistry.LIGHT_STRIP_VARIANTS[i],
                    lightStripStraightModelId,
                    lightStripInnerModelId,
                    lightStripOuterModelId
            ));
            ++i;
        }
    }

    protected void generateCandlestickModelsAndBlockStates() {
        generateCandlestickModelsAndBlockstatesForMetals(
                ActiveGenerationData.simpleCandlestickMaterials,
                ItemRegistry.CANDLESTICK_ITEM_VARIANTS,
                BlockRegistry.SIMPLE_CANDLESTICK_WALL_VARIANTS,
                BlockRegistry.SIMPLE_CANDLESTICK_FLOOR_VARIANTS
        );
        for (int i = 0; i < ItemRegistry.RUSTABLE_CANDLESTICK_ITEM_VARIANTS.length; ++i) {
            generateCandlestickModelsAndBlockstatesForMetals(
                    ActiveGenerationData.rustingCandlestickMaterials[i],
                    ItemRegistry.RUSTABLE_CANDLESTICK_ITEM_VARIANTS[i],
                    BlockRegistry.RUSTABLE_CANDLESTICK_WALL_VARIANTS[i],
                    BlockRegistry.RUSTABLE_CANDLESTICK_FLOOR_VARIANTS[i]
            );
        }
    }
    protected void generateCandlestickModelsAndBlockstatesForMetals(
            Iterable<BlockMaterial> materials,
            Item[] items, Block[] wallBlocks, Block[] standingBlocks
    ) {
        int i = 0;
        final Set<String> block_suffix_metals = Set.of("copper", "gold", "iron");
        for (BlockMaterial material : materials) {
            final String metal = material.layers()[0].name();

            final String suffix = block_suffix_metals.contains(metal) ? "_block" : "";
            final TextureMapping metalTextureMapping = new TextureMapping()
                    .putForced(TextureSlot.create("metal"), getVanillaId("block/" + metal + suffix));

            final ResourceLocation candlestickStandingMetalModelId = CANDLESTICK_STANDING_MODEL.create(
                    getId("block/candlestick/standing/" + metal + "/candlestick_" + metal),
                    metalTextureMapping,
                    blockStateModelGenerator.modelOutput
            );
            final ResourceLocation candlestickWallMetalModelId = CANDLESTICK_WALL_MODEL.create(
                    getId("block/candlestick/wall/" + metal + "/candlestick_wall_" + metal),
                    metalTextureMapping,
                    blockStateModelGenerator.modelOutput
            );

            final ResourceLocation candlestickWithCandleStandingMetalModelId = CANDLESTICK_STANDING_WITH_CANDLE_MODEL.create(
                    getId("block/candlestick/standing/" + metal + "/candlestick_" + metal + "_candle"),
                    metalTextureMapping,
                    blockStateModelGenerator.modelOutput
            );
            final ResourceLocation candlestickWithCandleLitStandingMetalModelId = CANDLESTICK_STANDING_WITH_CANDLE_LIT_MODEL.create(
                    getId("block/candlestick/standing/" + metal + "/candlestick_" + metal + "_candle_lit"),
                    metalTextureMapping,
                    blockStateModelGenerator.modelOutput
            );
            final ResourceLocation candlestickWithCandleWallMetalModelId = CANDLESTICK_WALL_WITH_CANDLE_MODEL.create(
                    getId("block/candlestick/wall/" + metal + "/candlestick_wall_" + metal + "_candle"),
                    metalTextureMapping,
                    blockStateModelGenerator.modelOutput
            );
            final ResourceLocation candlestickWithCandleLitWallMetalModelId = CANDLESTICK_WALL_WITH_CANDLE_LIT_MODEL.create(
                    getId("block/candlestick/wall/" + metal + "/candlestick_wall_" + metal + "_candle_lit"),
                    metalTextureMapping,
                    blockStateModelGenerator.modelOutput
            );

            final ModelTemplate candlestickWithCandleStandingMetalModel = new ModelTemplate(
                    Optional.of(candlestickWithCandleStandingMetalModelId),
                    Optional.empty()
            );
            final ModelTemplate candlestickWithCandleLitStandingMetalModel = new ModelTemplate(
                    Optional.of(candlestickWithCandleLitStandingMetalModelId),
                    Optional.empty()
            );
            final ModelTemplate candlestickWithCandleWallMetalModel = new ModelTemplate(
                    Optional.of(candlestickWithCandleWallMetalModelId),
                    Optional.empty()
            );
            final ModelTemplate candlestickWithCandleLitWallMetalModel = new ModelTemplate(
                    Optional.of(candlestickWithCandleLitWallMetalModelId),
                    Optional.empty()
            );

            Map<CandleColor, ResourceLocation> wallCandleColorModelMap = new HashMap<>();
            Map<CandleColor, ResourceLocation> wallLitCandleColorModelMap = new HashMap<>();
            Map<CandleColor, ResourceLocation> standingCandleColorModelMap = new HashMap<>();
            Map<CandleColor, ResourceLocation> standingLitCandleColorModelMap = new HashMap<>();
            for (String color : RawGenerationData.vanillaColorPallet) {
                final CandleColor candleColor = CandleColor.getColor(color);
                final TextureMapping candleColorTextureMapping = new TextureMapping()
                        .putForced(TextureSlot.create("candle"), getVanillaId("block/" + color + "_candle_lit"));

                String id = "block/candlestick/standing/" + metal + "/candlestick_" + metal + "_" + color;
                final ResourceLocation standingCandlestickColoredModelId = candlestickWithCandleStandingMetalModel.create(
                        getId(id),
                        candleColorTextureMapping,
                        blockStateModelGenerator.modelOutput
                );
                standingCandleColorModelMap.put(candleColor, standingCandlestickColoredModelId);
                final ResourceLocation standingCandlestickLitColoredModelId = candlestickWithCandleLitStandingMetalModel.create(
                        getId(id + "_lit"),
                        candleColorTextureMapping,
                        blockStateModelGenerator.modelOutput
                );
                standingLitCandleColorModelMap.put(candleColor, standingCandlestickLitColoredModelId);

                id = "block/candlestick/wall/" + metal + "/candlestick_wall_" + metal + "_" + color;
                final ResourceLocation wallCandlestickColoredModelId = candlestickWithCandleWallMetalModel.create(
                        getId(id),
                        candleColorTextureMapping,
                        blockStateModelGenerator.modelOutput
                );
                wallCandleColorModelMap.put(candleColor, wallCandlestickColoredModelId);
                final ResourceLocation wallCandlestickLitColoredModelId = candlestickWithCandleLitWallMetalModel.create(
                        getId(id + "_lit"),
                        candleColorTextureMapping,
                        blockStateModelGenerator.modelOutput
                );
                wallLitCandleColorModelMap.put(candleColor, wallCandlestickLitColoredModelId);
            }

            registerItemModel(items[i], candlestickStandingMetalModelId);
            blockStateModelGenerator.blockStateOutput.accept(getWallCandlestickBlockstate(
                    wallBlocks[i],
                    candlestickWallMetalModelId,
                    candlestickWithCandleWallMetalModelId,
                    candlestickWithCandleLitWallMetalModelId,
                    wallCandleColorModelMap,
                    wallLitCandleColorModelMap
            ));
            blockStateModelGenerator.blockStateOutput.accept(getStandingCandlestickBlockstate(
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

    protected void generateForcedCornerStairsModelsAndBlockstates() {
        int i = 0;
        for (BlockMaterial stairMaterial : ActiveGenerationData.forcedCornerStairsVariantMaterials) {
            if (stairMaterial.layers().length != 1)
                throw new IllegalArgumentException("Forced corner stairs material must have exactly one materialLayer, but found: " + stairMaterial.layers().length);

            final BlockMaterial.Layer materialLayer = stairMaterial.layers()[0];

            final SupportedMods sourceMod = materialLayer.metadata().sourceMod();
            final Function<String, ResourceLocation> getBlockId = (name) -> getBlockId(sourceMod, name);

            final MaterialType materialType = materialLayer.metadata().type();
            if (materialType == null)
                throw new IllegalStateException("Material type is null for forced corner stairs material layer: " + materialLayer.name());

            ResourceLocation topTextureId;
            ResourceLocation sideTextureId;
            ResourceLocation bottomTextureId;

            String textureBlockName;
            switch (materialType) {
                case BURNABLE_WOOD, FIREPROOF_WOOD -> {
                    textureBlockName = materialLayer.name() + "_planks";
                    final ResourceLocation textureId = getBlockId.apply(textureBlockName);
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

            final ResourceLocation innerStairsModelId = INNER_CORNER_STAIRS_MODEL.create(
                    getId("block/corner_stairs/inner_stairs/inner_stairs_" + stairMaterial.getSafeName()),
                    new TextureMapping()
                            .putForced(TextureSlot.TOP, topTextureId)
                            .putForced(TextureSlot.BOTTOM, bottomTextureId)
                            .putForced(TextureSlot.SIDE, sideTextureId),
                    blockStateModelGenerator.modelOutput
            );
            final ResourceLocation outerStairsModelId = OUTER_CORNER_STAIRS_MODEL.create(
                    getId("block/corner_stairs/outer_stairs/outer_stairs_" + stairMaterial.getSafeName()),
                    new TextureMapping()
                            .putForced(TextureSlot.TOP, topTextureId)
                            .putForced(TextureSlot.BOTTOM, bottomTextureId)
                            .putForced(TextureSlot.SIDE, sideTextureId),
                    blockStateModelGenerator.modelOutput
            );

            blockStateModelGenerator.delegateItemModel(BlockRegistry.INNER_STAIRS_BLOCK_VARIANTS[i], innerStairsModelId);
            blockStateModelGenerator.delegateItemModel(BlockRegistry.OUTER_STAIRS_BLOCK_VARIANTS[i], outerStairsModelId);
            blockStateModelGenerator.blockStateOutput.accept(getForcedCornerStairsBlockstate(BlockRegistry.INNER_STAIRS_BLOCK_VARIANTS[i], innerStairsModelId));
            blockStateModelGenerator.blockStateOutput.accept(getForcedCornerStairsBlockstate(BlockRegistry.OUTER_STAIRS_BLOCK_VARIANTS[i], outerStairsModelId));

            ++i;
        }
    }

    protected void generateWoodenMosaicModelsAndBlockStates() {
        int i = 0;
        for (BlockMaterial variantMaterial : ActiveGenerationData.woodenMosaicVariantMaterials) {
            if (variantMaterial.layers().length != 2) {
                throw new IllegalArgumentException("Wooden mosaic material must have exactly two materialLayers, but found: " + variantMaterial.layers().length);
            }
            final BlockMaterial.Layer layer1 = variantMaterial.layers()[0];
            final BlockMaterial.Layer layer2 = variantMaterial.layers()[1];
            final SupportedMods mod1 = layer1.metadata().sourceMod();
            final SupportedMods mod2 = layer2.metadata().sourceMod();

            final ResourceLocation planks1Id = getBlockId(mod1, layer1.name() + "_planks");
            final ResourceLocation planks2Id = getBlockId(mod2, layer2.name() + "_planks");

            String modFolders = "";
            if (mod1 != null) modFolders += mod1.modId;
            if (mod1 != null && mod2 != null) modFolders += "_";
            if (mod2 != null) modFolders += mod2.modId;
            if (!modFolders.isEmpty()) modFolders += '/';

            // TODO: Add the modFolders to other blocks
            final ResourceLocation modelLocationId = getId("block/wooden_mosaic/" + modFolders + "wooden_mosaic_" + variantMaterial.getSafeName());

            final ResourceLocation woodenMosaicModelId = CHECKER_2X2_MODEL.create(
                    modelLocationId,
                    new TextureMapping()
                            .putForced(TextureSlot.create("1"), planks1Id)
                            .putForced(TextureSlot.create("2"), planks2Id),
                    blockStateModelGenerator.modelOutput
            );
            blockStateModelGenerator.delegateItemModel(BlockRegistry.WOODEN_MOSAIC_VARIANTS[i], woodenMosaicModelId);
            blockStateModelGenerator.blockStateOutput.accept(getDefaultBlockstate(BlockRegistry.WOODEN_MOSAIC_VARIANTS[i], woodenMosaicModelId));
            ++i;
        }
    }
    protected void generateTerracottaTilesModelsAndBlockStates() {
        int i = 0;
        for (String color : RawGenerationData.vanillaColorPallet) {
            for (String color2 : RawGenerationData.vanillaColorPallet) {
                if (color.equals(color2)) continue;

                final ResourceLocation terracottaTileModelId = CHECKER_2X2_MODEL.create(
                        getId("block/terracotta_tiles/terracotta_tiles_" + color + "_" + color2),
                        new TextureMapping()
                                .putForced(TextureSlot.create("1"), getVanillaId("block/" + color + "_terracotta"))
                                .putForced(TextureSlot.create("2"), getVanillaId("block/" + color2 + "_terracotta")),
                        blockStateModelGenerator.modelOutput
                );
                blockStateModelGenerator.delegateItemModel(BlockRegistry.TERRACOTTA_TILE_VARIANTS[i], terracottaTileModelId);
                blockStateModelGenerator.blockStateOutput.accept(getDefaultBlockstate(BlockRegistry.TERRACOTTA_TILE_VARIANTS[i], terracottaTileModelId));

                ++i;
            }
        }
    }

    protected void generateCabinetModelsAndBlockStates() {
        Map<Pair<SupportedMods, String>, CabinetModelWoodSet> mod2baseWoodSet = new HashMap<>();
        int i = 0;
        for (BlockMaterial material : ActiveGenerationData.cabinetVariantMaterials) {
            if (material.layers().length != 2) throw new IllegalStateException("Cabinet material must have exactly two materialLayers, but found: " + material.layers().length);
            final BlockMaterial.Layer woodLayer = material.layers()[0];
            final Pair<SupportedMods, String> baseWoodSetKey = Pair.of(woodLayer.metadata().sourceMod(), woodLayer.name());
            mod2baseWoodSet.computeIfAbsent(baseWoodSetKey, k -> generateCabinetWoodSet(woodLayer));
            final CabinetModelWoodSet baseWoodSet = mod2baseWoodSet.get(baseWoodSetKey);

            final BlockMaterial.Layer colorLayer = material.layers()[1];

            final TextureMapping woolTextureMapping = new TextureMapping().putForced(
                    TextureSlot.create("wool"),
                    getVanillaId("block/" + colorLayer.name() + "_wool")
            );

            final ResourceLocation coloredCabinet = baseWoodSet.wallClosed.create(
                    getCabinetCompleteModelId("wall", "closed", woodLayer, colorLayer),
                    woolTextureMapping,
                    blockStateModelGenerator.modelOutput
            );
            final ResourceLocation coloredCabinetOpen = baseWoodSet.wallOpen.create(
                    getCabinetCompleteModelId("wall", "open", woodLayer, colorLayer),
                    woolTextureMapping,
                    blockStateModelGenerator.modelOutput
            );

            registerItemModel(ItemRegistry.CABINET_ITEM_VARIANTS[i], coloredCabinet);
            registerItemModel(ItemRegistry.ILLUMINATED_CABINET_ITEM_VARIANTS[i], coloredCabinet);
            blockStateModelGenerator.blockStateOutput.accept(getCabinetBlockstate(BlockRegistry.WALL_CABINET_BLOCK_VARIANTS[i], coloredCabinet, coloredCabinetOpen));
            blockStateModelGenerator.blockStateOutput.accept(getCabinetBlockstate(BlockRegistry.WALL_ILLUMINATED_CABINET_BLOCK_VARIANTS[i], coloredCabinet, coloredCabinetOpen));
            blockStateModelGenerator.blockStateOutput.accept(getFloorCabinetBlockstate(BlockRegistry.FLOOR_CABINET_BLOCK_VARIANTS[i], coloredCabinet, coloredCabinetOpen));
            blockStateModelGenerator.blockStateOutput.accept(getFloorCabinetBlockstate(BlockRegistry.FLOOR_ILLUMINATED_CABINET_BLOCK_VARIANTS[i], coloredCabinet, coloredCabinetOpen));
            ++i;
        }
    }
    protected CabinetModelWoodSet generateCabinetWoodSet(BlockMaterial.Layer wood) {
        final SupportedMods sourceMod = wood.metadata().sourceMod();
        final TextureMapping plankTextureMapping = new TextureMapping().putForced(
                TextureSlot.create("planks"),
                getBlockId(sourceMod, wood.name() + "_planks")
        );

        final ResourceLocation woodTypeCabinetId = CABINET_BLOCK_MODEL.create(
                getCabinetBaseWoodModelId(sourceMod, "wall", "closed", wood.name()),
                plankTextureMapping,
                blockStateModelGenerator.modelOutput
        );

        final ResourceLocation woodTypeCabinetOpenId = CABINET_BLOCK_OPEN_MODEL.create(
                getCabinetBaseWoodModelId(sourceMod, "wall", "open", wood.name()),
                plankTextureMapping,
                blockStateModelGenerator.modelOutput
        );

        return new CabinetModelWoodSet(getModel(woodTypeCabinetId), getModel(woodTypeCabinetOpenId));
    }
    protected static ResourceLocation getCabinetBaseWoodModelId(SupportedMods sourceMod, String wallFloor, String openClosed, String woodName) {
        final String cabinetPath = "block/cabinet/" + (sourceMod != null ? sourceMod.modId : "vanilla") + "/" + wallFloor + "/" + openClosed;
        final String cabinetName = "cabinet_block_" + (sourceMod != null ? sourceMod.modId + "_" : "") + woodName;
        return getId(cabinetPath + "/" + cabinetName);
    }
    protected static ResourceLocation getCabinetCompleteModelId(String wallFloor, String openClosed, BlockMaterial.Layer woodLayer, BlockMaterial.Layer colorLayer) {
        final SupportedMods woodSourceMod = woodLayer.metadata().sourceMod();
        final String woodName = woodLayer.name();
        final String cabinetPath = "block/cabinet/" + (woodSourceMod != null ? woodSourceMod.modId : "vanilla") + "/" + wallFloor + "/" + openClosed + "/" + woodName;
        final String cabinetName = "cabinet_block_" + (woodSourceMod != null ? woodSourceMod.modId + "_" : "") + woodName + "_" + colorLayer.name();
        return getId(cabinetPath + "/" + cabinetName);
    }
    protected record CabinetModelWoodSet(
            ModelTemplate wallClosed, ModelTemplate wallOpen
    ) {}


    protected static MultiVariantGenerator getLightStripBlockstate(
            Block block,
            ResourceLocation straightModel,
            ResourceLocation innerModel,
            ResourceLocation outerModel
    ) {
        EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
        final EnumProperty<Half> HALF = BlockStateProperties.HALF;
        final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;

        #if MC_VERSION < 12105
        return MultiVariantGenerator.multiVariant(block)
                .with(PropertyDispatch.properties(FACING, HALF, SHAPE)
        #else
        return VariantsBlockModelDefinitionCreator.of(block)
                .with(BlockStateVariantMap.models(FACING, HALF, SHAPE)
        #endif
                        // East Bottom
                        .select(
                                Direction.EAST, Half.BOTTOM, StairsShape.INNER_LEFT,
                                getUVLockedUpsideDownVariant(innerModel)
                        )
                        .select(
                                Direction.EAST, Half.BOTTOM, StairsShape.INNER_RIGHT,
                                getUVLockedUpsideDownVariantY(innerModel, Rotation.R90)
                        )
                        .select(
                                Direction.EAST, Half.BOTTOM, StairsShape.OUTER_LEFT,
                                getUVLockedUpsideDownVariant(outerModel)
                        )
                        .select(
                                Direction.EAST, Half.BOTTOM, StairsShape.OUTER_RIGHT,
                                getUVLockedUpsideDownVariantY(outerModel, Rotation.R90)
                        )
                        .select(
                                Direction.EAST, Half.BOTTOM, StairsShape.STRAIGHT,
                                getUVLockedUpsideDownVariantY(straightModel, Rotation.R90)
                        )
                        // East Top
                        .select(
                                Direction.EAST, Half.TOP, StairsShape.INNER_LEFT,
                                getUVLockedVariantY(innerModel, Rotation.R270)
                        )
                        .select(
                                Direction.EAST, Half.TOP, StairsShape.INNER_RIGHT,
                                getVariant(innerModel)
                        )
                        .select(
                                Direction.EAST, Half.TOP, StairsShape.OUTER_LEFT,
                                getUVLockedVariantY(outerModel, Rotation.R270)
                        )
                        .select(
                                Direction.EAST, Half.TOP, StairsShape.OUTER_RIGHT,
                                getVariant(outerModel)
                        )
                        .select(
                                Direction.EAST, Half.TOP, StairsShape.STRAIGHT,
                                getUVLockedVariantY(straightModel, Rotation.R270)
                        )
                        // North Bottom
                        .select(
                                Direction.NORTH, Half.BOTTOM, StairsShape.INNER_LEFT,
                                getUVLockedUpsideDownVariantY(innerModel, Rotation.R270)
                        )
                        .select(
                                Direction.NORTH, Half.BOTTOM, StairsShape.INNER_RIGHT,
                                getUVLockedUpsideDownVariant(innerModel)
                        )
                        .select(
                                Direction.NORTH, Half.BOTTOM, StairsShape.OUTER_LEFT,
                                getUVLockedUpsideDownVariantY(outerModel, Rotation.R270)
                        )
                        .select(
                                Direction.NORTH, Half.BOTTOM, StairsShape.OUTER_RIGHT,
                                getUVLockedUpsideDownVariant(outerModel)
                        )
                        .select(
                                Direction.NORTH, Half.BOTTOM, StairsShape.STRAIGHT,
                                getUVLockedUpsideDownVariant(straightModel)
                        )
                        // North Top
                        .select(
                                Direction.NORTH, Half.TOP, StairsShape.INNER_LEFT,
                                getUVLockedVariantY(innerModel, Rotation.R180)
                        )
                        .select(
                                Direction.NORTH, Half.TOP, StairsShape.INNER_RIGHT,
                                getUVLockedVariantY(innerModel, Rotation.R270)
                        )
                        .select(
                                Direction.NORTH, Half.TOP, StairsShape.OUTER_LEFT,
                                getUVLockedVariantY(outerModel, Rotation.R180)
                        )
                        .select(
                                Direction.NORTH, Half.TOP, StairsShape.OUTER_RIGHT,
                                getUVLockedVariantY(outerModel, Rotation.R270)
                        )
                        .select(
                                Direction.NORTH, Half.TOP, StairsShape.STRAIGHT,
                                getUVLockedVariantY(straightModel, Rotation.R180)
                        )
                        // South Bottom
                        .select(
                                Direction.SOUTH, Half.BOTTOM, StairsShape.INNER_LEFT,
                                getUVLockedUpsideDownVariantY(innerModel, Rotation.R90)
                        )
                        .select(
                                Direction.SOUTH, Half.BOTTOM, StairsShape.INNER_RIGHT,
                                getUVLockedUpsideDownVariantY(innerModel, Rotation.R180)
                        )
                        .select(
                                Direction.SOUTH, Half.BOTTOM, StairsShape.OUTER_LEFT,
                                getUVLockedUpsideDownVariantY(outerModel, Rotation.R90)
                        )
                        .select(
                                Direction.SOUTH, Half.BOTTOM, StairsShape.OUTER_RIGHT,
                                getUVLockedUpsideDownVariantY(outerModel, Rotation.R180)
                        )
                        .select(
                                Direction.SOUTH, Half.BOTTOM, StairsShape.STRAIGHT,
                                getUVLockedUpsideDownVariantY(straightModel, Rotation.R180)
                        )
                        // South Top
                        .select(
                                Direction.SOUTH, Half.TOP, StairsShape.INNER_LEFT,
                                getVariant(innerModel)
                        )
                        .select(
                                Direction.SOUTH, Half.TOP, StairsShape.INNER_RIGHT,
                                getUVLockedVariantY(innerModel, Rotation.R90)
                        )
                        .select(
                                Direction.SOUTH, Half.TOP, StairsShape.OUTER_LEFT,
                                getVariant(outerModel)
                        )
                        .select(
                                Direction.SOUTH, Half.TOP, StairsShape.OUTER_RIGHT,
                                getUVLockedVariantY(outerModel, Rotation.R90)
                        )
                        .select(
                                Direction.SOUTH, Half.TOP, StairsShape.STRAIGHT,
                                getVariant(straightModel)
                        )
                        // West Bottom
                        .select(
                                Direction.WEST, Half.BOTTOM, StairsShape.INNER_LEFT,
                                getUVLockedUpsideDownVariantY(innerModel, Rotation.R180)
                        )
                        .select(
                                Direction.WEST, Half.BOTTOM, StairsShape.INNER_RIGHT,
                                getUVLockedUpsideDownVariantY(innerModel, Rotation.R270)
                        )
                        .select(
                                Direction.WEST, Half.BOTTOM, StairsShape.OUTER_LEFT,
                                getUVLockedUpsideDownVariantY(outerModel, Rotation.R180)
                        )
                        .select(
                                Direction.WEST, Half.BOTTOM, StairsShape.OUTER_RIGHT,
                                getUVLockedUpsideDownVariantY(outerModel, Rotation.R270)
                        )
                        .select(
                                Direction.WEST, Half.BOTTOM, StairsShape.STRAIGHT,
                                getUVLockedUpsideDownVariantY(straightModel, Rotation.R270)
                        )
                        // West Top
                        .select(
                                Direction.WEST, Half.TOP, StairsShape.INNER_LEFT,
                                getUVLockedVariantY(innerModel, Rotation.R90)
                        )
                        .select(
                                Direction.WEST, Half.TOP, StairsShape.INNER_RIGHT,
                                getUVLockedVariantY(innerModel, Rotation.R180)
                        )
                        .select(
                                Direction.WEST, Half.TOP, StairsShape.OUTER_LEFT,
                                getUVLockedVariantY(outerModel, Rotation.R90)
                        )
                        .select(
                                Direction.WEST, Half.TOP, StairsShape.OUTER_RIGHT,
                                getUVLockedVariantY(outerModel, Rotation.R180)
                        )
                        .select(
                                Direction.WEST, Half.TOP, StairsShape.STRAIGHT,
                                getUVLockedVariantY(straightModel, Rotation.R90)
                        )
                );
    }

    protected static MultiPartGenerator getStandingCandlestickBlockstate(
            Block block,
            ResourceLocation emptyModel,
            ResourceLocation plainCandleModel,
            ResourceLocation litPlainCandleModel,
            Map<CandleColor, ResourceLocation> colouredCandleModels,
            Map<CandleColor, ResourceLocation> litColouredCandleModels
    ) {
        final EnumProperty<CandleColor> CANDLE_COLOR = ModProperties.CANDLE_COLOR;

        MultiPartGenerator multipart = MultiPartGenerator.multiPart(block)
                // NO CANDLE
                .with(
                        Condition.condition().term(CANDLE_COLOR, CandleColor.NONE)#if MC_VERSION >= 12105 .build() #endif,
                        getVariant(emptyModel)
                )
                // PLAIN NON-LIT
                .with(
                        candlestickWhenState(CandleColor.PLAIN, false),
                        getVariant(plainCandleModel)
                )
                // PLAIN LIT
                .with(
                        candlestickWhenState(CandleColor.PLAIN, true),
                        getVariant(litPlainCandleModel)
                );

        for (CandleColor candleColor : colouredCandleModels.keySet()) {
            multipart
                    // NON-LIT
                    .with(
                            candlestickWhenState(candleColor, false),
                            getVariant(colouredCandleModels.get(candleColor))
                    )
                    // LIT
                    .with(
                            candlestickWhenState(candleColor, true),
                            getVariant(litColouredCandleModels.get(candleColor))
                    );
        }

        return multipart;
    }
    protected static Condition candlestickWhenState(CandleColor candleColor, boolean lit) {
        return Condition.condition()
                .term(ModProperties.CANDLE_COLOR, candleColor)
                .term(BlockStateProperties.LIT, lit)
                #if MC_VERSION >= 12105 .build() #endif;
    }

    protected static MultiPartGenerator getWallCandlestickBlockstate(
            Block block,
            ResourceLocation emptyModel,
            ResourceLocation plainCandleModel,
            ResourceLocation litPlainCandleModel,
            Map<CandleColor, ResourceLocation> colouredCandleModels,
            Map<CandleColor, ResourceLocation> litColouredCandleModels
    ) {
        MultiPartGenerator multipart = MultiPartGenerator.multiPart(block)
                // NO CANDLE
                .with(
                        wallCandlestickNoCandleWhenState(Direction.NORTH),
                        getVariantY(emptyModel, Rotation.R180)
                )
                .with(
                        wallCandlestickNoCandleWhenState(Direction.SOUTH),
                        getVariant(emptyModel)
                )
                .with(
                        wallCandlestickNoCandleWhenState(Direction.EAST),
                        getVariantY(emptyModel, Rotation.R270)
                )
                .with(
                        wallCandlestickNoCandleWhenState(Direction.WEST),
                        getVariantY(emptyModel, Rotation.R90)
                )
                // PLAIN NON LIT
                .with(
                        wallCandlestickWhenState(CandleColor.PLAIN, false, Direction.NORTH),
                        getVariantY(plainCandleModel, Rotation.R180)
                )
                .with(
                        wallCandlestickWhenState(CandleColor.PLAIN, false, Direction.SOUTH),
                        getVariant(plainCandleModel)
                )
                .with(
                        wallCandlestickWhenState(CandleColor.PLAIN, false, Direction.EAST),
                        getVariantY(plainCandleModel, Rotation.R270)
                )
                .with(
                        wallCandlestickWhenState(CandleColor.PLAIN, false, Direction.WEST),
                        getVariantY(plainCandleModel, Rotation.R90)
                )
                // PLAIN LIT
                .with(
                        wallCandlestickWhenState(CandleColor.PLAIN, true, Direction.NORTH),
                        getVariantY(litPlainCandleModel, Rotation.R180)
                )
                .with(
                        wallCandlestickWhenState(CandleColor.PLAIN, true, Direction.SOUTH),
                        getVariant(litPlainCandleModel)
                )
                .with(
                        wallCandlestickWhenState(CandleColor.PLAIN, true, Direction.EAST),
                        getVariantY(litPlainCandleModel, Rotation.R270)
                )
                .with(
                        wallCandlestickWhenState(CandleColor.PLAIN, true, Direction.WEST),
                        getVariantY(litPlainCandleModel, Rotation.R90)
                );

        for (CandleColor candleColor : colouredCandleModels.keySet()) {
            multipart
                    // NON-LIT
                    .with(
                            wallCandlestickWhenState(candleColor, false, Direction.NORTH),
                            getVariantY(colouredCandleModels.get(candleColor), Rotation.R180)
                    )
                    .with(
                            wallCandlestickWhenState(candleColor, false, Direction.SOUTH),
                            getVariant(colouredCandleModels.get(candleColor))
                    )
                    .with(
                            wallCandlestickWhenState(candleColor, false, Direction.EAST),
                            getVariantY(colouredCandleModels.get(candleColor), Rotation.R270)
                    )
                    .with(
                            wallCandlestickWhenState(candleColor, false, Direction.WEST),
                            getVariantY(colouredCandleModels.get(candleColor), Rotation.R90)
                    )
                    // LIT
                    .with(
                            wallCandlestickWhenState(candleColor, true, Direction.NORTH),
                            getVariantY(litColouredCandleModels.get(candleColor), Rotation.R180)
                    )
                    .with(
                            wallCandlestickWhenState(candleColor, true, Direction.SOUTH),
                            getVariant(litColouredCandleModels.get(candleColor))
                    )
                    .with(
                            wallCandlestickWhenState(candleColor, true, Direction.EAST),
                            getVariantY(litColouredCandleModels.get(candleColor), Rotation.R270)
                    )
                    .with(
                            wallCandlestickWhenState(candleColor, true, Direction.WEST),
                            getVariantY(litColouredCandleModels.get(candleColor), Rotation.R90)
                    );
        }
        return multipart;
    }
    protected static Condition wallCandlestickWhenState(CandleColor candleColor, boolean lit, Direction facing) {
        return Condition.condition()
                .term(ModProperties.CANDLE_COLOR, candleColor)
                .term(BlockStateProperties.LIT, lit)
                .term(BlockStateProperties.HORIZONTAL_FACING, facing)
                #if MC_VERSION >= 12105 .build() #endif;
    }
    protected static Condition wallCandlestickNoCandleWhenState(Direction facing) {
        return Condition.condition()
                .term(ModProperties.CANDLE_COLOR, CandleColor.NONE)
                .term(BlockStateProperties.HORIZONTAL_FACING, facing)
                #if MC_VERSION >= 12105 .build() #endif;
    }

    protected static MultiVariantGenerator getForcedCornerStairsBlockstate(Block block, ResourceLocation modelId) {
        return MultiVariantGenerator.multiVariant(block)
                .with(PropertyDispatch.properties(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.HALF)
                        .select(
                                Direction.EAST, Half.BOTTOM,
                                getUVLockedVariantY(modelId, Rotation.R90)
                        )
                        .select(
                                Direction.EAST, Half.TOP,
                                getUVLockedUpsideDownVariantY(modelId, Rotation.R180)
                        )
                        .select(
                                Direction.NORTH, Half.BOTTOM,
                                getVariant(modelId)
                        )
                        .select(
                                Direction.NORTH, Half.TOP,
                                getUVLockedUpsideDownVariantY(modelId, Rotation.R90)
                        )
                        .select(
                                Direction.SOUTH, Half.BOTTOM,
                                getUVLockedVariantY(modelId, Rotation.R180)
                        )
                        .select(
                                Direction.SOUTH, Half.TOP,
                                getUVLockedUpsideDownVariantY(modelId, Rotation.R270)
                        )
                        .select(
                                Direction.WEST, Half.BOTTOM,
                                getUVLockedVariantY(modelId, Rotation.R270)
                        )
                        .select(
                                Direction.WEST, Half.TOP,
                                getUVLockedUpsideDownVariant(modelId)
                        )
                );
    }

    protected static MultiVariantGenerator getDefaultBlockstate(Block block, ResourceLocation modelId) {
        #if MC_VERSION < 12105
        return MultiVariantGenerator.multiVariant(block, getVariant(modelId));
        #else
        return VariantsBlockModelDefinitionCreator.of(block, variant(new ModelVariant(modelId)));
        #endif
    }

    protected static MultiVariantGenerator getCabinetBlockstate(Block cabinetBlock, ResourceLocation cabinetModel, ResourceLocation cabinetOpenModel) {
        return MultiVariantGenerator.multiVariant(cabinetBlock)
                .with(PropertyDispatch.properties(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.OPEN)
                        .select(
                                Direction.NORTH, true,
                                getVariantY(cabinetOpenModel, Rotation.R180)
                        )
                        .select(
                                Direction.NORTH, false,
                                getVariantY(cabinetModel, Rotation.R180)
                        )
                        .select(
                                Direction.EAST, true,
                                getVariantY(cabinetOpenModel, Rotation.R270)
                        )
                        .select(
                                Direction.EAST, false,
                                getVariantY(cabinetModel, Rotation.R270)
                        )
                        .select(
                                Direction.SOUTH, true,
                                getVariant(cabinetOpenModel)
                        )
                        .select(
                                Direction.SOUTH, false,
                                getVariant(cabinetModel)
                        )
                        .select(
                                Direction.WEST, true,
                                getVariantY(cabinetOpenModel, Rotation.R90)
                        )
                        .select(
                                Direction.WEST, false,
                                getVariantY(cabinetModel, Rotation.R90)
                        )
                );
    }
    protected static MultiVariantGenerator getFloorCabinetBlockstate(Block cabinetBlock, ResourceLocation cabinetModel, ResourceLocation cabinetOpenModel) {
        return MultiVariantGenerator.multiVariant(cabinetBlock)
                .with(PropertyDispatch.properties(BlockStateProperties.HALF, BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.OPEN)
                        .select(
                                Half.BOTTOM, Direction.NORTH, true,
                                getVariant90XY(cabinetOpenModel, Rotation.R180)
                        )
                        .select(
                                Half.BOTTOM, Direction.NORTH, false,
                                getVariant90XY(cabinetModel, Rotation.R180)
                        )
                        .select(
                                Half.BOTTOM, Direction.EAST, true,
                                getVariant90XY(cabinetOpenModel, Rotation.R270)
                        )
                        .select(
                                Half.BOTTOM, Direction.EAST, false,
                                getVariant90XY(cabinetModel, Rotation.R270)
                        )
                        .select(
                                Half.BOTTOM, Direction.SOUTH, true,
                                getVariant90X(cabinetOpenModel)
                        )
                        .select(
                                Half.BOTTOM, Direction.SOUTH, false,
                                getVariant90X(cabinetModel)
                        )
                        .select(
                                Half.BOTTOM, Direction.WEST, true,
                                getVariant90XY(cabinetOpenModel, Rotation.R90)
                        )
                        .select(
                                Half.BOTTOM, Direction.WEST, false,
                                getVariant90XY(cabinetModel, Rotation.R90)
                        )
                        .select(
                                Half.TOP, Direction.NORTH, true,
                                getVariant270XY(cabinetOpenModel, Rotation.R180)
                        )
                        .select(
                                Half.TOP, Direction.NORTH, false,
                                getVariant270XY(cabinetModel, Rotation.R180)
                        )
                        .select(
                                Half.TOP, Direction.EAST, true,
                                getVariant270XY(cabinetOpenModel, Rotation.R270)
                        )
                        .select(
                                Half.TOP, Direction.EAST, false,
                                getVariant270XY(cabinetModel, Rotation.R270)
                        )
                        .select(
                                Half.TOP, Direction.SOUTH, true,
                                getVariant270X(cabinetOpenModel)
                        )
                        .select(
                                Half.TOP, Direction.SOUTH, false,
                                getVariant270X(cabinetModel)
                        )
                        .select(
                                Half.TOP, Direction.WEST, true,
                                getVariant270XY(cabinetOpenModel, Rotation.R90)
                        )
                        .select(
                                Half.TOP, Direction.WEST, false,
                                getVariant270XY(cabinetModel, Rotation.R90)
                        )
                );
    }


    #if MC_VERSION < 12105
    protected static Variant getUVLockedUpsideDownVariantY(ResourceLocation model, Rotation rotationY) {
        return getUVLockedUpsideDownVariant(model).with(VariantProperties.Y_ROT, rotationY.get());
    }
    protected static Variant getUVLockedUpsideDownVariant(ResourceLocation model) {
        return getUVLockedVariant(model).with(VariantProperties.X_ROT, Rotation.R180.get());
    }
    protected static Variant getUVLockedVariantY(ResourceLocation model, Rotation rotationY) {
        return getUVLockedVariant(model).with(VariantProperties.Y_ROT, rotationY.get());
    }
    protected static Variant getUVLockedVariant(ResourceLocation model) {
        return getVariant(model).with(VariantProperties.UV_LOCK, true);
    }

    protected static Variant getVariant90XY(ResourceLocation model, Rotation rotationY) {
        return getVariant90X(model).with(VariantProperties.Y_ROT, rotationY.get());
    }
    protected static Variant getVariant270XY(ResourceLocation model, Rotation rotationY) {
        return getVariant270X(model).with(VariantProperties.Y_ROT, rotationY.get());
    }

    protected static Variant getVariantY(ResourceLocation model, Rotation rotationY) {
        return getVariant(model).with(VariantProperties.Y_ROT, rotationY.get());
    }

    protected static Variant getVariant(ResourceLocation model) {
        return Variant.variant().with(VariantProperties.MODEL, model);
    }

    protected static Variant getVariant90X(ResourceLocation model) {
        return getVariantX(model, Rotation.R90);
    }
    protected static Variant getVariant270X(ResourceLocation model) {
        return getVariantX(model, Rotation.R270);
    }
    protected static Variant getVariantX(ResourceLocation model, Rotation rotation) {
        return getVariant(model).with(VariantProperties.X_ROT, rotation.get());
    }
    #else
    protected static MultiVariant getUVLockedUpsideDownVariantY(ResourceLocation model, Rotation rotationY) {
        return variant(new Variant(model)
                .with(ModelVariantOperator.UV_LOCK.withValue(true))
                .with(ModelVariantOperator.ROTATION_X.withValue(Rotation.R180.get()))
                .with(ModelVariantOperator.ROTATION_Y.withValue(rotationY))
        );
    }
    protected static MultiVariant getUVLockedUpsideDownVariant(ResourceLocation model) {
        return variant(new Variant(model)
                .with(ModelVariantOperator.UV_LOCK.withValue(true))
                .with(ModelVariantOperator.ROTATION_X.withValue(Rotation.R180.get()))
        );
    }

    protected static MultiVariant getUVLockedVariantY(ResourceLocation model, Rotation rotationY) {
        return variant(new Variant(model)
                .with(ModelVariantOperator.UV_LOCK.withValue(true))
                .with(ModelVariantOperator.ROTATION_Y.withValue(rotationY.get()))
        );
    }

    protected static MultiVariant getVariant90XY(ResourceLocation model, Rotation rotationY) {
        return variant(new Variant(model)
                .with(ModelVariantOperator.ROTATION_X.withValue(VariantProperties.Rotation.R90))
                .with(ModelVariantOperator.ROTATION_Y.withValue(rotationY.get()))
        );
    }
    protected static MultiVariant getVariant270XY(ResourceLocation model, Rotation rotationY) {
        return variant(new Variant(model)
                .with(ModelVariantOperator.ROTATION_X.withValue(VariantProperties.Rotation.R270))
                .with(ModelVariantOperator.ROTATION_Y.withValue(rotationY.get()))
        );
    }

    protected static MultiVariant getVariantY(ResourceLocation model, Rotation rotationY) {
        return variant(new Variant(model)
                .with(ModelVariantOperator.ROTATION_Y.withValue(rotationY.get()))
        );
    }

    protected static MultiVariant getVariant(ResourceLocation model) {
        return variant(new Variant(model));
    }

    protected static MultiVariant getVariant90X(ResourceLocation model) {
        return getVariantX(model, Rotation.R90);
    }
    protected static MultiVariant getVariant270X(ResourceLocation model) {
        return getVariantX(model, Rotation.R270);
    }
    protected static MultiVariant getVariantX(ResourceLocation model, Rotation rotation) {
        return variant(new Variant(model)
                .with(ModelVariantOperator.ROTATION_X.withValue(rotation.get()))
        );
    }
    #endif


    protected static ModelTemplate getModel(String id) {
        return new ModelTemplate(Optional.of(getId(id)), Optional.empty());
    }
    protected static ModelTemplate getModel(ResourceLocation id) {
        return new ModelTemplate(Optional.of(id), Optional.empty());
    }


    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        for (Item item : ItemRegistry.GLOWING_POWDER_VARIANTS) {
            itemModelGenerator.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
        }
    }

    protected void registerItemModel(Item item, ResourceLocation modelId) {
        blockStateModelGenerator.delegateItemModel(item, modelId);
    }

    protected static ResourceLocation getBlockId(@Nullable SupportedMods mod, String name) {
        return getVMId(mod, "block/" + name);
    }


    protected enum Rotation {
        #if MC_VERSION < 12105
        R0(VariantProperties.Rotation.R0),
        R90(VariantProperties.Rotation.R90),
        R180(VariantProperties.Rotation.R180),
        R270(VariantProperties.Rotation.R270);

        protected final VariantProperties.Rotation rotation;
        public VariantProperties.Rotation get() { return rotation; }
        Rotation(VariantProperties.Rotation rotation) { this.rotation = rotation; }
    
        #else
        R0(VariantMutator.Rotation.R0),
        R90(VariantMutator.Rotation.R90),
        R180(VariantMutator.Rotation.R180),
        R270(VariantMutator.Rotation.R270);
        
        protected final VariantMutator.Rotation rotation;
        public VariantMutator.Rotation get() { return rotation; }
        Rotation(VariantMutator.Rotation rotation) { this.rotation = rotation; }
        #endif
    }
}
