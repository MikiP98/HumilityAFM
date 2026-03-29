package io.github.mikip98.humilityafm.util.wrappers;

import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;

public class PropertiesWrapper {
    public static final BooleanProperty LIT = #if MC_VERSION < 260000 Properties #else BlockStateProperties #endif.LIT;
    public static final BooleanProperty WATERLOGGED = #if MC_VERSION < 260000 Properties #else BlockStateProperties #endif.WATERLOGGED;
    public static final BooleanProperty OPEN = #if MC_VERSION < 260000 Properties #else BlockStateProperties #endif.OPEN;
    public static final EnumProperty<#if MC_VERSION < 260000 BlockHalf #else Half #endif> HALF = #if MC_VERSION < 260000 Properties.BLOCK_HALF #else BlockStateProperties.HALF #endif;
}
