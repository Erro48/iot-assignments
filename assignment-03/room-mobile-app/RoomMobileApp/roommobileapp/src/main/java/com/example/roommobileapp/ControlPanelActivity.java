package com.example.roommobileapp;

import android.os.Handler;
import android.os.Looper;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import android.annotation.SuppressLint;

public class ControlPanelActivity extends AppCompatActivity {

    private final static int SLIDER_DELAY = 200;
    private final static int ROLLER_BLIND_DELTA = 5;
    private OutputStream bluetoothOutputStream;
    private Button lightBtn;

    private Button minusArrowButton;
    private Button plusArrowButton;
    private TextView loadingText;
    private ProgressBar loadingBar;

    private BluetoothClientConnectionThread connectionThread;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(Looper.getMainLooper());
        setContentView(R.layout.activity_control_panel);
        initUI();
    }

    private void initUI() {

        lightBtn = findViewById(R.id.lightBtn);
        lightBtn.setOnClickListener((v) -> sendMessage(formatLightStatus()));
        lightBtn.setBackgroundColor(Color.DKGRAY);
        lightBtn.setTextColor(Color.WHITE);

        minusArrowButton = findViewById(R.id.leftArrowBtn);
        minusArrowButton.setOnClickListener((v) -> sendMessage(formatRollerblindStatus((-1) * ROLLER_BLIND_DELTA)));
        minusArrowButton.setOnTouchListener(new LongPressListener(handler, SLIDER_DELAY) {
            protected void update() {
                sendMessage(formatRollerblindStatus((-1) * ROLLER_BLIND_DELTA));
            }
        });

        plusArrowButton = findViewById(R.id.rightArrowBtn);
        plusArrowButton.setOnClickListener((v) -> sendMessage(formatRollerblindStatus(ROLLER_BLIND_DELTA)));
        plusArrowButton.setOnTouchListener(new LongPressListener(handler, SLIDER_DELAY) {
            protected void update() {
                sendMessage(formatRollerblindStatus(ROLLER_BLIND_DELTA));
            }
        });


        loadingText = findViewById(R.id.loadingText);
        loadingText.setText("Connecting...");
        loadingText.setVisibility(View.VISIBLE);
        loadingBar = findViewById(R.id.progressBar);
        loadingBar.setVisibility(View.VISIBLE);

        enableInputs(false);
    }

    private void sendMessage(final String message) {
        new Thread(() -> {
            try {
                bluetoothOutputStream.write((">" + message + "\n").getBytes(StandardCharsets.UTF_8));
                Log.i(C.TAG, message);
                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Stato aggiornato", Toast.LENGTH_LONG).show();
                    }
                });*/
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        BluetoothDevice bluetoothDevice = intent.getParcelableExtra(ScanActivity.X_BLUETOOTH_DEVICE_EXTRA);
        BluetoothAdapter btAdapter = getSystemService(BluetoothManager.class).getAdapter();
        Log.i(C.TAG, "Connecting to " + bluetoothDevice.getName());
        connectionThread = new BluetoothClientConnectionThread(bluetoothDevice, btAdapter, this::manageConnectedSocket);
        connectionThread.start();
    }

    private void manageConnectedSocket(BluetoothSocket socket) {
        try {
            bluetoothOutputStream = socket.getOutputStream();
            if (!socket.isConnected()) {
                throw new IOException();
            }
            Log.i(C.TAG, "Connection successful!");
            runOnUiThread(() -> {
                enableInputs(true);
                loadingText.setVisibility(View.INVISIBLE);
                loadingBar.setVisibility(View.INVISIBLE);
            });
        } catch (IOException e) {
            Log.e(C.TAG, "Error occurred when creating output stream", e);
            runOnUiThread(() -> {
                enableInputs(false);
                loadingBar.setVisibility(View.GONE);
                loadingText.setText("Cannot connect to the device");
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        connectionThread.cancel();
    }

    private String formatLightStatus() {
        return "L";
    }

    private String formatRollerblindStatus(final int rollerblindStatus) {
        return "M" + String.valueOf(rollerblindStatus);
    }


    private void enableInputs(final boolean enabled) {
        lightBtn.setEnabled(enabled);
        minusArrowButton.setEnabled(enabled);
        plusArrowButton.setEnabled(enabled);
    }
}
