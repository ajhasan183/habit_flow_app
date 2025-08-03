package com.example.habitflow.Adapter;

import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitflow.Classes.Video;
import com.example.habitflow.R;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder>{
    List<Video> videoItemList;

    public VideoAdapter(List<Video> videoItemList) {
        this.videoItemList = videoItemList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_viewholder,parent,false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.setVideoData(videoItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return videoItemList.size();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder{

        VideoView videoView;
        TextView videoTitle;
        TextView videoDescription;
        ProgressBar videoProgressBar;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);

            videoView = itemView.findViewById(R.id.videoView);
            videoTitle = itemView.findViewById(R.id.videoTitle);
            videoDescription = itemView.findViewById(R.id.videoDescription);
            videoProgressBar = itemView.findViewById(R.id.videoProgressBar);
        }

        void setVideoData(Video videoItem){
            videoTitle.setText(videoItem.getVideoTitle());
            videoDescription.setText(videoItem.getVideoDescription());
            videoView.setVideoPath(videoItem.getVideoURL());

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    videoProgressBar.setVisibility(View.GONE);
                    mp.start();

                    float videoRatio = mp.getVideoWidth() / (float) mp.getVideoHeight();
                    float screenRatio = videoView.getWidth() / (float) videoView.getHeight();

                    float scale = videoRatio / screenRatio;
                    if (scale >= 1f){
                        videoView.setScaleX(scale);
                    }else {
                        videoView.setScaleY(1f / scale);
                    }
                }
            });

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.start();
                }
            });
        }
    }
}
