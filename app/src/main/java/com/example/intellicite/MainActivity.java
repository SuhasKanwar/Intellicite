package com.example.intellicite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private TextView welcomeText;
    private Button logoutButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        welcomeText = findViewById(R.id.textViewWelcome);
        logoutButton = findViewById(R.id.buttonLogout);

        if (user != null) {
            welcomeText.setText("Welcome, " + user.getEmail());
        } else {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(MainActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}