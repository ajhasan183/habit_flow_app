package com.example.habitflow.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.habitflow.Adapter.TaskAdapter;
import com.example.habitflow.Classes.Task;
import com.example.habitflow.R;
import com.example.habitflow.ViewModels.TaskViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ToDoListFragment extends Fragment {

    private TaskViewModel viewModel;
    private TaskAdapter adapter;
    private EditText editTextTask;
    private Button buttonAddTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_do_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.todoRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new TaskAdapter(task -> viewModel.toggleTaskStatus(task.getId()));
        recyclerView.setAdapter(adapter);

        editTextTask = view.findViewById(R.id.editTextTask);
        buttonAddTask = view.findViewById(R.id.buttonAddTask);

        viewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

        viewModel.getTasks().observe(getViewLifecycleOwner(), tasks -> {
            List<Task> filtered = new ArrayList<>();
            for (Task task : tasks) {
                if (!task.isDone()) filtered.add(task);
            }
            adapter.setTaskList(filtered);
        });

        buttonAddTask.setOnClickListener(v -> {
            String taskTitle = editTextTask.getText().toString().trim();
            if (!taskTitle.isEmpty()) {
                String taskId = UUID.randomUUID().toString();
                Task newTask = new Task(taskId, taskTitle, false);
                viewModel.addTask(newTask);
                editTextTask.setText("");
            } else {
                Toast.makeText(getContext(), "Enter a task", Toast.LENGTH_SHORT).show();
            }
        });

        return view;

    }

    private void getTasksfromFirebase() {

    }

}