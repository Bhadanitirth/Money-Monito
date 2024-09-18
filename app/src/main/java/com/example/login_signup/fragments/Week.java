package com.example.login_signup.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.example.login_signup.R;
import com.example.login_signup.adapter.WeekFragmentAdapter;

public class Week extends Fragment {

    private static final String ARG_DAY_INDEX = "day_index";
    private int dayIndex;
    private TextView weekText;

    public static Week newInstance(int dayIndex) {
        Week fragment = new Week();
        Bundle args = new Bundle();
        args.putInt(ARG_DAY_INDEX, dayIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dayIndex = getArguments().getInt(ARG_DAY_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week, container, false);



        ViewPager2 viewPager = view.findViewById(R.id.view_pager);
        WeekFragmentAdapter adapter = new WeekFragmentAdapter(this);
        viewPager.setAdapter(adapter);

        view.findViewById(R.id.prev_week).setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem > 0) {
                viewPager.setCurrentItem(currentItem - 1);
            }
        });

        view.findViewById(R.id.next_week).setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem < adapter.getItemCount() - 1) {
                viewPager.setCurrentItem(currentItem + 1);
            }
        });

        return view;
    }


}
