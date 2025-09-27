package io.github.mikip98.humilityafm.registries;

import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.config.ModSupport;
import io.github.mikip98.humilityafm.util.mod_support.SupportedMods;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

import static io.github.mikip98.humilityafm.HumilityAFM.LOGGER;

public class ClientNetworkRegistry {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(NetworkRegistry.ConfigSyncPayload.ID, (payload, context) -> {
            Map<SupportedMods, ModSupport> serverConfigModSupport = new EnumMap<>(SupportedMods.class);
            int i = 0;
            for (SupportedMods mod : SupportedMods.values()) {
                if (mod == SupportedMods.SHIMMER) continue;
                serverConfigModSupport.put(mod, ModSupport.values()[payload.modSupport()[i]]);
                ++i;
            }

            DiffList differences = new DiffList();
            if (ModConfig.getEnableCandlestickBeta() != payload.enableCandlestick())
                differences.add(
                        "Candlestick BETA",
                        payload.enableCandlestick(),
                        ModConfig.getEnableCandlestickBeta()
                );
            if (ModConfig.getEnableCandlestickBeta() != payload.enableColouredFeature())
                differences.add(
                        "Coloured Feature Set BETA",
                        payload.enableColouredFeature(),
                        ModConfig.getEnableColouredFeatureSetBeta()
                );
            if (ModConfig.mosaicsAndTilesStrengthMultiplayer != payload.mosaicsStrength())
                differences.add(
                        "Mosaics & Tiles Strength Multiplayer",
                        payload.mosaicsStrength(),
                        ModConfig.mosaicsAndTilesStrengthMultiplayer
                );
            for (SupportedMods mod : SupportedMods.values()) {
                if (mod == SupportedMods.SHIMMER) continue;
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
                MinecraftClient client = context.client();
                client.execute(() -> {
                    printDiffs(LOGGER::error, differences, new String[]{"","",""});
                    if (ModConfig.printInChatServerClientMissmatch) {
                        PlayerEntity player = client.player;
                        assert player != null;
                        Consumer<String> message = (msg) -> sendMessage(player, msg);
                        printDiffs(message, differences, new String[]{"§e", "§7", "§c"});
                    }
                });
            }
        });
    }

    protected static void printDiffs(Consumer<String> printer, DiffList differences, String[] c) {
        printer.accept(c[0] + "Humility AFM mod detected server-client config mismatch!");
        printer.accept(c[0] + "Your client config of Humility AFM differs from the server config!");
        printer.accept(c[1] + "You should match the configs for optimal multiplayer experience");
        printer.accept(c[1] + "Detected differences:");
        differences.forEach((msg) -> printer.accept(c[1] + "  - " + msg));
        printer.accept(c[2] + "BE CAREFUL TO NOT BREAK YOUR SINGLE PLAYER WORLDS BY JOINING THEM WITH BLOCKS THAT WERE PREVIOUSLY ENABLED, NOW DISABLED!!!");
        printer.accept(c[1] + "As long as you can join the server, you can just ignore this message if you want");
    }

    protected static void sendMessage(PlayerEntity player, String message) {
        player.sendMessage(Text.literal(message), false);
    }

    protected static class DiffList extends ArrayList<String> {
        void add(String name, boolean server, boolean client) {
            add(name, String.valueOf(server), String.valueOf(client));
        }
        public void add(String name, float server, float client) {
            add(name, String.valueOf(server), String.valueOf(client));
        }
        void add(String name, Enum<?> server, Enum<?> client) {
            add(name, server.name(), client.name());
        }
        void add(String name, String server, String client) {
            super.add(name + " -> server: " + server + " (yours: " + client + ")");
        }

    }
}
