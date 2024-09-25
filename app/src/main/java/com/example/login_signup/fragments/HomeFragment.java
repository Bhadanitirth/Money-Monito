package com.example.login_signup.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.login_signup.R;
import com.example.login_signup.SQLiteDB.FinancialDB;
import com.example.login_signup.SQLiteDB.Login_Signin_Db;
import com.example.login_signup.adapter.CustomAdapter;
import com.example.login_signup.allEntry;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    Calendar calendar;
    String phone;
    TextView no_data_text,totalExpenseTextView, totalIncomeTextView, BalanceTextView, name,seeAll;
    FinancialDB listDb;
    Login_Signin_Db dbHelper;
    ArrayList<HashMap<String, String>> entryList;
    RecyclerView recyclerView;
    CustomAdapter adapter;
    SharedPreferences sdp;
    ImageView searchIcon, profileImg,empty_list;

    PieChart month_budget_pieChart;
    Login_Signin_Db database;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        name = view.findViewById(R.id.hello);
        sdp = requireContext().getSharedPreferences("user_details", Context.MODE_PRIVATE);
        phone = sdp.getString("phone", "null");
        dbHelper = new Login_Signin_Db(requireContext());
        String firstName = dbHelper.getFirstNameByPhone(phone);
        if (name != null) {
            name.setText(String.format(firstName));
        }

        database = new Login_Signin_Db(getActivity());
        profileImg = view.findViewById(R.id.profile);
        empty_list=view.findViewById(R.id.empty_list);
        no_data_text=view.findViewById(R.id.no_data_txt);

        String gender = database.getGender(phone);
        if (gender.equals("Male")) {
            profileImg.setImageResource(R.drawable.male);
        } else if (gender.equals("FeMale")) {
            profileImg.setImageResource(R.drawable.female);
        } else {
            profileImg.setImageResource(R.drawable.coustom);
        }

        calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        searchIcon = view.findViewById(R.id.searchIcon);
        searchIcon.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), allEntry.class);
            startActivity(intent);
        });

        listDb = new FinancialDB(getActivity());

        entryList = listDb.GetUsers(phone, month, year);

        if (entryList.isEmpty()) {

            empty_list.setImageResource(R.drawable.no_transaction);
            no_data_text.setText("No Transaction");

        } else {

            Collections.reverse(entryList);

            if (entryList.size() > 5) {
                entryList = new ArrayList<>(entryList.subList(0, 5));
            }

            recyclerView = view.findViewById(R.id.user_list);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new CustomAdapter(getActivity(), entryList);
            recyclerView.setAdapter(adapter);

        }

        seeAll=view.findViewById(R.id.seeAll);
        seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), allEntry.class);
                startActivity(intent);
            }
        });

        double totalExpense = listDb.getTotalForMonth(month, year, phone, false);
        totalExpenseTextView = view.findViewById(R.id.spendingAmount);
        totalExpenseTextView.setText(String.format("₹%.1f", totalExpense));

        double totalIncome = listDb.getTotalForMonth(month, year, phone, true);
        totalIncomeTextView = view.findViewById(R.id.incomeAmount);
        totalIncomeTextView.setText(String.format("₹%.1f", totalIncome));

        double balance = (totalIncome - totalExpense);
        BalanceTextView = view.findViewById(R.id.balanceText);
        BalanceTextView.setText(String.format("Balance: ₹%.1f", balance));

//month_budget_pieChart

        HashMap<String, Double> MonthBudgetData = listDb.getTotalExpenseByCategory(phone, month, year);

        month_budget_pieChart = view.findViewById(R.id.month_budget_pieChart);
        List<PieEntry> MonthBudgetEntries = new ArrayList<>();
        List<Integer> MonthBudgetColors = new ArrayList<>();


        Map<String, Integer> categoryColors = new HashMap<>();
        categoryColors.put("Food", Color.parseColor("#ffb23c"));    // Orange
        categoryColors.put("Travelling", Color.parseColor("#7250a1")); // Blue
        categoryColors.put("Rent", Color.parseColor("#86803e"));     // Green
        categoryColors.put("Shopping", Color.parseColor("#2ba2dd")); // Purple
        categoryColors.put("Entertainment", Color.parseColor("#d4d4ff")); // Pink
        categoryColors.put("Gift and donation", Color.parseColor("#0b1977")); // Yellow
        categoryColors.put("Medical", Color.parseColor("#ff5462")); // Deep Purple
        categoryColors.put("Education", Color.parseColor("#0e714f")); // Brown
        categoryColors.put("Personal Care", Color.parseColor("#baced3"));    // Teal
        categoryColors.put("Others", Color.parseColor("#000000"));

        for (String category : MonthBudgetData.keySet()) {
            MonthBudgetEntries.add(new PieEntry(MonthBudgetData.get(category).floatValue(), category));

            if (categoryColors.containsKey(category)) {
                MonthBudgetColors.add(categoryColors.get(category));
            } else {
                MonthBudgetColors.add(Color.parseColor("#607D8B"));
            }
        }
        PieDataSet MonthBudgetDataSet = new PieDataSet(MonthBudgetEntries, "");
        MonthBudgetDataSet.setColors(MonthBudgetColors);
        MonthBudgetDataSet.setDrawValues(true);
        MonthBudgetDataSet.setValueTextColor(Color.WHITE);
        MonthBudgetDataSet.setValueTextSize(12f);

        MonthBudgetDataSet.setValueFormatter(new PercentFormatter(month_budget_pieChart));

        PieData expensePieData = new PieData(MonthBudgetDataSet);
        month_budget_pieChart.setData(expensePieData);
        month_budget_pieChart.invalidate();

        month_budget_pieChart.setUsePercentValues(true);
        month_budget_pieChart.getDescription().setEnabled(false);
        month_budget_pieChart.setDrawEntryLabels(false);

        month_budget_pieChart.getLegend().setEnabled(false);

        return view;
    }
}
