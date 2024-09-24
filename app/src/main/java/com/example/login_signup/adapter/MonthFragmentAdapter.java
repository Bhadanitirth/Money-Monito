package com.example.login_signup.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.login_signup.fragments.Month;
import com.example.login_signup.fragments.month12;

public class MonthFragmentAdapter extends FragmentStateAdapter {

    public MonthFragmentAdapter(@NonNull Month month12) {
        super(month12);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return month12.newInstance(position); // Pass the month index
    }

    @Override
    public int getItemCount() {
        return 12;
    }
}

