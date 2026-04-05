package io.github.mikip98.humilityafm.registries;

import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.config.enums.ModSupportState;
import io.github.mikip98.humilityafm.util.mod_support.SupportedMods;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
#if MC_VERSION >= 12006
import net.minecraft.client.Minecraft;
#endif
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

import static io.github.mikip98.humilityafm.HumilityAFM.LOGGER;

public class ClientNetworkRegistry {
    public static void register() {
        #if MC_VERSION >= 12006
        ClientPlayNetworking.registerGlobalReceiver(NetworkRegistry.ConfigSyncPayload.TYPE, (payload, context) -> {
        #else
        ClientPlayNetworking.registerGlobalReceiver(NetworkRegistry.CONFIG_SYNC, (client, handler, buf, responseSender) -> {
            final NetworkRegistry.ConfigSyncPayload payload = new NetworkRegistry.ConfigSyncPayload(buf);
            #endif

            Map<SupportedMods, ModSupportState> serverConfigModSupport = new EnumMap<>(SupportedMods.class);
            int i = 0;
            for (SupportedMods mod : SupportedMods.values()) {
                if (mod == SupportedMods.SHIMMER) continue;
                serverConfigModSupport.put(mod, ModSupportState.values()[payload.modSupport()[i]]);
                ++i;
            }

            DiffList differences = new DiffList();
            if (ModConfig.getEnableCandlestickBeta() != payload.enableCandlestick())
                differences.add(
                        "Candlestick BETA",
                        payload.enableCandlestick(),
                        ModConfig.getEnableCandlestickBeta()
                );
            if (ModConfig.getEnableColouredFeatureSetBeta() != payload.enableColouredFeature())
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
            i = 0;
            for (SupportedMods mod : SupportedMods.values()) {
                if (mod == SupportedMods.SHIMMER) continue;
                ModSupportState serverModSupportSetting = serverConfigModSupport.get(mod);
                ModSupportState clientModSupportSetting = ModConfig.modSupport.get(mod);
                if (clientModSupportSetting != serverModSupportSetting)
                    differences.add(
                            "Support for mod '" + mod.modName + " (" + mod.modId + ")'",
                            serverModSupportSetting,
                            clientModSupportSetting
                    );
                ++i;
            }

            // Log the differences
            if (!differences.isEmpty()) {
                #if MC_VERSION >= 12006
                Minecraft client = context.client();
                #endif
                client.execute(() -> {
                    printDiffs(LOGGER::error, differences, new String[]{"","",""});
                    if (ModConfig.printInChatServerClientMissmatch) {
                        Player player = client.player;
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

    protected static void sendMessage(Player player, String message) {
        #if MC_VERSION < 260000
        player.displayClientMessage(Component.literal(message), false);
        #else
        player.sendSystemMessage(Component.literal(message));
        #endif
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
