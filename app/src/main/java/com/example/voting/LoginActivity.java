package com.example.voting;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;

import com.example.voting.data.repository.AuthRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private MaterialButton loginButton;
    private MaterialButton biometricButton;
    private MaterialButton otpButton;
    private MaterialButton registerButton;
    private FirebaseAuth firebaseAuth;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize views
        initializeViews();
        
        // Setup biometric authentication
        setupBiometricAuth();

        // Setup click listeners
        setupClickListeners();
    }

    private void initializeViews() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        biometricButton = findViewById(R.id.biometricButton);
        otpButton = findViewById(R.id.otpButton);
        registerButton = findViewById(R.id.registerButton);
    }

    private void setupBiometricAuth() {
        Executor executor = this::runOnUiThread;
        biometricPrompt = new BiometricPrompt(this, executor,
            new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    Toast.makeText(LoginActivity.this, 
                        "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    // TODO: Implement biometric login logic
                    Toast.makeText(LoginActivity.this, 
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Toast.makeText(LoginActivity.this, 
                        "Authentication failed", Toast.LENGTH_SHORT).show();
                }
            });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build();
    }

    private void setupClickListeners() {
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, 
                            "Authentication failed: " + task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                    }
                });
        });

        biometricButton.setOnClickListener(v -> {
            BiometricManager biometricManager = BiometricManager.from(this);
            if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                == BiometricManager.BIOMETRIC_SUCCESS) {
                biometricPrompt.authenticate(promptInfo);
            } else {
                Toast.makeText(this, 
                    "Biometric authentication is not available", 
                    Toast.LENGTH_SHORT).show();
            }
        });

        registerButton.setOnClickListener(v -> {
            // TODO: Launch registration activity
            Toast.makeText(this, "Registration coming soon", Toast.LENGTH_SHORT).show();
        });
    }
} 