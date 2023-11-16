package io.github.mikip98;

import io.github.mikip98.config.ModConfig;
import io.github.mikip98.content.blockentities.cabinetBlock.IlluminatedCabinetBlockEntityRenderer;
import io.github.mikip98.helpers.CabinetBlockHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import io.github.mikip98.content.blockentities.cabinetBlock.CabinetBlockEntityRenderer;
import io.github.mikip98.content.blockentities.LEDBlockEntityRenderer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class HumilityAFMClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		// Check if shimmer config exists, if not, create it
		Path configDir = FabricLoader.getInstance().getConfigDir();
		Path configPath = configDir.resolve("shimmer/humility.json");
		if (!configPath.toFile().exists()) {
			// Create the config file
			createShimmerConfig(configPath);
		}

		BlockRenderLayerMap.INSTANCE.putBlock(HumilityAFM.CABINET_BLOCK, RenderLayer.getCutout());
		// Replace `RenderLayer.getCutout()` with `RenderLayer.getTranslucent()` if you have a translucent texture.
		BlockRenderLayerMap.INSTANCE.putBlock(HumilityAFM.ILLUMINATED_CABINET_BLOCK, RenderLayer.getCutout());

		BlockEntityRendererFactories.register(HumilityAFM.CABINET_BLOCK_ENTITY, CabinetBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(HumilityAFM.ILLUMINATED_CABINET_BLOCK_ENTITY, IlluminatedCabinetBlockEntityRenderer::new);

		for (Block cabinetBlockVariant : CabinetBlockHelper.cabinetBlockVariants) {
			BlockRenderLayerMap.INSTANCE.putBlock(cabinetBlockVariant, RenderLayer.getCutout());
		}
		for (Block illuminatedCabinetBlockVariant : CabinetBlockHelper.illuminatedCabinetBlockVariants) {
			BlockRenderLayerMap.INSTANCE.putBlock(illuminatedCabinetBlockVariant, RenderLayer.getCutout());
		}

		// LED block variants
		if (ModConfig.enableLEDs) {
			BlockEntityRendererFactories.register(HumilityAFM.LED_BLOCK_ENTITY, LEDBlockEntityRenderer::new);
		}
	}

	private void createShimmerConfig(Path path) {
		// Create the config file
		String JSON = """
	{
	   "ColorReference": {
		 "white" : {
		   "r": 255,
		   "g": 255,
		   "b": 255,
		   "a": 170
		 },
		 "lightgrey" : {
		   "r": 200,
		   "g": 200,
		   "b": 200,
		   "a": 170
		 },
		 "gray" : {
		   "r": 100,
		   "g": 100,
		   "b": 100,
		   "a": 170
		 },
		 "black" : {
		   "r": 0,
		   "g": 0,
		   "b": 0,
		   "a": 170
		 },
		 "brown" : {
		   "r": 139,
		   "g": 69,
		   "b": 19,
		   "a": 170
		 },
		 "red" : {
		   "r": 255,
		   "g": 0,
		   "b": 0,
		   "a": 170
		 },
		 "orange" : {
		   "r": 255,
		   "g": 165,
		   "b": 0,
		   "a": 170
		 },
		 "yellow" : {
		   "r": 255,
		   "g": 255,
		   "b": 0,
		   "a": 170
		 },
		 "lime" : {
		   "r": 0,
		   "g": 255,
		   "b": 0,
		   "a": 170
		 },
		 "green" : {
		   "r": 0,
		   "g": 128,
		   "b": 0,
		   "a": 170
		 },
		 "cyan" : {
		   "r": 0,
		   "g": 255,
		   "b": 255,
		   "a": 170
		 },
		 "lightblue" : {
		   "r": 0,
		   "g": 0,
		   "b": 255,
		   "a": 170
		 },
		 "blue" : {
		   "r": 0,
		   "g": 0,
		   "b": 128,
		   "a": 170
		 },
		 "purple" : {
		   "r": 128,
		   "g": 0,
		   "b": 128,
		   "a": 170
		 },
		 "magenta" : {
		   "r": 255,
		   "g": 0,
		   "b": 255,
		   "a": 170
		 },
		 "pink" : {
		   "r": 255,
		   "g": 192,
		   "b": 203,
		   "a": 170
		 }
	   },
	 
	   "LightBlock": [
		 {
		   "block": "humility-afm:led_white",
		   "color": "#white",
		   "radius": 9
		 },
		 {
		   "block": "humility-afm:led_light_grey",
		   "color": "#lightgrey",
		   "radius": 9
		 },
		 {
		   "block": "humility-afm:led_gray",
		   "color": "#gray",
		   "radius": 9
		 },
		 {
		   "block": "humility-afm:led_black",
		   "color": "#black",
		   "radius": 9
		 },
		 {
		   "block": "humility-afm:led_brown",
		   "color": "#brown",
		   "radius": 9
		 },
		 {
		   "block": "humility-afm:led_red",
		   "color": "#red",
		   "radius": 9
		 },
		 {
		   "block": "humility-afm:led_orange",
		   "color": "#orange",
		   "radius": 9
		 },
		 {
		   "block": "humility-afm:led_yellow",
		   "color": "#yellow",
		   "radius": 9
		 },
		 {
		   "block": "humility-afm:led_lime",
		   "color": "#lime",
		   "radius": 9
		 },
		 {
		   "block": "humility-afm:led_green",
		   "color": "#green",
		   "radius": 9
		 },
		 {
		   "block": "humility-afm:led_cyan",
		   "color": "#cyan",
		   "radius": 9
		 },
		 {
		   "block": "humility-afm:led_light_blue",
		   "color": "#lightblue",
		   "radius": 9
		 },
		 {
		   "block": "humility-afm:led_blue",
		   "color": "#blue",
		   "radius": 9
		 },
		 {
		   "block": "humility-afm:led_purple",
		   "color": "#purple",
		   "radius": 9
		 },
		 {
		   "block": "humility-afm:led_magenta",
		   "color": "#magenta",
		   "radius": 9
		 },
		 {
		   "block": "humility-afm:led_pink",
		   "color": "#pink",
		   "radius": 9
		 }
	   ]
	 }
		""";

		// Save the config file
		try (FileWriter writer = new FileWriter(String.valueOf(path))) {
			writer.write(JSON);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}