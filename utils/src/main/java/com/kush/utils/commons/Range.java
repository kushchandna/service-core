package com.kush.utils.commons;

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
        this.isStartInclusive = start == null ? false : isStartInclusive;
        this.end = Optional.ofNullable(end);
        this.isEndInclusive = end == null ? false : isEndInclusive;
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
