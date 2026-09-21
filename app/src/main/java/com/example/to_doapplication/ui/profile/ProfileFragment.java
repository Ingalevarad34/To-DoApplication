package com.example.to_doapplication.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.to_doapplication.MainActivity;
import com.example.to_doapplication.auth.AuthManager;
import com.example.to_doapplication.data.Task;
import com.example.to_doapplication.databinding.FragmentProfileBinding;
import com.example.to_doapplication.ui.TaskViewModel;

import java.util.List;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private TaskViewModel viewModel;
    private AuthManager authManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            viewModel = activity.getViewModel();
            authManager = activity.getAuthManager();
        }

        if (authManager != null) {
            String name = authManager.getCurrentUserDisplayName();
            binding.tvProfileName.setText(name != null ? name : "Martha Hays");
        }

        if (viewModel != null) {
            viewModel.getTasks().observe(getViewLifecycleOwner(), tasks -> {
                if (tasks != null) {
                    int left = 0;
                    int done = 0;
                    for (Task t : tasks) {
                        if (t.isCompleted()) done++;
                        else left++;
                    }
                    binding.tvProfileTasksLeft.setText(left + " Task left");
                    binding.tvProfileTasksDone.setText(done + " Task done");
                }
            });
        }

        binding.tvProfileLogout.setOnClickListener(v -> {
            if (viewModel != null) {
                viewModel.signOut();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
