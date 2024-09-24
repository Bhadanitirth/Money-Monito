package com.example.login_signup.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.login_signup.adapter.AnalysisAdapter;
import com.example.login_signup.R;
import com.google.android.material.tabs.TabLayout;


public class AnalisisFragment extends Fragment {

TabLayout tabLayout;
ViewPager2 viewPager2;
com.example.login_signup.adapter.AnalysisAdapter AnalysisAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_analisis, container, false);

        tabLayout=view.findViewById(R.id.tabfortime);
        viewPager2=view.findViewById(R.id.view_page);
        AnalysisAdapter=new AnalysisAdapter(this);
        viewPager2.setAdapter(AnalysisAdapter);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });
        return view;
    }
}