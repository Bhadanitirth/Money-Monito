package com.example.login_signup.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.login_signup.Befor_Home;
import com.example.login_signup.R;
import com.example.login_signup.SQLiteDB.FinancialDB;


import java.util.Calendar;

public class Transfer extends Fragment {

    String phone;
    SharedPreferences sdp;

    private int pay_img_from_res = -1;
    private int pay_img_to_res = -1;

    ImageView pay_img_from,getPay_img_to;
    EditText pay_from,pay_to;

    private final int[] payIds = {
            R.id.cash, R.id.bank_account
    };
    private final String[] payNames = {
            "Cash", "Bank Account"
    };
    private final int[] payImages = {
            R.drawable.cash, R.drawable.bank
    };

//    batabase
    private EditText datePickerEditText, timePickerEditText,datePicker, timePicker, amountNo, note, attachment;
    private FinancialDB dbHelper;
    ImageButton saveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_transfer, container, false);

        sdp = requireContext().getSharedPreferences("user_details", Context.MODE_PRIVATE);
        phone = sdp.getString("phone", "null");

        // Initialize UI components
        datePicker = view.findViewById(R.id.date_picker);
        timePicker = view.findViewById(R.id.time_picker);
        amountNo = view.findViewById(R.id.Amount_No);
        note = view.findViewById(R.id.note);
        attachment = view.findViewById(R.id.Attachment);
        saveButton = view.findViewById(R.id.Save);

        // Bottom add category Pay Type
        pay_from= view.findViewById(R.id.pay_textform);
        pay_img_from= view.findViewById(R.id.pay_imgfrom);
        pay_from.setOnClickListener(v -> showBankAccDialog_from());

        // Bottom add category Pay Type
        pay_to= view.findViewById(R.id.pay_textto);
        getPay_img_to= view.findViewById(R.id.pay_imgto);
        pay_to.setOnClickListener(v -> showBankAccDialog_to());

        // Initialize database helper
        dbHelper = new FinancialDB(getContext());

        saveButton.setOnClickListener(v -> {
            saveTransferDetails();
            Intent intent = new Intent(getActivity(), Befor_Home.class);
            startActivity(intent);
        });

        // Date and time pickers
        datePickerEditText = view.findViewById(R.id.date_picker);
        timePickerEditText = view.findViewById(R.id.time_picker);
        initializeDateAndTime();

        datePickerEditText.setOnClickListener(v -> showDatePicker());
        timePickerEditText.setOnClickListener(v -> showTimePicker());

    return view;
    }

    private void showBankAccDialog_from() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_add_cash_bank_acc);

        // Set click listeners dynamically
        for (int i = 0; i < payIds.length; i++) {
            int index = i; // Capture the index in a final variable
            dialog.findViewById(payIds[i]).setOnClickListener(v -> {
                pay_from.setText(payNames[index]);
                pay_img_from.setImageResource(payImages[index]);

                pay_img_from_res = payImages[index];

                dialog.dismiss(); // Close the dialog
            });
        }

        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.getAttributes().windowAnimations = R.style.DialogAnimation;
            window.setGravity(Gravity.BOTTOM);
        }
    }

    private void showBankAccDialog_to() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_add_cash_bank_acc);

        // Set click listeners dynamically
        for (int i = 0; i < payIds.length; i++) {
            int index = i; // Capture the index in a final variable
            dialog.findViewById(payIds[i]).setOnClickListener(v -> {
                pay_to.setText(payNames[index]);
                getPay_img_to.setImageResource(payImages[index]);

                pay_img_to_res = payImages[index];

                dialog.dismiss(); // Close the dialog
            });
        }

        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.getAttributes().windowAnimations = R.style.DialogAnimation;
            window.setGravity(Gravity.BOTTOM);
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                (view, year1, month1, dayOfMonth) -> {
                    String date = String.format("%02d/%02d/%d", dayOfMonth, (month1 + 1), year1);
                    datePickerEditText.setText(date);
                },
                year,
                month,
                day
        );
        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getActivity(),
                (view, hourOfDay, minute1) -> {
                    String time = String.format("%02d:%02d", hourOfDay, minute1);
                    timePickerEditText.setText(time);
                },
                hour,
                minute,
                true // 24-hour format
        );
        timePickerDialog.show();
    }

    private void initializeDateAndTime() {
        Calendar calendar = Calendar.getInstance();

        // Set default date and time in EditTexts
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String date = String.format("%02d/%02d/%d", day, (month + 1), year);
        datePickerEditText.setText(date);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String time = String.format("%02d:%02d", hour, minute);
        timePickerEditText.setText(time);
    }

    private void saveTransferDetails() {
        String date = datePicker.getText().toString().trim();
        String time = timePicker.getText().toString().trim();
        String amountStr = amountNo.getText().toString().trim();
        String fromAccount = pay_from.getText().toString().trim();
        String toAccount = pay_to.getText().toString().trim();
        String noteText = note.getText().toString().trim();
        String attachmentText = attachment.getText().toString().trim();

        if (date.isEmpty() || time.isEmpty() || amountStr.isEmpty() || fromAccount.isEmpty() || toAccount.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);

        long result = dbHelper.insertTransfer(date, time, amount, fromAccount, toAccount, noteText, attachmentText,phone);

        if (result != -1) {
            Toast.makeText(getContext(), "Transfer saved successfully", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getContext(), "Failed to save transfer", Toast.LENGTH_SHORT).show();
        }
    }

}