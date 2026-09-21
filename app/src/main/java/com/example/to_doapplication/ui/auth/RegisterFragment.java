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
import com.example.to_doapplication.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private AuthManager authManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof AuthActivity) {
            authManager = ((AuthActivity) getActivity()).getAuthManager();
        }

        binding.btnRegisterBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        binding.btnRegisterSubmit.setOnClickListener(v -> {
            String email = binding.etRegisterEmail.getText().toString().trim();
            String password = binding.etRegisterPassword.getText().toString().trim();
            String confirmPassword = binding.etRegisterConfirmPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                showError("Fields cannot be blank");
                return;
            }

            if (!password.equals(confirmPassword)) {
                showError("Passwords do not match");
                return;
            }

            if (authManager != null) {
                authManager.registerWithEmail(email, password, new AuthManager.AuthCallback() {
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

        binding.tvSwitchLogin.setOnClickListener(v -> {
            if (getActivity() instanceof AuthActivity) {
                ((AuthActivity) getActivity()).switchFragment(new LoginFragment());
            }
        });
    }

    private void showError(String msg) {
        if (binding != null) {
            binding.tvRegisterError.setVisibility(View.VISIBLE);
            binding.tvRegisterError.setText(msg);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
