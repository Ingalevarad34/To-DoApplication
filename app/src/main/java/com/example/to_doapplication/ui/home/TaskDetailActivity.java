package com.example.to_doapplication.ui.home;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.to_doapplication.data.Task;
import com.example.to_doapplication.data.TaskRepository;
import com.example.to_doapplication.databinding.ActivityTaskDetailBinding;
import com.google.firebase.database.FirebaseDatabase;

public class TaskDetailActivity extends AppCompatActivity {

    private ActivityTaskDetailBinding binding;
    private TaskRepository repository;

    private String taskId;
    private String taskTitle;
    private String taskDescription;
    private boolean taskCompleted;
    private String taskUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTaskDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = new TaskRepository(FirebaseDatabase.getInstance().getReference());

        if (getIntent() != null) {
            taskId = getIntent().getStringExtra("task_id");
            taskTitle = getIntent().getStringExtra("task_title");
            taskDescription = getIntent().getStringExtra("task_description");
            taskCompleted = getIntent().getBooleanExtra("task_completed", false);
            taskUserId = getIntent().getStringExtra("task_user_id");
        }

        binding.etDetailTitle.setText(taskTitle != null ? taskTitle : "");
        binding.etDetailDescription.setText(taskDescription != null ? taskDescription : "");
        binding.cbDetailCompleted.setChecked(taskCompleted);

        binding.btnDetailBack.setOnClickListener(v -> finish());

        binding.btnDetailDelete.setOnClickListener(v -> {
            if (taskId != null && taskUserId != null) {
                Task task = new Task(taskId, taskTitle, taskDescription, taskCompleted, taskUserId, 0, null, null, null, null);
                repository.deleteTask(task);
                Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show();
            }
            finish();
        });

        binding.btnDetailSave.setOnClickListener(v -> {
            String newTitle = binding.etDetailTitle.getText().toString().trim();
            String newDesc = binding.etDetailDescription.getText().toString().trim();
            boolean newCompleted = binding.cbDetailCompleted.isChecked();

            if (newTitle.isEmpty()) {
                Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (taskId != null && taskUserId != null) {
                Task task = new Task(taskId, newTitle, newDesc, newCompleted, taskUserId, System.currentTimeMillis(), null, null, null, null);
                repository.updateTask(task);
                Toast.makeText(this, "Task updated", Toast.LENGTH_SHORT).show();
            }
            finish();
        });
    }
}
