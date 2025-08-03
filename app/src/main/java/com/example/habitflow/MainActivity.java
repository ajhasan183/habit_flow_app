package com.example.habitflow;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.habitflow.Fragments.DashboardFragment;
import com.example.habitflow.Fragments.HomeFragment;
import com.example.habitflow.Fragments.VideoPlayerFragment;
import com.example.habitflow.Fragments.VoiceRecorderFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment = new HomeFragment();
    private DashboardFragment dashboardFragment = new DashboardFragment();
    private VoiceRecorderFragment voiceRecorderFragment = new VoiceRecorderFragment();
    private VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.nav_home){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
                    return true;
                }else  if(item.getItemId() == R.id.nav_dashboard){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,dashboardFragment).commit();
                    return true;
                }else  if(item.getItemId() == R.id.nav_recorder){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,voiceRecorderFragment).commit();
                    return true;
                }else  if(item.getItemId() == R.id.nav_videoplayer){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,videoPlayerFragment).commit();
                    return true;
                }

                return false;
            }
        });

    }
}