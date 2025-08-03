package com.example.habitflow.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitflow.Classes.Task;
import com.example.habitflow.R;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    public interface OnTaskClickListener {
        void onTaskToggle(Task task); // renamed for clarity
    }

    private List<Task> taskList = new ArrayList<>();
    private final OnTaskClickListener listener;

    public TaskAdapter(OnTaskClickListener listener) {
        this.listener = listener;
    }

    public void setTaskList(List<Task> list) {
        taskList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.title.setText(task.getTitle());
        holder.checkBox.setChecked(task.isDone());

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(task.isDone());
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (task.isDone() != isChecked) {
                listener.onTaskToggle(task);
            }
        });

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        CheckBox checkBox;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.taskTitle);
            checkBox = itemView.findViewById(R.id.taskCheckBox);
        }
    }
}
