package com.example.habitflow;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.habitflow.Classes.AuthManager;

public class LoginActivity extends AppCompatActivity {

    private EditText EmailInput;
    private EditText PasswordInput;
    private Button LoginButton;
    private TextView SignUpButton;
    private TextView ForgetPasswordButton;

    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EmailInput = findViewById(R.id.EmailInput);
        PasswordInput = findViewById(R.id.PasswordInput);
        LoginButton = findViewById(R.id.LoginButton);
        SignUpButton = findViewById(R.id.SignUpButton);
        ForgetPasswordButton = findViewById(R.id.ForgetPasswordButton);

        authManager = new AuthManager(this);
        authManager.setInputs(EmailInput, PasswordInput);

        if (authManager.isUserLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        LoginButton.setOnClickListener(v -> {
            String email = EmailInput.getText().toString().trim();
            String password = PasswordInput.getText().toString().trim();

            if (authManager.isValidInput()) {
                authManager.checkIfUserExists(email, password);
            }
        });

        SignUpButton.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });

        ForgetPasswordButton.setOnClickListener(v -> {
            String email = EmailInput.getText().toString().trim();
            if (!email.isEmpty()) {
                authManager.sendPasswordResetEmail(email, new AuthManager.AuthCallback() {
                    @Override
                    public void onSuccess(com.google.firebase.auth.FirebaseUser user) {

                    }

                    @Override
                    public void onFailure(Exception e) {
                        EmailInput.setError("Failed to send reset email: " + e.getMessage());
                    }
                });
            } else {
                EmailInput.setError("Enter your email to reset password");
            }
        });

    }
}