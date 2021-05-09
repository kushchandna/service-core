package com.kush.utils.signaling;

import static com.kush.commons.utils.ExecutorUtils.newThreadExecutor;
import static com.kush.commons.utils.ExecutorUtils.sameThreadExecutor;

import java.util.concurrent.Executor;

public class SignalEmitters {

    public static SignalEmitter newSyncEmitter() {
        return new SignalEmitter();
    }

    public static SignalEmitter newAsyncEmitter() {
        return new AsynchronousSignalEmitter(newThreadExecutor(), sameThreadExecutor());
    }

    public static SignalEmitter newAsyncEmitter(Executor signalSpecificExecutor, Executor receiverSpecificExecutor) {
        return new AsynchronousSignalEmitter(signalSpecificExecutor, receiverSpecificExecutor);
    }
}
