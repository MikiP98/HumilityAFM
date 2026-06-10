#if MC_VERSION >= 12111
package io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering;

import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.CabinetBlockEntity;
import net.minecraft.client.Minecraft;
#if MC_VERSION < 260200
import net.minecraft.client.renderer.LevelRenderer;
#endif
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CabinetBlockEntityRenderState extends BlockEntityRenderState {
    public final ItemStackRenderState itemRenderState = new ItemStackRenderState();
    public BlockState blockState;
    public boolean hasItem = false;
    public int light;
    public int overlay;

    public void extractSharedState(CabinetBlockEntity blockEntity) {
        extractSharedState(blockEntity, null);
    }
    public void extractSharedState(CabinetBlockEntity blockEntity, @Nullable Integer light) {
        Level level = blockEntity.getLevel();
        if (level == null) return;

        this.blockState = blockEntity.getBlockState();
        #if MC_VERSION < 260200
        this.light = light != null ? light : LevelRenderer #if MC_VERSION < 260000 .getLightColor #else .getLightCoords #endif (level, blockEntity.getBlockPos());
        #else
        this.light = light != null ? light : this.lightCoords;
        #endif
        this.overlay = OverlayTexture.NO_OVERLAY;

        ItemStack stack = blockEntity.getItem(0);
        this.hasItem = !stack.isEmpty();

        if (this.hasItem) {
            Minecraft.getInstance().getItemModelResolver().updateForTopItem(
                    this.itemRenderState,
                    stack,
                    ItemDisplayContext.FIXED,
                    level,
                    null,
                    0
            );
        } else {
            this.itemRenderState.clear();
        }
    }
}
#endif