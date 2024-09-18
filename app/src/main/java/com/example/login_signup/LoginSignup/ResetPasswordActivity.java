package com.example.login_signup.LoginSignup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login_signup.R;
import com.example.login_signup.SQLiteDB.Login_Signin_Db;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText editTextPhone, editTextOtp, editTextNewPassword;
    Button buttonGetOtp, buttonResetPassword;
    Login_Signin_Db dbHelper;
    Intent intent;
    SharedPreferences sdp;
    TextView go_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);


        editTextPhone=findViewById(R.id.textid);
        sdp = getSharedPreferences("user_details",MODE_PRIVATE);
        editTextPhone.setText(sdp.getString("phone",null));


        editTextOtp = findViewById(R.id.otp);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        buttonGetOtp = findViewById(R.id.getotp);
        buttonResetPassword = findViewById(R.id.buttonResetPassword);

        dbHelper = new Login_Signin_Db(this);

        go_back=findViewById(R.id.back);
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(ResetPasswordActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
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


                if (dbHelper.verifyOtpAndResetPassword(phone, otp, newPassword)) {
                    Toast.makeText(ResetPasswordActivity.this, "Password reset successfully!", Toast.LENGTH_SHORT).show();
                    intent=new Intent(ResetPasswordActivity.this,Login.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Password reset unsuccessfully!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
