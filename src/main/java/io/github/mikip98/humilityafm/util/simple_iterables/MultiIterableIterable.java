package io.github.mikip98.humilityafm.util.simple_iterables;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Allows iteration over multiple {@link Iterable}s as a single combined {@link Iterable}.
 * Useful for combining multiple lists, sets, arrays (wrapped with Arrays.asList), etc.
 */
public class MultiIterableIterable<T> implements Iterable<T> {
    private final List<Iterable<T>> iterableList;

    @SafeVarargs
    public MultiIterableIterable(Iterable<T>... iterables) {
        this.iterableList = List.of(iterables);
    }
    @SafeVarargs
    public static <T> MultiIterableIterable<T> of(@NotNull Iterable<T>... iterables) {
        return new MultiIterableIterable<>(iterables);
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return new CompositeIterator();
    }

    private class CompositeIterator implements Iterator<T> {
        private final Iterator<Iterable<T>> outerIterator = iterableList.iterator();
        private Iterator<T> current = Collections.emptyIterator();

        @Override
        public boolean hasNext() {
            while (!current.hasNext() && outerIterator.hasNext()) {
                current = outerIterator.next().iterator();
            }
            return current.hasNext();
        }

        @Override
        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            return current.next();
        }
    }

    public int size() {
        return iterableList.stream().mapToInt(iterable -> {
            int size = 0;
            for (T ignored : iterable) size++;
            return size;
        }).sum();
    }
}
