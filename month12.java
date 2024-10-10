package com.example.login_signup.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.login_signup.R;

import android.graphics.Color;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.login_signup.SQLiteDB.FinancialDB;
import com.example.login_signup.adapter.ExpenseAdapter;
import com.example.login_signup.adapter.IncomeAdapter;
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

public class month12 extends Fragment {

    TextView totalExpenseTextView,totalIncomeTextView,BalanceTextView,name;
    RecyclerView spendingRecyclerView,incomeRecyclerView;
    Calendar calendar;

    String phone;
    SharedPreferences sdp;

    FinancialDB expenseDb ;
    FinancialDB incomeDb ;
    FinancialDB transferDb ;
    ArrayList<HashMap<String, String>> expenseList ,incomeList;

    private static final String ARG_MONTH_INDEX = "month_index";
    private int monthIndex;

    public static month12 newInstance(int monthIndex) {
        month12 fragment = new month12();
        Bundle args = new Bundle();
        args.putInt(ARG_MONTH_INDEX, monthIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            monthIndex = getArguments().getInt(ARG_MONTH_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_month12, container, false);


        calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        TextView monthText = view.findViewById(R.id.month_text);
        String[] monthNames = getResources().getStringArray(R.array.month_names);
        monthText.setText(monthNames[monthIndex]);


        sdp = requireContext().getSharedPreferences("user_details", Context.MODE_PRIVATE);
        phone = sdp.getString("phone", "null");


        expenseDb = new FinancialDB(getActivity());
        incomeDb = new FinancialDB(getActivity());
        transferDb = new FinancialDB(getActivity());


        String[] type={"expense","income"};
        expenseList = expenseDb.GetUsers(phone,monthIndex+1,year,type[0]);
        incomeList = incomeDb.GetUsers(phone,monthIndex+1,year,type[1]);

        if (expenseList.isEmpty() && incomeList.isEmpty()) {

            View noDataView = inflater.inflate(R.layout.no_data_layout, container, false);

            ((ViewGroup) view).removeAllViews();
            ((ViewGroup) view).addView(noDataView);

            TextView monthText1 = view.findViewById(R.id.month_text);
            String[] monthNames1 = getResources().getStringArray(R.array.month_names);
            monthText1.setText(monthNames1[monthIndex]);
        } else {

            HashMap<String, Double> aggregatedExpenses = new HashMap<>();

            for (HashMap<String, String> item : expenseList) {
                String category = item.get("expense_category");
                double amount = Double.parseDouble(item.get("amount"));

                if (aggregatedExpenses.containsKey(category)) {
                    aggregatedExpenses.put(category, aggregatedExpenses.get(category) + amount);
                } else {
                    aggregatedExpenses.put(category, amount);
                }
            }

            ArrayList<HashMap<String, Object>> aggregatedExpenseList = new ArrayList<>();
            for (String category : aggregatedExpenses.keySet()) {
                HashMap<String, Object> newItem = new HashMap<>();
                newItem.put("expense_category", category);
                newItem.put("amount", aggregatedExpenses.get(category));
                aggregatedExpenseList.add(newItem);
            }

            spendingRecyclerView = view.findViewById(R.id.user_list_spending);
            spendingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            spendingRecyclerView.setAdapter(new ExpenseAdapter(getActivity(), aggregatedExpenseList));

            HashMap<String, Double> aggregatedIncome = new HashMap<>();
            for (HashMap<String, String> item : incomeList) {
                String category = item.get("income_category");
                double amount = Double.parseDouble(item.get("amount"));

                if (aggregatedIncome.containsKey(category)) {
                    aggregatedIncome.put(category, aggregatedIncome.get(category) + amount);
                } else {
                    aggregatedIncome.put(category, amount);
                }
            }

            ArrayList<HashMap<String, Object>> aggregatedIncomeList = new ArrayList<>();
            for (String category : aggregatedIncome.keySet()) {
                HashMap<String, Object> newItem = new HashMap<>();
                newItem.put("income_category", category);
                newItem.put("amount", aggregatedIncome.get(category));
                aggregatedIncomeList.add(newItem);
            }

            incomeRecyclerView = view.findViewById(R.id.user_list_income);
            incomeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            incomeRecyclerView.setAdapter(new IncomeAdapter(getActivity(), aggregatedIncomeList));

            HashMap<String, Double> incomeData = incomeDb.getTotalIncomeByCategory(phone, monthIndex + 1, year);
            HashMap<String, Double> expenseData = expenseDb.getTotalExpenseByCategory(phone, monthIndex + 1, year);

//Income PieChart

            PieChart incomePieChart = view.findViewById(R.id.income_pieChart);
            List<PieEntry> incomeEntries = new ArrayList<>();
            List<Integer> incomeColors = new ArrayList<>();

            Map<String, Integer> incomeCategoryColors = new HashMap<>();
            incomeCategoryColors.put("Salary", Color.parseColor("#60ba4d"));    // Green
            incomeCategoryColors.put("Sold Items", Color.parseColor("#ff8800"));  // Orange
            incomeCategoryColors.put("Coupons", Color.parseColor("#6c28b1")); // Blue
            incomeCategoryColors.put("Others", Color.parseColor("#000000"));


            for (String category : incomeData.keySet()) {
                incomeEntries.add(new PieEntry(incomeData.get(category).floatValue(), category));

                if (incomeCategoryColors.containsKey(category)) {
                    incomeColors.add(incomeCategoryColors.get(category));
                } else {
                    incomeColors.add(Color.parseColor("#607D8B")); // Default Grey
                }
            }

            PieDataSet incomeDataSet = new PieDataSet(incomeEntries, "");
            incomeDataSet.setColors(incomeColors);
            incomeDataSet.setValueTextColor(Color.WHITE);
            incomeDataSet.setValueTextSize(12f);

            incomeDataSet.setValueFormatter(new PercentFormatter(incomePieChart));

            PieData incomePieData = new PieData(incomeDataSet);
            incomePieChart.setData(incomePieData);
            incomePieChart.invalidate();

            incomePieChart.setUsePercentValues(true);
            incomePieChart.getDescription().setEnabled(false);
            incomePieChart.setDrawEntryLabels(false);

            incomePieChart.getLegend().setEnabled(false);


            PieChart expensePieChart = view.findViewById(R.id.spending_pieChart);
            List<PieEntry> expenseEntries = new ArrayList<>();
            List<Integer> expenseColors = new ArrayList<>();


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

            for (String category : expenseData.keySet()) {
                expenseEntries.add(new PieEntry(expenseData.get(category).floatValue(), category));

                if (categoryColors.containsKey(category)) {
                    expenseColors.add(categoryColors.get(category));
                } else {
                    expenseColors.add(Color.parseColor("#607D8B"));
                }
            }
            PieDataSet expenseDataSet = new PieDataSet(expenseEntries, "");
            expenseDataSet.setColors(expenseColors);
            expenseDataSet.setDrawValues(true);
            expenseDataSet.setValueTextColor(Color.WHITE);
            expenseDataSet.setValueTextSize(12f);

            expenseDataSet.setValueFormatter(new PercentFormatter(expensePieChart));

            PieData expensePieData = new PieData(expenseDataSet);
            expensePieChart.setData(expensePieData);
            expensePieChart.invalidate();

            expensePieChart.setUsePercentValues(true);
            expensePieChart.getDescription().setEnabled(false);
            expensePieChart.setDrawEntryLabels(false);

            expensePieChart.getLegend().setEnabled(false);

            FinancialDB financialDB = new FinancialDB(getActivity());

            double cashSpending = financialDB.getSpendingForMode("Cash", phone, monthIndex + 1, year);
            double bankSpending = financialDB.getSpendingForMode("Bank Account", phone, monthIndex + 1, year);
            double cashIncome = financialDB.getIncomeForMode("Cash", phone, monthIndex + 1, year);
            double bankIncome = financialDB.getIncomeForMode("Bank Account", phone, monthIndex + 1, year);
            double cashTransfers = financialDB.getTransfersForMode("Cash -> Bank Account", phone, monthIndex + 1, year);
            double bankTransfers = financialDB.getTransfersForMode("Bank Account -> Cash", phone, monthIndex + 1, year);

            TextView cashAmountSpending = view.findViewById(R.id.cash_amount_spending);
            TextView bankAccountAmountSpending = view.findViewById(R.id.bank_account_amount_spending);
            TextView cashAmountIncome = view.findViewById(R.id.cash_amount_income);
            TextView bankAccountAmountIncome = view.findViewById(R.id.bank_account_amount_income);
            TextView cashAmountTransfers = view.findViewById(R.id.cash_amount_transfers);
            TextView bankAccountAmountTransfers = view.findViewById(R.id.bank_account_amount_transfers);

            cashAmountSpending.setText("₹" + String.valueOf(cashSpending));
            bankAccountAmountSpending.setText("₹" + String.valueOf(bankSpending));
            cashAmountIncome.setText("₹" + String.valueOf(cashIncome));
            bankAccountAmountIncome.setText("₹" + String.valueOf(bankIncome));
            cashAmountTransfers.setText("₹" + String.valueOf(cashTransfers));
            bankAccountAmountTransfers.setText("₹" + String.valueOf(bankTransfers));

            int numTransactions = financialDB.getNumberOfTransactions(phone, monthIndex + 1, year);
            TextView transactionAmount = view.findViewById(R.id.Transaction_amount);
            transactionAmount.setText(String.valueOf(numTransactions));

            double avgSpending = financialDB.getAverageSpending(phone, monthIndex + 1, year);
            TextView avgSpendingTxt = view.findViewById(R.id.PerTransaction_amount);
            avgSpendingTxt.setText("₹" + String.format("%.2f", avgSpending));

            double spendingPerDay = financialDB.getSpendingPerDay(phone, monthIndex + 1, year);
            TextView spendingPerDayTxt = view.findViewById(R.id.PerDay_amount);
            spendingPerDayTxt.setText("₹" + String.format("%.2f", spendingPerDay));

            double avgIncomePerTransaction = financialDB.getAverageIncomePerTransaction(phone, monthIndex + 1, year);
            TextView avgIncomePerTransactionTxt = view.findViewById(R.id.PerIncomeTransaction_amount);
            avgIncomePerTransactionTxt.setText("₹" + String.format("%.2f", avgIncomePerTransaction));

            double incomePerDay = financialDB.getIncomePerDay(phone, monthIndex + 1, year);
            TextView incomePerDayTxt = view.findViewById(R.id.PerDayIncome_amount);
            incomePerDayTxt.setText("₹" + String.format("%.2f", incomePerDay));

            double totalExpense = expenseDb.getTotalForMonth(monthIndex + 1, year, phone, false);
            totalExpenseTextView = view.findViewById(R.id.spendingAmount);
            totalExpenseTextView.setText(String.format("₹%.1f", totalExpense));

            double totalIncome = incomeDb.getTotalForMonth(monthIndex + 1, year, phone, true);
            totalIncomeTextView = view.findViewById(R.id.incomeAmount);
            totalIncomeTextView.setText(String.format("₹%.1f", totalIncome));

            double Balance = (totalIncome - totalExpense);
            BalanceTextView = view.findViewById(R.id.balanceText);
            BalanceTextView.setText(String.format("Balance: ₹%.1f", Balance));


        }

        return view;

    }
}

