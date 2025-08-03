package com.example.habitflow.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.habitflow.Adapter.TaskAdapter;
import com.example.habitflow.Classes.Task;
import com.example.habitflow.R;
import com.example.habitflow.ViewModels.TaskViewModel;

import java.util.ArrayList;
import java.util.List;

public class FinishedListFragment extends Fragment {

    private TaskViewModel viewModel;
    private TaskAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finished_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.finishedRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new TaskAdapter(task -> viewModel.toggleTaskStatus(task.getId()));
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        viewModel.getTasks().observe(getViewLifecycleOwner(), tasks -> {
            List<Task> filtered = new ArrayList<>();
            for (Task task : tasks) {
                if (task.isDone()) filtered.add(task);
            }
            adapter.setTaskList(filtered);
        });


        return view;

    }
}