package com.example.habitflow.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitflow.Adapter.RecordingAdapter;
import com.example.habitflow.R;
import com.example.habitflow.ViewModels.TaskViewModel;
import com.example.habitflow.ViewModels.VoiceLogViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DashboardFragment extends Fragment {

    private TextView greetingText, motivationText, taskProgress, streakText, voiceLogCount;
    private RecyclerView recordingsRecyclerView;
    private RecordingAdapter recordingAdapter;
    private Button addTaskButton, recordAudioButton;

    private TaskViewModel taskViewModel;
    private VoiceLogViewModel voiceLogViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        greetingText = view.findViewById(R.id.greetingText);
        motivationText = view.findViewById(R.id.motivationText);
        taskProgress = view.findViewById(R.id.taskProgress);
        streakText = view.findViewById(R.id.streakText);
        voiceLogCount = view.findViewById(R.id.voiceLogCount);

        addTaskButton = view.findViewById(R.id.addTaskButton);
        recordAudioButton = view.findViewById(R.id.recordAudioButton);

        recordingsRecyclerView = view.findViewById(R.id.recordingsRecyclerView);
        recordingsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recordingAdapter = new RecordingAdapter(getContext());
        recordingsRecyclerView.setAdapter(recordingAdapter);

        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        voiceLogViewModel = new ViewModelProvider(requireActivity()).get(VoiceLogViewModel.class);

        setupGreeting();

        taskViewModel.getTasks().observe(getViewLifecycleOwner(), tasks -> {
            int completed = 0;
            for (var task : tasks) {
                if (task.isDone()) completed++;
            }
            taskProgress.setText("Tasks Completed: " + completed + " / " + tasks.size());
        });

        streakText.setText("🔥 1-day streak");

        voiceLogViewModel.getTodayLogs().observe(getViewLifecycleOwner(), logs -> {
            voiceLogCount.setText("🎙️ " + logs.size() + " recording today");
            recordingAdapter.setRecordings(logs);
        });

        addTaskButton.setOnClickListener(v -> {
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();

            BottomNavigationView nav = requireActivity().findViewById(R.id.bottom_navigation);
            nav.setSelectedItemId(R.id.nav_home);
        });

        recordAudioButton.setOnClickListener(v -> {
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();

            BottomNavigationView nav = requireActivity().findViewById(R.id.bottom_navigation);
            nav.setSelectedItemId(R.id.nav_recorder);
        });


        return view;
    }

    private void setupGreeting() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Users")
                .document(auth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String name = task.getResult().getString("FullName");
                        Log.d("DashboardFragment", "User name: " + name);
                        if (name == null) name = "User";

                        String greeting = "Hi, " + name + " 👋";
                        greetingText.setText(greeting);
                    }
                    if (!task.isSuccessful()) {
                        Log.e("DashboardFragment", "Error getting user name", task.getException());
                    }
                });

        String timeBased = getTimeBasedMessage();
        motivationText.setText(timeBased);
    }

    private String getTimeBasedMessage() {
        int hour = Integer.parseInt(new SimpleDateFormat("HH", Locale.getDefault()).format(new Date()));
        if (hour < 12) return "Start fresh. You got this!";
        if (hour < 18) return "Keep going, you're doing great!";
        return "Reflect and recharge. You did well today.";
    }
}
