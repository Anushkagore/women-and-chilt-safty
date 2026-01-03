package com.example.womenchildsafetyapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ChildActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        // Create the notification channel
        NotificationHelper.createNotificationChannel(this);

        // Button to send a notification
        Button checkInButton = findViewById(R.id.checkInButton);
        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send a notification when the button is clicked
                NotificationHelper.sendNotification(ChildActivity.this, "Check-In", "You have checked in safely.");
            }
        });
    }
}