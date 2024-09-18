package com.example.login_signup.LoginSignup;

import android.content.Intent;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login_signup.R;
import com.example.login_signup.SQLiteDB.Login_Signin_Db;

public class ResetPinActivity extends AppCompatActivity {



    TextView go_back;
    EditText editTextPhone, editTextOtp, editTextNewPin;
    Button buttonGetOtp, buttonResetPin;
    Login_Signin_Db dbHelper;
    Intent intent;
    SharedPreferences sdp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pin);

        editTextPhone=findViewById(R.id.textid);
        sdp = getSharedPreferences("user_details",MODE_PRIVATE);
        editTextPhone.setText(sdp.getString("phone",null));

        editTextOtp = findViewById(R.id.otp);
        editTextNewPin = findViewById(R.id.editTextNewPin);
        buttonGetOtp = findViewById(R.id.getotp);
        buttonResetPin = findViewById(R.id.buttonResetPin);

        dbHelper = new Login_Signin_Db(this);

        go_back=findViewById(R.id.back);
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(ResetPinActivity.this, Pin.class);
                startActivity(intent);
                finish();
            }
        });

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
                if (dbHelper.verifyOtpAndResetPin(phone, otp, newPin)) {
                    Toast.makeText(ResetPinActivity.this, "PIN reset successfully!", Toast.LENGTH_SHORT).show();
                    intent=new Intent(ResetPinActivity.this,Pin.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ResetPinActivity.this, "Password reset unsuccessfully!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
