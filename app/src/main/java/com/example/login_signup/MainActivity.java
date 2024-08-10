package com.example.login_signup;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button pin;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    pin = findViewById(R.id.pin);
    pin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            intent=new Intent(MainActivity.this,Pin.class);
            startActivity(intent);
        }
    });
    }
}


