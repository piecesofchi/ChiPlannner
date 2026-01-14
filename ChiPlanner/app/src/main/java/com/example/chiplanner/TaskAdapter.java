package com.example.chiplanner;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chiplanner.db.AppDatabase;
import com.example.chiplanner.db.Task;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.tvTaskTitle.setText(task.title);
        holder.tvTaskDescription.setText(task.description);
        holder.cbDone.setChecked(task.isCompleted);

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("TASK_ID", task.id);

            AddFragment addFragment = new AddFragment();
            addFragment.setArguments(bundle);

            ((FragmentActivity) v.getContext())
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, addFragment)
                    .addToBackStack(null)
                    .commit();
        });


        holder.cbDone.setOnClickListener(v -> {
            task.isCompleted = holder.cbDone.isChecked();
            executor.execute(() -> {
                AppDatabase.getDatabase(v.getContext()).taskDao().updateTask(task);
                // No Toast here to avoid issues with context from background thread
            });
        });

        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Delete Task")
                    .setMessage("Are you sure you want to delete this task?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        executor.execute(() -> {
                            AppDatabase.getDatabase(v.getContext()).taskDao().deleteTask(task);
                            v.post(() -> {
                                taskList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, taskList.size());
                                Toast.makeText(v.getContext(), "Task deleted", Toast.LENGTH_SHORT).show();
                            });
                        });
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void setTasks(List<Task> tasks) {
        this.taskList = tasks;
        notifyDataSetChanged();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvTaskTitle, tvTaskDescription;
        CheckBox cbDone;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTaskTitle = itemView.findViewById(R.id.tvTaskTitle);
            tvTaskDescription = itemView.findViewById(R.id.tvTaskDescription);
            cbDone = itemView.findViewById(R.id.cbDone);
        }
    }
}
