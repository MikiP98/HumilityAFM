#if POLYMER
package io.github.mikip98.humilityafm.mod_support.polymer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import io.github.mikip98.humilityafm.config.ModConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import static io.github.mikip98.humilityafm.HumilityAFM.*;

public class PolymerUtil {
    public static void init() {
        PolymerModelCache.init();
    }

    public static BlockState requestBlockState(String name) {
        return PolymerBlockResourceUtils.requestBlock(
                BlockModelType.FULL_BLOCK,
                getModelFromBlockstateVariant(name, null)
        );
    }

    public static Block createTexturedBlock(BlockBehaviour.Properties properties, Block disguise, String name) {
        final BlockState mappedState = ModConfig.polymerAllowOptimisedMosaicsAndTiles ? requestBlockState(name) : null;

        if (mappedState != null) {
            return new PolymerBlocks.PureTexturedBlock(properties, mappedState);
        } else {
            return new PolymerBlocks.FallbackTexturedBlock(properties, disguise);
        }
    }

    public static PolymerBlockModel getModelFromBlockstateVariant(String name, @Nullable BlockState state) {
        final JsonObject variants = getVariantsObjectFromBlockstate(name);

        for (Map.Entry<String, JsonElement> entry : variants.entrySet()) {
            final String variantKey = entry.getKey();

            if (variantKey.isEmpty() || (state != null && matchesState(state, variantKey))) {
                final JsonObject variantObj = entry.getValue().getAsJsonObject();

                #if MC_VERSION < 12111 ResourceLocation #else Identifier #endif modelId = getIdRaw(variantObj.get("model").getAsString());

                final int x = variantObj.has("x") ? variantObj.get("x").getAsInt() : 0;
                final int y = variantObj.has("y") ? variantObj.get("y").getAsInt() : 0;
                final boolean uvlock = variantObj.has("uvlock") && variantObj.get("uvlock").getAsBoolean();

                return PolymerBlockModel.of(modelId, x, y, uvlock, 1);
            }
        }
        throw new JsonSyntaxException("No matching variant found for state '" + state + "' in block '" + name + "'");
    }

    protected static boolean matchesState(BlockState state, String variantKey) {
        final String[] conditions = variantKey.split(",");
        for (String condition : conditions) {
            final String[] parts = condition.split("=");
            if (parts.length != 2) {
                throw new JsonSyntaxException(
                        "Invalid variant key '" + condition + "' in blockstate of block '" + state.getBlock() + "'"
                );
            }
            final String propertyName = parts[0];
            final String propertyValue = parts[1];

            boolean matched = false;
            for (Property<?> property : state.getProperties()) {
                if (property.getName().equals(propertyName)) {
                    if (state.getValue(property).toString().toLowerCase().equals(propertyValue)) {
                        matched = true;
                    }
                    break;
                }
            }
            if (!matched) return false;
        }
        return true;
    }

    public static #if MC_VERSION < 12111 ResourceLocation #else Identifier #endif getRawModelIdFromBlockstateVariant(String name, String variantString) {
        final JsonObject variants = getVariantsObjectFromBlockstate(name);
        if (variants.has(variantString)) {
            return getIdRaw(variants.getAsJsonObject(variantString).get("model").getAsString());
        }
        throw new IllegalStateException("Could not find variant '" + variantString + "' in '" + name + "'");
    }

    protected static JsonObject getVariantsObjectFromBlockstate(String block) {
        final InputStream stream = PolymerUtil.class.getResourceAsStream("/assets/humility-afm/blockstates/" + block + ".json");
        if (stream != null) {
            final JsonObject json = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject();
            final JsonObject variants = json.getAsJsonObject("variants");
            if (variants == null) {
                throw new JsonSyntaxException("Blockstate for '" + block + "' block is not a VARIANTS blockstate");
            }
            return variants;
        }
        throw new IllegalStateException("Could not find blockstate for block -> " + block);
    }
}
#endif