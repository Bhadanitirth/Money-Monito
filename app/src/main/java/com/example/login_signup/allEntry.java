package com.example.login_signup;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login_signup.SQLiteDB.FinancialDB;
import com.example.login_signup.adapter.CustomAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class allEntry extends AppCompatActivity {

    FinancialDB entryDb;
    ArrayList<HashMap<String, String>> entryList;
    SharedPreferences sdp;
    String phone;
    ListView lv;
    CustomAdapter adapter;
    SearchView search_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allentry);

        sdp = getSharedPreferences("user_details", Context.MODE_PRIVATE);
        phone = sdp.getString("phone", "null");


        // Initialize database helpers
        entryDb = new FinancialDB(this);

        entryList = entryDb.GetAllUsers(phone);

        Collections.reverse(entryList);

        // Set up the ListView
        lv = findViewById(R.id.user_list);
        adapter = new CustomAdapter(this, entryList);
        lv.setAdapter(adapter);

        search_bar=findViewById(R.id.search_bar);
        // Set up the SearchView listener
        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query); // Use adapter's filter
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText); // Use adapter's filter
                return false;
            }
        });
    }

}
