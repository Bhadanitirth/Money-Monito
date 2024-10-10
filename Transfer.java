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

public class Transfer extends Fragment {

    String amount , note , date , time , fromAccount , toAccount , expenseCategory , incomeCategory , incomePaymentMethod , expensePaymentMethod ;

    String phone,id;
    ArrayList<HashMap<String, String>> entryList;
    SharedPreferences sdp;


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
    private EditText datePickerEditText, timePickerEditText, amountEditText, noteEditText, attachment;
    private FinancialDB dbHelper;
    ImageButton saveButton;

    public Transfer(String id) {
        this.id=id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_transfer, container, false);

        sdp = requireContext().getSharedPreferences("user_details", Context.MODE_PRIVATE);
        phone = sdp.getString("phone", "null");

        datePickerEditText = view.findViewById(R.id.date_picker);
        timePickerEditText = view.findViewById(R.id.time_picker);
        amountEditText = view.findViewById(R.id.Amount_No);
        noteEditText = view.findViewById(R.id.note);
        attachment = view.findViewById(R.id.Attachment);
        saveButton = view.findViewById(R.id.Save);
        pay_to= view.findViewById(R.id.pay_textto);
        getPay_img_to= view.findViewById(R.id.pay_imgto);
        pay_from= view.findViewById(R.id.pay_textform);
        pay_img_from= view.findViewById(R.id.pay_imgfrom);

        dbHelper = new FinancialDB(getContext());

        if(id.equals("-1")) {
            pay_from.setOnClickListener(v -> showBankAccDialog_from());

            pay_to.setOnClickListener(v -> showBankAccDialog_to());

            initializeDateAndTime();

            datePickerEditText.setOnClickListener(v -> showDatePicker());
            timePickerEditText.setOnClickListener(v -> showTimePicker());
            attachment.setOnClickListener(v -> showPremiumDialog());

            saveButton.setOnClickListener(v -> {
                saveTransferDetails();
            });
        }else {
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
            pay_to.setText(toAccount);
            pay_from.setText(fromAccount);

            for (int i = 0; i < payIds.length; i++) {
                if(toAccount.equals(payNames[i]))
                {
                    getPay_img_to.setImageResource(payImages[i]);
                }
            }
            for (int i = 0; i < payIds.length; i++) {
                if(fromAccount.equals(payNames[i]))
                {
                    pay_img_from.setImageResource(payImages[i]);
                }
            }
            pay_from.setOnClickListener(v -> showBankAccDialog_from());

            pay_to.setOnClickListener(v -> showBankAccDialog_to());

            datePickerEditText.setOnClickListener(v -> showDatePicker());
            timePickerEditText.setOnClickListener(v -> showTimePicker());
            attachment.setOnClickListener(v -> showPremiumDialog());

            saveButton.setOnClickListener(v -> {
                updateTransfer(id);
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

    private void showBankAccDialog_from() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_add_cash_bank_acc);

        for (int i = 0; i < payIds.length; i++) {
            int index = i;
            dialog.findViewById(payIds[i]).setOnClickListener(v -> {
                pay_from.setText(payNames[index]);
                pay_img_from.setImageResource(payImages[index]);

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

    private void showBankAccDialog_to() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_add_cash_bank_acc);


        for (int i = 0; i < payIds.length; i++) {
            int index = i;
            dialog.findViewById(payIds[i]).setOnClickListener(v -> {
                pay_to.setText(payNames[index]);
                getPay_img_to.setImageResource(payImages[index]);

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

    private void saveTransferDetails() {
        String date = datePickerEditText.getText().toString().trim();
        String time = timePickerEditText.getText().toString().trim();
        String amountStr = amountEditText.getText().toString().trim();
        String fromAccount = pay_from.getText().toString().trim();
        String toAccount = pay_to.getText().toString().trim();
        String noteText = noteEditText.getText().toString().trim();
        String attachmentText = attachment.getText().toString().trim();

        if (date.isEmpty() || time.isEmpty() || amountStr.isEmpty() || fromAccount.isEmpty() || toAccount.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);

        long result = dbHelper.insertTransfer(date, time, amount, fromAccount, toAccount, noteText, attachmentText,phone);

        if (result != -1) {
            Toast.makeText(getContext(), "Transfer saved successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), Before_Home.class);
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "Failed to save transfer", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTransfer(String transferId) {
        int transferIdInt = Integer.parseInt(transferId);
        String date = datePickerEditText.getText().toString();
        String time = timePickerEditText.getText().toString();
        String amountStr = amountEditText.getText().toString();
        String fromAccount = pay_from.getText().toString();
        String toAccount = pay_to.getText().toString();
        String noteText = noteEditText.getText().toString();
        String attachmentText = attachment.getText().toString();
        String type = "Transfer";

        if (date.isEmpty() || time.isEmpty() || amountStr.isEmpty() || fromAccount.isEmpty() || toAccount.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);

        int result = dbHelper.updateList(transferIdInt, date, time, amount, fromAccount, toAccount, noteText, attachmentText, type);

        if (result > 0) {
            Toast.makeText(getContext(), "Transfer updated successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), Before_Home.class);
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "Failed to update transfer", Toast.LENGTH_SHORT).show();
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