package com.kush.utils.commons.adapters;

public interface Convertor<FROM> {

    <TO> TO convert(FROM source, Class<TO> targetType);
}
