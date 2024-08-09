package com.example.login_signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    EditText editTextPhone, editTextPassword;
    TextView ForgetPassword;
    Button buttonLogin,buttonSignUp;
    Login_Signin_Db dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        editTextPhone = findViewById(R.id.textid);
        editTextPassword = findViewById(R.id.textpass);
        buttonLogin = findViewById(R.id.blogin);
        ForgetPassword = findViewById(R.id.textView);
        buttonSignUp = findViewById(R.id.bsignin);

        dbHelper = new Login_Signin_Db(this);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = editTextPhone.getText().toString();
                String password = editTextPassword.getText().toString();

                if (dbHelper.checkUserCredentials(phone, password)) {
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Login.this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = editTextPhone.getText().toString();

                if (!phone.isEmpty()) {
                    Intent intent = new Intent(Login.this, ResetPasswordActivity.class);
                    intent.putExtra("phone", phone);
                    startActivity(intent);
                } else {
                    Toast.makeText(Login.this, "Please enter your phone number!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Signin.class);
                startActivity(intent);
            }
        });
    }
}
