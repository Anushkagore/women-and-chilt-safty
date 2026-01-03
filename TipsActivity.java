package com.example.womenchildsafetyapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class TipsActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private ToggleButton shakeToggle;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int SHAKE_THRESHOLD = 800;
    private float lastX, lastY, lastZ;
    private long lastUpdate;

//    private ImageView safetyImage;
    private TextView safetyDescription;
    private Button openYouTubeButton;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "ShakePrefs";
    private static final String SHAKE_TOGGLE_KEY = "shake_toggle_state";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        // Initialize shared preferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Initialize shake detection components
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize safety tips components
//        safetyImage = findViewById(R.id.ic_safety_image);
        safetyDescription = findViewById(R.id.safety_description);
        openYouTubeButton = findViewById(R.id.open_youtube_button);
        shakeToggle = findViewById(R.id.shake_toggle);

        // Restore the toggle button state
        boolean isShakeEnabled = sharedPreferences.getBoolean(SHAKE_TOGGLE_KEY, false);
        shakeToggle.setChecked(isShakeEnabled);

        // Set the safety tip image
//        safetyImage.setImageResource(R.drawable.safety_image); // Replace with your image resources

        // Set an OnClickListener on the button to open the YouTube link
        openYouTubeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openYouTubeVideo();
            }
        });

        // Set an OnCheckedChangeListener on the toggle button
        shakeToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save the toggle state
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(SHAKE_TOGGLE_KEY, isChecked);
            editor.apply();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        long curTime = System.currentTimeMillis();
        if ((curTime - lastUpdate) > 100) {
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float speed = Math.abs(x + y + z - lastX - lastY - lastZ) / diffTime * 10000;

            if (speed > SHAKE_THRESHOLD && shakeToggle.isChecked()) {
                shareLocationAndSaveContact();
            }

            lastX = x;
            lastY = y;
            lastZ = z;
        }
    }

    private void shareLocationAndSaveContact() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        String message = "I am in danger! My location is: " + location.getLatitude() + ", " + location.getLongitude();
                        sendSMS("recipient_phone_number", message); // Replace with actual phone number
                    }
                });
    }

    private void sendSMS(String phoneNumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }

    private void openYouTubeVideo() {
        String videoUrl = "https://www.youtube.com/watch?v=EgTTmcVY5lk"; // Replace with your YouTube video link
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
        startActivity(intent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }
}