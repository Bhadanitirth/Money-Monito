package com.example.login_signup.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.login_signup.fragments.Year4;

public class YearFragmentAdapter extends FragmentStateAdapter {

    public YearFragmentAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return Year4.newInstance(position); // Pass the year index
    }

    @Override
    public int getItemCount() {
        return 100; // 10 years
    }
}
