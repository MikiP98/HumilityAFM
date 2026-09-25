package io.github.mikip98.humilityafm;

import io.github.mikip98.humilityafm.registries.*;
import net.fabricmc.api.ClientModInitializer;

public class HumilityAFMClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockEntityRendererRegistry.register();
		ResourcepackRegistry.register();
		ClientNetworkRegistry.register();
		RenderLayerRegistry.register();
	}
}