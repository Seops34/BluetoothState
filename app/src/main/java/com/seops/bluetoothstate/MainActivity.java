package com.seops.bluetoothstate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.seops.bluetoothstate.R;
import com.seops.bluetoothstatelib.BluetoothState;
import com.seops.bluetoothstatelib.BluetoothStateManagerImpl;
import com.seops.bluetoothstatelib.BluetoothStateObserver;

public class MainActivity extends AppCompatActivity implements BluetoothStateObserver {
    private static final String TAG = "[MainActivity] ";
    private Context mContext;
    private BluetoothStateManagerImpl bluetoothStateManager;

    private TextView textBluetoothState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSettings();
        initViews();
        setViews();
    }

    private void initSettings() {
        mContext = getApplicationContext();
        bluetoothStateManager = BluetoothStateManagerImpl.getInstance(mContext);
        bluetoothStateManager.registerObserver(this);
    }

    private void initViews() {
        textBluetoothState = (TextView) findViewById(R.id.bluetoothState);
    }

    private void setViews() {
        textBluetoothState.setText("NONE");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bluetoothStateManager != null) {
            bluetoothStateManager.unregisterObserver(this);
        }
    }

    @Override
    public void updateBluetoothState(BluetoothState bluetoothState) {
        final BluetoothState state = bluetoothState;

        switch (state) {
            case STATE_ON:
                Log.d("seosh", TAG + "[updateBluetoothState] STATE_ON");
                break;
            case STATE_OFF:
                Log.d("seosh", TAG + "[updateBluetoothState] STATE_OFF");
                break;
            case STATE_CONNECTED:
                Log.d("seosh", TAG + "[updateBluetoothState] STATE_CONNECTED");
                break;
            case STATE_DISCONNECTED:
                Log.d("seosh", TAG + "[updateBluetoothState] STATE_DISCONNECTED");
                break;
            case NONE:
            default:
                break;
        }

        textBluetoothState.setText(state.name());
    }
}
