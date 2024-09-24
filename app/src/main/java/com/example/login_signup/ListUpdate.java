package com.example.login_signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.example.login_signup.SQLiteDB.FinancialDB;
import com.example.login_signup.adapter.AddTransectionAdapter;
import com.example.login_signup.databinding.HomeActivityBinding;
import com.example.login_signup.databinding.ListUpdateBinding;
import com.example.login_signup.fragments.AccountFragment;
import com.example.login_signup.fragments.AddTransaction;
import com.example.login_signup.fragments.AnalisisFragment;
import com.example.login_signup.fragments.Expense;
import com.example.login_signup.fragments.HomeFragment;
import com.example.login_signup.fragments.Income;
import com.example.login_signup.fragments.MoreFragment;
import com.example.login_signup.fragments.Transfer;
import com.google.android.material.tabs.TabLayout;

public class ListUpdate extends AppCompatActivity {

    ImageView back,delet;
    FinancialDB dbHelper;
    String id,type;
    ListUpdateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_update);

        binding = ListUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        id = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");


        if (savedInstanceState == null) {

            switch (type) {
                case "expense":
                    replaceFragment(new Expense(id));
                    break;
                case "income":
                    replaceFragment(new Income(id));
                    break;
                case "transfer":
                    replaceFragment(new Transfer(id));
                    break;
                default:
                    replaceFragment(new Expense(id));
                    break;
            }
        }


        back=findViewById(R.id.back);
        delet=findViewById(R.id.delet);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListUpdate.this, Before_Home.class);
                startActivity(intent);
            }
        });


        dbHelper = new FinancialDB(this);
        delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int expenseId = Integer.parseInt(id);
                int count = dbHelper.delete(expenseId);

                if (count > 0) {
                    Toast.makeText(ListUpdate.this, "Delete done", Toast.LENGTH_SHORT).show(); // Fixed context
                    Intent intent = new Intent(ListUpdate.this, Before_Home.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ListUpdate.this, "Failed to delete", Toast.LENGTH_SHORT).show(); // Optional error handling
                }
            }
        });

    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }


}