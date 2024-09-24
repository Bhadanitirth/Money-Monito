package com.example.login_signup.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

public class HomeFragment extends Fragment {

    Calendar calendar;
    String phone;
    TextView totalExpenseTextView, totalIncomeTextView, BalanceTextView, name,seeAll;
    FinancialDB listDb;
    Login_Signin_Db dbHelper;
    ArrayList<HashMap<String, String>> entryList;
    RecyclerView recyclerView;
    CustomAdapter adapter;
    SharedPreferences sdp;
    ImageView searchIcon, profileImg;

    Login_Signin_Db database;

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
        Collections.reverse(entryList);

        if (entryList.size() > 5) {
            entryList = new ArrayList<>(entryList.subList(0, 5));
        }

        recyclerView = view.findViewById(R.id.user_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CustomAdapter(getActivity(), entryList);
        recyclerView.setAdapter(adapter);

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

        return view;
    }
}
