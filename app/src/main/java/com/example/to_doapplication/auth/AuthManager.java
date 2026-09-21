package com.example.to_doapplication.auth;

import android.content.Context;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthManager {
    private final FirebaseAuth auth;

    public interface AuthCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    public AuthManager(Context context) {
        this.auth = FirebaseAuth.getInstance();
    }

    public void registerWithEmail(String email, String password, AuthCallback callback) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    String msg = task.getException() != null ? task.getException().getLocalizedMessage() : "Registration failed";
                    callback.onFailure(msg);
                }
            });
    }

    public void loginWithEmail(String email, String password, AuthCallback callback) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    String msg = task.getException() != null ? task.getException().getLocalizedMessage() : "Login failed";
                    callback.onFailure(msg);
                }
            });
    }

    public void signOut() {
        auth.signOut();
    }

    public String getCurrentUserId() {
        FirebaseUser user = auth.getCurrentUser();
        return user != null ? user.getUid() : null;
    }

    public String getCurrentUserDisplayName() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) return null;
        if (user.getDisplayName() != null && !user.getDisplayName().isEmpty()) {
            return user.getDisplayName();
        }
        return user.getEmail();
    }
}
