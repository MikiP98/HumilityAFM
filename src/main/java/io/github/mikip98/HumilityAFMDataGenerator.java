package io.github.mikip98;

import io.github.mikip98.datagen.BlockLootTableGenerator;
import io.github.mikip98.datagen.BlockTagGenerator;
import io.github.mikip98.datagen.language.PolishLangProvider;
import io.github.mikip98.datagen.language.UKEnglishLangProvider;
import io.github.mikip98.datagen.language.USEnglishLangProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import java.io.File;

public class HumilityAFMDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(BlockTagGenerator::new);
		pack.addProvider(BlockLootTableGenerator::new);
		// Language providers
		pack.addProvider(PolishLangProvider::new);
		pack.addProvider(UKEnglishLangProvider::new);
		pack.addProvider(USEnglishLangProvider::new);
	}
}
