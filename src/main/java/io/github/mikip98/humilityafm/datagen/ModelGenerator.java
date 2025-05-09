package io.github.mikip98.humilityafm.datagen;

import io.github.mikip98.humilityafm.generators.CabinetBlockGenerator;
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
    protected static final Model CABINET_BLOCK_MODEL = new Model(
            Optional.of(getId("block/cabinet_block")),
            Optional.empty()
    );
    protected static final Model CABINET_BLOCK_OPEN = new Model(
            Optional.of(getId("block/cabinet_block_open")),
            Optional.empty()
    );


    public ModelGenerator(FabricDataOutput output) {
        super(output);
    }


    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerParentedItemModel(BlockRegistry.CABINET_BLOCK, getId("block/cabinet_block"));
        blockStateModelGenerator.registerParentedItemModel(BlockRegistry.ILLUMINATED_CABINET_BLOCK, getId("block/cabinet_block"));
        blockStateModelGenerator.blockStateCollector.accept(getCabinetBlockstate(BlockRegistry.CABINET_BLOCK, getId("block/cabinet_block"), getId("block/cabinet_block_open")));
        blockStateModelGenerator.blockStateCollector.accept(getCabinetBlockstate(BlockRegistry.ILLUMINATED_CABINET_BLOCK, getId("block/cabinet_block"), getId("block/cabinet_block_open")));

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
                blockStateModelGenerator.blockStateCollector.accept(getCabinetBlockstate(CabinetBlockGenerator.cabinetBlockVariants[i], coloredCabinet, coloredCabinetOpen));
                blockStateModelGenerator.blockStateCollector.accept(getCabinetBlockstate(CabinetBlockGenerator.illuminatedCabinetBlockVariants[i], coloredCabinet, coloredCabinetOpen));
                ++i;
            }
        }
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
