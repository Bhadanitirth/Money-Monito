package com.example.login_signup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login_signup.fragments.MoreFragment;

public class Tags extends AppCompatActivity {

    Button unlock_Btn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tags);

        unlock_Btn=findViewById(R.id.unlock_now);

        unlock_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Tags.this,Premium.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
