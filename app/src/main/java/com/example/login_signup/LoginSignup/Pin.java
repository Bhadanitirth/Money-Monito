package com.example.login_signup.LoginSignup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login_signup.Before_Home;
import com.example.login_signup.R;
import com.example.login_signup.SQLiteDB.Login_Signin_Db;
import com.google.android.material.snackbar.Snackbar;

public class Pin extends AppCompatActivity {

    EditText editTextPin;
    TextView forgetPin, sdp_text;
    Button[] numberButtons = new Button[10];
    ImageButton clearTextButton;
    GridLayout numberGridLayout;
    StringBuilder pinBuilder = new StringBuilder();
    Login_Signin_Db dbHelper;
    TextView switchUser;
    SharedPreferences sdp;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        sdp_text = findViewById(R.id.sdptxt);
        sdp = getSharedPreferences("user_details", MODE_PRIVATE);
        phone = sdp.getString("phone", "null");
        sdp_text.setText(phone);

        editTextPin = findViewById(R.id.editTextPin);
        numberGridLayout = findViewById(R.id.numberGridLayout);
        clearTextButton = findViewById(R.id.Clear_text);
        forgetPin = findViewById(R.id.ForgetPin);
        switchUser = findViewById(R.id.Switch_User);

        dbHelper = new Login_Signin_Db(this);

        for (int i = 0; i < 10; i++) {
            int resID = getResources().getIdentifier("b" + i, "id", getPackageName());
            numberButtons[i] = findViewById(resID);
            final int digit = i;
            numberButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pinBuilder.length() < 4) {
                        pinBuilder.append(digit);
                        editTextPin.setText(pinBuilder.toString());
                    }
                }
            });
        }

        clearTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pinBuilder.length() > 0) {
                    pinBuilder.deleteCharAt(pinBuilder.length() - 1);
                    editTextPin.setText(pinBuilder.toString());

                }
            }
        });

        switchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sdp.edit();
                editor.clear();
                editor.commit();

                Intent intent = new Intent(Pin.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        forgetPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Pin.this, ResetPinActivity.class);
                startActivity(intent);
                finish();
            }
        });

        editTextPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 4) {
                    String enteredPin = s.toString();
                    if (isValidPin(enteredPin, phone)) {
                        Intent intent = new Intent(Pin.this, Before_Home.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "Incorrect PIN", Snackbar.LENGTH_SHORT).show();
                        editTextPin.setText("");
                    }
                }
            }
        });
    }

    private boolean isValidPin(String pin, String phone) {
        Boolean storedPin = dbHelper.getStoredPin(pin, phone);
        return storedPin;
    }
}
