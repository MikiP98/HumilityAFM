package io.github.mikip98.humilityafm.registries;

import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.util.mod_support.SupportedMods;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import static io.github.mikip98.humilityafm.HumilityAFM.getId;

public class NetworkRegistry {
    public static final Identifier CONFIG_SYNC = getId("config_sync");

    @Environment(EnvType.SERVER)
    public static void registerNetworkServerMessage() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            PacketByteBuf buf = PacketByteBufs.create();

            buf.writeBoolean(ModConfig.getEnableCandlestickBeta());
            buf.writeBoolean(ModConfig.getEnableColouredFeatureSetBeta());
            buf.writeFloat(ModConfig.mosaicsAndTilesStrengthMultiplayer);
            for (SupportedMods mod : SupportedMods.values()) {
                if (mod == SupportedMods.SHIMMER) continue;
                buf.writeByte(ModConfig.modSupport.get(mod).ordinal());
            }

            sender.sendPacket(CONFIG_SYNC, buf);
        });
    }
}
