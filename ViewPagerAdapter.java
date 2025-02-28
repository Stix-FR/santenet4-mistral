
package com.example.healthtracker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private static final int NUM_PAGES = 4;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new TensionFragment();
            case 1:
                return new DiabeteFragment();
            case 2:
                return new MasseCorporelleFragment();
            case 3:
                return new SouffleFragment();
            default:
                return new TensionFragment();
        }
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}
