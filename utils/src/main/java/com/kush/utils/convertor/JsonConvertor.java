package com.kush.utils.convertor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonConvertor implements StringConvertor {

    @Override
    public <T> T convert(String source, Class<T> targetType) {
        Gson gson = new GsonBuilder()
            .setLenient()
            .create();
        return gson.fromJson(source, targetType);
    }
}
