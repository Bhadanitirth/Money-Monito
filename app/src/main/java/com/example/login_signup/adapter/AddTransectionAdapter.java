package com.example.login_signup.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.login_signup.fragments.AddTransaction;
import com.example.login_signup.fragments.AnalisisFragment;
import com.example.login_signup.fragments.Custom;
import com.example.login_signup.fragments.Expense;
import com.example.login_signup.fragments.Income;
import com.example.login_signup.fragments.Month;
import com.example.login_signup.fragments.Transfer;
import com.example.login_signup.fragments.Week;
import com.example.login_signup.fragments.Year;


public class AddTransectionAdapter extends FragmentStateAdapter {
    public AddTransectionAdapter(@NonNull AddTransaction addTransaction) {
        super(addTransaction);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch ((position))
        {
            case 0:
                return new Expense();
            case 1:
                return new Income();
            case 2:
                return new Transfer();
            default:
                return new Expense();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}
