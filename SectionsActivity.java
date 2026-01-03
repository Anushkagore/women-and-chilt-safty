package com.example.womenchildsafetyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class SectionsActivity extends AppCompatActivity {

    private ImageButton women_Button;
    private ImageButton child_Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sections);

        women_Button = findViewById(R.id.women_button);
        child_Button = findViewById(R.id.child_button);

        women_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Women section
                Intent intent = new Intent(SectionsActivity.this, WomenActivity.class);
                startActivity(intent);
            }
        });

        child_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Child section
                Intent intent = new Intent(SectionsActivity.this, ChildActivity.class);
                startActivity(intent);
            }
        });
    }
}