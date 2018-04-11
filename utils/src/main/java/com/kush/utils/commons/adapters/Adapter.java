package com.kush.utils.commons.adapters;

public interface Adapter<FROM> {

    <TO> TO adapt(FROM source, Class<TO> targetType);
}
