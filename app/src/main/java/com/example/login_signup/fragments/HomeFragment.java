package com.example.login_signup.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.login_signup.R;
import com.example.login_signup.SQLiteDB.FinancialDB;
import com.example.login_signup.SQLiteDB.Login_Signin_Db;
import com.example.login_signup.adapter.CustomAdapter;
import com.example.login_signup.allEntry;

import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

public class HomeFragment extends Fragment {

    Calendar calendar;
    String phone;
    TextView totalExpenseTextView,totalIncomeTextView,BalanceTextView,name;
    FinancialDB listDb;
    Login_Signin_Db dbHelper;
    ArrayList<HashMap<String, String>> entryList;
    ListView lv;
    ListAdapter adapter;
    SharedPreferences sdp;
    ImageView searchIcon;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_home, container, false);

        name=view.findViewById(R.id.hello);
        sdp = requireContext().getSharedPreferences("user_details", Context.MODE_PRIVATE);
        phone = sdp.getString("phone", "null");
        dbHelper = new Login_Signin_Db(requireContext());
        String firstName = dbHelper.getFirstNameByPhone(phone);
        if (name != null) {
            name.setText(String.format(firstName));
        }

                // Get the current month and year
        calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero-based, so we add 1
        int year = calendar.get(Calendar.YEAR);

        searchIcon=view.findViewById(R.id.searchIcon);
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), allEntry.class);
                startActivity(intent);
            }
        });

        // Initialize database helpers
        listDb = new FinancialDB(getActivity());

        // Fetch data
        entryList = listDb.GetUsers(phone,month,year);

        Collections.reverse(entryList);

        // Set up the ListView
        lv = view.findViewById(R.id.user_list);
        adapter = new CustomAdapter(getActivity(), entryList);
        lv.setAdapter(adapter);

        // Calculate total expense for a specific month and year

        double totalExpense = listDb.getTotalForMonth(month, year,phone,false);
        totalExpenseTextView = view.findViewById(R.id.spendingAmount);
        totalExpenseTextView.setText(String.format("₹%.1f", totalExpense));

        double totalIncome = listDb.getTotalForMonth(month, year,phone,true);
        totalIncomeTextView = view.findViewById(R.id.incomeAmount);
        totalIncomeTextView.setText(String.format("₹%.1f", totalIncome));

        double Balance= (totalIncome - totalExpense);
        BalanceTextView= view.findViewById(R.id.balanceText);
        BalanceTextView.setText(String.format("Balance: ₹%.1f", Balance));

        return view;
    }
}