package com.example.login_signup;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.login_signup.databinding.HomeActivityBinding;
import com.example.login_signup.fragments.AccountFragment;
import com.example.login_signup.fragments.AddTransaction;
import com.example.login_signup.fragments.AnalisisFragment;
import com.example.login_signup.fragments.HomeFragment;
import com.example.login_signup.fragments.MoreFragment;

public class Before_Home extends AppCompatActivity {

    HomeActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        binding = HomeActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int fragment = getIntent().getIntExtra("fragment", -1);
        if(fragment == 4)
        {
            replaceFragment(new MoreFragment());
        }else {
            replaceFragment(new HomeFragment());
        }

        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.analysis) {
                replaceFragment(new AnalisisFragment());
            } else if (itemId == R.id.account) {
                replaceFragment(new AccountFragment());
            } else if (itemId == R.id.more) {
                replaceFragment(new MoreFragment());
            }else if (itemId == R.id.addtransaction){
                replaceFragment(new AddTransaction());
            } else {
                return false;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }



}
