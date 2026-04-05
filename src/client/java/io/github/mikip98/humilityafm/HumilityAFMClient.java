package io.github.mikip98.humilityafm;

import io.github.mikip98.humilityafm.registries.*;
import net.fabricmc.api.ClientModInitializer;

public class HumilityAFMClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// ------------------------------------ REGISTRATION --------------------------------------
		// ............ Block Entity Renderers ............
		BlockEntityRendererRegistry.register();
		// ............ Resource Packs & Data Packs ............
		ResourcepackRegistry.register();
		// ............ Network ............
		ClientNetworkRegistry.register();
		// ............ Render Layers ............
		#if MC_VERSION < 260000
		RenderLayerRegistry.register();
		#endif
	}
}