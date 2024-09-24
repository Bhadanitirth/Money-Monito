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

public class Year4 extends Fragment {

    private static final String ARG_YEAR_INDEX = "year_index";
    private int yearIndex;

    public static Year4 newInstance(int yearIndex) {
        Year4 fragment = new Year4();
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
        View view = inflater.inflate(R.layout.fragment_year4, container, false);

        TextView yearText = view.findViewById(R.id.year_text);
        int startYear = 2020; // Update this to the start year
        yearText.setText(String.valueOf(startYear + yearIndex));

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
