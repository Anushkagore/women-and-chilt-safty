package com.example.womenchildsafetyapp;


import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class LocationActivity extends AppCompatActivity {
    private Button shareLocationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        shareLocationButton = findViewById(R.id.shareLocationButton);
        shareLocationButton.setOnClickListener(v -> shareLocation());
    }

    private void shareLocation() {
        // Implement location sharing logic here
    }
}