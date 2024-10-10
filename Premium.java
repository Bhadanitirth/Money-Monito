package com.example.login_signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.login_signup.fragments.HomeFragment;
import com.example.login_signup.fragments.MoreFragment;

public class Premium extends AppCompatActivity {

    TextView close;
    ImageView backgroundImage;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.premium);

        close = findViewById(R.id.imageView2);
        backgroundImage = findViewById(R.id.backgroundImage);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int fragment = getIntent().getIntExtra("fragment", -1);
                if(fragment == 4)
                {   Intent intent=new Intent(Premium.this, Before_Home.class);
                    intent.putExtra("fragment",4);
                    startActivity(intent);
                }else {
                    Intent intent=new Intent(Premium.this, Before_Home.class);
                    intent.putExtra("fragment",4);
                    startActivity(intent);
                }

            }
        });
    }
}
