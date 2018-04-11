package com.kush.utils.commons.adapters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonAdapter implements StringAdapter {

    @Override
    public <T> T adapt(String source, Class<T> targetType) {
        Gson gson = new GsonBuilder()
            .setLenient()
            .create();
        return gson.fromJson(source, targetType);
    }
}
