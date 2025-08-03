package com.example.habitflow.ViewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.habitflow.Adapter.RecordingAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.*;

public class VoiceLogViewModel extends ViewModel {

    private final MutableLiveData<List<RecordingAdapter.Recording>> recordings = new MutableLiveData<>(new ArrayList<>());
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    public VoiceLogViewModel() {
        fetchVoiceLogs();
    }

    public LiveData<List<RecordingAdapter.Recording>> getRecordings() {
        return recordings;
    }

    public LiveData<List<RecordingAdapter.Recording>> getTodayLogs() {
        return Transformations.map(recordings, allLogs -> {
            List<RecordingAdapter.Recording> todayLogs = new ArrayList<>();
            Calendar today = Calendar.getInstance();

            for (RecordingAdapter.Recording log : allLogs) {
                Calendar logDate = Calendar.getInstance();
                logDate.setTimeInMillis(log.timestamp);

                boolean isSameDay =
                        today.get(Calendar.YEAR) == logDate.get(Calendar.YEAR) &&
                                today.get(Calendar.DAY_OF_YEAR) == logDate.get(Calendar.DAY_OF_YEAR);

                if (isSameDay) {
                    todayLogs.add(log);
                }
            }

            return todayLogs;
        });
    }

    private void fetchVoiceLogs() {
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (userId == null) {
            Log.w("VoiceLogViewModel", "No user is logged in");
            return;
        }

        db.collection("Users")
                .document(userId)
                .collection("Recordings")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.e("VoiceLogViewModel", "Listen failed.", e);
                        return;
                    }

                    if (snapshots != null) {
                        List<RecordingAdapter.Recording> list = new ArrayList<>();
                        for (DocumentSnapshot doc : snapshots) {
                            String title = doc.getString("title");
                            String url = doc.getString("url");
                            Long timestamp = doc.getLong("timestamp");

                            if (title != null && url != null && timestamp != null) {
                                list.add(new RecordingAdapter.Recording(title, url, timestamp));
                            }
                        }
                        recordings.setValue(list);
                    }
                });
    }
}
