package com.example.intellicite;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intellicite.api.ApiService;
import com.example.intellicite.api.RetrofitClient;
import com.example.intellicite.models.LoginRequest;
import com.example.intellicite.models.LoginResponse;
import com.example.intellicite.utils.SessionManager;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;
    private ImageButton backButton;
    private ProgressBar progressBar;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        // Initialize views
        initViews();

        // Set up click listeners
        setupClickListeners();
    }

    private void initViews() {
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.registerButton); // ID in XML is registerButton but used for login
        backButton = findViewById(R.id.arrowButton);
        progressBar = findViewById(R.id.progressBar);

        // Initialize session manager
        sessionManager = new SessionManager(this);

        // Check if user is already logged in
        if (sessionManager.isLoggedIn()) {
            navigateToMainScreen();
        }
    }

    private void setupClickListeners() {
        // Back button click listener
        backButton.setOnClickListener(v -> onBackPressed());

        // Login button click listener
        loginButton.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        // Reset errors
        emailInput.setError(null);
        passwordInput.setError(null);

        // Get input values
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Validate input
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password
        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Password is required");
            focusView = passwordInput;
            cancel = true;
        }

        // Check for a valid email address
        if (TextUtils.isEmpty(email)) {
            emailInput.setError("Email is required");
            focusView = emailInput;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailInput.setError("Invalid email format");
            focusView = emailInput;
            cancel = true;
        }

        if (cancel) {
            // There was an error; focus the first form field with an error
            focusView.requestFocus();
        } else {
            // Show progress and perform the login attempt
            performLogin(email, password);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".");
    }

    private void performLogin(String email, String password) {
        // Create the login request model
        LoginRequest loginRequest = new LoginRequest(email, password);

        // Get API service
        ApiService apiService = RetrofitClient.getInstance(this).getApiService();

        // Show loading indicator
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);

        // Make API call
        Call<LoginResponse> call = apiService.loginUser(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                // Hide loading indicator
                progressBar.setVisibility(View.GONE);
                loginButton.setEnabled(true);

                // Debug: Log all headers to see what we're getting
                Headers headers = response.headers();
                for (String name : headers.names()) {
                    Log.d(TAG, "Header: " + name + " = " + headers.get(name));
                }

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    // Debug: Log response body
                    Log.d(TAG, "Response success: " + loginResponse.isSuccess());
                    Log.d(TAG, "Response message: " + loginResponse.getMessage());
                    if (loginResponse.getToken() != null) {
                        Log.d(TAG, "Response token: " + loginResponse.getToken());
                    }

                    if (loginResponse.isSuccess()) {
                        // Login successful
                        Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();

                        // First, check if token is in the response body
                        String token = loginResponse.getToken();

                        // If not in body, check common header locations
                        if (token == null || token.isEmpty()) {
                            // Try various header names where token might be found
                            token = response.headers().get("Authorization");

                            if (token == null || token.isEmpty()) {
                                token = response.headers().get("Bearer");
                            }

                            if (token == null || token.isEmpty()) {
                                token = response.headers().get("Token");
                            }

                            if (token == null || token.isEmpty()) {
                                token = response.headers().get("X-Auth-Token");
                            }

                            // Remove "Bearer " prefix if present
                            if (token != null && token.startsWith("Bearer ")) {
                                token = token.substring(7);
                            }
                        }

                        if (token != null && !token.isEmpty()) {
                            // Save login state and token
                            sessionManager.saveAuthToken(token);

                            // Save user details if available
                            if (loginResponse.getUser() != null) {
                                LoginResponse.UserDetails user = loginResponse.getUser();
                                sessionManager.saveUserDetails(
                                        user.getId(),
                                        user.getName(),
                                        user.getEmail()
                                );
                                Log.d(TAG, "User name saved: " + user.getName());
                            } else {
                                // For backward compatibility if the API doesn't provide user details yet
                                // We store the email as both ID and email, and use a generic name
                                sessionManager.saveUserDetails(email, "User", email);
                                Log.d(TAG, "No user details in response, using defaults");
                            }

                            navigateToMainScreen();
                        } else {
                            // No token found, but login was successful according to the API
                            // Contact backend team to understand how to retrieve the token
                            Log.e(TAG, "Login successful but no token received");
                            Toast.makeText(LoginActivity.this,
                                    "Authentication error: No token received. Please contact support.",
                                    Toast.LENGTH_LONG).show();

                            // For debug - show all headers received
                            StringBuilder headerDebug = new StringBuilder("Headers received:\n");
                            for (String name : response.headers().names()) {
                                headerDebug.append(name).append(": ").append(response.headers().get(name)).append("\n");
                            }
                            Log.d(TAG, headerDebug.toString());
                        }
                    } else {
                        // Login failed with an error message from the server
                        Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Handle API error responses
                    try {
                        String errorBody = response.errorBody() != null ?
                                response.errorBody().string() : "Unknown error occurred";
                        Log.e(TAG, "API Error: " + errorBody);
                        Toast.makeText(LoginActivity.this, errorBody, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing error response", e);
                        Toast.makeText(LoginActivity.this,
                                "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Hide loading indicator
                progressBar.setVisibility(View.GONE);
                loginButton.setEnabled(true);

                // Network or other errors
                Log.e(TAG, "Network Error", t);
                Toast.makeText(LoginActivity.this,
                        "Connection error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void navigateToMainScreen() {
        // Navigate to main activity
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}