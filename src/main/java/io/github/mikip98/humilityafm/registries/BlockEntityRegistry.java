package io.github.mikip98.humilityafm.registries;

#if POLYMER import eu.pb4.polymer.core.api.block.PolymerBlockUtils; #endif
import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.content.blockentities.LightStripBlockEntity;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.CabinetBlockEntity;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.FloorCabinetBlockEntity;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.FloorIlluminatedCabinetBlockEntity;
import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.IlluminatedCabinetBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Arrays;
import java.util.stream.Stream;

import static io.github.mikip98.humilityafm.HumilityAFM.getId;

public class BlockEntityRegistry {
    // Cabinet block entity
    public static BlockEntityType<CabinetBlockEntity> CABINET_BLOCK_ENTITY;
    public static BlockEntityType<IlluminatedCabinetBlockEntity> ILLUMINATED_CABINET_BLOCK_ENTITY;
    public static BlockEntityType<FloorCabinetBlockEntity> FLOOR_CABINET_BLOCK_ENTITY;
    public static BlockEntityType<FloorIlluminatedCabinetBlockEntity> FLOOR_ILLUMINATED_CABINET_BLOCK_ENTITY;
    // Light strip block entity
    public static BlockEntityType<LightStripBlockEntity> LIGHT_STRIP_BLOCK_ENTITY;

    #if POLYMER
    public static BlockEntityType<Polymer.FallbackBlockEntity> FALLBACK_BLOCK_ENTITY;
    #endif

    public static void register() {
        //Register cabinet block entity
        CABINET_BLOCK_ENTITY = register(
                "cabinet_block_entity",
                CabinetBlockEntity::new,
                concat(BlockRegistry.CABINET_BLOCK, BlockRegistry.WALL_CABINET_BLOCK_VARIANTS)
        );
        //Register illuminated cabinet block entity
        ILLUMINATED_CABINET_BLOCK_ENTITY = register(
                "illuminated_cabinet_block_entity",
                IlluminatedCabinetBlockEntity::new,
                concat(BlockRegistry.ILLUMINATED_CABINET_BLOCK, BlockRegistry.WALL_ILLUMINATED_CABINET_BLOCK_VARIANTS)
        );
        //Register floor cabinet block entity
        FLOOR_CABINET_BLOCK_ENTITY = register(
                "floor_cabinet_block_entity",
                FloorCabinetBlockEntity::new,
                concat(BlockRegistry.FLOOR_CABINET_BLOCK, BlockRegistry.FLOOR_CABINET_BLOCK_VARIANTS)
        );
        //Register floor illuminated cabinet block entity
        FLOOR_ILLUMINATED_CABINET_BLOCK_ENTITY = register(
                "floor_illuminated_cabinet_block_entity",
                FloorIlluminatedCabinetBlockEntity::new,
                concat(BlockRegistry.FLOOR_ILLUMINATED_CABINET_BLOCK, BlockRegistry.FLOOR_ILLUMINATED_CABINET_BLOCK_VARIANTS)
        );

        //Register LED block entity
        if (ModConfig.getEnableColouredFeatureSetBeta()) {
            LIGHT_STRIP_BLOCK_ENTITY = register(
                    "light_strip_block_entity",
                    LightStripBlockEntity::new,
                    BlockRegistry.LIGHT_STRIP_VARIANTS
            );
        }

        #if POLYMER
        FALLBACK_BLOCK_ENTITY = register(
                "polymer_fallback_block_entity",
                Polymer.FallbackBlockEntity::new,
                concat(BlockRegistry.WOODEN_MOSAIC_VARIANTS, BlockRegistry.TERRACOTTA_TILE_VARIANTS)
        );
        #endif
    }

    protected static Block[] concat(Block block, Block... blocks) {
        return Stream.concat(
                Stream.of(block),
                Arrays.stream(blocks)
        ).toArray(Block[]::new);
    }
    protected static Block[] concat(Block[] blocks1, Block... blocks2) {
        return Stream.concat(
                Arrays.stream(blocks1),
                Arrays.stream(blocks2)
        ).toArray(Block[]::new);
    }

    protected static <T extends BlockEntity> BlockEntityType<T> register(
            String name,
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks
    ) {
        final BlockEntityType<T> type = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE, getId(name),
                FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build()
        );
        #if POLYMER PolymerBlockUtils.registerBlockEntity(type); #endif
        return type;
    }
}
