package com.example.login_signup.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.example.login_signup.R;
import com.example.login_signup.adapter.YearFragmentAdapter;

public class Year extends Fragment {

    private static final String ARG_YEAR_INDEX = "year_index";
    private int yearIndex;
    private TextView yearText;

    public static Year newInstance(int yearIndex) {
        Year fragment = new Year();
        Bundle args = new Bundle();
        args.putInt(ARG_YEAR_INDEX, yearIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            yearIndex = getArguments().getInt(ARG_YEAR_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_year, container, false);



        ViewPager2 viewPager = view.findViewById(R.id.view_pager);
        YearFragmentAdapter adapter = new YearFragmentAdapter(this);
        viewPager.setAdapter(adapter);

        view.findViewById(R.id.prev_year).setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem > 0) {
                viewPager.setCurrentItem(currentItem - 1);
            }
        });

        view.findViewById(R.id.next_year).setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem < adapter.getItemCount() - 1) {
                viewPager.setCurrentItem(currentItem + 1);
            }
        });

        return view;
    }

}
