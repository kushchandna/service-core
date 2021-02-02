package com.kush.utils.commons;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Stream;

public interface IterableResult<T> extends Iterable<T> {

    long UNKNOWN_COUNT = -1L;

    Stream<T> stream();

    long count();

    default boolean isCountKnown() {
        return count() != UNKNOWN_COUNT;
    }

    default Collection<T> asList() {
        return stream().collect(toList());
    }

    static <T> IterableResult<T> empty() {
        return new DefaultIterableResult<>(Stream.empty(), 0L);
    }

    static <T> IterableResult<T> fromCollection(Collection<T> collection) {
        return new DefaultIterableResult<>(collection.stream(), collection.size());
    }

    static <T> IterableResult<T> fromCollections(Collection<Collection<T>> collections) {
        Stream<T> stream = collections.stream().flatMap(Collection::stream);
        long count = 0L;
        for (Collection<T> collection : collections) {
            count += collection.size();
        }
        return new DefaultIterableResult<>(stream, count);
    }

    static <T> IterableResult<T> fromValue(T object) {
        return new DefaultIterableResult<>(Stream.of(object), 1);
    }

    @SafeVarargs
    static <T> IterableResult<T> fromValues(T... values) {
        return new DefaultIterableResult<>(Stream.of(values), values.length);
    }

    static <T> IterableResult<T> fromStream(Stream<T> stream) {
        return new DefaultIterableResult<>(stream, UNKNOWN_COUNT);
    }

    static <T> IterableResult<T> merge(Collection<IterableResult<T>> results) {
        Stream<T> mergedStream = results.stream()
            .map(IterableResult::stream)
            .flatMap(stream -> stream);
        long count = 0L;
        for (IterableResult<T> result : results) {
            if (!result.isCountKnown()) {
                count = UNKNOWN_COUNT;
                break;
            }
            count += result.count();
        }
        return new DefaultIterableResult<>(mergedStream, count);
    }

    @SafeVarargs
    static <T> IterableResult<T> merge(IterableResult<T>... results) {
        return merge(Arrays.asList(results));
    }

    static class DefaultIterableResult<T> implements IterableResult<T> {

        private final Stream<T> stream;
        private final long count;

        public DefaultIterableResult(Stream<T> stream, long count) {
            this.stream = stream;
            this.count = count;
        }

        @Override
        public Stream<T> stream() {
            return stream;
        }

        @Override
        public long count() {
            return count;
        }

        @Override
        public Iterator<T> iterator() {
            return stream().iterator();
        }
    }
}
