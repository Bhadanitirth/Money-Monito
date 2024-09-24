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

    private Calendar calendar;
    private TextView curr_year;
    private int currentMonth;
    private int currentYear;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_month, container, false);

        calendar = Calendar.getInstance();
        currentMonth = calendar.get(Calendar.MONTH)+1;
        currentYear = calendar.get(Calendar.YEAR);

        curr_year = view.findViewById(R.id.month_text);
        curr_year.setText(String.valueOf(currentYear));

        ViewPager2 viewPager = view.findViewById(R.id.view_pager);
        MonthFragmentAdapter adapter = new MonthFragmentAdapter(this);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentMonth);

        view.findViewById(R.id.prev_month).setOnClickListener(v -> {
            if (currentMonth > 1) {
                currentMonth--;
            } else {
                currentMonth = 12;
                currentYear--;
            }
            updateMonthYear();
            viewPager.setCurrentItem(currentMonth);
        });

        view.findViewById(R.id.next_month).setOnClickListener(v -> {
            if (currentMonth < 12) {
                currentMonth++;
            } else {
                currentMonth = 1;
                currentYear++;
            }
            updateMonthYear();
            viewPager.setCurrentItem(currentMonth);
        });

        return view;
    }

    private void updateMonthYear() {
        curr_year.setText(String.valueOf(currentYear));
    }
}
