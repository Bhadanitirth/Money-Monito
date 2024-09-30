package com.example.login_signup.LoginSignup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login_signup.R;
import com.example.login_signup.SQLiteDB.Login_Signin_Db;
import com.google.android.material.snackbar.Snackbar;

public class Login extends AppCompatActivity {

    EditText editTextPhone, editTextPassword;
    TextView ForgetPassword, signup, blanktxt;
    Button buttonLogin;
    Login_Signin_Db dbHelper;
    LinearLayout mainLayout;
    SharedPreferences sdp;
    private boolean isPasswordVisible = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        editTextPhone = findViewById(R.id.textid);
        blanktxt= findViewById(R.id.blanktxt);
        editTextPassword = findViewById(R.id.textpass);
        buttonLogin = findViewById(R.id.blogin);
        ForgetPassword = findViewById(R.id.textView);
        signup = findViewById(R.id.signin);
        mainLayout = findViewById(R.id.mainLayout);
        dbHelper = new Login_Signin_Db(this);
        sdp = getSharedPreferences("user_details", MODE_PRIVATE);

        if (sdp.contains("phone") && sdp.contains("password")) {
            Intent intent = new Intent(Login.this, Pin.class);
            startActivity(intent);
        }

        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                return true;
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = editTextPhone.getText().toString();
                String password = editTextPassword.getText().toString();

                if (dbHelper.checkUserCredentials(phone, password)) {
                    SharedPreferences.Editor editor = sdp.edit();
                    editor.putString("phone", phone);
                    editor.putString("password", password);
                    editor.commit();

                    Intent intent = new Intent(Login.this, Pin.class);
                    startActivity(intent);
                    finish();
                } else {
                    blanktxt.setText("Invalid credentials!");
                    blanktxt.setTextColor(Color.RED);
                    Snackbar.make(findViewById(android.R.id.content), "Invalid credentials!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        ForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = editTextPhone.getText().toString();

                if (!phone.isEmpty()) {
                    Intent intent = new Intent(Login.this, ResetPasswordActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Please enter your phone number!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
