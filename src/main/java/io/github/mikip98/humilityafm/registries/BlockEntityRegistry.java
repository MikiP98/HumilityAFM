package io.github.mikip98.humilityafm.registries;

import io.github.mikip98.humilityafm.content.blockentities.LightStripBlockEntity;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.CabinetBlockEntity;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.FloorCabinetBlockEntity;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.FloorIlluminatedCabinetBlockEntity;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.IlluminatedCabinetBlockEntity;
import io.github.mikip98.humilityafm.generators.CabinetBlockGenerator;
import io.github.mikip98.humilityafm.generators.ColouredLightsGenerator;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.stream.Stream;

import static io.github.mikip98.humilityafm.HumilityAFM.MOD_ID;
import static io.github.mikip98.humilityafm.registries.BlockRegistry.CABINET_BLOCK;
import static io.github.mikip98.humilityafm.registries.BlockRegistry.ILLUMINATED_CABINET_BLOCK;

public class BlockEntityRegistry {
    //Cabinet block entity
    public static BlockEntityType<CabinetBlockEntity> CABINET_BLOCK_ENTITY;
    public static BlockEntityType<IlluminatedCabinetBlockEntity> ILLUMINATED_CABINET_BLOCK_ENTITY;
    public static BlockEntityType<FloorCabinetBlockEntity> FLOOR_CABINET_BLOCK_ENTITY;
    public static BlockEntityType<FloorIlluminatedCabinetBlockEntity> FLOOR_ILLUMINATED_CABINET_BLOCK_ENTITY;
    // LED block entity
    public static BlockEntityType<LightStripBlockEntity> LIGHT_STRIP_BLOCK_ENTITY;

    public static void register() {
        //Register cabinet block entity
        CABINET_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(MOD_ID, "cabinet_block_entity"),
                FabricBlockEntityTypeBuilder.create(
                        CabinetBlockEntity::new,
                        concat(CABINET_BLOCK, CabinetBlockGenerator.cabinetBlockVariants)
                ).build()
        );
        //Register illuminated cabinet block entity
        ILLUMINATED_CABINET_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(MOD_ID, "illuminated_cabinet_block_entity"),
                FabricBlockEntityTypeBuilder.create(
                        IlluminatedCabinetBlockEntity::new,
                        concat(ILLUMINATED_CABINET_BLOCK, CabinetBlockGenerator.illuminatedCabinetBlockVariants)
                ).build()
        );
        //Register floor cabinet block entity
        FLOOR_CABINET_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(MOD_ID, "floor_cabinet_block_entity"),
                FabricBlockEntityTypeBuilder.create(
                        FloorCabinetBlockEntity::new,
                        CabinetBlockGenerator.floorCabinetBlockVariants
                ).build()
        );
        //Register floor illuminated cabinet block entity
        FLOOR_ILLUMINATED_CABINET_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(MOD_ID, "floor_illuminated_cabinet_block_entity"),
                FabricBlockEntityTypeBuilder.create(
                        FloorIlluminatedCabinetBlockEntity::new,
                        CabinetBlockGenerator.floorIlluminatedCabinetBlockVariants
                ).build()
        );

        //Register LED block entity
        LIGHT_STRIP_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(MOD_ID, "light_strip_block_entity"),
                FabricBlockEntityTypeBuilder.create(LightStripBlockEntity::new, ColouredLightsGenerator.LightStripBlockVariants).build()
        );
    }

    protected static Block[] concat(Block block, Block[] blocks) {
        return Stream.concat(
                Stream.of(block),
                Arrays.stream(blocks)
        ).toArray(Block[]::new);
    }
}
