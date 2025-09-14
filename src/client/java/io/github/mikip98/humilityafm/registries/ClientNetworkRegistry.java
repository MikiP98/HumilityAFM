package io.github.mikip98.humilityafm.registries;

import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.config.ModSupport;
import io.github.mikip98.humilityafm.util.mod_support.SupportedMods;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

import static io.github.mikip98.humilityafm.HumilityAFM.LOGGER;

public class ClientNetworkRegistry {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(NetworkRegistry.CONFIG_SYNC, (client, handler, buf, responseSender) -> {
            boolean serverConfigEnableCandlestickBeta = buf.readBoolean();
            boolean serverConfigEnableColouredFeatureSetBeta  = buf.readBoolean();
            Map<SupportedMods, ModSupport> serverConfigModSupport = new EnumMap<>(SupportedMods.class);
            for (SupportedMods mod : SupportedMods.values()) {
                if (mod == SupportedMods.SHIMMER) continue;
                serverConfigModSupport.put(mod, ModSupport.values()[buf.readByte()]);
            }

            DiffList differences = new DiffList();
            if (ModConfig.getEnableCandlestickBeta() != serverConfigEnableCandlestickBeta)
                differences.add(
                        "Candlestick BETA",
                        serverConfigEnableCandlestickBeta,
                        ModConfig.getEnableCandlestickBeta()
                );
            if (ModConfig.getEnableCandlestickBeta() != serverConfigEnableColouredFeatureSetBeta)
                differences.add(
                        "Coloured Feature Set BETA",
                        serverConfigEnableColouredFeatureSetBeta,
                        ModConfig.getEnableColouredFeatureSetBeta()
                );
            for (SupportedMods mod : SupportedMods.values()) {
                ModSupport serverModSupportSetting = serverConfigModSupport.get(mod);
                ModSupport clientModSupportSetting = ModConfig.modSupport.get(mod);
                if (clientModSupportSetting != serverModSupportSetting)
                    differences.add(
                            "Support for mod '" + mod.modName + " (" + mod.modId + ")'",
                            serverModSupportSetting,
                            clientModSupportSetting
                    );
            }

            // Log the differences
            if (!differences.isEmpty()) {
                client.execute(() -> {
                    PlayerEntity player = client.player;
                    assert player != null;
                    LOGGER.error("Humility AFM mod detected server-client config mismatch!");
                    LOGGER.error("Your client config of Humility AFM differs from the server config!");
                    LOGGER.error("You should match the configs for optimal multiplayer experience");
                    LOGGER.error("Detected differences:");
                    differences.forEach((msg) -> LOGGER.error("  - {}", msg));
                    LOGGER.error("BE CAREFUL TO NOT BREAK YOUR SINGLE PLAYER WORLDS BY JOINING THEM WITH BLOCKS THAT WERE PREVIOUSLY ENABLED, NOW DISABLED!!!");
                });
            }
        });
    }
    static class DiffList extends ArrayList<String> {
        void add(String name, Enum<?> server, Enum<?> client) {
            add(name, server.name(), client.name());
        }
        void add(String name, boolean server, boolean client) {
            add(name, String.valueOf(server), String.valueOf(client));
        }
        void add(String name, String server, String client) {
            super.add(name + " -> server: " + server + " (yours: " + client + ")");
        }
    }
}
