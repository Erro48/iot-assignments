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
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import android.annotation.SuppressLint;
import android.widget.Toast;

public class ControlPanelActivity extends AppCompatActivity {

    private final static int SLIDER_DELAY = 200;
    private final static int SLIDER_DELTA = 5;
    private final static int SLIDER_MAX = 100;
    private final static int SLIDER_MIN = 0;
    private OutputStream bluetoothOutputStream;
    private Button lightBtn;
    private TextView lightTextStatus;

    private SeekBar rollerblindSlider;
    private TextView rollerblindTextStatus;
    private Button leftArrowButton;
    private Button rightArrowButton;
    private TextView loadingText;
    private ProgressBar loadingBar;

    //private Button confirmChangeButton;
    private boolean lightStatus;
    private int rollerblindStatus;
    private BluetoothClientConnectionThread connectionThread;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(Looper.getMainLooper());
        setContentView(R.layout.activity_control_panel);
        lightStatus = false;
        rollerblindStatus = 0;
        initUI();
    }

    private void initUI() {

        lightBtn = findViewById(R.id.lightBtn);
        lightBtn.setOnClickListener((v) -> updateLightStatus());
        lightBtn.setBackgroundColor(Color.DKGRAY);
        lightBtn.setTextColor(Color.WHITE);

        lightTextStatus = findViewById(R.id.lightStatus);
        lightTextStatus.setText(lightStatus ? "On" : "Off");

        rollerblindSlider = findViewById(R.id.rollerblindSlider);
        rollerblindSlider.setProgress(rollerblindStatus);
        rollerblindSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                updateRollerblindStatus(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        leftArrowButton = findViewById(R.id.leftArrowBtn);
        leftArrowButton.setOnClickListener((v) -> updateRollerblindStatus(rollerblindStatus - SLIDER_DELTA));
        leftArrowButton.setOnTouchListener(new LongPressListener(handler, SLIDER_DELAY) {
            protected void update() {
                updateRollerblindStatus(rollerblindStatus - SLIDER_DELTA);
            }
        });

        rightArrowButton = findViewById(R.id.rightArrowBtn);
        rightArrowButton.setOnClickListener((v) -> updateRollerblindStatus(rollerblindStatus + SLIDER_DELTA));
        rightArrowButton.setOnTouchListener(new LongPressListener(handler, SLIDER_DELAY) {
            protected void update() {
                updateRollerblindStatus(rollerblindStatus + SLIDER_DELTA);
            }
        });

        rollerblindTextStatus = findViewById(R.id.rollerblindStatus);
        rollerblindTextStatus.setText(rollerblindStatus + "%");

        loadingText = findViewById(R.id.loadingText);
        loadingText.setText("Connecting...");
        loadingText.setVisibility(View.VISIBLE);
        loadingBar = findViewById(R.id.progressBar);
        loadingBar.setVisibility(View.VISIBLE);

        //confirmChangeButton = findViewById(R.id.confirmChangeBtn);
        //confirmChangeButton.setOnClickListener((v) -> sendMessage());
        //confirmChangeButton.setEnabled(false);

        enableInputs(false);
    }

    private void sendMessage(final String message) {
        new Thread(() -> {
            try {
                bluetoothOutputStream.write(message.getBytes(StandardCharsets.UTF_8));
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

    private String formatLightStatus(final boolean lightStatus) {
        return "l:" + lightStatus;
    }

    private String formatRollerblindStatus(final int rollerblindStatus) {
        return "r:" + rollerblindStatus;
    }

    private void updateLightStatus() {
        lightStatus = !lightStatus;
        lightTextStatus.setText(lightStatus ? "On" : "Off");

        if (lightStatus) {
            lightBtn.setBackgroundColor(Color.YELLOW);
            lightBtn.setTextColor(Color.BLACK);
        } else {
            lightBtn.setBackgroundColor(Color.DKGRAY);
            lightBtn.setTextColor(Color.WHITE);
        }

        sendMessage(formatLightStatus(lightStatus));
    }

    private void updateRollerblindStatus(final int value) {
        rollerblindStatus = value;
        if (rollerblindStatus > SLIDER_MAX) rollerblindStatus = SLIDER_MAX;
        if (rollerblindStatus < SLIDER_MIN) rollerblindStatus = SLIDER_MIN;

        rollerblindTextStatus.setText(rollerblindStatus + "%");
        rollerblindSlider.setProgress(rollerblindStatus);

        sendMessage(formatRollerblindStatus(rollerblindStatus));
    }

    private void enableInputs(final boolean enabled) {
        lightBtn.setEnabled(enabled);
        leftArrowButton.setEnabled(enabled);
        rightArrowButton.setEnabled(enabled);
        rollerblindSlider.setEnabled(enabled);
    }
}
