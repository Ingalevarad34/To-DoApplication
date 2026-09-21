package com.example.to_doapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.example.to_doapplication.auth.AuthManager;
import com.example.to_doapplication.data.Task;
import com.example.to_doapplication.data.TaskRepository;
import com.example.to_doapplication.databinding.ActivityMainBinding;
import com.example.to_doapplication.ui.TaskViewModel;
import com.example.to_doapplication.ui.auth.AuthActivity;
import com.example.to_doapplication.ui.calendar.CalendarFragment;
import com.example.to_doapplication.ui.focus.FocusFragment;
import com.example.to_doapplication.ui.home.AddTaskBottomSheetDialog;
import com.example.to_doapplication.ui.home.HomeFragment;
import com.example.to_doapplication.ui.profile.ProfileFragment;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private AuthManager authManager;
    private TaskRepository repository;
    private TaskViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authManager = new AuthManager(this);
        repository = new TaskRepository(FirebaseDatabase.getInstance().getReference());
        viewModel = new TaskViewModel(repository, authManager);

        viewModel.getIsUserLoggedIn().observe(this, isLoggedIn -> {
            if (isLoggedIn == null || !isLoggedIn) {
                startActivity(new Intent(MainActivity.this, AuthActivity.class));
                finish();
            }
        });

        setupNavigation();
        setupDrawerHeader();

        // Default tab
        if (savedInstanceState == null) {
            switchFragment(new HomeFragment());
        }

        binding.fabAddTask.setOnClickListener(v -> {
            AddTaskBottomSheetDialog bottomSheet = new AddTaskBottomSheetDialog((title, description, categoryId, priority, dueDate, dueTime) -> {
                viewModel.addTask(title, description, categoryId, priority, dueDate, dueTime);
            });
            bottomSheet.show(getSupportFragmentManager(), "ADD_TASK_SHEET");
        });
    }

    public TaskViewModel getViewModel() {
        return viewModel;
    }

    public AuthManager getAuthManager() {
        return authManager;
    }

    public void openDrawer() {
        if (binding != null && binding.drawerLayout != null) {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    private void setupNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_index) {
                switchFragment(new HomeFragment());
                return true;
            } else if (itemId == R.id.nav_calendar) {
                switchFragment(new CalendarFragment());
                return true;
            } else if (itemId == R.id.nav_focus) {
                switchFragment(new FocusFragment());
                return true;
            } else if (itemId == R.id.nav_profile) {
                switchFragment(new ProfileFragment());
                return true;
            }
            return false;
        });
    }

    private void setupDrawerHeader() {
        View headerView = binding.navigationView.getHeaderView(0);
        if (headerView != null) {
            TextView tvName = headerView.findViewById(R.id.drawer_user_name);
            TextView tvLeft = headerView.findViewById(R.id.drawer_tasks_left);
            TextView tvDone = headerView.findViewById(R.id.drawer_tasks_done);
            TextView tvLogout = headerView.findViewById(R.id.drawer_logout);

            if (tvName != null && authManager != null) {
                String name = authManager.getCurrentUserDisplayName();
                tvName.setText(name != null ? name : "Martha Hays");
            }

            if (viewModel != null) {
                viewModel.getTasks().observe(this, tasks -> {
                    if (tasks != null) {
                        int left = 0, done = 0;
                        for (Task t : tasks) {
                            if (t.isCompleted()) done++;
                            else left++;
                        }
                        if (tvLeft != null) tvLeft.setText(left + " Task left");
                        if (tvDone != null) tvDone.setText(done + " Task done");
                    }
                });
            }

            if (tvLogout != null) {
                tvLogout.setOnClickListener(v -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                    viewModel.signOut();
                });
            }
        }
    }

    private void switchFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
