package com.example.login_signup.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.login_signup.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import java.util.ArrayList;
import java.util.List;

public class Week4 extends Fragment {

    private static final String ARG_DAY_INDEX = "day_index";
    private int dayIndex;

    public static Week4 newInstance(int dayIndex) {
        Week4 fragment = new Week4();
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
        View view = inflater.inflate(R.layout.fragment_week4, container, false);

        TextView weekText = view.findViewById(R.id.week_text);
        String[] weekDays = getResources().getStringArray(R.array.week_ranges);
        weekText.setText(weekDays[dayIndex]);

        PieChart pieChart = view.findViewById(R.id.spending_pieChart);

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(100f, "Others"));

        PieDataSet dataSet = new PieDataSet(entries, "Category-wise spending");
        dataSet.setColors(new int[]{Color.parseColor("#90A4AE")});
        dataSet.setDrawValues(false);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);

        pieChart.setHoleRadius(70f);
        pieChart.setTransparentCircleRadius(75f);
        pieChart.setCenterText("100.0%");
        pieChart.setCenterTextColor(Color.WHITE);
        pieChart.setCenterTextSize(18f);
        pieChart.setDrawEntryLabels(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);

        pieChart.invalidate();

        return view;
    }
}
