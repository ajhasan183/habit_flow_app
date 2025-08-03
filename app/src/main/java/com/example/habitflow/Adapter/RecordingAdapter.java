package com.example.habitflow.Adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitflow.R;

import java.text.SimpleDateFormat;
import java.util.*;

public class RecordingAdapter extends RecyclerView.Adapter<RecordingAdapter.RecordingViewHolder> {

    public static class Recording {
        public String title;
        public String url;
        public long timestamp;

        public Recording(String title, String url, long timestamp) {
            this.title = title;
            this.url = url;
            this.timestamp = timestamp;
        }
    }

    private final List<Recording> recordings = new ArrayList<>();
    private final Context context;

    public RecordingAdapter(Context context) {
        this.context = context;
    }

    public void setRecordings(List<Recording> newList) {
        recordings.clear();
        recordings.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecordingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recording_item, parent, false);
        return new RecordingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordingViewHolder holder, int position) {
        Recording item = recordings.get(position);
        holder.title.setText(item.title);
        holder.timestamp.setText(formatTimestamp(item.timestamp));

        holder.playButton.setOnClickListener(v -> {
            MediaPlayer player = new MediaPlayer();
            holder.playButton.setEnabled(false);
            try {
                player.setDataSource(item.url);
                player.setOnPreparedListener(mp -> {
                    holder.playButton.setEnabled(true);
                    mp.start();
                });
                player.setOnCompletionListener(mp -> {
                    mp.release();
                    holder.playButton.setText("Play");
                });
                player.setOnErrorListener((mp, what, extra) -> {
                    holder.playButton.setEnabled(true);
                    mp.release();
                    return true;
                });
                player.prepareAsync();
                holder.playButton.setText("Loading...");
            } catch (Exception e) {
                e.printStackTrace();
                holder.playButton.setText("Play");
                holder.playButton.setEnabled(true);
            }
        });

    }

    @Override
    public int getItemCount() {
        return recordings.size();
    }

    static class RecordingViewHolder extends RecyclerView.ViewHolder {
        TextView title, timestamp;
        Button playButton;

        public RecordingViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.recordingTitle);
            timestamp = itemView.findViewById(R.id.recordingTimestamp);
            playButton = itemView.findViewById(R.id.playButton);
        }
    }

    private String formatTimestamp(long millis) {
        return new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                .format(new Date(millis));
    }
}
