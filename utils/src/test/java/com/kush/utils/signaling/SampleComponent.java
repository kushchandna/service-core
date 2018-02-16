package com.kush.utils.signaling;

public class SampleComponent implements SignalEmitter {

    private final SignalEmitter emitter = new DefaultSignalEmittter();

    public void start() {
        System.out.println("Component started");
    }

    public void stop() {
        System.out.println("Componenet stopped");
    }

    public void print(Object object) {
        System.out.println("Printed: " + object);
    }

    @Override
    public void registerReceiver(SignalReceiver receiver) {
        emitter.registerReceiver(receiver);
    }

    @Override
    public void unregisterReceiver(SignalReceiver receiver) {
        emitter.unregisterReceiver(receiver);
    }

    @Override
    public void emit(Signal signal) {
        emitter.emit(signal);
    }
}
