package com.example.habitflow.ViewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.habitflow.Classes.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class TaskViewModel extends ViewModel {
    private final MutableLiveData<List<Task>> tasks = new MutableLiveData<>(new ArrayList<>());
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private CollectionReference userTasksRef;

    public TaskViewModel() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            userTasksRef = db.collection("Users")
                    .document(currentUser.getUid())
                    .collection("tasks");

            fetchTasksFromFirestore();
        } else {
            Log.w("TaskViewModel", "No user logged in");
        }
    }

    public LiveData<List<Task>> getTasks() {
        return tasks;
    }

    public void addTask(Task task) {
        if (userTasksRef != null) {
            userTasksRef.document(task.getId())
                    .set(task)
                    .addOnFailureListener(e -> Log.e("TaskViewModel", "Failed to add task", e));
        }
    }

    public void toggleTaskStatus(String taskId) {
        if (userTasksRef != null) {
            userTasksRef.document(taskId).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    Boolean currentStatus = documentSnapshot.getBoolean("done");
                    userTasksRef.document(taskId).update("done", !Boolean.TRUE.equals(currentStatus));
                }
            });
        }
    }


    private void fetchTasksFromFirestore() {
        if (userTasksRef != null) {
            userTasksRef.addSnapshotListener((snapshots, error) -> {
                if (error != null) {
                    Log.e("TaskViewModel", "Snapshot error", error);
                    return;
                }

                if (snapshots != null) {
                    List<Task> updatedTasks = new ArrayList<>();
                    for (DocumentSnapshot doc : snapshots.getDocuments()) {
                        Task task = doc.toObject(Task.class);
                        if (task != null) {
                            updatedTasks.add(task);
                        }
                    }
                    tasks.setValue(updatedTasks);
                }
            });
        }
    }

}


