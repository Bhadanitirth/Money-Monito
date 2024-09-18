package com.example.login_signup.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.login_signup.adapter.MonthFragmentAdapter;
import com.example.login_signup.R;

import java.util.Calendar;


public class Month extends Fragment {

    Calendar calendar;
    TextView curr_year;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_month, container, false);

        calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        curr_year=view.findViewById(R.id.month_text);
        curr_year.setText(String.valueOf(year));
        ViewPager2 viewPager = view.findViewById(R.id.view_pager);
        MonthFragmentAdapter adapter = new MonthFragmentAdapter(this);
        viewPager.setAdapter(adapter);

        // Handle button clicks
        view.findViewById(R.id.prev_month).setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem > 0) {
                viewPager.setCurrentItem(currentItem - 1);
            }
        });

        view.findViewById(R.id.next_month).setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem < adapter.getItemCount() - 1) {
                viewPager.setCurrentItem(currentItem + 1);
            }
        });

        return view;
    }
}