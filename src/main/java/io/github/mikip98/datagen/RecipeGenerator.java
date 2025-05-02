package io.github.mikip98.datagen;

import io.github.mikip98.HumilityAFM;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

import static io.github.mikip98.HumilityAFM.MOD_ID;

public class RecipeGenerator extends FabricRecipeProvider {
    public RecipeGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        offerDoubleInputShapelessRecipe(
                exporter,
                HumilityAFM.ILLUMINATED_CABINET_BLOCK,
                HumilityAFM.CABINET_BLOCK,
                Items.GLOW_INK_SAC,
                MOD_ID,
                1
        );
    }

    protected static void offerDoubleInputShapelessRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible output, ItemConvertible input1, ItemConvertible input2, @Nullable String group, int outputCount) {
        ShapelessRecipeJsonBuilder
                .create(RecipeCategory.MISC, output, outputCount)
                .input(input1)
                .input(input2)
                .group(group)
                .criterion(hasItem(input1), conditionsFromItem(input1))
                .criterion(hasItem(input2), conditionsFromItem(input2))
                .offerTo(exporter, getRecipeName(output));
    }
}
