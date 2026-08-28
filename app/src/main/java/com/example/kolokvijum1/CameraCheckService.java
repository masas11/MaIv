package com.example.kolokvijum1;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class CameraCheckService extends Service {

    public static final String ACTION_CAMERA_ALLOWED = "com.example.kolokvijum1.CAMERA_ALLOWED";
    private static final long CHECK_INTERVAL_MS = 60_000; // jedan minut

    private Handler handler;
    private Runnable checkRunnable;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler(Looper.getMainLooper());

        checkRunnable = new Runnable() {
            @Override
            public void run() {
                checkCameraPermission();
                handler.postDelayed(this, CHECK_INTERVAL_MS);
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.post(checkRunnable);
        return START_STICKY;
    }

    private void checkCameraPermission() {
        boolean granted = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;

        if (granted) {
            Intent intent = new Intent(ACTION_CAMERA_ALLOWED);
            sendBroadcast(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(checkRunnable);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
