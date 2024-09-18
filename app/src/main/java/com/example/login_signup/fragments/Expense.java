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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.login_signup.Befor_Home;
import com.example.login_signup.LoginSignup.Pin;
import com.example.login_signup.R;

import com.example.login_signup.SQLiteDB.FinancialDB;
import com.example.login_signup.allEntry;

import java.util.Calendar;

public class Expense extends Fragment {

    private ImageView pay_img, selectedCategoryImageView;

    private int pay_category_res = -1;
    private int pay_img_res = -1;

    String phone;
    SharedPreferences sdp;
    private ImageButton saveButton;
    private FinancialDB dbHelper;
    private EditText pay_edittext, selectCategoryEditText, datePickerEditText, timePickerEditText, amountEditText, noteEditText, attachmentEditText;

    private final int[] payIds = {
            R.id.cash, R.id.bank_account
    };
    private final String[] payNames = {
            "Cash", "Bank Account"
    };
    private final int[] payImages = {
            R.drawable.cash, R.drawable.bank
    };
    private final int[] categoryIds = {
            R.id.category_other, R.id.category_food, R.id.category_shopping,
            R.id.category_travelling, R.id.category_medical, R.id.category_education,
            R.id.category_rent, R.id.category_entertainment, R.id.category_personal_care,
            R.id.category_gift_donation
    };
    private final String[] categoryNames = {
            "Other", "Food", "Shopping", "Travelling", "Medical", "Education",
            "Rent", "Entertainment", "Personal Care", "Gift and Donation"
    };
    private final int[] categoryImages = {
            R.drawable.other, R.drawable.food, R.drawable.shopping, R.drawable.travel,
            R.drawable.medical, R.drawable.education, R.drawable.rent, R.drawable.game,
            R.drawable.personal_care, R.drawable.gift_and_donation
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_expense, container, false);

        sdp = requireContext().getSharedPreferences("user_details", Context.MODE_PRIVATE);
        phone = sdp.getString("phone", "null");

        // Initialize UI elements for database
        datePickerEditText = view.findViewById(R.id.date_picker);
        timePickerEditText = view.findViewById(R.id.time_picker);
        amountEditText = view.findViewById(R.id.Amount_No);
        noteEditText = view.findViewById(R.id.note);
        attachmentEditText = view.findViewById(R.id.Attachment);
        saveButton = view.findViewById(R.id.Save);

        // Initialize database helper
        dbHelper = new FinancialDB(getActivity());

        // Set onClickListener for Save button
        saveButton.setOnClickListener(v -> {

            saveExpense();
            Intent intent = new Intent(getActivity(), Befor_Home.class);
            startActivity(intent);
            });

        // Bottom add category
        selectCategoryEditText = view.findViewById(R.id.Select_Category);
        selectedCategoryImageView = view.findViewById(R.id.selected_category_image);
        selectCategoryEditText.setOnClickListener(v -> showCategoryDialog());

        // Bottom add category Pay Type
        pay_edittext = view.findViewById(R.id.pay_text);
        pay_img = view.findViewById(R.id.pay_img);
        pay_edittext.setOnClickListener(v -> showBankAccDialog());

        // Date and time pickers
        datePickerEditText = view.findViewById(R.id.date_picker);
        timePickerEditText = view.findViewById(R.id.time_picker);
        initializeDateAndTime();

        datePickerEditText.setOnClickListener(v -> showDatePicker());
        timePickerEditText.setOnClickListener(v -> showTimePicker());


        return view;
    }


    private void showBankAccDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_add_cash_bank_acc);

        // Set click listeners dynamically
        for (int i = 0; i < payIds.length; i++) {
            int index = i; // Capture the index in a final variable
            dialog.findViewById(payIds[i]).setOnClickListener(v -> {
                pay_edittext.setText(payNames[index]);
                pay_img.setImageResource(payImages[index]);

                pay_img_res = payImages[index];

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


    private void showCategoryDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_add_layout_expense);

        // Set click listeners dynamically
        for (int i = 0; i < categoryIds.length; i++) {
            int index = i; // Capture the index in a final variable
            dialog.findViewById(categoryIds[i]).setOnClickListener(v -> {
                selectCategoryEditText.setText(categoryNames[index]);
                selectedCategoryImageView.setImageResource(categoryImages[index]);

                pay_category_res = categoryImages[index];

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

    private void saveExpense() {


        String date = datePickerEditText.getText().toString();
        String time = timePickerEditText.getText().toString();
        String amountStr = amountEditText.getText().toString();
        String category = selectCategoryEditText.getText().toString();
        String paymentMethod = pay_edittext.getText().toString();
        String note = noteEditText.getText().toString();
        String attachment = attachmentEditText.getText().toString(); // Optional field

        if (date.isEmpty() || time.isEmpty() || amountStr.isEmpty() || category.isEmpty() || paymentMethod.isEmpty()) { //note.isEmpty()
            Toast.makeText(requireContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);

        long result = dbHelper.insertExpense(date, time, amount, category, paymentMethod, note, attachment, phone);

        if (result != -1) {
            Toast.makeText(requireContext(), "Expense saved successfully", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(requireContext(), "Failed to save expense", Toast.LENGTH_SHORT).show();
        }
    }
}