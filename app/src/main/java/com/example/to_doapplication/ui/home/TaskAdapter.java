package com.example.to_doapplication.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_doapplication.data.Task;
import com.example.to_doapplication.databinding.ItemTaskBinding;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    public interface OnTaskClickListener {
        void onTaskClick(Task task);
        void onToggleCompletion(Task task);
    }

    private List<Task> tasks = new ArrayList<>();
    private final OnTaskClickListener listener;

    public TaskAdapter(OnTaskClickListener listener) {
        this.listener = listener;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks != null ? tasks : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTaskBinding binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TaskViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        private final ItemTaskBinding binding;

        public TaskViewHolder(@NonNull ItemTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Task task) {
            binding.tvTaskTitle.setText(task.getTitle());
            binding.tvTaskTime.setText("Today At 16:45");
            binding.cbTaskComplete.setChecked(task.isCompleted());

            if (task.getCategoryId() != null && !task.getCategoryId().isEmpty()) {
                binding.tvTaskCategory.setVisibility(View.VISIBLE);
                binding.tvTaskCategory.setText(task.getCategoryId());
            } else {
                binding.tvTaskCategory.setVisibility(View.GONE);
            }

            if (task.getPriority() != null) {
                binding.tvTaskPriority.setVisibility(View.VISIBLE);
                binding.tvTaskPriority.setText("⚑ " + task.getPriority());
            } else {
                binding.tvTaskPriority.setVisibility(View.GONE);
            }

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) listener.onTaskClick(task);
            });

            binding.cbTaskComplete.setOnClickListener(v -> {
                if (listener != null) listener.onToggleCompletion(task);
            });
        }
    }
}
