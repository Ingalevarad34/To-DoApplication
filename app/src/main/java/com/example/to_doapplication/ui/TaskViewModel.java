package com.example.to_doapplication.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.to_doapplication.auth.AuthManager;
import com.example.to_doapplication.data.Task;
import com.example.to_doapplication.data.TaskRepository;

import java.util.ArrayList;
import java.util.List;

public class TaskViewModel extends ViewModel {
    private final TaskRepository repository;
    private final AuthManager authManager;

    private LiveData<List<Task>> tasksLiveData;
    private final MutableLiveData<Boolean> isUserLoggedIn = new MutableLiveData<>(false);

    public TaskViewModel(TaskRepository repository, AuthManager authManager) {
        this.repository = repository;
        this.authManager = authManager;
        loadTasks();
    }

    public LiveData<List<Task>> getTasks() {
        if (tasksLiveData == null) {
            loadTasks();
        }
        return tasksLiveData;
    }

    public LiveData<Boolean> getIsUserLoggedIn() {
        return isUserLoggedIn;
    }

    public void loadTasks() {
        String userId = authManager.getCurrentUserId();
        if (userId != null) {
            isUserLoggedIn.setValue(true);
            tasksLiveData = repository.getTasksForUser(userId);
        } else {
            isUserLoggedIn.setValue(false);
            tasksLiveData = new MutableLiveData<>(new ArrayList<>());
        }
    }

    public void addTask(String title, String description, String categoryId, Integer priority, Long dueDate, Long dueTime) {
        String userId = authManager.getCurrentUserId();
        if (userId == null) return;

        Task task = new Task(
                null,
                title,
                description,
                false,
                userId,
                System.currentTimeMillis(),
                categoryId,
                priority,
                dueDate,
                dueTime
        );
        repository.insertTask(task);
    }

    public void toggleTaskCompletion(Task task) {
        if (task == null) return;
        task.setCompleted(!task.isCompleted());
        repository.updateTask(task);
    }

    public void updateTask(Task task) {
        if (task == null) return;
        repository.updateTask(task);
    }

    public void deleteTask(Task task) {
        if (task == null) return;
        repository.deleteTask(task);
    }

    public void signOut() {
        authManager.signOut();
        isUserLoggedIn.setValue(false);
    }
}
