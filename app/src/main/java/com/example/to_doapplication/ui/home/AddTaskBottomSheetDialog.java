package com.example.to_doapplication.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.to_doapplication.databinding.BottomSheetAddTaskBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.MaterialDatePicker;

public class AddTaskBottomSheetDialog extends BottomSheetDialogFragment {

    public interface OnTaskSaveListener {
        void onSave(String title, String description, String categoryId, Integer priority, Long dueDate, Long dueTime);
    }

    private BottomSheetAddTaskBinding binding;
    private OnTaskSaveListener saveListener;

    private String selectedCategory = null;
    private Integer selectedPriority = null;
    private Long selectedDueDate = null;

    public AddTaskBottomSheetDialog() {}

    public AddTaskBottomSheetDialog(OnTaskSaveListener saveListener) {
        this.saveListener = saveListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetAddTaskBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnPickDate.setOnClickListener(v -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select Due Date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build();

            datePicker.addOnPositiveButtonClickListener(selection -> {
                selectedDueDate = selection;
                Toast.makeText(getContext(), "Date Selected", Toast.LENGTH_SHORT).show();
            });

            datePicker.show(getParentFragmentManager(), "DATE_PICKER");
        });

        binding.btnPickCategory.setOnClickListener(v -> {
            String[] categories = new String[]{"Work", "Personal", "University", "Home"};
            new AlertDialog.Builder(requireContext())
                    .setTitle("Select Category")
                    .setItems(categories, (dialog, which) -> {
                        selectedCategory = categories[which];
                        Toast.makeText(getContext(), "Category: " + selectedCategory, Toast.LENGTH_SHORT).show();
                    })
                    .show();
        });

        binding.btnPickPriority.setOnClickListener(v -> {
            String[] priorities = new String[]{"Priority 1", "Priority 2", "Priority 3", "Priority 4", "Priority 5"};
            new AlertDialog.Builder(requireContext())
                    .setTitle("Select Priority")
                    .setItems(priorities, (dialog, which) -> {
                        selectedPriority = which + 1;
                        Toast.makeText(getContext(), "Priority: " + selectedPriority, Toast.LENGTH_SHORT).show();
                    })
                    .show();
        });

        binding.btnSendTask.setOnClickListener(v -> {
            String title = binding.etAddTitle.getText().toString().trim();
            String desc = binding.etAddDescription.getText().toString().trim();

            if (title.isEmpty()) {
                Toast.makeText(getContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (saveListener != null) {
                saveListener.onSave(title, desc, selectedCategory, selectedPriority, selectedDueDate, selectedDueDate);
            }
            dismiss();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
