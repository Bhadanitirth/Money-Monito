package com.example.login_signup.LoginSignup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login_signup.Befor_Home;
import com.example.login_signup.R;
import com.example.login_signup.SQLiteDB.Login_Signin_Db;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Signin extends AppCompatActivity {

    SharedPreferences sdp;
    EditText editTextFirstName, editTextSurname, etPhoneNumber, etOtp, editTextPassword, editTextDOB, editTextPin;
    RadioGroup radioGroupGender;
    Button buttonSignUp, btnSendOtp;
    Login_Signin_Db dbHelper;
    private String otpSent;
    Intent intent;
    TextView login;
    private FirebaseAuth mAuth;
    private String verificationId;

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

        mAuth = FirebaseAuth.getInstance();

        btnSendOtp.setOnClickListener(v -> sendOtp());
        etOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6) {
                    // You might want to handle OTP verification here if needed
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        login.setOnClickListener(view -> {
            Intent intent = new Intent(Signin.this, Login.class);
            startActivity(intent);
        });

        buttonSignUp.setOnClickListener(v -> {
            // Call verifyOtp with a callback
            verifyOtp(isVerified -> {
                if (isVerified) {
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
                        Toast.makeText(Signin.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (dbHelper.isPhoneAlreadyUsed(phone)) {
                        Toast.makeText(Signin.this, "Phone no. already in use. Please choose a different Phone no.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (dbHelper.isPinAlreadyUsed(pin)) {
                        Toast.makeText(Signin.this, "PIN already in use. Please choose a different PIN.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (dbHelper.insertUser(firstName, surname, phone, password, dob, gender, pin)) {
                        Toast.makeText(Signin.this, "Sign up Done", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = sdp.edit();
                        editor.putString("phone", phone);
                        editor.putString("password", password);
                        editor.apply();
                        intent = new Intent(Signin.this, Befor_Home.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Signin.this, "Sign up failed, try again!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Signin.this, "OTP verification failed", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void sendOtp() {
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        if (phoneNumber.isEmpty()) {
            etPhoneNumber.setError("Enter phone number");
            return;
        }

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                        // Auto verification or instant verification
                        signInWithCredential(credential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(Signin.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId,
                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        // This callback will be invoked when the OTP is sent.
                        Signin.this.verificationId = verificationId;
                        Toast.makeText(Signin.this, "OTP sent", Toast.LENGTH_SHORT).show();
                    }
                })
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyOtp(final OnOtpVerificationListener listener) {
        String otp = etOtp.getText().toString().trim();
        if (otp.isEmpty()) {
            etOtp.setError("Enter OTP");
            if (listener != null) listener.onVerificationCompleted(false);
            return;
        }

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Signin.this, "Verification successful", Toast.LENGTH_SHORT).show();
                        if (listener != null) listener.onVerificationCompleted(true);
                    } else {
                        Toast.makeText(Signin.this, "Verification failed", Toast.LENGTH_SHORT).show();
                        if (listener != null) listener.onVerificationCompleted(false);
                    }
                });
    }

    // Define the callback interface
    public interface OnOtpVerificationListener {
        void onVerificationCompleted(boolean isVerified);
    }
    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in was successful
                        Toast.makeText(Signin.this, "Verification successful", Toast.LENGTH_SHORT).show();
                    } else {
                        // Sign in failed
                        Toast.makeText(Signin.this, "Verification failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
