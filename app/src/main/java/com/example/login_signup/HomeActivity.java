package com.example.login_signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    Button buttonViewBookings, buttonManageProfile, buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        buttonViewBookings = findViewById(R.id.buttonViewBookings);
        buttonManageProfile = findViewById(R.id.buttonManageProfile);
        buttonLogout = findViewById(R.id.buttonLogout);

//        buttonViewBookings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(HomeActivity.this, ViewBookingsActivity.class);
//                startActivity(intent);
//            }
//        });

//        buttonManageProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(HomeActivity.this, ManageProfileActivity.class);
//                startActivity(intent);
//            }
//        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement logout logic
                Intent intent = new Intent(HomeActivity.this, Login.class);
                startActivity(intent);
                finish(); // Close HomeActivity
            }
        });
    }
}
