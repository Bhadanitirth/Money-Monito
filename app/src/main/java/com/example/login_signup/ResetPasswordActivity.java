package com.example.login_signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText editTextPhone, editTextOtp, editTextNewPassword;
    Button buttonGetOtp, buttonResetPassword;
    Login_Signin_Db dbHelper;
Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        editTextPhone = findViewById(R.id.textid);
        editTextOtp = findViewById(R.id.otp);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        buttonGetOtp = findViewById(R.id.getotp);
        buttonResetPassword = findViewById(R.id.buttonResetPassword);

        dbHelper = new Login_Signin_Db(this);

        buttonGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = editTextPhone.getText().toString();
                // OTP sending later
                Toast.makeText(ResetPasswordActivity.this, "OTP sent to " + phone, Toast.LENGTH_SHORT).show();
            }
        });

        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = editTextPhone.getText().toString();
                String otp = editTextOtp.getText().toString();
                String newPassword = editTextNewPassword.getText().toString();

                // Verify OTP and reset password
                if (dbHelper.verifyOtpAndResetPassword(phone, otp, newPassword)) {
                    Toast.makeText(ResetPasswordActivity.this, "Password reset successfully!", Toast.LENGTH_SHORT).show();
                    intent=new Intent(ResetPasswordActivity.this,Login.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "OTP verification failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
