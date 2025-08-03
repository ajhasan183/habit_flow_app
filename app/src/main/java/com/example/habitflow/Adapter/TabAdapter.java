package com.example.habitflow.Adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.habitflow.Fragments.FinishedListFragment;
import com.example.habitflow.Fragments.HomeFragment;
import com.example.habitflow.Fragments.ToDoListFragment;

public class TabAdapter extends FragmentStateAdapter {
    public TabAdapter(FragmentActivity activity) {
        super(activity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ToDoListFragment();
            case 1:
                return new FinishedListFragment();
            default:
                throw new IllegalArgumentException("Invalid position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}