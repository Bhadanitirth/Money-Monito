package com.example.login_signup.LoginSignup;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login_signup.Before_Home;
import com.example.login_signup.R;
import com.example.login_signup.SQLiteDB.Login_Signin_Db;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class SignUp extends AppCompatActivity {

    SharedPreferences sdp;
    EditText editTextFirstName, editTextSurname, etPhoneNumber, etOtp, editTextPassword, editTextDOB, editTextPin;
    RadioGroup radioGroupGender;
    Button buttonSignUp, btnSendOtp;
    Login_Signin_Db dbHelper;

    Intent intent;
    TextView login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        sdp = getSharedPreferences("user_details", MODE_PRIVATE);
        editTextFirstName = findViewById(R.id.textfname);
        editTextSurname = findViewById(R.id.textsurname);
        etPhoneNumber = findViewById(R.id.textid);
        etOtp = findViewById(R.id.otp);
        btnSendOtp = findViewById(R.id.getotp);
        editTextPassword = findViewById(R.id.textpass);
        editTextPin = findViewById(R.id.TextPin);
        editTextDOB = findViewById(R.id.dob);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        buttonSignUp = findViewById(R.id.bsignin);
        login = findViewById(R.id.gologin);

        dbHelper = new Login_Signin_Db(this);

        editTextDOB.setOnClickListener(v -> showDatePicker());

        login.setOnClickListener(view -> {
            Intent intent = new Intent(SignUp.this, Login.class);
            startActivity(intent);
        });

        buttonSignUp.setOnClickListener(v -> {


                    String firstName = editTextFirstName.getText().toString().trim();
                    String surname = editTextSurname.getText().toString().trim();
                    String phone = etPhoneNumber.getText().toString().trim();
                    String password = editTextPassword.getText().toString().trim();
                    String dob = editTextDOB.getText().toString().trim();
                    String pin = editTextPin.getText().toString().trim();
                    int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
                    RadioButton radioButton = findViewById(selectedGenderId);
                    String gender = radioButton != null ? radioButton.getText().toString() : "";

                    if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(surname) || TextUtils.isEmpty(phone) ||
                            TextUtils.isEmpty(password) || TextUtils.isEmpty(dob) || TextUtils.isEmpty(pin) ||
                            selectedGenderId == -1) {
                        Toast.makeText(SignUp.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (dbHelper.isPhoneAlreadyUsed(phone)) {
                        Toast.makeText(SignUp.this, "Phone no. already in use. Please choose a different Phone no.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (dbHelper.isPinAlreadyUsed(pin)) {
                        Toast.makeText(SignUp.this, "PIN already in use. Please choose a different PIN.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (dbHelper.insertUser(firstName, surname, phone, password, dob, gender, pin)) {
                        Toast.makeText(SignUp.this, "Sign up Done", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = sdp.edit();
                        editor.putString("phone", phone);
                        editor.putString("password", password);
                        editor.apply();
                        intent = new Intent(SignUp.this, Before_Home.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignUp.this, "Sign up failed, try again!", Toast.LENGTH_SHORT).show();
                    }
            });

    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, month1, dayOfMonth) -> {
                    String date = String.format("%02d/%02d/%d", dayOfMonth, (month1 + 1), year1);
                    editTextDOB.setText(date);
                },
                year,
                month,
                day
        );
        datePickerDialog.show();
    }
}
