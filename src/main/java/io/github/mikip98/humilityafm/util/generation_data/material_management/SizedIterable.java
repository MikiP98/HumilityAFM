package io.github.mikip98.humilityafm.util.generation_data.material_management;

// TODO: The size() implementation is very unoptimized
//  It would be best to stop relaying on it
public interface SizedIterable<T> extends Iterable<T> {
    int size();
}
