package com.kush.utils.signaling;

import java.util.concurrent.Executor;

public class DefaultSignalEmitterFactory implements SignalEmitterFactory {

    public static final Executor SAME_THREAD_EXECUTOR = new SameThreadExecutor();

    private final Executor signalSpecificExecutor;
    private final Executor receiverSpecificExecutor;

    public DefaultSignalEmitterFactory() {
        this(null, null);
    }

    public DefaultSignalEmitterFactory(Executor executor, boolean useOneThreadPerSignal) {
        if (useOneThreadPerSignal) {
            signalSpecificExecutor = executor;
            receiverSpecificExecutor = null;
        } else {
            signalSpecificExecutor = null;
            receiverSpecificExecutor = executor;
        }
    }

    public DefaultSignalEmitterFactory(Executor signalSpecificExecutor, Executor receiverSpecificExecutor) {
        this.signalSpecificExecutor = signalSpecificExecutor;
        this.receiverSpecificExecutor = receiverSpecificExecutor;
    }

    @Override
    public SignalEmitter create() {
        if (signalSpecificExecutor == null && receiverSpecificExecutor == null) {
            return new SignalEmitter();
        }
        Executor signalSpecificExecutorLocal = signalSpecificExecutor == null ? SAME_THREAD_EXECUTOR : signalSpecificExecutor;
        Executor receiverSpecificExecutorLocal =
                receiverSpecificExecutor == null ? SAME_THREAD_EXECUTOR : receiverSpecificExecutor;
        return new AsynchronousSignalEmitter(signalSpecificExecutorLocal, receiverSpecificExecutorLocal);
    }

    private static class SameThreadExecutor implements Executor {

        @Override
        public void execute(Runnable command) {
            command.run();
        }
    }
}
