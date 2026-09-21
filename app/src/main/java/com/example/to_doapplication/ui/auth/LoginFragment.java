package com.example.to_doapplication.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.to_doapplication.MainActivity;
import com.example.to_doapplication.auth.AuthManager;
import com.example.to_doapplication.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private AuthManager authManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof AuthActivity) {
            authManager = ((AuthActivity) getActivity()).getAuthManager();
        }

        binding.btnLoginBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        binding.btnLoginSubmit.setOnClickListener(v -> {
            String email = binding.etLoginEmail.getText().toString().trim();
            String password = binding.etLoginPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                showError("Email and Password cannot be blank");
                return;
            }

            if (authManager != null) {
                authManager.loginWithEmail(email, password, new AuthManager.AuthCallback() {
                    @Override
                    public void onSuccess() {
                        if (getActivity() != null) {
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            getActivity().finish();
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        showError(errorMessage);
                    }
                });
            }
        });

        binding.tvSwitchRegister.setOnClickListener(v -> {
            if (getActivity() instanceof AuthActivity) {
                ((AuthActivity) getActivity()).switchFragment(new RegisterFragment());
            }
        });
    }

    private void showError(String msg) {
        if (binding != null) {
            binding.tvLoginError.setVisibility(View.VISIBLE);
            binding.tvLoginError.setText(msg);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
