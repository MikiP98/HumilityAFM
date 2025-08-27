package io.github.mikip98.humilityafm.util.generation_data.material_management;

import io.github.mikip98.humilityafm.util.generation_data.material_management.material.BlockMaterial;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class MaterialArrayCombiner implements SizedIterable<BlockMaterial> {
    private final String[][] arrays;
    private final BlockMaterial.Metadata[] metadataArray;

    public MaterialArrayCombiner(BlockMaterial.Metadata[] metadataArray, String[]... arrays) {
        if (arrays.length != metadataArray.length) {
            throw new IllegalArgumentException("Number of arrays must match number of metadata entries.");
        }
        this.arrays = arrays;
        this.metadataArray = metadataArray;
    }
    public MaterialArrayCombiner(String[]... arrays) {
        this.arrays = arrays;
        this.metadataArray = new BlockMaterial.Metadata[arrays.length];
        for (int i = 0; i < arrays.length; i++) {
            this.metadataArray[i] = BlockMaterial.Metadata.empty();
        }
    }

    @NotNull
    @Override
    public Iterator<BlockMaterial> iterator() {
        return new MarkedCombinatorIterator();
    }

    private class MarkedCombinatorIterator implements Iterator<BlockMaterial> {
        // TODO: Make layer amount configurable
        int outerArrayIndex = 0;
        int outerElementIndex = 0;
        int innerArrayIndex = 0;
        int innerElementIndex = 0;
        BlockMaterial nextValue = null;

        @Override
        public boolean hasNext() {
            if (nextValue != null) return true;

            while (outerArrayIndex < arrays.length) {
                String outerValue = arrays[outerArrayIndex][outerElementIndex];

                while (innerArrayIndex < arrays.length) {
                    String innerValue = arrays[innerArrayIndex][innerElementIndex];

                    BlockMaterial.Metadata outerMark = metadataArray[outerArrayIndex];
                    BlockMaterial.Metadata innerMark = metadataArray[innerArrayIndex];
                    if (!outerValue.equals(innerValue) || outerMark.sourceMod() != innerMark.sourceMod()) {
                        nextValue = BlockMaterial.of(outerValue, outerMark, innerValue, innerMark);
                        advanceInnerArrayIndexes();
                        return true;
                    }

                    advanceInnerArrayIndexes();
                }

                advanceOuterArrayIndexes();
            }

            return false;
        }

        @Override
        public BlockMaterial next() {
            if (!hasNext()) throw new NoSuchElementException();
            BlockMaterial result = nextValue;
            nextValue = null;
            return result;
        }

        private void advanceInnerArrayIndexes() {
            innerElementIndex++;
            if (innerElementIndex >= arrays[innerArrayIndex].length) {
                innerElementIndex = 0;
                innerArrayIndex++;
            }
        }

        private void advanceOuterArrayIndexes() {
            outerElementIndex++;
            if (outerElementIndex >= arrays[outerArrayIndex].length) {
                outerElementIndex = 0;
                outerArrayIndex++;
            }
            innerArrayIndex = 0;
            innerElementIndex = 0;
        }
    }

    @Override
    public void forEach(java.util.function.Consumer<? super BlockMaterial> action) {
        for (BlockMaterial s : this) {
            action.accept(s);
        }
    }

    public <R> Iterable<R> map(Function<? super BlockMaterial, ? extends R> mapper) {
        return () -> new Iterator<>() {
            private final Iterator<BlockMaterial> baseIterator = MaterialArrayCombiner.this.iterator();

            @Override
            public boolean hasNext() {
                return baseIterator.hasNext();
            }

            @Override
            public R next() {
                return mapper.apply(baseIterator.next());
            }
        };
    }

    // TODO: Optimize this somehow
    @Override
    public int size() {
        int count = 0;
        for (BlockMaterial ignored : this) ++count;
        return count;
    }
}
