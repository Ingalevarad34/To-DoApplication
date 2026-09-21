package com.example.to_doapplication.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    private final DatabaseReference databaseReference;
    private final MutableLiveData<List<Task>> tasksLiveData = new MutableLiveData<>(new ArrayList<>());
    private ValueEventListener tasksListener;

    public TaskRepository(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public LiveData<List<Task>> getTasksForUser(String userId) {
        if (userId == null || userId.isEmpty()) {
            tasksLiveData.setValue(new ArrayList<>());
            return tasksLiveData;
        }

        DatabaseReference tasksRef = databaseReference.child("users").child(userId).child("tasks");
        
        if (tasksListener != null) {
            tasksRef.removeEventListener(tasksListener);
        }

        tasksListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Task> tasks = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Task task = child.getValue(Task.class);
                    if (task != null) {
                        if (task.getUserId() == null || task.getUserId().isEmpty()) {
                            task.setUserId(userId);
                        }
                        tasks.add(task);
                    }
                }
                tasksLiveData.setValue(tasks);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // handle cancelled
            }
        };

        tasksRef.addValueEventListener(tasksListener);
        return tasksLiveData;
    }

    public void insertTask(Task task) {
        if (task.getUserId() != null && !task.getUserId().isEmpty()) {
            databaseReference.child("users").child(task.getUserId()).child("tasks").child(task.getId()).setValue(task);
        }
    }

    public void updateTask(Task task) {
        if (task.getUserId() != null && !task.getUserId().isEmpty()) {
            databaseReference.child("users").child(task.getUserId()).child("tasks").child(task.getId()).setValue(task);
        }
    }

    public void deleteTask(Task task) {
        if (task.getUserId() != null && !task.getUserId().isEmpty()) {
            databaseReference.child("users").child(task.getUserId()).child("tasks").child(task.getId()).removeValue();
        }
    }
}
