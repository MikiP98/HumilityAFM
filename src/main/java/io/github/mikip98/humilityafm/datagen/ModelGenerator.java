package io.github.mikip98.humilityafm.datagen;

import io.github.mikip98.humilityafm.generators.CabinetBlockGenerator;
import io.github.mikip98.humilityafm.helpers.TerracottaTilesHelper;
import io.github.mikip98.humilityafm.helpers.WoodenMosaicHelper;
import io.github.mikip98.humilityafm.registries.BlockRegistry;
import io.github.mikip98.humilityafm.util.GenerationData;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Optional;

import static io.github.mikip98.humilityafm.HumilityAFM.getId;

public class ModelGenerator extends FabricModelProvider {
    // Cabinet Models
    protected static final Model CABINET_BLOCK_MODEL = new Model(
            Optional.of(getId("block/cabinet_block")),
            Optional.empty()
    );
    protected static final Model CABINET_BLOCK_OPEN = new Model(
            Optional.of(getId("block/cabinet_block_open")),
            Optional.empty()
    );
    // Checker 2x2 Model
    protected static final Model CHECKER_2X2 = new Model(
            Optional.of(getId("block/checker_2x2")),
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

        // ............ FINAL BLOCKS & BLOCK ITEMS ............
        generateCabinetModelsAndBlockStates(blockStateModelGenerator);
        generateWoodenMosaicModelsAndBlockStates(blockStateModelGenerator);
        generateTerracottaTilesModelsAndBlockStates(blockStateModelGenerator);
    }

    protected static void generateWoodenMosaicModelsAndBlockStates(BlockStateModelGenerator blockStateModelGenerator) {
        int i = 0;
        for (String woodType : GenerationData.vanillaWoodTypes) {
            for (String woodType2 : GenerationData.vanillaWoodTypes) {
                if (woodType.equals(woodType2)) continue;

                final Identifier woodenMosaicModelId = CHECKER_2X2.upload(
                        getId("block/wooden_mosaic/wooden_mosaic_" + woodType + "_" + woodType2),
                        new TextureMap()
                                .register(TextureKey.of("1"), new Identifier("block/" + woodType + "_planks"))
                                .register(TextureKey.of("2"), new Identifier("block/" + woodType2 + "_planks"))
                                .register(TextureKey.PARTICLE, new Identifier("block/" + woodType + "_planks")),
                        blockStateModelGenerator.modelCollector
                );
                blockStateModelGenerator.registerParentedItemModel(WoodenMosaicHelper.woodenMosaicVariants[i], woodenMosaicModelId);
                blockStateModelGenerator.blockStateCollector.accept(getDefaultBlockstate(WoodenMosaicHelper.woodenMosaicVariants[i], woodenMosaicModelId));

                ++i;
            }
        }
    }
    protected static void generateTerracottaTilesModelsAndBlockStates(BlockStateModelGenerator blockStateModelGenerator) {
        int i = 0;
        for (String color : GenerationData.vanillaColorPallet) {
            for (String color2 : GenerationData.vanillaColorPallet) {
                if (color.equals(color2)) continue;

                final Identifier terracottaTileModelId = CHECKER_2X2.upload(
                        getId("block/terracotta_tiles/terracotta_tiles_" + color + "_" + color2),
                        new TextureMap()
                                .register(TextureKey.of("1"), new Identifier("block/" + color + "_terracotta"))
                                .register(TextureKey.of("2"), new Identifier("block/" + color2 + "_terracotta"))
                                .register(TextureKey.PARTICLE, new Identifier("block/" + color + "_terracotta")),
                        blockStateModelGenerator.modelCollector
                );
                blockStateModelGenerator.registerParentedItemModel(TerracottaTilesHelper.terracottaTilesVariants[i], terracottaTileModelId);
                blockStateModelGenerator.blockStateCollector.accept(getDefaultBlockstate(TerracottaTilesHelper.terracottaTilesVariants[i], terracottaTileModelId));

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

            final Identifier woodTypeCabinetOpenId = CABINET_BLOCK_OPEN.upload(
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


    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

    }
}
