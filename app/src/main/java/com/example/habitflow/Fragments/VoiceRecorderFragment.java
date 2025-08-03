package com.example.habitflow.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitflow.Adapter.RecordingAdapter;
import com.example.habitflow.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;
import com.google.firebase.storage.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class VoiceRecorderFragment extends Fragment {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private MediaRecorder recorder;
    private File audioFile;
    private boolean isRecording = false;

    private Button recordButton;
    private TextView statusText;
    private RecyclerView recordingsRecyclerView;
    private RecordingAdapter recordingAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voice_recorder, container, false);

        recordButton = view.findViewById(R.id.recordButton);
        statusText = view.findViewById(R.id.statusText);

        recordingsRecyclerView = view.findViewById(R.id.recordingsRecyclerView);
        recordingsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recordingAdapter = new RecordingAdapter(requireContext());
        recordingsRecyclerView.setAdapter(recordingAdapter);

        fetchUserRecordings();

        recordButton.setOnClickListener(v -> {
            if (isRecording) {
                stopRecording();
            } else {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
                } else {
                    startRecording();
                }
            }
        });

        return view;
    }

    private void startRecording() {
        try {
            File outputDir = requireContext().getCacheDir();
            audioFile = File.createTempFile("voice_", ".3gp", outputDir);

            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setOutputFile(audioFile.getAbsolutePath());
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.prepare();
            recorder.start();

            isRecording = true;
            recordButton.setText("Stop Recording");
            statusText.setText("Recording...");
        } catch (IOException e) {
            Toast.makeText(getContext(), "Recording failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;

            isRecording = false;
            recordButton.setText("Start Recording");
            statusText.setText("Uploading...");

            uploadToFirebase(audioFile);
        }
    }

    private void uploadToFirebase(File file) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        String uid = user.getUid();
        String filename = "recordings/" + uid + "/" + System.currentTimeMillis() + ".3gp";

        StorageReference storageRef = FirebaseStorage.getInstance().getReference(filename);
        storageRef.putFile(Uri.fromFile(file))
                .addOnSuccessListener(taskSnapshot -> {
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        saveMetadataToFirestore(uid, uri.toString());
                    });
                })
                .addOnFailureListener(e -> {
                    statusText.setText("Upload failed");
                    Toast.makeText(getContext(), "Upload failed", Toast.LENGTH_SHORT).show();
                });
    }

    private void saveMetadataToFirestore(String userId, String url) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> data = new HashMap<>();
        data.put("url", url);
        data.put("title", "Voice Note");
        data.put("timestamp", new Date().getTime());

        db.collection("Users").document(userId)
                .collection("Recordings")
                .add(data)
                .addOnSuccessListener(doc -> statusText.setText("Upload complete"))
                .addOnFailureListener(e -> statusText.setText("Failed to save metadata"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startRecording();
            } else {
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fetchUserRecordings() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(user.getUid())
                .collection("Recordings")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null || value == null) return;

                    List<RecordingAdapter.Recording> list = new ArrayList<>();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        String title = doc.getString("title");
                        String url = doc.getString("url");
                        long timestamp = doc.getLong("timestamp") != null ? doc.getLong("timestamp") : 0;
                        list.add(new RecordingAdapter.Recording(title, url, timestamp));
                    }

                    recordingAdapter.setRecordings(list);
                });
    }
}
