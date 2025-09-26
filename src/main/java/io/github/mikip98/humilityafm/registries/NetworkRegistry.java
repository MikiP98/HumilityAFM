package io.github.mikip98.humilityafm.registries;

import io.github.mikip98.humilityafm.HumilityAFM;
import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.util.mod_support.SupportedMods;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public class NetworkRegistry {
    public static void registerPayload() {
        PayloadTypeRegistry.playS2C().register(ConfigSyncPayload.ID, ConfigSyncPayload.CODEC);
    }

    @Environment(EnvType.SERVER)
    public static void registerNetworkServerMessage() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            byte[] modSupport = new byte[SupportedMods.values().length - 1];
            int i = 0;
            for (SupportedMods mod : SupportedMods.values()) {
                if (mod == SupportedMods.SHIMMER) continue;
                modSupport[i] = (byte) ModConfig.modSupport.get(mod).ordinal();
                ++i;
            }
            ServerPlayNetworking.send(handler.player, new ConfigSyncPayload(
                    ModConfig.getEnableCandlestickBeta(),
                    ModConfig.getEnableColouredFeatureSetBeta(),
                    ModConfig.mosaicsAndTilesStrengthMultiplayer,
                    modSupport
            ));
        });
    }

    public record ConfigSyncPayload(
            boolean enableCandlestick,
            boolean enableColouredFeature,
            float mosaicsStrength,
            byte[] modSupport
    ) implements CustomPayload {

        public static final Id<ConfigSyncPayload> ID = new Id<>(HumilityAFM.getId("config_sync"));

        public static final PacketCodec<PacketByteBuf, ConfigSyncPayload> CODEC =
                PacketCodec.of(ConfigSyncPayload::write, ConfigSyncPayload::new);

        public ConfigSyncPayload(PacketByteBuf buf) {
            this(
                    buf.readBoolean(),
                    buf.readBoolean(),
                    buf.readFloat(),
                    buf.readByteArray()
            );
        }
        public void write(PacketByteBuf buf) {
            buf.writeBoolean(enableCandlestick);
            buf.writeBoolean(enableColouredFeature);
            buf.writeFloat(mosaicsStrength);
            buf.writeByteArray(modSupport);
        }

        @Override
        public Id<? extends CustomPayload> getId() { return ID; }
    }
}
