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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.login_signup.Before_Home;
import com.example.login_signup.Calculator;
import com.example.login_signup.Premium;
import com.example.login_signup.R;

import com.example.login_signup.SQLiteDB.FinancialDB;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class Income extends Fragment {

    String amount , note , date , time , fromAccount , toAccount , expenseCategory , incomeCategory , incomePaymentMethod , expensePaymentMethod ;
    ArrayList<HashMap<String, String>> entryList;



    String id;
    String phone;
    SharedPreferences sdp;
    ImageView pay_img,selectedCategoryImageView;
    private EditText pay_edittext,datePickerEditText, timePickerEditText, amountEditText, selectCategoryEditText, noteEditText, attachmentEditText;
    private FinancialDB dbHelper;
    private ImageButton saveButton;

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
            R.id.category_other, R.id.category_salary, R.id.category_sold_items,
            R.id.category_coupons
    };
        private final String[] categoryNames = {
                "Other", "Salary", "Sold Items", "Coupons"
        };

    private final int[] categoryImages = {
            R.drawable.other, R.drawable.salary, R.drawable.sold, R.drawable.coupon
    };

    public Income(String id) {
        this.id=id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_income, container, false);

        sdp = requireContext().getSharedPreferences("user_details", Context.MODE_PRIVATE);
        phone = sdp.getString("phone", "null");

        datePickerEditText = view.findViewById(R.id.date_picker);
        timePickerEditText = view.findViewById(R.id.time_picker);
        amountEditText = view.findViewById(R.id.Amount_No);
        noteEditText = view.findViewById(R.id.note);
        attachmentEditText = view.findViewById(R.id.Attachment);
        saveButton = view.findViewById(R.id.Save);
        selectCategoryEditText = view.findViewById(R.id.Select_Category);
        pay_edittext= view.findViewById(R.id.pay_text);
        selectedCategoryImageView = view.findViewById(R.id.selected_category_image);
        pay_img= view.findViewById(R.id.pay_img);

        dbHelper = new FinancialDB(getActivity());

        if(id.equals("-1")) {

            selectCategoryEditText.setOnClickListener(v -> showCategoryDialog());

            pay_edittext.setOnClickListener(v -> showBankAccDialog());

            initializeDateAndTime();

            datePickerEditText.setOnClickListener(v -> showDatePicker());
            timePickerEditText.setOnClickListener(v -> showTimePicker());
            attachmentEditText.setOnClickListener(v -> showPremiumDialog());


            saveButton.setOnClickListener(v -> {
                saveIncomeData();
                Intent intent = new Intent(getActivity(), Before_Home.class);
                startActivity(intent);
            });

        }else{

            entryList = dbHelper.getData(id);

            for (HashMap<String, String> entry : entryList) {

                amount = entry.get("amount");
                note = entry.get("note");
                date = entry.get("date");
                time = entry.get("time");
                fromAccount = entry.get("from_account");
                toAccount = entry.get("to_account");
                expenseCategory = entry.get("expense_category");
                incomeCategory = entry.get("income_category");
                incomePaymentMethod = entry.get("income_payment_method");
                expensePaymentMethod = entry.get("expense_payment_method");

            }
            datePickerEditText.setText(date);
            timePickerEditText.setText(time);
            amountEditText.setText(amount);
            noteEditText.setText(note);
            selectCategoryEditText.setText(incomeCategory);
            pay_edittext.setText(incomePaymentMethod);

            for (int i = 0; i < categoryIds.length; i++) {
                if(incomeCategory.equals(categoryNames[i]))
                {
                    selectedCategoryImageView.setImageResource(categoryImages[i]);
                }
            }

            for (int i = 0; i < payIds.length; i++) {
                int index = i;
                if(incomePaymentMethod.equals(payNames[index]))
                {
                    pay_img.setImageResource(payImages[index]);
                }
            }

            selectCategoryEditText.setOnClickListener(v -> showCategoryDialog());

            pay_edittext.setOnClickListener(v -> showBankAccDialog());

            datePickerEditText.setOnClickListener(v -> showDatePicker());
            timePickerEditText.setOnClickListener(v -> showTimePicker());
            attachmentEditText.setOnClickListener(v -> showPremiumDialog());

            saveButton.setOnClickListener(v -> {
                updateIncome(id);
            });


        }

        amountEditText.setOnTouchListener((v, event) -> {
            final int DRAWABLE_END = 2; // index for the drawableEnd position

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (amountEditText.getRight() - amountEditText.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {
                    // Open the calculator dialog
                    showCalculatorDialog();
                    return true;
                }
            }
            return false;
        });


        return view;
    }

    private void showBankAccDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_add_cash_bank_acc);


        for (int i = 0; i < payIds.length; i++) {
            int index = i;
            dialog.findViewById(payIds[i]).setOnClickListener(v -> {
                pay_edittext.setText(payNames[index]);
                pay_img.setImageResource(payImages[index]);



                dialog.dismiss();
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
        dialog.setContentView(R.layout.bottom_add_layout_income);

        for (int i = 0; i < categoryIds.length; i++) {
            int index = i;
            dialog.findViewById(categoryIds[i]).setOnClickListener(v -> {
                selectCategoryEditText.setText(categoryNames[index]);
                selectedCategoryImageView.setImageResource(categoryImages[index]);

                dialog.dismiss();
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
                true
        );
        timePickerDialog.show();
    }

    private void initializeDateAndTime() {
        Calendar calendar = Calendar.getInstance();

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

    private void saveIncomeData() {
        String date = datePickerEditText.getText().toString();
        String time = timePickerEditText.getText().toString();
        String amountString = amountEditText.getText().toString();
        String category = selectCategoryEditText.getText().toString();
        String paymentMethod = pay_edittext.getText().toString();
        String note = noteEditText.getText().toString();
        String attachment = attachmentEditText.getText().toString();

        if (date.isEmpty() || time.isEmpty() || amountString.isEmpty() || category.isEmpty() ) {  //|| paymentMethod.isEmpty()
            Toast.makeText(getContext(), "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountString);
        long result = dbHelper.insertIncome(date, time, amount, category, paymentMethod, note, attachment,phone);

        if (result != -1) {
            Toast.makeText(getContext(), "Income saved successfully.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Error saving income.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateIncome(String incomeId) {
        int incomeIdInt = Integer.parseInt(incomeId);
        String date = datePickerEditText.getText().toString();
        String time = timePickerEditText.getText().toString();
        String amountStr = amountEditText.getText().toString();
        String category = selectCategoryEditText.getText().toString();
        String paymentMethod = pay_edittext.getText().toString();
        String note = noteEditText.getText().toString();
        String attachment = attachmentEditText.getText().toString(); // Optional field

        // Validate fields
        if (date.isEmpty() || time.isEmpty() || amountStr.isEmpty() || category.isEmpty() || paymentMethod.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);

        // Update the income in the database with type "income"
        String type = "income";

        int result = dbHelper.updateList(incomeIdInt, date, time, amount, category, paymentMethod, note, attachment, type);

        if (result > 0) {
            Toast.makeText(requireContext(), "Income updated successfully", Toast.LENGTH_SHORT).show();

            // Navigate back to the desired screen
            Intent intent = new Intent(getActivity(), Before_Home.class);
            startActivity(intent);

        } else {
            Toast.makeText(requireContext(), "Failed to update income", Toast.LENGTH_SHORT).show();
        }
    }

    private void showCalculatorDialog() {
        final Dialog calculatorDialog = new Dialog(requireContext());
        calculatorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        calculatorDialog.setContentView(R.layout.dialog_calculator);
        calculatorDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Pass the dialog's root view and the EditText where the result should be shown
        Calculator calculator = new Calculator(calculatorDialog.findViewById(android.R.id.content), amountEditText);

        calculatorDialog.show();
    }

}