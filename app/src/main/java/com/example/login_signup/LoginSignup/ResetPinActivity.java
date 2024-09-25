package com.example.login_signup.LoginSignup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login_signup.R;
import com.example.login_signup.SQLiteDB.Login_Signin_Db;
import com.google.android.material.snackbar.Snackbar;

public class ResetPinActivity extends AppCompatActivity {

    TextView go_back;
    EditText editTextPhone, editTextOtp, editTextNewPin, txtMessage;
    Button buttonGetOtp, buttonResetPin;
    Login_Signin_Db dbHelper;
    Intent intent;
    SharedPreferences sdp;
    int otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pin);

        editTextPhone = findViewById(R.id.textid);
        sdp = getSharedPreferences("user_details", MODE_PRIVATE);
        editTextPhone.setText(sdp.getString("phone", null));

        editTextOtp = findViewById(R.id.otp);
        editTextNewPin = findViewById(R.id.editTextNewPin);
        buttonGetOtp = findViewById(R.id.getotp);
        buttonResetPin = findViewById(R.id.buttonResetPin);

        dbHelper = new Login_Signin_Db(this);

        go_back = findViewById(R.id.back);
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(ResetPinActivity.this, Pin.class);
                startActivity(intent);
                finish();
            }
        });

        buttonGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = editTextPhone.getText().toString();
                try {
                    otp = generateRandomOtp(); // Method defined below
                    String otpMessage = "Your OTP is: " + otp;
                    txtMessage.setText(otpMessage);

                    SmsManager smgr = SmsManager.getDefault();
                    smgr.sendTextMessage(editTextPhone.getText().toString(), null, txtMessage.getText().toString(), null, null);

                    Snackbar.make(findViewById(android.R.id.content), "SMS Sent Successfully", Snackbar.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Snackbar.make(findViewById(android.R.id.content), "SMS Failed to Send, Please try again", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        buttonResetPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_otp = editTextOtp.getText().toString();
                String rendom_otp = otp + "";

                if (rendom_otp.equals(user_otp)) {
                    String phone = editTextPhone.getText().toString();
                    String newPin = editTextNewPin.getText().toString();

                    if (dbHelper.ResetPin(phone, newPin)) {
                        Snackbar.make(findViewById(android.R.id.content), "PIN reset successfully!", Snackbar.LENGTH_SHORT).show();
                        intent = new Intent(ResetPinActivity.this, Pin.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "PIN reset unsuccessfully!", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Invalid OTP", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private int generateRandomOtp() {
        // This will generate a 6-digit number between 100000 and 999999
        return (int)(Math.random() * 900000) + 100000;
    }
}
