package com.kush.lib.service.api.server.id;

import static com.kush.lib.service.api.server.id.Identifier.id;

import java.util.concurrent.atomic.AtomicInteger;

public class SequentialIdGenerator implements IdGenerator {

    private final AtomicInteger lastUsedId = new AtomicInteger(0);

    @Override
    public Identifier next() {
        return id(lastUsedId.incrementAndGet());
    }
}
