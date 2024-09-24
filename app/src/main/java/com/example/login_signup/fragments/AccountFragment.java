package com.example.login_signup.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.login_signup.R;
import com.example.login_signup.SQLiteDB.FinancialDB;


public class AccountFragment extends Fragment {

    FinancialDB listDb;
    SharedPreferences sdp;
    TextView Balance, Bank_acc, Cash;
    Switch show_balance;
    double totalBankAccount, totalCash, available_balance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_account, container, false);


        sdp = requireContext().getSharedPreferences("user_details", Context.MODE_PRIVATE);
        String phone = sdp.getString("phone", "null");

        Balance =view.findViewById(R.id.balance);
        Bank_acc =view.findViewById(R.id.bank_acc);
        Cash =view.findViewById(R.id.cash);
        show_balance=view.findViewById(R.id.switchShowBalance);

        listDb = new FinancialDB(getActivity());

        updateBalanceVisibility(show_balance.isChecked());

        // Set the listener for the switch
        show_balance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateBalanceVisibility(isChecked);
            }
        });

        totalBankAccount=listDb.getTotal(phone,true);

        totalCash=listDb.getTotal(phone,false);

        available_balance=(totalBankAccount + totalCash);

        return view;
    }

    private void updateBalanceVisibility(boolean showNumbers) {
        if (showNumbers) {

            Bank_acc.setText(String.format("₹%.1f", totalBankAccount));
            Bank_acc.setTextColor(totalBankAccount < 0 ? Color.RED : Color.BLACK);

            Cash.setText(String.format("₹%.1f", totalCash));
            Cash.setTextColor(totalCash < 0 ? Color.RED : Color.BLACK);

            Balance.setText(String.format("₹%.1f", available_balance));
            Balance.setTextColor(available_balance < 0 ? Color.RED : Color.BLACK);

        } else {

            Bank_acc.setText("****");
            Cash.setText("****");
            Balance.setText("****");

        }
    }
}