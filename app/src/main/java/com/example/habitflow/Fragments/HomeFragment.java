package com.example.habitflow.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.habitflow.Adapter.TabAdapter;
import com.example.habitflow.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager2 viewPager2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager2 = view.findViewById(R.id.home_fragment_container);

        TabAdapter adapter = new TabAdapter(requireActivity());
        viewPager2.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("To Do");
                    break;
                case 1:
                    tab.setText("Finished");
                    break;
            }
        }).attach();


        return view;
    }
}