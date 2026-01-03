package com.example.womenchildsafetyapp;



import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MessagingActivity extends AppCompatActivity {
    private Button sendMessageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        sendMessageButton = findViewById(R.id.sendMessageButton);
        sendMessageButton.setOnClickListener(v -> sendMessageToContacts("Help! I need assistance."));
    }

    private void sendMessageToContacts(String message) {
        // Implement messaging logic here
    }
}
