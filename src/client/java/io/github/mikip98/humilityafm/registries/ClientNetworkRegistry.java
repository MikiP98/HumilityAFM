package io.github.mikip98.humilityafm.registries;

import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.config.ModSupportState;
import io.github.mikip98.humilityafm.util.mod_support.SupportedMods;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

import static io.github.mikip98.humilityafm.HumilityAFM.LOGGER;

public class ClientNetworkRegistry {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(NetworkRegistry.CONFIG_SYNC, (client, handler, buf, responseSender) -> {
            boolean serverConfigEnableCandlestickBeta = buf.readBoolean();
            boolean serverConfigEnableColouredFeatureSetBeta = buf.readBoolean();
            float serverMosaicsAndTilesStrengthMultiplayer = buf.readFloat();
            Map<SupportedMods, ModSupportState> serverConfigModSupport = new EnumMap<>(SupportedMods.class);
            for (SupportedMods mod : SupportedMods.values()) {
                if (mod == SupportedMods.SHIMMER) continue;
                serverConfigModSupport.put(mod, ModSupportState.values()[buf.readByte()]);
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
            if (ModConfig.mosaicsAndTilesStrengthMultiplayer != serverMosaicsAndTilesStrengthMultiplayer)
                differences.add(
                        "Mosaics & Tiles Strength Multiplayer",
                        serverMosaicsAndTilesStrengthMultiplayer,
                        ModConfig.mosaicsAndTilesStrengthMultiplayer
                );
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
            }

            // Log the differences
            if (!differences.isEmpty()) {
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
        player.sendMessage(Text.literal(message));
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
