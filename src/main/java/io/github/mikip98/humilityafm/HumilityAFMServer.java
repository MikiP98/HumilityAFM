package io.github.mikip98.humilityafm;

import io.github.mikip98.humilityafm.registries.NetworkRegistry;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.SERVER)
public class HumilityAFMServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        NetworkRegistry.registerNetworkServerMessage();
    }
}
