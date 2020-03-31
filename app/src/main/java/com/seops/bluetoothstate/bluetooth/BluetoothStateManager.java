package com.seops.bluetoothstate.bluetooth;

public interface BluetoothStateManager {
    void registerObserver(BluetoothStateObserver observer);
    void unregisterObserver(BluetoothStateObserver observer);
    void notifyAll(BluetoothState bluetoothState);
}
