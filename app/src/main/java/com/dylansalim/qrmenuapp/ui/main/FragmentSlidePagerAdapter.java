package com.dylansalim.qrmenuapp.ui.main;

import android.os.Bundle;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragmentSlidePagerAdapter extends FragmentStateAdapter {

    private List<Fragment> fragments;
    private Bundle bundle;

    public FragmentSlidePagerAdapter(FragmentActivity fa, List<Fragment> fragments) {
        super(fa);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (bundle != null) {
            fragments.get(position).setArguments(bundle);
        }
        return fragments.get(position);
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
