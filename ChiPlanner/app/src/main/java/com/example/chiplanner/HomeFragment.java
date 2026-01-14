package com.example.chiplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chiplanner.db.AppDatabase;
import com.example.chiplanner.db.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {

    private RecyclerView tasksRecyclerView;
    private TaskAdapter taskAdapter;
    private AppDatabase db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        db = AppDatabase.getDatabase(getContext());
        tasksRecyclerView = view.findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskAdapter = new TaskAdapter(new ArrayList<>());
        tasksRecyclerView.setAdapter(taskAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTasks();
    }

    private void loadTasks() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Task> tasks = db.taskDao().getAllTasks();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    taskAdapter.setTasks(tasks);
                });
            }
        });
    }
}
