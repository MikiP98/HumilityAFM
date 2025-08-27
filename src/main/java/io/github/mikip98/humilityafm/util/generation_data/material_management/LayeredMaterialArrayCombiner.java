package io.github.mikip98.humilityafm.util.generation_data.material_management;

import io.github.mikip98.humilityafm.util.data_types.Pair;
import io.github.mikip98.humilityafm.util.generation_data.material_management.data_types.BlockMaterial;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

public class LayeredMaterialArrayCombiner implements SizedIterable<BlockMaterial> {
    private final List<String[]>[] layersArray;
    private final List<BlockMaterial.Metadata[]> metadataArrays;

    @SafeVarargs
    public LayeredMaterialArrayCombiner(List<BlockMaterial.Metadata[]> metadataArrays, List<String[]>... layers) {
        int i = 0;
        for (List<String[]> layer : layers) {
            if (layer.size() != metadataArrays.get(i).length) {
                throw new IllegalArgumentException("Metadata array size does not match the layer array amount at index " + i);
            }
            ++i;
        }
        this.layersArray = layers;
        this.metadataArrays = metadataArrays;
    }
    @SafeVarargs
    public LayeredMaterialArrayCombiner(List<String[]>... layers) {
        int i = 0;
        this.metadataArrays = new ArrayList<>();
        for (List<String[]> layer : layers) {
            this.metadataArrays.add(new BlockMaterial.Metadata[layer.size()]);
            for (int j = 0; j < layer.size(); j++) {
                this.metadataArrays.get(i)[j] = BlockMaterial.Metadata.empty();
            }
            ++i;
        }
        this.layersArray = layers;
    }

    @NotNull
    @Override
    public Iterator<BlockMaterial> iterator() {
        return new LayeredMarkedCombinatorIterator();
    }

    private class LayeredMarkedCombinatorIterator implements Iterator<BlockMaterial> {
        int[] layerArrayIndexes = new int[layersArray.length];
        int[] layerElementIndexes = new int[layersArray.length];
        boolean stop = false; //

        BlockMaterial nextValue = null;

        public LayeredMarkedCombinatorIterator() {
            super();
            Arrays.fill(layerArrayIndexes, 0);
            Arrays.fill(layerElementIndexes, 0);
        }

        @Override
        public boolean hasNext() {
            if (nextValue != null) return true;
            if (stop) return false; //

            NameMetaPair[] pairs = new NameMetaPair[layersArray.length];

            while (true) {
                for (int i = 0; i < layersArray.length; i++) {
                    List<String[]> layer = layersArray[i];
                    BlockMaterial.Metadata[] layerMetadata = metadataArrays.get(i);

                    String name = layer.get(layerArrayIndexes[i])[layerElementIndexes[i]];
                    BlockMaterial.Metadata metadata = layerMetadata[layerArrayIndexes[i]];

                    pairs[i] = new NameMetaPair(name, metadata);
                }

                if (checkForUniqueness(pairs)) {
                    List<BlockMaterial.Layer> layers = new ArrayList<>();
                    for (NameMetaPair pair : pairs) {
                        layers.add(new BlockMaterial.Layer(pair.name, pair.metadata));
                    }
                    nextValue = BlockMaterial.of(layers.toArray(BlockMaterial.Layer[]::new));
                    if (!advanceArrayIndexes()) stop = true;
                    return true;
                }

                if (!advanceArrayIndexes()) return false;
            }
        }
        protected record NameMetaPair(String name, BlockMaterial.Metadata metadata) {}
        protected static boolean checkForUniqueness(NameMetaPair[] nameMetaPairs) {
            Set<NameMetaPair> seen = new HashSet<>();
            boolean allUniquePairs = true;
            for (NameMetaPair nameMetaPair : nameMetaPairs) {
                if (!seen.add(nameMetaPair)) {
                    allUniquePairs = false;
                    break;
                }
            }
            return allUniquePairs;
        }

        @Override
        public BlockMaterial next() {
            if (!hasNext()) throw new NoSuchElementException();
            BlockMaterial result = nextValue;
            nextValue = null;
            return result;
        }

        // TODO: Last possible combination 'Warped' + 'Black' is being skipped
        //  Fix this
        private boolean advanceArrayIndexes() {
            int i = layersArray.length - 1;
            while (!advanceLayerIndexes(i)) {
                --i;
                if (i < 0) return false;
            }
            return true;
        }
        private boolean advanceLayerIndexes(int i) {
            int layerArrayIndex = layerArrayIndexes[i];
            int layerArrayAmount = layersArray[i].size();
            int currentArraySize = layersArray[i].get(layerArrayIndex).length;
            int layerElementIndex = layerElementIndexes[i];

            if (layerElementIndex + 1 < currentArraySize) {
                layerElementIndexes[i] = layerElementIndex + 1;
                return true;
            } else {
                layerElementIndexes[i] = 0;
                if (layerArrayIndex + 1 < layerArrayAmount) {
                    layerArrayIndexes[i] = layerArrayIndex + 1;
                    return true;
                } else {
                    layerArrayIndexes[i] = 0;
                    return false;
                }
            }
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
            private final Iterator<BlockMaterial> baseIterator = LayeredMaterialArrayCombiner.this.iterator();

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

    @Override
    public int size() {
        int count = 0;
        for (BlockMaterial ignored : this) {
            count++;
        }
        return count;
    }
}
