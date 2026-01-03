package com.example.womenchildsafetyapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, phoneEditText, genderEditText, passwordEditText;
    private DBHelper dbHelper;
    private TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DBHelper(this);

        // Initialize views
        initializeViews();

        // Set up click listeners
        setupClickListeners();
    }

    private void initializeViews() {
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        genderEditText = findViewById(R.id.genderEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button registerButton = findViewById(R.id.registerButton);
        loginLink = findViewById(R.id.loginLink);
    }

    private void setupClickListeners() {
        findViewById(R.id.registerButton).setOnClickListener(v -> registerUser());
        findViewById(R.id.loginLink).setOnClickListener(v -> navigateToLogin());
    }

    private void registerUser() {
        // Get all input values
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String gender = genderEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate inputs
        if (!validateInputs(name, email, phone, gender, password)) {
            return;
        }

        // Check if user already exists
        if (dbHelper.checkUser(email)) {
            showError(emailEditText, "Email already registered");
            return;
        }

        // Register the user
        boolean result = dbHelper.insertUser(name, email, phone, gender, password);

        if (result) {
            showSuccessAndNavigate();
        } else {
            showRegistrationError();
        }
    }

    private boolean validateInputs(String name, String email, String phone, String gender, String password) {
        if (name.isEmpty()) {
            showError(nameEditText, "Name is required");
            return false;
        }

        if (email.isEmpty()) {
            showError(emailEditText, "Email is required");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError(emailEditText, "Invalid email format");
            return false;
        }

        if (phone.isEmpty()) {
            showError(phoneEditText, "Phone is required");
            return false;
        }

        if (phone.length() < 10 || !phone.matches("\\d+")) {
            showError(phoneEditText, "Enter valid 10-digit number");
            return false;
        }

        if (gender.isEmpty()) {
            showError(genderEditText, "Gender is required");
            return false;
        }

        if (password.isEmpty()) {
            showError(passwordEditText, "Password is required");
            return false;
        }

        if (password.length() < 6) {
            showError(passwordEditText, "Password too short (min 6 chars)");
            return false;
        }

        return true;
    }

    private void showError(EditText editText, String message) {
        editText.setError(message);
        editText.requestFocus();
    }

    private void showSuccessAndNavigate() {
        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void showRegistrationError() {
        Toast.makeText(this, "Registration failed! Please try again.", Toast.LENGTH_LONG).show();
    }

    private void navigateToLogin() {
        startActivity(new Intent(this, MainActivity.class)); // Changed to LoginActivity
        finish();
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}