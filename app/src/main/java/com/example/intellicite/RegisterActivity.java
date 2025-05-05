package com.example.intellicite;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intellicite.R;
import com.example.intellicite.api.RetrofitClient;
import com.example.intellicite.models.RegisterRequest;
import com.example.intellicite.models.RegisterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    // UI Components
    private EditText emailInput;
    private EditText nameInput;
    private EditText passwordInput;
    private Button registerButton;
    private ImageButton arrowButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        // Initialize UI components by finding views by ID
        initializeViews();

        // Set up click listeners for buttons
        setupListeners();
    }

    /**
     * Initialize all UI components by finding views by their ID
     */
    private void initializeViews() {
        // Connect Java variables to XML UI elements using findViewById
        emailInput = findViewById(R.id.emailInput);
        nameInput = findViewById(R.id.nameInput);
        passwordInput = findViewById(R.id.passwordInput);
        registerButton = findViewById(R.id.registerButton);
        arrowButton = findViewById(R.id.arrowButton);

        // You'll need to add this ProgressBar to your XML layout
        progressBar = findViewById(R.id.progressBar);
    }

    /**
     * Set up click listeners for buttons
     */
    private void setupListeners() {
        // Back arrow click listener
        arrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Go back to previous screen
            }
        });

        // Register button click listener
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When register button is clicked, validate inputs first
                if (validateInputs()) {
                    // If inputs are valid, proceed with registration
                    registerUser();
                }
            }
        });
    }

    /**
     * Validate user inputs before registration
     */
    private boolean validateInputs() {
        // Get text from EditText fields and trim whitespace
        String email = emailInput.getText().toString().trim();
        String name = nameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Validate email
        if (TextUtils.isEmpty(email)) {
            emailInput.setError("Email is required");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Please enter a valid email address");
            return false;
        }

        // Validate name
        if (TextUtils.isEmpty(name)) {
            nameInput.setError("Name is required");
            return false;
        }

        // Validate password
        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Password is required");
            return false;
        } else if (password.length() < 6) {
            passwordInput.setError("Password must be at least 6 characters");
            return false;
        }

        return true;
    }

    /**
     * Register user by making API call
     */
    private void registerUser() {
        // Get inputs from UI
        String email = emailInput.getText().toString().trim();
        String fullName = nameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Show progress indicator while API call is in progress
        progressBar.setVisibility(View.VISIBLE);

        // Create registration request model
        RegisterRequest registerRequest = new RegisterRequest(fullName, email, password);

        // Make API call using Retrofit
        RetrofitClient.getInstance(this)
                .getApiService()
                .registerUser(registerRequest)
                .enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                        // Hide progress indicator when API response is received
                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful() && response.body() != null) {
                            RegisterResponse registerResponse = response.body();

                            if (registerResponse.isSuccess()) {
                                // Registration successful
                                Toast.makeText(RegisterActivity.this, registerResponse.getMessage(), Toast.LENGTH_SHORT).show();

                                // Registration successful, redirect to login screen
                                redirectToLogin();
                            } else {
                                // Registration failed with API error
                                Toast.makeText(RegisterActivity.this, registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Handle HTTP error responses
                            try {
                                String errorMessage = response.errorBody() != null ?
                                        response.errorBody().string() : "Unknown error occurred";
                                Log.e(TAG, "Registration Error: " + errorMessage);
                                Toast.makeText(RegisterActivity.this, "Registration failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Log.e(TAG, "Error parsing error response", e);
                                Toast.makeText(RegisterActivity.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        // Hide progress indicator when network call fails
                        progressBar.setVisibility(View.GONE);

                        // Log error and show message to user
                        Log.e(TAG, "Network Error: " + t.getMessage());
                        Toast.makeText(RegisterActivity.this, "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Navigate to login screen after successful registration
     */
    private void redirectToLogin() {
        // Create intent to navigate to login activity
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Close registration activity
    }
}