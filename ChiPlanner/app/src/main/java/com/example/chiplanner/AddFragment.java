package com.example.chiplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chiplanner.db.AppDatabase;
import com.example.chiplanner.db.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddFragment extends Fragment {

    private EditText etTaskTitle, etDescription;
    private Button btnSaveTask;
    private TextView tvFragmentTitle;
    private AppDatabase db;
    private int taskId = -1;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = AppDatabase.getDatabase(getContext());
        etTaskTitle = view.findViewById(R.id.etTaskTitle);
        etDescription = view.findViewById(R.id.etDescription);
        btnSaveTask = view.findViewById(R.id.btnSaveTask);
        tvFragmentTitle = view.findViewById(R.id.tvFragmentTitle);

        if (getArguments() != null) {
            taskId = getArguments().getInt("TASK_ID", -1);
        }

        if (taskId != -1) {
            tvFragmentTitle.setText("Edit Task");
            btnSaveTask.setText("UPDATE TASK");
            loadTaskData();
        } else {
            tvFragmentTitle.setText("Add New Task");
            btnSaveTask.setText("SAVE TASK");
        }

        btnSaveTask.setOnClickListener(v -> saveTask());
    }

    private void loadTaskData() {
        executor.execute(() -> {
            Task task = db.taskDao().getTaskById(taskId);
            if (task != null && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    etTaskTitle.setText(task.title);
                    etDescription.setText(task.description);
                });
            }
        });
    }

    private void saveTask() {
        String title = etTaskTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a title", Toast.LENGTH_SHORT).show();
            return;
        }

        executor.execute(() -> {
            if (taskId == -1) { // Create new task
                Task newTask = new Task();
                newTask.title = title;
                newTask.description = description;
                db.taskDao().insertTask(newTask);
            } else { // Update existing task
                Task taskToUpdate = db.taskDao().getTaskById(taskId);
                if (taskToUpdate != null) {
                    taskToUpdate.title = title;
                    taskToUpdate.description = description;
                    db.taskDao().updateTask(taskToUpdate);
                }
            }

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Task saved", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack();
                });
            }
        });
    }
}
