package io.github.mikip98;

import io.github.mikip98.helpers.CabinetBlockHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class HumilityAFMDataGenerator implements DataGeneratorEntrypoint {

	private static class MineableAxeTagGenerator extends FabricTagProvider.BlockTagProvider {
		public MineableAxeTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
			super(output, completableFuture);
		}

		private static final TagKey<Block> AXE_MINEABLE = TagKey.of(RegistryKeys.BLOCK, new Identifier("minecraft:mineable/axe"));

		@Override
		protected void configure(RegistryWrapper.WrapperLookup arg) {
			for (Block block: CabinetBlockHelper.moddedCabinetBlockVariants) {
				getOrCreateTagBuilder(AXE_MINEABLE).add(block);
			}
		}
	}


	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(MineableAxeTagGenerator::new);
	}
}
