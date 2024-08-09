package com.example.login_signup;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText editTextPin;
    TextView forgetPin;
    Button[] numberButtons = new Button[10];
    Button clearTextButton;
    GridLayout numberGridLayout;
    StringBuilder pinBuilder = new StringBuilder();
    Login_Signin_Db dbHelper;
    TextView switchUser , signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextPin = findViewById(R.id.editTextPin);
        numberGridLayout = findViewById(R.id.numberGridLayout);
        clearTextButton = findViewById(R.id.clear_text);
        forgetPin = findViewById(R.id.ForgetPin);
        switchUser = findViewById(R.id.Switch_User);
        signup = findViewById(R.id.Sign_Up);
        dbHelper = new Login_Signin_Db(this);

        for (int i = 0; i < 10; i++) {
            int resID = getResources().getIdentifier("b" + i, "id", getPackageName());
            numberButtons[i] = findViewById(resID);
            final int digit = i;
            numberButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pinBuilder.append(digit);
                    editTextPin.setText(pinBuilder.toString());
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
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Signin.class);
                startActivity(intent);
            }
        });

        forgetPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResetPinActivity.class);
                startActivity(intent);
            }
        });

        editTextPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed before text is changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed during text change
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 4) {
                    String enteredPin = s.toString();
                    if (isValidPin(enteredPin)) {
                        // PIN is correct, proceed to home activity
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish(); // Close the current activity
                    } else {
                        // PIN is incorrect, show a toast message
                        Toast.makeText(MainActivity.this, "Incorrect PIN", Toast.LENGTH_SHORT).show();
                        editTextPin.setText(""); // Clear the input field
                    }
                }
            }
        });
    }

    private boolean isValidPin(String pin) {
        // Check the pin in the SQLite database
        Boolean storedPin = dbHelper.getStoredPin(pin); // Assuming you have a method to get stored PIN
        return storedPin;
    }
}


