package com.example.habitflow.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.habitflow.Adapter.VideoAdapter;
import com.example.habitflow.Classes.Video;
import com.example.habitflow.R;

import java.util.ArrayList;
import java.util.List;

public class VideoPlayerFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_player, container, false);

        final ViewPager2 videoViewPager = view.findViewById(R.id.videoViewPager);

        List<Video> videoItemList = new ArrayList<>();

        Video video1 = new Video("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4","Big Buck Bunny","A short animated film by the Blender Foundation featuring a gentle giant bunny and his forest friends.");
        Video video2 = new Video("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4","Elephants Dream","A surreal animated short about communication and isolation, also created by the Blender Foundation.");
        Video video3 = new Video("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4","For Bigger Blazes","A promotional video by Google for showcasing video playback, featuring dramatic fire effects.");
        Video video4 = new Video("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscape.mp4","For Bigger Escape","Another Google promo video, this time focused on fast-paced action and cinematic escape scenes.");
        Video video5 = new Video("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4","For Bigger Joyrides","A promotional clip by Google designed to demonstrate motion and joy in video playback.");
        Video video6 = new Video("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4","Tears Of Steel","A live-action and CGI science fiction short by Blender Foundation about love, memory, and machines.");

        videoItemList.add(video1);
        videoItemList.add(video2);
        videoItemList.add(video3);
        videoItemList.add(video4);
        videoItemList.add(video5);
        videoItemList.add(video6);

        VideoAdapter videoAdapter = new VideoAdapter(videoItemList);
        videoViewPager.setAdapter(videoAdapter);

        return view;
    }
}