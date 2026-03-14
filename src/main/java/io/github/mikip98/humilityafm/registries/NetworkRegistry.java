package io.github.mikip98.humilityafm.registries;

#if MC_VERSION >= 12006
import io.github.mikip98.humilityafm.HumilityAFM;
#endif
import io.github.mikip98.humilityafm.config.ModConfig;
import io.github.mikip98.humilityafm.util.mod_support.SupportedMods;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
#if MC_VERSION < 12006
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
#else
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
#endif
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.PacketByteBuf;
#if MC_VERSION >= 12006
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
#else
import net.minecraft.util.Identifier;

import static io.github.mikip98.humilityafm.HumilityAFM.getId;
#endif

public class NetworkRegistry {
    #if MC_VERSION >= 12006
    public static void registerPayload() {
        PayloadTypeRegistry.playS2C().register(ConfigSyncPayload.ID, ConfigSyncPayload.CODEC);
    }
    #else
    public static final Identifier CONFIG_SYNC = getId("config_sync");
    #endif

    @Environment(EnvType.SERVER)
    public static void registerNetworkServerMessage() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            byte[] modSupport = new byte[SupportedMods.values().length - 1];
            int i = 0;
            for (SupportedMods mod : SupportedMods.values()) {
                if (mod == SupportedMods.SHIMMER) continue;
                modSupport[i] = (byte) ModConfig.modSupport.get(mod).ordinal();  // ModSupportState (AUTO, ON, OFF)
                ++i;
            }
            final ConfigSyncPayload payload = new ConfigSyncPayload(
                    ModConfig.getEnableCandlestickBeta(),
                    ModConfig.getEnableColouredFeatureSetBeta(),
                    ModConfig.mosaicsAndTilesStrengthMultiplayer,
                    modSupport
            );
            #if MC_VERSION < 12006
            sender.sendPacket(CONFIG_SYNC, payload.save());
            #else
            sender.sendPacket(payload);
            #endif
        });
    }

    public record ConfigSyncPayload(
            boolean enableCandlestick,
            boolean enableColouredFeature,
            float mosaicsStrength,
            byte[] modSupport
    ) #if MC_VERSION >= 12006 implements CustomPayload #endif {

        #if MC_VERSION >= 12006
        public static final Id<ConfigSyncPayload> ID = new Id<>(HumilityAFM.getId("config_sync"));

        public static final PacketCodec<PacketByteBuf, ConfigSyncPayload> CODEC =
                PacketCodec.of(ConfigSyncPayload::write, ConfigSyncPayload::new);
        #endif

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
        #if MC_VERSION < 12006
        public PacketByteBuf save() {
            PacketByteBuf buf = PacketByteBufs.create();
            write(buf);
            return buf;
        }
        #endif

        #if MC_VERSION >= 12006
        @Override
        public Id<? extends CustomPayload> getId() { return ID; }
        #endif
    }
}
