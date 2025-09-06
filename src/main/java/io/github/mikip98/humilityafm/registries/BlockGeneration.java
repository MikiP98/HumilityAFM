package io.github.mikip98.humilityafm.registries;

import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.content.blocks.LightStripBlock;
import io.github.mikip98.humilityafm.content.blocks.cabinet.CabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.cabinet.FloorCabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.cabinet.FloorIlluminatedCabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.cabinet.IlluminatedCabinetBlock;
import io.github.mikip98.humilityafm.content.blocks.candlestick.Candlestick;
import io.github.mikip98.humilityafm.content.blocks.candlestick.FloorCandlestick;
import io.github.mikip98.humilityafm.content.blocks.candlestick.FloorRustableCandlestick;
import io.github.mikip98.humilityafm.content.blocks.candlestick.RustableCandlestick;
import io.github.mikip98.humilityafm.content.blocks.candlestick.logic.RustableCandlestickLogic;
import io.github.mikip98.humilityafm.content.blocks.jack_o_lanterns.JackOLanternRedStone;
import io.github.mikip98.humilityafm.content.blocks.stairs.InnerStairs;
import io.github.mikip98.humilityafm.content.blocks.stairs.OuterStairs;
import io.github.mikip98.humilityafm.util.generation_data.ActiveGenerationData;
import io.github.mikip98.humilityafm.util.generation_data.RawGenerationData;
import io.github.mikip98.humilityafm.util.generation_data.material_management.material.BlockMaterial;
import io.github.mikip98.humilityafm.util.generation_data.material_management.material.BlockStrength;
import io.github.mikip98.humilityafm.util.generation_data.material_management.material.MaterialType;
import io.github.mikip98.humilityafm.util.mod_support.SupportedMods;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.TorchBlock;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.BlockSoundGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static io.github.mikip98.humilityafm.registries.BlockRegistry.*;

public abstract class BlockGeneration {
    protected static CabinetBlockSet generateCabinetBlockSet() {
        final FabricBlockSettings fireproofCabinetSettings = CabinetBlock.defaultSettings;
        final FabricBlockSettings burnableCabinetSettings = FabricBlockSettings.copyOf(fireproofCabinetSettings).burnable();
        final FabricBlockSettings fireproofIlluminatedCabinetSettings = IlluminatedCabinetBlock.defaultSettings;
        final FabricBlockSettings burnableIlluminatedCabinetSettings = FabricBlockSettings.copyOf(fireproofIlluminatedCabinetSettings).burnable();

        List<Block> wallCabinetVariants = new ArrayList<>();
        List<Block> floorCabinetVariants = new ArrayList<>();
        List<Block> wallIlluminatedCabinetVariants = new ArrayList<>();
        List<Block> floorIlluminatedCabinetVariants = new ArrayList<>();

        for (BlockMaterial material : ActiveGenerationData.cabinetVariantMaterials) {
            MaterialType woodMaterialType = material.layers()[0].metadata().type();
            if (woodMaterialType == null)
                throw new IllegalStateException("Wood material layer for cabinets must have a valid material type");

            final boolean fireproof = woodMaterialType.isFireproof;
            final FabricBlockSettings normalSettingToUse = fireproof ? fireproofCabinetSettings : burnableCabinetSettings;
            final FabricBlockSettings illuminatedSettingToUse = fireproof ? fireproofIlluminatedCabinetSettings : burnableIlluminatedCabinetSettings;

            final String name = material.getSafeName();
            final Block cabinetBlock = BlockRegistry.register(new CabinetBlock(normalSettingToUse), "wall_cabinet_block_" + name);
            final Block floorCabinetBlock = register(new FloorCabinetBlock(normalSettingToUse), "cabinet_block_" + name);
            final Block illuminatedCabinetBlock = register(new IlluminatedCabinetBlock(illuminatedSettingToUse), "wall_illuminated_cabinet_block_" + name);
            final Block floorIlluminatedCabinetBlock = register(new FloorIlluminatedCabinetBlock(illuminatedSettingToUse), "illuminated_cabinet_block_" + name);

            wallCabinetVariants.add(cabinetBlock);
            floorCabinetVariants.add(floorCabinetBlock);
            wallIlluminatedCabinetVariants.add(illuminatedCabinetBlock);
            floorIlluminatedCabinetVariants.add(floorIlluminatedCabinetBlock);

            if (!fireproof) {
                registerFlammable(cabinetBlock, ModConfig.cabinetBlockBurnTime, ModConfig.cabinetBlockFireSpread);
                registerFlammable(floorCabinetBlock, ModConfig.cabinetBlockBurnTime, ModConfig.cabinetBlockFireSpread);
                registerFlammable(illuminatedCabinetBlock, ModConfig.cabinetBlockBurnTime, ModConfig.cabinetBlockFireSpread);
                registerFlammable(floorIlluminatedCabinetBlock, ModConfig.cabinetBlockBurnTime, ModConfig.cabinetBlockFireSpread);
            }
        }
        return new CabinetBlockSet(
                wallCabinetVariants.toArray(Block[]::new),
                floorCabinetVariants.toArray(Block[]::new),
                wallIlluminatedCabinetVariants.toArray(Block[]::new),
                floorIlluminatedCabinetVariants.toArray(Block[]::new)
        );
    }
    protected record CabinetBlockSet(Block[] wallCabinets, Block[] floorCabinets, Block[] wallIlluminatedCabinets, Block[] floorIlluminatedCabinets) {}


    protected static ForcedCornerStairsBlockSet generateForcedCornerStairsBlockSet() {
        FabricBlockSettings woodStairsBlockSettings = FabricBlockSettings.create()
                .strength(RawGenerationData.vanillaWoodHardness, RawGenerationData.vanillaWoodResistance)
                .requiresTool()
                .sounds(BlockSoundGroup.WOOD);
        FabricBlockSettings burnableWoodStairsBlockSettings = FabricBlockSettings.copyOf(woodStairsBlockSettings).burnable();
        BiFunction<Float, Float, FabricBlockSettings> stonyStairsBlockSettingsGenerator =
                (hardness, resistance) -> FabricBlockSettings.create()
                        .strength(hardness, resistance)
                        .requiresTool()
                        .sounds(BlockSoundGroup.STONE);  // TODO: Consider adding a custom sound group for stony materials

        List<Block> innerStairs = new ArrayList<>();
        List<Block> outerStairs = new ArrayList<>();

        for (BlockMaterial variantMaterial : ActiveGenerationData.forcedCornerStairsVariantMaterials) {
            if (variantMaterial.layers().length != 1) throw new IllegalStateException("Forced Corner Stairs Variant material must have exactly one layer");
            BlockMaterial.Layer variant = variantMaterial.layers()[0];
            BlockMaterial.Metadata materialMetadata = variant.metadata();
            MaterialType materialType = materialMetadata.type();
            if (materialType == null) throw new IllegalStateException("Material type cannot be null for variant: " + variant.name());
            String variantName = variant.name();

            FabricBlockSettings settingsToUse = switch (materialType) {
                case BURNABLE_WOOD -> burnableWoodStairsBlockSettings;
                case FIREPROOF_WOOD -> woodStairsBlockSettings;
                case STONY -> {
                    BlockStrength blockStrength = materialMetadata.blockStrength();
                    if (blockStrength == null) throw new IllegalStateException("Block strength cannot be null for stony material: " + variant.name());
                    yield stonyStairsBlockSettingsGenerator.apply(blockStrength.hardness(), blockStrength.resistance());
                }
                default -> throw new IllegalStateException("Unsupported material type: " + materialType);
            };
            SupportedMods sourceMod = materialMetadata.sourceMod();
            if (sourceMod != null) variantName = sourceMod.modId + "_" + variantName;

            innerStairs.add(registerWithItem(new InnerStairs(settingsToUse), "inner_stairs_" + variantName));
            outerStairs.add(registerWithItem(new OuterStairs(woodStairsBlockSettings), "outer_stairs_" + variantName));
        }
        return new ForcedCornerStairsBlockSet(innerStairs.toArray(Block[]::new), outerStairs.toArray(Block[]::new));
    }
    protected record ForcedCornerStairsBlockSet(Block[] innerStairs, Block[] outerStairs) {}


    protected static Block[] generateWoodenMosaicVariants() {
        final FabricBlockSettings fireproofWoodenMosaicSettings = FabricBlockSettings.create()
                .strength(
                        RawGenerationData.vanillaWoodHardness * ModConfig.mosaicsAndTilesStrengthMultiplayer,
                        RawGenerationData.vanillaWoodResistance * ModConfig.mosaicsAndTilesStrengthMultiplayer
                )
                .sounds(BlockSoundGroup.WOOD)
                .requiresTool();
        final FabricBlockSettings burnableWoodenMosaicSettings = FabricBlockSettings.copyOf(fireproofWoodenMosaicSettings).burnable();

        final byte burn = (byte) Math.round(RawGenerationData.vanillaWoodBurnTime * ModConfig.mosaicsAndTilesStrengthMultiplayer);
        final byte spread = (byte) Math.round(RawGenerationData.vanillaWoodSpreadSpeed / ModConfig.mosaicsAndTilesStrengthMultiplayer);

        List<Block> woodenMosaicVariants = new ArrayList<>();
        for (BlockMaterial variantMaterial : ActiveGenerationData.woodenMosaicVariantMaterials) {
            if (variantMaterial.layers().length != 2) throw new IllegalStateException("Wooden Mosaic Variant material must have exactly two layers");
            final String variantName = variantMaterial.getSafeName();

            final boolean isFirstWoodFireproof = variantMaterial.layers()[0].metadata().type() == MaterialType.FIREPROOF_WOOD;
            final boolean isSecondWoodFireproof = variantMaterial.layers()[1].metadata().type() == MaterialType.FIREPROOF_WOOD;

            Block block;
            if (isFirstWoodFireproof && isSecondWoodFireproof) {
                block = registerWithItem(new Block(fireproofWoodenMosaicSettings), "wooden_mosaic_" + variantName);
            } else {
                block = registerWithItem(new Block(burnableWoodenMosaicSettings), "wooden_mosaic_" + variantName);
                if (isFirstWoodFireproof || isSecondWoodFireproof) {
                    registerFlammable(block, burn * 4, spread / 4);
                } else {
                    registerFlammable(block, burn, spread);
                }
            }
            woodenMosaicVariants.add(block);
        }
        return woodenMosaicVariants.toArray(Block[]::new);
    }


    protected static Block[] generateTerracottaTilesVariants() {
        final FabricBlockSettings terracottaTilesSettings = FabricBlockSettings.create()
                .strength(
                        RawGenerationData.vanillaTerracottaHardness * ModConfig.mosaicsAndTilesStrengthMultiplayer,
                        RawGenerationData.vanillaTerracottaResistance * ModConfig.mosaicsAndTilesStrengthMultiplayer
                )
                .requiresTool();
        List<Block> terracottaTilesVariants = new ArrayList<>();
        for (BlockMaterial material : ActiveGenerationData.terracottaTilesMaterials) {
            terracottaTilesVariants.add(registerWithItem(
                    new Block(terracottaTilesSettings),
                    "terracotta_tiles_" + material.getSafeName()
            ));
        }
        return terracottaTilesVariants.toArray(Block[]::new);
    }


    protected static CandlestickBlockSet generateCandlestickBlockSet() {
        if (!ModConfig.getEnableCandlestickBeta()) {
            // If Candlesticks are disabled, return an empty set
            return new CandlestickBlockSet(null, null, null, null);
        } else {
            // Simple Candlesticks
            List<Block> simpleCandlestickWallVariants = new ArrayList<>();
            List<Block> simpleCandlestickFloorVariants = new ArrayList<>();
            for (BlockMaterial material : ActiveGenerationData.simpleCandlestickMaterials) {
                simpleCandlestickWallVariants.add(register(new Candlestick(), "candlestick_wall_" + material.getSafeName()));
                simpleCandlestickFloorVariants.add(register(new FloorCandlestick(), "candlestick_" + material.getSafeName()));
            }

            // Rustable Candlesticks
            int metalSetCount = ActiveGenerationData.rustingCandlestickMaterials.length;
            RustableCandlestick[][] rustableCandlestickWallVariants = new RustableCandlestick[metalSetCount][];
            FloorRustableCandlestick[][] rustableCandlestickFloorVariants = new FloorRustableCandlestick[metalSetCount][];
            for (int i = 0; i < metalSetCount; i++) {
                List<RustableCandlestick> metalSetWallVaraintList = new ArrayList<>();
                List<FloorRustableCandlestick> metalSetFloorVariantList = new ArrayList<>();
                for (BlockMaterial material : ActiveGenerationData.rustingCandlestickMaterials[i]) {
                    metalSetWallVaraintList.add((RustableCandlestick) register(new RustableCandlestick(), "candlestick_wall_" + material.getSafeName()));
                    metalSetFloorVariantList.add((FloorRustableCandlestick) register(new FloorRustableCandlestick(), "candlestick_" + material.getSafeName()));
                }
                rustableCandlestickWallVariants[i] = metalSetWallVaraintList.toArray(RustableCandlestick[]::new);
                rustableCandlestickFloorVariants[i] = metalSetFloorVariantList.toArray(FloorRustableCandlestick[]::new);
                fillRustStages(rustableCandlestickWallVariants[i]);
                fillRustStages(rustableCandlestickFloorVariants[i]);
            }

            return new CandlestickBlockSet(
                    simpleCandlestickWallVariants.toArray(Block[]::new),
                    simpleCandlestickFloorVariants.toArray(Block[]::new),
                    rustableCandlestickWallVariants,
                    rustableCandlestickFloorVariants
            );
        }
    }
    protected static void fillRustStages(RustableCandlestickLogic[] candlestickMetalSet) {
        int length = candlestickMetalSet.length;
        if (length >= 2) {
            candlestickMetalSet[0].setRustNextLevel(((Block) candlestickMetalSet[1]).getDefaultState());
            for (int i = 1; i < length - 1; i++) {
                candlestickMetalSet[i].setRustPreviousLevel(((Block) candlestickMetalSet[i - 1]).getDefaultState());
                candlestickMetalSet[i].setRustNextLevel(((Block) candlestickMetalSet[i + 1]).getDefaultState());
            }
            candlestickMetalSet[length - 1].setRustPreviousLevel(((Block) candlestickMetalSet[length - 2]).getDefaultState());
        }
    }
    protected record CandlestickBlockSet(
            Block[] simpleCandlestickWallVariants,
            Block[] simpleCandlestickFloorVariants,
            Block[][] rustableCandlestickWallVariants,
            Block[][] rustableCandlestickFloorVariants
    ) {}


    protected static ColouredFeatureBlockSet generateColouredFeatureBlockSet() {
        if (!ModConfig.getEnableColouredFeatureSetBeta()) {
            // If Coloured Feature Set is disabled, return an empty set
            return new ColouredFeatureBlockSet(
                    null,
                    null, null, null,
                    null, null, null
            );
        } else {
            final FabricBlockSettings weakTorchSettings = FabricBlockSettings.copyOf(Blocks.TORCH).luminance(7);
            final FabricBlockSettings normalTorchSettings = FabricBlockSettings.copyOf(Blocks.TORCH).luminance(11);
            final FabricBlockSettings strongTorchSettings = FabricBlockSettings.copyOf(Blocks.TORCH).luminance(15);
            final DefaultParticleType torchParticle = ParticleTypes.FLAME;

            List<Block> lightStripVariants = new ArrayList<>();
            List<Block> colouredTorchWeakVariants = new ArrayList<>();
            List<Block> colouredTorchVariants = new ArrayList<>();
            List<Block> colouredTorchStrongVariants = new ArrayList<>();
            List<Block> colouredJackOLanternsWeak = new ArrayList<>();
            List<Block> colouredJackOLanterns = new ArrayList<>();
            List<Block> colouredJackOLanternsStrong = new ArrayList<>();

            for (BlockMaterial material : ActiveGenerationData.colouredFeatureSetMaterials) {
                final String name = material.getSafeName();
                // Light Strip
                lightStripVariants.add(registerWithItem(new LightStripBlock(), "light_strip_" + name));
                // Coloured Torches
                colouredTorchWeakVariants.add(registerWithItem(new TorchBlock(weakTorchSettings, torchParticle), "coloured_torch_" + name + "_weak"));
                colouredTorchVariants.add(registerWithItem(new TorchBlock(normalTorchSettings, torchParticle), "coloured_torch_" + name));
                colouredTorchStrongVariants.add(registerWithItem(new TorchBlock(strongTorchSettings, torchParticle), "torch_strong" + name));
                // Coloured Jack O'Lanterns
                colouredJackOLanternsWeak.add(registerWithItem(new JackOLanternRedStone(), "coloured_weak_jack_o_lantern_" + name));
                colouredJackOLanterns.add(registerWithItem(new JackOLanternRedStone(), "coloured_jack_o_lantern_" + name));
                colouredJackOLanternsStrong.add(registerWithItem(new JackOLanternRedStone(), "coloured_strong_jack_o_lantern_" + name));
            }
            return new ColouredFeatureBlockSet(
                    lightStripVariants.toArray(Block[]::new),
                    colouredTorchWeakVariants.toArray(Block[]::new),
                    colouredTorchVariants.toArray(Block[]::new),
                    colouredTorchStrongVariants.toArray(Block[]::new),
                    colouredJackOLanternsWeak.toArray(Block[]::new),
                    colouredJackOLanterns.toArray(Block[]::new),
                    colouredJackOLanternsStrong.toArray(Block[]::new)
            );
        }
    }
    protected record ColouredFeatureBlockSet(
            Block[] lightStripVariants,
            Block[] colouredTorchWeakVariants,
            Block[] colouredTorchVariants,
            Block[] colouredTorchStrongVariants,
            Block[] colouredJackOLanternWeakVariants,
            Block[] colouredJackOLanternVariants,
            Block[] colouredJackOLanternStrongVariants
    ) {}



    // TODO: Try to think of system that can universally generate every block

//    protected static Block[] universalBlockGenerator(
//            BiFunction<Block, String, Block> registeringFunction,
//            String namePrefix,
//            Iterable<BlockMaterial> materials,
//            Function<AbstractBlock.Settings, Block> blockFunction,
//            Integer defaultBurnTime,
//            Integer defaultSpreadSpeed,
//            Map<MaterialType, FabricBlockSettings> materialType2BlockSettingsMap
//    ) {
//        if (materialType2BlockSettingsMap.containsKey(MaterialType.FIREPROOF_WOOD) && !materialType2BlockSettingsMap.containsKey(MaterialType.BURNABLE_WOOD)) {
//            materialType2BlockSettingsMap.put(
//                    MaterialType.BURNABLE_WOOD,
//                    FabricBlockSettings.copyOf(materialType2BlockSettingsMap.get(MaterialType.FIREPROOF_WOOD)).burnable()
//            );
//        }
//
//        List<Block> blocks = new ArrayList<>();
//        for (BlockMaterial material : materials) {
//            byte layerCount = (byte) material.layers().length;
//
//            byte fireproofLayerCount = 0;
//            byte stonyLayerCount = 0;
//            byte woodenLayerCount = 0;
//
//            for (BlockMaterial.Layer layer : material.layers()) {
//                if (layer.metadata().type() != null)
//                    switch (layer.metadata().type()) {
//                        case STONY -> {
//                            ++fireproofLayerCount;
//                            ++stonyLayerCount;
//                        }
//                        case FIREPROOF_WOOD -> {
//                            ++fireproofLayerCount;
//                            ++woodenLayerCount;
//                        }
//                        case BURNABLE_WOOD -> ++woodenLayerCount;
//                    }
//                else {
//                    --layerCount;
//                }
//                if (layer.metadata().type() != MaterialType.BURNABLE_WOOD) {
//                    ++fireproofLayerCount;
//                }
//            }
//
//            float stonyRatio = stonyLayerCount == 0 ? 0 : (float) layerCount / (float) stonyLayerCount;
//            float woodenRatio = woodenLayerCount == 0 ? 0 : (float) layerCount / (float) woodenLayerCount;
//
//            FabricBlockSettings settingToUse;
//            if (fireproofLayerCount != layerCount) {
//                settingToUse = materialType2BlockSettingsMap.get(MaterialType.BURNABLE_WOOD);
//            } else if (stonyRatio > woodenRatio) {
//                settingToUse = materialType2BlockSettingsMap.get(MaterialType.STONY);
//            } else {
//                settingToUse = materialType2BlockSettingsMap.get(MaterialType.FIREPROOF_WOOD);
//            }
//
//            if (settingToUse == null) throw new IllegalStateException("Settings to use is Null");
//
//            Block block = registeringFunction.apply(blockFunction.apply(settingToUse), namePrefix + material.getSafeName());
//            blocks.add(block);
//
//            if (fireproofLayerCount != layerCount) {
//                double burnMultiplayer = Math.pow((float) layerCount / (float) fireproofLayerCount, 2);
//                registerFlammable(block, (int) Math.round(defaultBurnTime * burnMultiplayer), (int) Math.round(defaultSpreadSpeed / burnMultiplayer));
//            }
//        }
//        return blocks.toArray(Block[]::new);
//    }
}
