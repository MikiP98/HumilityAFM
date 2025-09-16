package io.github.mikip98.humilityafm;

import io.github.mikip98.humilityafm.registries.NetworkRegistry;
import net.fabricmc.api.DedicatedServerModInitializer;

public class HumilityAFMServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        NetworkRegistry.registerNetworkServerMessage();
    }
}
