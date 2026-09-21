package com.example.to_doapplication.ui.auth;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.to_doapplication.R;
import com.example.to_doapplication.auth.AuthManager;
import com.example.to_doapplication.databinding.ActivityAuthBinding;

public class AuthActivity extends AppCompatActivity {

    private ActivityAuthBinding binding;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authManager = new AuthManager(this);

        if (savedInstanceState == null) {
            switchFragment(new OnboardingFragment());
        }
    }

    public AuthManager getAuthManager() {
        return authManager;
    }

    public void switchFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.auth_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void navigateToStart() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.auth_container, new StartFragment())
                .commit();
    }
}
