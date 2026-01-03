package com.example.womenchildsafetyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class WomenActivity extends AppCompatActivity {

    private LinearLayout contactButton; // Declare the button variable
    private LinearLayout safetyTipsButton; // Declare the button variable
    private LinearLayout complaintButton; // Declare the button variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_women); // Ensure this layout file exists

        // Initialize buttons
        contactButton = findViewById(R.id.contact_button); // Ensure this ID exists in your layout
        safetyTipsButton = findViewById(R.id.safety_tips_button); // Ensure this ID exists in your layout
        complaintButton = findViewById(R.id.complaint_button); // Ensure this ID exists in your layout
        Button sosButton = findViewById(R.id.sosButton); // Ensure this ID exists in your layout

        // Set up button click listeners
        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logic for SOS button
                sendSOSNotification();
            }
        });

        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WomenActivity.this, "Contact clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(WomenActivity.this, SaveContactActivity.class);
                startActivity(intent);
            }
        });

        safetyTipsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WomenActivity.this, "Safety Tips clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(WomenActivity.this, TipsActivity.class);
                startActivity(intent);
            }
        });

        complaintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WomenActivity.this, "Complaint clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(WomenActivity.this, ComplaintActivity.class);
                startActivity(intent);
            }
        });
    }

    private void sendSOSNotification() {
        NotificationHelper.sendNotification(this, "SOS Alert", "SOS button pressed! Location shared.");
    }
}