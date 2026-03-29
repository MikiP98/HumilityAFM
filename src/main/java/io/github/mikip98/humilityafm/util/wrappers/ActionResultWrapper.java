package io.github.mikip98.humilityafm.util.wrappers;

#if MC_VERSION >= 260000
import net.minecraft.world.InteractionResult;
#endif

public class ActionResultWrapper {
    public static final #if MC_VERSION < 260000 ActionResult #else InteractionResult #endif SUCCESS =
        #if MC_VERSION < 260000 ActionResult.SUCCESS #else InteractionResult.SUCCESS #endif;

    public static final #if MC_VERSION < 260000 ActionResult #else InteractionResult #endif FAIL =
        #if MC_VERSION < 260000 ActionResult.FAIL #else InteractionResult.FAIL #endif;
}
