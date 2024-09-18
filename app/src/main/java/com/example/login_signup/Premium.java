package com.example.login_signup;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login_signup.SQLiteDB.FinancialDB;
import com.example.login_signup.adapter.CustomAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Premium extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.premium);

    }
}
