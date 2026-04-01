package io.github.mikip98.humilityafm.datagen;

import io.github.mikip98.humilityafm.registries.BlockRegistry;
import io.github.mikip98.humilityafm.registries.ItemRegistry;
import io.github.mikip98.humilityafm.util.mod_support.SupportedMods;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
#if MC_VERSION < 12006
import net.fabricmc.fabric.api.resource.conditions.v1.DefaultResourceConditions;
#else
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.core.HolderLookup;
#endif
import net.minecraft.core.registries.BuiltInRegistries;
#if MC_VERSION < 12006
import net.minecraft.resources.ResourceLocation;
#else
import net.minecraft.resources.ResourceKey;
#endif
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;

#if MC_VERSION >= 12006
import java.util.concurrent.CompletableFuture;
#endif

public class BlockLootTableGenerator extends FabricBlockLootTableProvider {
    #if MC_VERSION < 12006
    public BlockLootTableGenerator(FabricDataOutput dataOutput) {
        super(dataOutput);
    }
    #else
    public BlockLootTableGenerator(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(dataOutput, registriesFuture);
    }
    #endif

    @Override
    public void generate() {}

    @Override
    #if MC_VERSION < 12006
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> exporter) {
    #elif MC_VERSION < 12101
    public void generate(HolderLookup.Provider registryLookup, BiConsumer<ResourceKey<LootTable>, LootTable.Builder> exporter) {
    #else
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> exporter) {
    #endif
        Exporter exp = new Exporter(exporter, this);

        // Cabinet blocks
        exp.addDrop(BlockRegistry.CABINET_BLOCK, ItemRegistry.CABINET_ITEM);  // Manual testing block
        exp.addDrop(BlockRegistry.ILLUMINATED_CABINET_BLOCK, ItemRegistry.ILLUMINATED_CABINET_ITEM);  // Manual testing block
        exp.addDrop(BlockRegistry.FLOOR_CABINET_BLOCK, ItemRegistry.CABINET_ITEM);  // Manual testing block
        exp.addDrop(BlockRegistry.FLOOR_ILLUMINATED_CABINET_BLOCK, ItemRegistry.ILLUMINATED_CABINET_ITEM);  // Manual testing block
        for (int i = 0; i < BlockRegistry.WALL_CABINET_BLOCK_VARIANTS.length; ++i) {
            Item cabinetItem = ItemRegistry.CABINET_ITEM_VARIANTS[i];
            Item illuminatedCabinetItem = ItemRegistry.ILLUMINATED_CABINET_ITEM_VARIANTS[i];

            exp.addDrop(BlockRegistry.WALL_CABINET_BLOCK_VARIANTS[i], cabinetItem);
            exp.addDrop(BlockRegistry.FLOOR_CABINET_BLOCK_VARIANTS[i], cabinetItem);

            exp.addDrop(BlockRegistry.WALL_ILLUMINATED_CABINET_BLOCK_VARIANTS[i], illuminatedCabinetItem);
            exp.addDrop(BlockRegistry.FLOOR_ILLUMINATED_CABINET_BLOCK_VARIANTS[i], illuminatedCabinetItem);
        }
        // Wooden Mosaics
        Arrays.stream(BlockRegistry.WOODEN_MOSAIC_VARIANTS).forEach(exp::addDrop);
        // Terracotta Tiles
        Arrays.stream(BlockRegistry.TERRACOTTA_TILE_VARIANTS).forEach(exp::addDrop);
        // Inner & Outer Stairs
        Arrays.stream(BlockRegistry.INNER_STAIRS_BLOCK_VARIANTS).forEach(exp::addDrop);
        Arrays.stream(BlockRegistry.OUTER_STAIRS_BLOCK_VARIANTS).forEach(exp::addDrop);
        // Jack o'Lanterns
        exp.addDrop(BlockRegistry.JACK_O_LANTERN_REDSTONE);
        exp.addDrop(BlockRegistry.JACK_O_LANTERN_SOUL);


        // CANDLESTICK BETA
        // Candlesticks
        for (int i = 0; i < ItemRegistry.CANDLESTICK_ITEM_VARIANTS.length; ++i) {
            Item item = ItemRegistry.CANDLESTICK_ITEM_VARIANTS[i];
            exp.addDrop(BlockRegistry.SIMPLE_CANDLESTICK_WALL_VARIANTS[i], item);
            exp.addDrop(BlockRegistry.SIMPLE_CANDLESTICK_FLOOR_VARIANTS[i], item);
        }
        for (int i = 0; i < ItemRegistry.RUSTABLE_CANDLESTICK_ITEM_VARIANTS.length; ++i) {
            Item[] itemSet = ItemRegistry.RUSTABLE_CANDLESTICK_ITEM_VARIANTS[i];
            Block[] wallSet = BlockRegistry.RUSTABLE_CANDLESTICK_WALL_VARIANTS[i];
            Block[] standingSet = BlockRegistry.RUSTABLE_CANDLESTICK_FLOOR_VARIANTS[i];
            for (int j = 0; j < itemSet.length; ++j) {
                exp.addDrop(wallSet[j], itemSet[j]);
                exp.addDrop(standingSet[j], itemSet[j]);
            }
        }


        // COLOURED FEATURE SET BETA
        // Light Strips
        Arrays.stream(BlockRegistry.LIGHT_STRIP_VARIANTS).forEach(exp::addDrop);
        // Coloured Torches
        Arrays.stream(BlockRegistry.COLOURED_TORCH_VARIANTS).forEach(exp::addDrop);
        // Coloured Jack o'Lanterns
        Arrays.stream(BlockRegistry.COLOURED_JACK_O_LANTERNS).forEach(exp::addDrop);
    }

    protected static class Exporter {
        #if MC_VERSION < 12006
        protected BiConsumer<ResourceLocation, LootTable.Builder> exporter;
        #else
        protected BiConsumer<ResourceKey<LootTable>, LootTable.Builder> exporter;
        #endif

        protected BlockLootTableGenerator parent;

        #if MC_VERSION < 12006
        public Exporter(BiConsumer<ResourceLocation, LootTable.Builder> exporter, BlockLootTableGenerator parent) {
        #else
        public Exporter(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> exporter, BlockLootTableGenerator parent) {
        #endif
            this.exporter = exporter;
            this.parent = parent;
        }

        protected void addDrop(Block block) {
            addDrop(block, block.asItem());
        }

        protected void addDrop(Block block, Item drop) {
            // Registries.BLOCK.getId -> BuiltInRegistries.BLOCK.getKey
            String block_name = BuiltInRegistries.BLOCK.getKey(block).getPath();

            Set<SupportedMods> mods = EnumSet.noneOf(SupportedMods.class);
            for (SupportedMods mod : SupportedMods.values()) {
                if (
                        block_name.contains("_" + mod.modId + "_") ||
                                block_name.startsWith(mod.modId + "_") ||
                                block_name.endsWith("_" + mod.modId)
                ) {
                    mods.add(mod);
                }
            }

            #if MC_VERSION < 12006
            ResourceLocation id = block.getLootTable();
            #elif MC_VERSION < 12104
            ResourceKey<LootTable> id = block.getLootTable();//.get();
            #else
            ResourceKey<LootTable> id = block.getLootTable().get();
            #endif

            // parent.drops -> parent.createSingleItemTable
            if (mods.isEmpty()) {
                exporter.accept(id, parent.createSingleItemTable(drop));
            } else {
                String[] modIds = mods.stream().map((mod) -> mod.modId).toArray(String[]::new);
                #if MC_VERSION < 12006
                BiConsumer<ResourceLocation, LootTable.Builder> conditionalExporter =
                        parent.withConditions(exporter, DefaultResourceConditions.allModsLoaded(modIds));
                #else
                BiConsumer<ResourceKey<LootTable>, LootTable.Builder> conditionalExporter =
                        parent.withConditions(exporter, ResourceConditions.allModsLoaded(modIds));
                #endif
                conditionalExporter.accept(id, parent.createSingleItemTable(drop));
            }
        }
    }
}
