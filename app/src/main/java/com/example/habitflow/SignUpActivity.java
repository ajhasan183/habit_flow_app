package com.example.habitflow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.habitflow.Classes.AuthManager;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private EditText FullNameInput;
    private EditText EmailInput;
    private EditText PasswordInput;
    private Button SignUpButton;
    private TextView GoToLoginButton;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FullNameInput = findViewById(R.id.FullName);
        EmailInput = findViewById(R.id.InputEmail);
        PasswordInput = findViewById(R.id.InputPassword);
        SignUpButton = findViewById(R.id.LoginButton_SignUp);
        GoToLoginButton = findViewById(R.id.GoToLogin);


        GoToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        authManager = new AuthManager(this);
        authManager.setInputs(EmailInput, PasswordInput);

        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authManager.setFullName(FullNameInput.getText().toString().trim());

                if (authManager.isValidInput()) {
                    String email = EmailInput.getText().toString().trim();
                    String password = PasswordInput.getText().toString().trim();

                    authManager.signUp(email, password, new AuthManager.AuthCallback() {
                        @Override
                        public void onSuccess(FirebaseUser user) {
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(Exception e) {
                            EmailInput.setError("Sign up failed: " + e.getMessage());
                        }
                    });
                }
            }
        });

    }
}