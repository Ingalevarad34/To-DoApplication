package com.example.to_doapplication.ui.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.to_doapplication.MainActivity;
import com.example.to_doapplication.data.Task;
import com.example.to_doapplication.databinding.FragmentCalendarBinding;
import com.example.to_doapplication.ui.TaskViewModel;
import com.example.to_doapplication.ui.home.TaskAdapter;
import com.example.to_doapplication.ui.home.TaskDetailActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarFragment extends Fragment implements TaskAdapter.OnTaskClickListener {

    private FragmentCalendarBinding binding;
    private TaskViewModel viewModel;
    private TaskAdapter taskAdapter;
    private CalendarAdapter calendarAdapter;

    private List<Task> allTasks = new ArrayList<>();
    private int selectedTabPosition = 0; // 0: Today, 1: Completed

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof MainActivity) {
            viewModel = ((MainActivity) getActivity()).getViewModel();
        }

        taskAdapter = new TaskAdapter(this);
        binding.rvCalendarTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvCalendarTasks.setAdapter(taskAdapter);

        calendarAdapter = new CalendarAdapter(dateMillis -> applyFilter());
        binding.rvCalendarDates.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvCalendarDates.setAdapter(calendarAdapter);

        binding.tabLayoutCalendar.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedTabPosition = tab.getPosition();
                applyFilter();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        if (viewModel != null) {
            viewModel.getTasks().observe(getViewLifecycleOwner(), tasks -> {
                allTasks = tasks != null ? tasks : new ArrayList<>();
                applyFilter();
            });
        }
    }

    private void applyFilter() {
        if (calendarAdapter == null) return;
        long selectedMillis = calendarAdapter.getSelectedMillis();
        Calendar selCal = Calendar.getInstance();
        selCal.setTimeInMillis(selectedMillis);

        int selYear = selCal.get(Calendar.YEAR);
        int selMonth = selCal.get(Calendar.MONTH);
        int selDay = selCal.get(Calendar.DAY_OF_MONTH);

        List<Task> filtered = new ArrayList<>();

        for (Task task : allTasks) {
            boolean matchesTab = (selectedTabPosition == 0 && !task.isCompleted()) ||
                    (selectedTabPosition == 1 && task.isCompleted());

            boolean matchesDate;
            if (task.getDueDate() != null && task.getDueDate() > 0) {
                Calendar taskCal = Calendar.getInstance();
                taskCal.setTimeInMillis(task.getDueDate());
                matchesDate = (taskCal.get(Calendar.YEAR) == selYear &&
                        taskCal.get(Calendar.MONTH) == selMonth &&
                        taskCal.get(Calendar.DAY_OF_MONTH) == selDay);
            } else {
                Calendar todayCal = Calendar.getInstance();
                matchesDate = (todayCal.get(Calendar.YEAR) == selYear &&
                        todayCal.get(Calendar.MONTH) == selMonth &&
                        todayCal.get(Calendar.DAY_OF_MONTH) == selDay);
            }

            if (matchesTab && matchesDate) {
                filtered.add(task);
            }
        }

        if (filtered.isEmpty() && selectedTabPosition == 1) {
            for (Task task : allTasks) {
                if (task.isCompleted()) {
                    filtered.add(task);
                }
            }
        }

        if (filtered.isEmpty()) {
            binding.layoutCalendarEmpty.setVisibility(View.VISIBLE);
            binding.rvCalendarTasks.setVisibility(View.GONE);
        } else {
            binding.layoutCalendarEmpty.setVisibility(View.GONE);
            binding.rvCalendarTasks.setVisibility(View.VISIBLE);
        }

        taskAdapter.setTasks(filtered);
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
