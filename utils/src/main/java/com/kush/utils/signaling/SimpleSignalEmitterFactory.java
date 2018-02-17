package com.kush.utils.signaling;

import java.util.Collection;

public class SimpleSignalEmitterFactory implements SignalEmitterFactory {

    @Override
    public SignalEmitter create(Signal<?> signal) {
        return new SimpleSignalEmitter(signal);
    }

    private static class SimpleSignalEmitter implements SignalEmitter {

        private final Signal<?> signal;

        public SimpleSignalEmitter(Signal<?> signal) {
            this.signal = signal;
        }

        @Override
        public void emit(Collection<SignalReceiver> receivers) {
            for (SignalReceiver receiver : receivers) {
                sendSignalToReceiver(receiver);
            }
        }

        private void sendSignalToReceiver(SignalReceiver receiver) {
            signal.emit(receiver);
        }
    }

}
