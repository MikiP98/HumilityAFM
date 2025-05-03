package io.github.mikip98.humilityafm;

import io.github.mikip98.humilityafm.datagen.BlockLootTableGenerator;
import io.github.mikip98.humilityafm.datagen.BlockTagGenerator;
import io.github.mikip98.humilityafm.datagen.RecipeGenerator;
import io.github.mikip98.humilityafm.datagen.language.PolishLangProvider;
import io.github.mikip98.humilityafm.datagen.language.UKEnglishLangProvider;
import io.github.mikip98.humilityafm.datagen.language.USEnglishLangProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class HumilityAFMDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(BlockTagGenerator::new);
		pack.addProvider(BlockLootTableGenerator::new);
		pack.addProvider(RecipeGenerator::new);
		// Language providers
		pack.addProvider(PolishLangProvider::new);
		pack.addProvider(UKEnglishLangProvider::new);
		pack.addProvider(USEnglishLangProvider::new);
	}
}
