package io.github.mikip98.humilityafm.util.simple_iterables;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Allows iteration over multiple arrays of type T as one iterable sequence.
 */
public class MultiArrayIterable<T> implements Iterable<T> {
    private final T[][] arrays;

    @SafeVarargs
    public MultiArrayIterable(T[]... arrays) {
        this.arrays = arrays;
    }
    @SafeVarargs
    public static <T> MultiArrayIterable<T> of(T[]... arrays) {
        return new MultiArrayIterable<>(arrays);
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return new MultiArrayIterator();
    }

    private class MultiArrayIterator implements Iterator<T> {
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
        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            return arrays[arrayIndex][elementIndex++];
        }
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        for (T[] array : arrays) {
            for (T element : array) {
                action.accept(element);
            }
        }
    }

    public <R> Stream<R> map(Function<? super T, ? extends R> mapper) {
        return Stream.of(arrays)
                .flatMap(Stream::of)
                .map(mapper);
    }

    public int size() {
        return Stream.of(arrays)
                .mapToInt(array -> array.length)
                .sum();
    }
}
