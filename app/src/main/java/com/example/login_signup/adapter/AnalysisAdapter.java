package com.example.login_signup.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.login_signup.fragments.AnalisisFragment;
import com.example.login_signup.fragments.Custom;
import com.example.login_signup.fragments.Month;
import com.example.login_signup.fragments.Week;
import com.example.login_signup.fragments.Year;

public class AnalysisAdapter extends FragmentStateAdapter {
    public AnalysisAdapter(@NonNull AnalisisFragment analisisFragment) {
        super(analisisFragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch ((position))
        {
            case 0:
                return new Week();
            case 1:
                return new Month();
            case 2:
                return new Year();
            case 3:
                return new Custom();
            default:
                return new Month();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

}
