package com.kush.utils.signaling;

import static com.kush.utils.commons.CommonExecutors.newThreadExecutor;

import java.util.concurrent.Executor;

import com.kush.utils.commons.CommonExecutors;

public class SignalEmitters {

    public static SignalEmitter newSyncEmitter() {
        return new SignalEmitter();
    }

    public static SignalEmitter newAsyncEmitter() {
        return new AsynchronousSignalEmitter(newThreadExecutor(), CommonExecutors.sameThreadExecutor());
    }

    public static SignalEmitter newAsyncEmitter(Executor signalSpecificExecutor, Executor receiverSpecificExecutor) {
        return new AsynchronousSignalEmitter(signalSpecificExecutor, receiverSpecificExecutor);
    }
}
