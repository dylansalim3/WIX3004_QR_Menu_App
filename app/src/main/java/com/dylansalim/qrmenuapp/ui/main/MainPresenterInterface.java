package com.dylansalim.qrmenuapp.ui.main;

import android.app.Activity;

public interface MainPresenterInterface {
    void populateView(Activity activity);

    void getNavigationItemSelected(int resId);

    void onPageSelected(int position);
}
