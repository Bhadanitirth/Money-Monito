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
    ListView lv;
    ListAdapter adapter;
    String phone;
    SharedPreferences sdp;
    PieChart SpendingPieChart, IncomePieChart;
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_month12, container, false);

        //current month,year
        calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero-based, so we add 1
        int year = calendar.get(Calendar.YEAR);

        // Set the month name based on the index
        TextView monthText = view.findViewById(R.id.month_text);
        String[] monthNames = getResources().getStringArray(R.array.month_names);
        monthText.setText(monthNames[monthIndex]);


        sdp = requireContext().getSharedPreferences("user_details", Context.MODE_PRIVATE);
        phone = sdp.getString("phone", "null");

        // Initialize database helpers
        expenseDb = new FinancialDB(getActivity());
        incomeDb = new FinancialDB(getActivity());
        transferDb = new FinancialDB(getActivity());

        // Fetch data from each database
        String[] type={"expense","income"};
        expenseList = expenseDb.GetUsers(phone,monthIndex+1,year,type[0]);
        incomeList = incomeDb.GetUsers(phone,monthIndex+1,year,type[1]);


        // Aggregate expenses
        HashMap<String, Double> aggregatedExpenses = new HashMap<>();

        for (HashMap<String, String> item : expenseList) {
            String category = item.get("expense_category");
            double amount = Double.parseDouble(item.get("amount"));

            // If the category already exists, add the amount; otherwise, put a new entry
            if (aggregatedExpenses.containsKey(category)) {
                aggregatedExpenses.put(category, aggregatedExpenses.get(category) + amount);
            } else {
                aggregatedExpenses.put(category, amount);
            }
        }

        // Convert list items to include image resource ID
        ArrayList<HashMap<String, Object>> aggregatedExpenseList = new ArrayList<>();
        for (String category : aggregatedExpenses.keySet()) {
            HashMap<String, Object> newItem = new HashMap<>();
            newItem.put("expense_category", category);
            newItem.put("amount", aggregatedExpenses.get(category));
            aggregatedExpenseList.add(newItem);
        }

        // Set up RecyclerView for Spending
        spendingRecyclerView = view.findViewById(R.id.user_list_spending);
        spendingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        spendingRecyclerView.setAdapter(new ExpenseAdapter(getActivity(), aggregatedExpenseList));

        // Aggregate income
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

        // Convert aggregated income into a list for the adapter
        ArrayList<HashMap<String, Object>> aggregatedIncomeList = new ArrayList<>();
        for (String category : aggregatedIncome.keySet()) {
            HashMap<String, Object> newItem = new HashMap<>();
            newItem.put("income_category", category);
            newItem.put("amount", aggregatedIncome.get(category));
            aggregatedIncomeList.add(newItem);
        }
        // Set up RecyclerView for Income
        incomeRecyclerView = view.findViewById(R.id.user_list_income);
        incomeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        incomeRecyclerView.setAdapter(new IncomeAdapter(getActivity(), aggregatedIncomeList));



        // Get income and expense data grouped by category
        HashMap<String, Double> incomeData = incomeDb.getTotalIncomeByCategory(phone, monthIndex + 1, year);
        HashMap<String, Double> expenseData = expenseDb.getTotalExpenseByCategory(phone, monthIndex + 1, year);

// Initialize Income PieChart
        PieChart incomePieChart = view.findViewById(R.id.income_pieChart);
        List<PieEntry> incomeEntries = new ArrayList<>();
        List<Integer> incomeColors = new ArrayList<>();

        // Map each income category to a specific color
        Map<String, Integer> incomeCategoryColors = new HashMap<>();
        incomeCategoryColors.put("Salary", Color.parseColor("#60ba4d"));    // Green
        incomeCategoryColors.put("Sold Items", Color.parseColor("#ff8800"));  // Orange
        incomeCategoryColors.put("Coupons", Color.parseColor("#6c28b1")); // Blue
        incomeCategoryColors.put("Others", Color.parseColor("#000000"));


        for (String category : incomeData.keySet()) {
            incomeEntries.add(new PieEntry(incomeData.get(category).floatValue(), category));

            // Add the color corresponding to the income category
            if (incomeCategoryColors.containsKey(category)) {
                incomeColors.add(incomeCategoryColors.get(category));
            } else {
                // Add a default color if the category is not in the map
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
        incomePieChart.invalidate(); // Refresh the chart

        // Hide labels and show percentages
        incomePieChart.setUsePercentValues(true);  // Display percentages
        incomePieChart.getDescription().setEnabled(false);
        incomePieChart.setDrawEntryLabels(false);  // Disable category names

        incomePieChart.getLegend().setEnabled(false);//don't show color name


// Initialize Expense PieChart
        PieChart expensePieChart = view.findViewById(R.id.spending_pieChart);
        List<PieEntry> expenseEntries = new ArrayList<>();
        List<Integer> expenseColors = new ArrayList<>();

        // Map each category to a specific color
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
                // Add a default color if the category is not in the map
                expenseColors.add(Color.parseColor("#607D8B")); // Default Grey
            }
        }



        PieDataSet expenseDataSet = new PieDataSet(expenseEntries, "");
        expenseDataSet.setColors(expenseColors);
        expenseDataSet.setDrawValues(true);
        expenseDataSet.setValueTextColor(Color.WHITE);  // Set the text color for the values
        expenseDataSet.setValueTextSize(12f);

        expenseDataSet.setValueFormatter(new PercentFormatter(expensePieChart));

        PieData expensePieData = new PieData(expenseDataSet);
        expensePieChart.setData(expensePieData);
        expensePieChart.invalidate(); // Refresh the chart

        // Hide labels and show percentages
        expensePieChart.setUsePercentValues(true);  // Display percentages
        expensePieChart.getDescription().setEnabled(false);
        expensePieChart.setDrawEntryLabels(false);  // Disable category names

        expensePieChart.getLegend().setEnabled(false);//don't show color name


        // Fetch data from your database or other data source
        FinancialDB financialDB = new FinancialDB(getActivity());

// Assume phone, month, and year are available

        double cashSpending = financialDB.getSpendingForMode("Cash", phone, monthIndex+1, year);
        double bankSpending = financialDB.getSpendingForMode("Bank Account", phone, monthIndex+1, year);
        double cashIncome = financialDB.getIncomeForMode("Cash", phone, monthIndex+1, year);
        double bankIncome = financialDB.getIncomeForMode("Bank Account", phone, monthIndex+1, year);
        double cashTransfers = financialDB.getTransfersForMode("Cash -> Bank Account", phone, monthIndex+1, year);
        double bankTransfers = financialDB.getTransfersForMode("Bank Account -> Cash", phone, monthIndex+1, year);

// Find the TextViews by their ID
        TextView cashAmountSpending = view.findViewById(R.id.cash_amount_spending);
        TextView bankAccountAmountSpending = view.findViewById(R.id.bank_account_amount_spending);
        TextView cashAmountIncome = view.findViewById(R.id.cash_amount_income);
        TextView bankAccountAmountIncome = view.findViewById(R.id.bank_account_amount_income);
        TextView cashAmountTransfers = view.findViewById(R.id.cash_amount_transfers);
        TextView bankAccountAmountTransfers = view.findViewById(R.id.bank_account_amount_transfers);

// Set the fetched data to the TextViews
        cashAmountSpending.setText("₹" + String.valueOf(cashSpending));
        bankAccountAmountSpending.setText("₹" + String.valueOf(bankSpending));
        cashAmountIncome.setText("₹" + String.valueOf(cashIncome));
        bankAccountAmountIncome.setText("₹" + String.valueOf(bankIncome));
        cashAmountTransfers.setText("₹" + String.valueOf(cashTransfers));
        bankAccountAmountTransfers.setText("₹" + String.valueOf(bankTransfers));


// Fetch and display number of transactions
        int numTransactions = financialDB.getNumberOfTransactions(phone, monthIndex+1, year);
        TextView transactionAmount = view.findViewById(R.id.Transaction_amount);
        transactionAmount.setText(String.valueOf(numTransactions));

// Fetch and display average spending
        double avgSpending = financialDB.getAverageSpending(phone, monthIndex+1, year);
        TextView avgSpendingTxt = view.findViewById(R.id.PerTransaction_amount);
        avgSpendingTxt.setText("₹" + String.format("%.2f", avgSpending));

// Fetch and display spending per day
        double spendingPerDay = financialDB.getSpendingPerDay(phone, monthIndex+1, year);
        TextView spendingPerDayTxt = view.findViewById(R.id.PerDay_amount);
        spendingPerDayTxt.setText("₹" + String.format("%.2f", spendingPerDay));

// Fetch and display average income per transaction
        double avgIncomePerTransaction = financialDB.getAverageIncomePerTransaction(phone, monthIndex+1, year);
        TextView avgIncomePerTransactionTxt = view.findViewById(R.id.PerIncomeTransaction_amount);
        avgIncomePerTransactionTxt.setText("₹" + String.format("%.2f", avgIncomePerTransaction));

// Fetch and display income per day
        double incomePerDay = financialDB.getIncomePerDay(phone, monthIndex+1 , year);
        TextView incomePerDayTxt = view.findViewById(R.id.PerDayIncome_amount);
        incomePerDayTxt.setText("₹" + String.format("%.2f", incomePerDay));

        // Calculate total expense for a specific month and year

        double totalExpense = expenseDb.getTotalForMonth(monthIndex+1, year,phone,false);
        totalExpenseTextView = view.findViewById(R.id.spendingAmount);
        totalExpenseTextView.setText(String.format("₹%.1f", totalExpense));

        double totalIncome = incomeDb.getTotalForMonth(monthIndex+1, year,phone,true);
        totalIncomeTextView = view.findViewById(R.id.incomeAmount);
        totalIncomeTextView.setText(String.format("₹%.1f", totalIncome));

        double Balance= (totalIncome - totalExpense);
        BalanceTextView= view.findViewById(R.id.balanceText);
        BalanceTextView.setText(String.format("Balance: ₹%.1f", Balance));

        return view;
    }

}

