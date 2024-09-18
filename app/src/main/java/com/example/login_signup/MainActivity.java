package com.example.login_signup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login_signup.LoginSignup.Login;

public class MainActivity extends AppCompatActivity {

    
    Button pin;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        intent=new Intent(MainActivity.this, Login.class);
        startActivity(intent);
        finish();
    }
}


