package com.kush.utils.commons;

import java.util.Comparator;
import java.util.Optional;

public class Range<T> {

    private final Optional<T> start;
    private final boolean isStartInclusive;
    private final Optional<T> end;
    private final boolean isEndInclusive;

    public static <T> Range.Builder<T> builder() {
        return new Range.Builder<>();
    }

    private Range(T start, boolean isStartInclusive, T end, boolean isEndInclusive) {
        this.start = Optional.ofNullable(start);
        this.isStartInclusive = isStartInclusive;
        this.end = Optional.ofNullable(end);
        this.isEndInclusive = isEndInclusive;
    }

    public Optional<T> getStart() {
        return start;
    }

    public boolean isStartInclusive() {
        return isStartInclusive;
    }

    public Optional<T> getEnd() {
        return end;
    }

    public boolean isEndInclusive() {
        return isEndInclusive;
    }

    public boolean isInRange(T key, Comparator<T> comparator) {
        if (start.isPresent()) {
            int resultStart = comparator.compare(key, start.get());
            if (resultStart < 0) {
                return false;
            }
            if (resultStart == 0 && !isStartInclusive) {
                return false;
            }
        }
        if (end.isPresent()) {
            int resultEnd = comparator.compare(key, end.get());
            if (resultEnd < 0) {
                return false;
            }
            if (resultEnd == 0 && !isEndInclusive) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder = append(builder, "FROM", start, isStartInclusive);
        if (builder.length() > 0) {
            builder = builder.append(" ");
        }
        builder = append(builder, "TO", end, isEndInclusive);
        return builder.toString();
    }

    private StringBuilder append(StringBuilder builder, String tag, Optional<T> value, boolean isInclusive) {
        if (value.isPresent()) {
            builder = builder.append(tag)
                .append(" ")
                .append(value.get())
                .append(" ");
            if (isInclusive) {
                builder = builder.append("(including)");
            } else {
                builder = builder.append("(excluding)");
            }
        }
        return builder;
    }

    public static class Builder<T> {

        private T start;
        private boolean isStartInclusive;
        private T end;
        private boolean isEndInclusive;

        public Range<T> build() {
            return new Range<>(start, isStartInclusive, end, isEndInclusive);
        }

        public Range.Builder<T> startingFrom(T value, boolean isInclusive) {
            start = value;
            isStartInclusive = isInclusive;
            return this;
        }

        public Range.Builder<T> startingFrom(T value) {
            return startingFrom(value, true);
        }

        public Range.Builder<T> endingAt(T value, boolean isInclusive) {
            end = value;
            isEndInclusive = isInclusive;
            return this;
        }

        public Range.Builder<T> endingAt(T value) {
            return endingAt(value, false);
        }
    }
}
