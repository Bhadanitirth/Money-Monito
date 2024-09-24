package com.example.login_signup.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.login_signup.fragments.Week;
import com.example.login_signup.fragments.Week4;

public class WeekFragmentAdapter extends FragmentStateAdapter {

    public WeekFragmentAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return Week4.newInstance(position);
    }

    @Override
    public int getItemCount() {
        return 53;
    }
}
