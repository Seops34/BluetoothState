package com.seops.bluetoothstatelib;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.util.ArrayList;

public class BluetoothStateManagerImpl implements BluetoothStateManager{
    private static final String TAG = "[BluetoothStateManagerImpl] ";

    private static BluetoothStateManagerImpl bluetoothStateManagerImpl;

    private ArrayList<BluetoothStateObserver> observerList;

    public BluetoothStateManagerImpl(Context mContext) {
        observerList = new ArrayList<>();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);

        mContext.registerReceiver(broadcastReceiver, intentFilter);
    }

    public static BluetoothStateManagerImpl getInstance(Context mContext) {
        if(bluetoothStateManagerImpl == null) {
            bluetoothStateManagerImpl = new BluetoothStateManagerImpl(mContext);
        }

        return bluetoothStateManagerImpl;
    }

    @Override
    public void registerObserver(BluetoothStateObserver observer) {
        if(observerList != null) {
            observerList.add(observer);
        }
    }

    @Override
    public void unregisterObserver(BluetoothStateObserver observer) {
        if(observerList != null) {
            if(observerList.contains(observer)) {
                observerList.remove(observer);
            }
        }
    }

    @Override
    public void notifyAll(BluetoothState bluetoothState) {
        for(BluetoothStateObserver observer : observerList) {
            observer.updateBluetoothState(bluetoothState);
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            BluetoothState bluetoothState = BluetoothState.NONE;

            switch (action) {
                case BluetoothAdapter.ACTION_STATE_CHANGED :
                    final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                    switch (state) {
                        case BluetoothAdapter.STATE_OFF:
                            Log.d("seosh", TAG + "[onReceive] Bluetooth STATE OFF");
                            bluetoothState = BluetoothState.STATE_OFF;
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            Log.d("seosh", TAG + "[onReceive] Bluetooth TURNING OFF");
                            bluetoothState = BluetoothState.STATE_OFF;
                            break;
                        case BluetoothAdapter.STATE_ON:
                            Log.d("seosh", TAG + "[onReceive] Bluetooth STATE ON");
                            bluetoothState = BluetoothState.STATE_ON;
                            break;
                        case BluetoothAdapter.STATE_TURNING_ON:
                            Log.d("seosh", TAG + "[onReceive] Bluetooth TURNING ON");
                            bluetoothState = BluetoothState.STATE_ON;
                            break;
                    }
                    break;

                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    Log.d("seosh", TAG + "[onReceive] Bluetooth CONNECTED");
                    bluetoothState = BluetoothState.STATE_CONNECTED;
                    break;

                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    Log.d("seosh", TAG + "[onReceive] Bluetooth DISCONNECTED");
                    bluetoothState = BluetoothState.STATE_DISCONNECTED;
                    break;
            }

            BluetoothStateManagerImpl.this.notifyAll(bluetoothState);
        }
    };
}
