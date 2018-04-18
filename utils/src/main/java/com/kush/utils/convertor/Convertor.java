package com.kush.utils.convertor;

public interface Convertor<FROM> {

    <TO> TO convert(FROM source, Class<TO> targetType);
}
