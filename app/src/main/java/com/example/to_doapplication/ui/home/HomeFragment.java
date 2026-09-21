package com.example.to_doapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.to_doapplication.MainActivity;
import com.example.to_doapplication.data.Task;
import com.example.to_doapplication.databinding.FragmentIndexBinding;
import com.example.to_doapplication.ui.TaskViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements TaskAdapter.OnTaskClickListener {

    private FragmentIndexBinding binding;
    private TaskViewModel viewModel;
    private TaskAdapter adapter;
    private List<Task> allTasks = new ArrayList<>();
    private String currentSearch = "";
    private String currentFilter = "Today";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentIndexBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof MainActivity) {
            viewModel = ((MainActivity) getActivity()).getViewModel();
        }

        adapter = new TaskAdapter(this);
        binding.rvTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvTasks.setAdapter(adapter);

        binding.btnMenu.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).openDrawer();
            }
        });

        // Filter Spinner
        String[] filters = new String[]{"Today", "Completed", "All Tasks"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, filters);
        binding.spinnerFilter.setAdapter(spinnerAdapter);
        binding.spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentFilter = filters[position];
                applyFilter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Search Bar
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentSearch = s != null ? s.toString().trim() : "";
                applyFilter();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        if (viewModel != null) {
            viewModel.getTasks().observe(getViewLifecycleOwner(), tasks -> {
                allTasks = tasks != null ? tasks : new ArrayList<>();
                applyFilter();
            });
        }
    }

    private void applyFilter() {
        List<Task> filtered = new ArrayList<>();
        
        for (Task task : allTasks) {
            boolean matchesSearch = currentSearch.isEmpty() ||
                    (task.getTitle() != null && task.getTitle().toLowerCase().contains(currentSearch.toLowerCase())) ||
                    (task.getDescription() != null && task.getDescription().toLowerCase().contains(currentSearch.toLowerCase()));

            if (!matchesSearch) continue;

            if (!currentSearch.isEmpty()) {
                filtered.add(task);
            } else {
                if ("Today".equals(currentFilter) && !task.isCompleted()) {
                    filtered.add(task);
                } else if ("Completed".equals(currentFilter) && task.isCompleted()) {
                    filtered.add(task);
                } else if ("All Tasks".equals(currentFilter)) {
                    filtered.add(task);
                }
            }
        }

        if (allTasks.isEmpty()) {
            binding.layoutEmptyState.setVisibility(View.VISIBLE);
            binding.tvEmptyText.setText("What do you want to do today?");
            binding.tvEmptySubtext.setText("Tap + to add your tasks");
            binding.rvTasks.setVisibility(View.GONE);
        } else if (filtered.isEmpty()) {
            binding.layoutEmptyState.setVisibility(View.VISIBLE);
            binding.tvEmptyText.setText("No task pending");
            binding.tvEmptySubtext.setText("");
            binding.rvTasks.setVisibility(View.GONE);
        } else {
            binding.layoutEmptyState.setVisibility(View.GONE);
            binding.rvTasks.setVisibility(View.VISIBLE);
        }

        adapter.setTasks(filtered);
    }

    @Override
    public void onTaskClick(Task task) {
        Intent intent = new Intent(getContext(), TaskDetailActivity.class);
        intent.putExtra("task_id", task.getId());
        intent.putExtra("task_title", task.getTitle());
        intent.putExtra("task_description", task.getDescription());
        intent.putExtra("task_completed", task.isCompleted());
        intent.putExtra("task_user_id", task.getUserId());
        startActivity(intent);
    }

    @Override
    public void onToggleCompletion(Task task) {
        if (viewModel != null) {
            viewModel.toggleTaskCompletion(task);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
