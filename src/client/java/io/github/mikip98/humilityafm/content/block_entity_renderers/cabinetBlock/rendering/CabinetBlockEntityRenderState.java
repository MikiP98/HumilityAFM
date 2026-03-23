#if MC_VERSION >= 12111
package io.github.mikip98.humilityafm.content.block_entity_renderers.cabinetBlock.rendering;

import io.github.mikip98.humilityafm.content.blockentities.cabinetBlock.CabinetBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CabinetBlockEntityRenderState extends BlockEntityRenderState {
    public final ItemRenderState itemRenderState = new ItemRenderState();
    public BlockState blockState;
    public boolean hasItem = false;
    public int light;
    public int overlay;

    public void extractSharedState(CabinetBlockEntity blockEntity) {
        extractSharedState(blockEntity, null);
    }
    public void extractSharedState(CabinetBlockEntity blockEntity, Integer light) {
        World world = blockEntity.getWorld();
        if (world == null) return;

        this.blockState = blockEntity.getCachedState();
        this.light = light != null ? light : WorldRenderer.getLightmapCoordinates(world, blockEntity.getPos());
        this.overlay = OverlayTexture.DEFAULT_UV;

        ItemStack stack = blockEntity.getStack(0);
        this.hasItem = !stack.isEmpty();

        if (this.hasItem) {
            MinecraftClient.getInstance().getItemModelManager().update(
                    this.itemRenderState,
                    stack,
                    ItemDisplayContext.FIXED,
                    world,
                    null,
                    0
            );
        } else {
            this.itemRenderState.clear();
        }
    }
}
#endif