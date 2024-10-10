package com.example.login_signup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    RecyclerView recyclerView; // Change the variable name
    CustomAdapter adapter;
    SearchView search_bar;
    ImageView empty_list,delete;
    TextView no_data_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allentry);

        sdp = getSharedPreferences("user_details", Context.MODE_PRIVATE);
        phone = sdp.getString("phone", "null");

        empty_list=findViewById(R.id.empty_list);
        no_data_text=findViewById(R.id.no_data_txt);

        delete = findViewById(R.id.delet);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteConfirmationDialog();
            }
        });

        // Initialize database helpers
        entryDb = new FinancialDB(this);

        entryList = entryDb.GetAllUsers(phone);
        Collections.reverse(entryList);

        if (entryList.isEmpty()) {

            empty_list.setImageResource(R.drawable.no_transaction);
            no_data_text.setText("No Transaction");

        } else {

            // Set up the RecyclerView
            recyclerView = findViewById(R.id.user_recycler_view); // Updated ID
            recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Set layout manager
            adapter = new CustomAdapter(this, entryList);
            recyclerView.setAdapter(adapter); // Use RecyclerView's setAdapter

        }

        search_bar = findViewById(R.id.search_bar);
        // Set up the SearchView listener
        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Confirmation")
                .setMessage("Are you sure you want to delete this all transaction?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int count = entryDb.delete(phone);

                        if (count > 0) {
                            Toast.makeText(allEntry.this, "Delete done", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(allEntry.this, Before_Home.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(allEntry.this, "Failed to delete", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
