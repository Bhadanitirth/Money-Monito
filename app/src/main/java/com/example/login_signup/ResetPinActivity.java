package com.example.login_signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResetPinActivity extends AppCompatActivity {

    EditText editTextPhone, editTextOtp, editTextNewPin;
    Button buttonGetOtp, buttonResetPin;
    Login_Signin_Db dbHelper;
Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pin);

        editTextPhone = findViewById(R.id.textid);
        editTextOtp = findViewById(R.id.otp);
        editTextNewPin = findViewById(R.id.editTextNewPin);
        buttonGetOtp = findViewById(R.id.getotp);
        buttonResetPin = findViewById(R.id.buttonResetPin);

        dbHelper = new Login_Signin_Db(this);

        buttonGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = editTextPhone.getText().toString();
                // OTP sending later
                Toast.makeText(ResetPinActivity.this, "OTP sent to " + phone, Toast.LENGTH_SHORT).show();
            }
        });

        buttonResetPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = editTextPhone.getText().toString();
                String otp = editTextOtp.getText().toString();
                String newPin = editTextNewPin.getText().toString();

                // Verify OTP and reset PIN
                if (dbHelper.verifyOtpAndResetPin(phone, otp, newPin)) {
                    Toast.makeText(ResetPinActivity.this, "PIN reset successfully!", Toast.LENGTH_SHORT).show();
                    intent=new Intent(ResetPinActivity.this,Pin.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ResetPinActivity.this, "OTP verification failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
