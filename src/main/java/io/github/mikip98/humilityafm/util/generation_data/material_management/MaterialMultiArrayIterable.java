package io.github.mikip98.humilityafm.util.generation_data.material_management;

import io.github.mikip98.humilityafm.util.generation_data.material_management.material.BlockMaterial;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

// TODO: Improve the DOC
/**
 * This class contains the generation data that is ready for block generation and fabric data generation.
 * It is initialised from the RawGenerationData based on the active mods detected by ModSupportManager.
 */
public class MaterialMultiArrayIterable implements SizedIterable<BlockMaterial> {
    private final String[][] arrays;
    private final BlockMaterial.Metadata[] metadataArray;

    public MaterialMultiArrayIterable(BlockMaterial.Metadata[] metadataArray, String[]... arrays) {
        this.arrays = arrays;
        this.metadataArray = metadataArray;
    }
    public static MaterialMultiArrayIterable of(BlockMaterial.Metadata[] metadataArray, String[][] arrays) {
        return new MaterialMultiArrayIterable(metadataArray, arrays);
    }
    public MaterialMultiArrayIterable(String[]... arrays) {
        this.arrays = arrays;
        this.metadataArray = new BlockMaterial.Metadata[arrays.length];
        for (int i = 0; i < arrays.length; i++) {
            this.metadataArray[i] = BlockMaterial.Metadata.empty();
        }
    }

    @Override
    public @NotNull Iterator<BlockMaterial> iterator() {
        return new MarkedMultiArrayIterator();
    }

    private class MarkedMultiArrayIterator implements Iterator<BlockMaterial> {
        int arrayIndex = 0;
        int elementIndex = 0;

        @Override
        public boolean hasNext() {
            // Skip empty arrays or fully traversed arrays
            while (arrayIndex < arrays.length && elementIndex >= arrays[arrayIndex].length) {
                arrayIndex++;
                elementIndex = 0;
            }
            return arrayIndex < arrays.length;
        }

        @Override
        public BlockMaterial next() {
            if (!hasNext()) throw new NoSuchElementException();
            return BlockMaterial.of(arrays[arrayIndex][elementIndex++], metadataArray[arrayIndex]);
        }
    }

    @Override
    public int size() {
        return Arrays.stream(arrays).mapToInt((array) -> array.length).sum();
    }
}
