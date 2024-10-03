package com.example.login_signup.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login_signup.PinDialog;
import com.example.login_signup.Premium;
import com.example.login_signup.R;
import com.example.login_signup.SQLiteDB.FinancialDB;
import com.example.login_signup.SQLiteDB.Login_Signin_Db;


public class AccountFragment extends Fragment {

    FinancialDB listDb;
    Login_Signin_Db dbHelper;
    SharedPreferences sdp;
    TextView Balance, Bank_acc, Cash, add_acc;
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
        add_acc=view.findViewById(R.id.add_acc);

        add_acc.setOnClickListener(v -> showPremiumDialog());

        listDb = new FinancialDB(getActivity());
        dbHelper = new Login_Signin_Db(getContext());

        updateBalanceVisibility(show_balance.isChecked());

        show_balance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    PinDialog pinDialog = new PinDialog(getContext(), new PinDialog.PinDialogListener() {
                        @Override
                        public void onPinEntered(String pin) {
                            Boolean storedPin = dbHelper.getStoredPin(pin, phone);
                            if (storedPin) {
                                Toast.makeText(getContext(), "PIN is correct!", Toast.LENGTH_SHORT).show();
                                updateBalanceVisibility(true);
                            } else {
                                Toast.makeText(getContext(), "Incorrect PIN . Please try again.", Toast.LENGTH_SHORT).show();

                                show_balance.setChecked(false);
                            }
                        }
                    },phone);

                    pinDialog.showDialog();
                } else {

                    updateBalanceVisibility(false);
                }
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
            Bank_acc.setTextColor(Color.BLACK);
            Cash.setTextColor(Color.BLACK);
            Balance.setTextColor(Color.BLACK);

        }
    }

    private void showPremiumDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_get_premium);

        Button unlock_now=dialog.findViewById(R.id.unlock_now);
        unlock_now.setOnClickListener(v -> {
            Intent intent=new Intent(getActivity(), Premium.class);
            startActivity(intent);
            dialog.dismiss();
        });


        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            window.getAttributes().windowAnimations = R.style.DialogAnimation;
            window.setGravity(Gravity.BOTTOM);
        }
    }
}