package com.example.login_signup.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.login_signup.R;
import com.example.login_signup.adapter.AddTransectionAdapter;
import com.example.login_signup.adapter.AnalysisAdapter;
import com.google.android.material.tabs.TabLayout;

public class AddTransaction extends Fragment {


    TabLayout tabLayout;
    ViewPager2 viewPager2;
    AddTransectionAdapter addTransectionAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_add_transaction, container, false);

        tabLayout=view.findViewById(R.id.tabforadd);
        viewPager2=view.findViewById(R.id.view_page);
        addTransectionAdapter=new AddTransectionAdapter(this);
        viewPager2.setAdapter(addTransectionAdapter);


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