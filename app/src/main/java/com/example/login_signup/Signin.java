package com.example.login_signup;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class Signin extends AppCompatActivity {

    EditText editTextFirstName, editTextSurname, editTextPhone, editTextOtp, editTextPassword, editTextDOB, editTextPin;
    RadioGroup radioGroupGender;
    Button buttonSignUp,getotp;
    Login_Signin_Db dbHelper;
    private String otpSent;
    Intent intent;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        editTextFirstName = findViewById(R.id.textfname);
        editTextSurname = findViewById(R.id.textsurname);
        editTextPhone = findViewById(R.id.textid);
        editTextOtp=findViewById(R.id.otp);
        getotp = findViewById(R.id.getotp);
        editTextPassword = findViewById(R.id.textpass);
        editTextPin = findViewById(R.id.TextPin);
        editTextDOB = findViewById(R.id.dob);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        buttonSignUp = findViewById(R.id.bsignin);
        login=findViewById(R.id.gologin);

        dbHelper = new Login_Signin_Db(this);

        getotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sendOtp();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Signin.this, Login.class);
                startActivity(intent);
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean otp=verifyOtp(otpSent);
                if(otp) {
                    String firstName = editTextFirstName.getText().toString();
                    String surname = editTextSurname.getText().toString();
                    String phone = editTextPhone.getText().toString();
                    String password = editTextPassword.getText().toString();
                    String dob = editTextDOB.getText().toString();
                    String pin = editTextPin.getText().toString();
                    int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
                    RadioButton radioButton = findViewById(selectedGenderId);
                    String gender = radioButton.getText().toString();

                    if (dbHelper.insertUser(firstName, surname, phone, password, dob, gender, pin)) {
//                        Intent intent = new Intent(Signin.this, OtpVerificationActivity.class);
//                        intent.putExtra("phone", phone);
//                        intent.putExtra("resetType", "none");
//                        startActivity(intent);
                        Toast.makeText(Signin.this, "Sign up Done", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Signin.this, HomeActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(Signin.this, "Sign up failed, try again!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private boolean verifyOtp(String otpSent) {
//        String enteredOtp = editTextOtp.getText().toString().trim();
//
//        if (TextUtils.isEmpty(enteredOtp)) {
//            Toast.makeText(this, "Please enter OTP", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//        if (enteredOtp.equals(otpSent)) {
//            // OTP verified successfully
//            Toast.makeText(this, "OTP Verified Successfully", Toast.LENGTH_SHORT).show();
//            finish(); // Close the OTP verification activity
//            return true;
//        } else {
//            // OTP verification failed
//            Toast.makeText(this, "Invalid OTP. Please try again.", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        return true;
    }

    private void sendOtp() {
        Random r = new Random();
        String otp = String.format("%04d", r.nextInt(10000)); // Generates a random 4-digit OTP

        String mobileNumber = editTextPhone.getText().toString();
        if (mobileNumber.isEmpty() || mobileNumber.length() != 10) {
            Toast.makeText(Signin.this, "Please enter a valid 10-digit mobile number", Toast.LENGTH_SHORT).show();
            return;
        }

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(mobileNumber, null, "Your OTP is: " + otp, null, null);
        otpSent=otp;
        Boolean otp_ver=verifyOtp(otpSent);
    }
}
