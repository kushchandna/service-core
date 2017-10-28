package com.kush.lib.persistence.helpers;

import static com.kush.lib.persistence.api.Identifier.id;

import java.util.concurrent.atomic.AtomicInteger;

import com.kush.lib.persistence.api.Identifier;

public class SequentialIdGenerator implements IdGenerator {

    private final AtomicInteger lastUsedId = new AtomicInteger(0);

    @Override
    public Identifier next() {
        return id(lastUsedId.incrementAndGet());
    }
}
