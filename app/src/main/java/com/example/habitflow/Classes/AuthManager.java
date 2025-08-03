package com.example.habitflow.Classes;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.habitflow.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AuthManager {

    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firestore;
    private final Activity activity;
    private EditText emailInput;
    private EditText passwordInput;
    private String fullName;

    public AuthManager(Activity activity) {
        this.activity = activity;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.firestore = FirebaseFirestore.getInstance();
    }

    public void setInputs(EditText emailInput, EditText passwordInput) {
        this.emailInput = emailInput;
        this.passwordInput = passwordInput;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isValidInput() {
        return isValidEmail() & isValidPassword();
    }

    private boolean isValidEmail() {
        String email = emailInput.getText().toString().trim();
        if (email.isEmpty()) {
            emailInput.setError("Email is required");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Invalid email format");
            return false;
        } else {
            emailInput.setError(null);
            return true;
        }
    }

    private boolean isValidPassword() {
        String password = passwordInput.getText().toString().trim();
        if (password.isEmpty()) {
            passwordInput.setError("Password is required");
            return false;
        } else {
            passwordInput.setError(null);
            return true;
        }
    }

    public void signUp(String email, String password, AuthCallback callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        Log.d("SignUp", "Sign up successful");
                        FirebaseUser user = firebaseAuth.getCurrentUser();

                        Log.d("SignUp", "User UID: " + user.getUid());
                        Log.d("SignUp", "User Email: " + user.getEmail());
                        Log.d("SignUp", "User Full Name: " + fullName);
                        Log.d("SignUp", "User Created At: " + FieldValue.serverTimestamp());


                        if (user != null) {
                            addUserToFirestore(user);
                        }
                        callback.onSuccess(user);
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    public void sendPasswordResetEmail(String email, AuthCallback callback) {
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(null);
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    public boolean isUserLoggedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }

    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    public void signOut() {
        firebaseAuth.signOut();
    }

    public void checkIfUserExists(String email, String password) {
        firestore.collection("Users")
                .whereEqualTo("Email", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        authenticateUser(email, password);
                    } else {
                        emailInput.setError("No user account found for this email");
                        Toast.makeText(activity, "Email not registered as a user.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(activity, "Error checking user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Error checking user existence", e);
                });
    }

    private void authenticateUser(String email, String password) {
        emailInput.setError(null);
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d("Login", "Login successful");
                        Toast.makeText(activity, "Logged In successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity, MainActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Login", "Login failed: " + e.getMessage());
                        emailInput.setError("Email doesn't have an account");
                        Toast.makeText(activity, "Login failed. Check your credentials.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addUserToFirestore(FirebaseUser user) {
        if (user == null || fullName == null || fullName.trim().isEmpty()) {
            Log.e("Firestore", "User or full name is null, skipping Firestore add");
            return;
        }

        Map<String, Object> userData = new HashMap<>();
        userData.put("UID", user.getUid());
        userData.put("Email", user.getEmail());
        userData.put("FullName", fullName);
        userData.put("CreatedAt", FieldValue.serverTimestamp());

        firestore.collection("Users")
                .document(user.getUid())
                .set(userData)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "User successfully added to Firestore"))
                .addOnFailureListener(e -> Log.e("Firestore", "Failed to add user to Firestore", e));
    }

    public interface AuthCallback {
        void onSuccess(FirebaseUser user);
        void onFailure(Exception e);
    }
}

