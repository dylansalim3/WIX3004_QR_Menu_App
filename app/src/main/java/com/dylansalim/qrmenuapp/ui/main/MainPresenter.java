package com.dylansalim.qrmenuapp.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.dao.UserDetailDao;
import com.dylansalim.qrmenuapp.ui.main.account.AccountFragment;
import com.dylansalim.qrmenuapp.ui.main.favorite.FavoriteFragment;
import com.dylansalim.qrmenuapp.ui.main.home.HomeFragment;
import com.dylansalim.qrmenuapp.ui.main.notifications.NotificationsFragment;
import com.dylansalim.qrmenuapp.utils.JWTUtils;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import androidx.fragment.app.Fragment;


public class MainPresenter implements MainPresenterInterface {
    private MainViewInterface mvi;
    private static final String TAG = "MVI";
    String MERCHANT;
    String CUSTOMER;
    private static final int[] CUSTOMER_ITEM_ID_LIST = new int[]{R.id.navigation_home, R.id.navigation_favorite, R.id.navigation_notifications, R.id.navigation_account};
    private static final int[] MERCHANT_ITEM_ID_LIST = new int[]{R.id.navigation_home, R.id.navigation_favorite, R.id.navigation_notifications, R.id.navigation_account};
    private static final List<Fragment> CUSTOMER_FRAGMENTS = Arrays.asList(new HomeFragment(), new FavoriteFragment(), new NotificationsFragment(), new AccountFragment());
    private static final List<Fragment> MERCHANT_FRAGMENTS = Arrays.asList(new HomeFragment(), new FavoriteFragment(), new NotificationsFragment(), new AccountFragment());
    private String userRole;

    public MainPresenter(MainViewInterface mvi) {
        this.mvi = mvi;
    }

    @Override
    public void populateView(Activity activity) {
        UserDetailDao userDetail = getUserDetail(activity);
        MERCHANT = activity.getString(R.string.merchant);
        CUSTOMER = activity.getString(R.string.customer);
        Log.d(TAG, "user role " + userRole);
        if (userDetail != null) {
            userRole = userDetail.getRole();
            if (MERCHANT.equalsIgnoreCase(userRole) && userDetail.getStoreId() != null) {
                mvi.populateBottomNavBar(R.menu.merchant_bottom_nav_menu);
                mvi.showFab();
                mvi.populateFragmentAdapter(MERCHANT_FRAGMENTS);
            } else {
                mvi.populateBottomNavBar(R.menu.customer_bottom_nav_menu);
                mvi.populateFragmentAdapter(CUSTOMER_FRAGMENTS);
            }
        } else {
            //only home fragment
            mvi.populateBottomNavBar(R.menu.customer_bottom_nav_menu);
            mvi.populateFragmentAdapter(CUSTOMER_FRAGMENTS);
        }
    }

    @Override
    public void getNavigationItemSelected(int resId) {
        int[] selectedItemIdList;
        if (CUSTOMER.equalsIgnoreCase(userRole)) {
            selectedItemIdList = CUSTOMER_ITEM_ID_LIST;
        } else {
            selectedItemIdList = MERCHANT_ITEM_ID_LIST;
        }
        OptionalInt optionalInt = IntStream.range(0, selectedItemIdList.length).filter(index -> selectedItemIdList[index] == resId).findFirst();
        if (optionalInt.isPresent()) {
            mvi.setViewPagerIndex(optionalInt.getAsInt());
        }
    }

    @Override
    public void onPageSelected(int position) {
        int[] selectedItemIdList;
        if (CUSTOMER.equalsIgnoreCase(userRole)) {
            selectedItemIdList = CUSTOMER_ITEM_ID_LIST;
        } else {
            selectedItemIdList = MERCHANT_ITEM_ID_LIST;
        }
        try {
            int itemId = selectedItemIdList[position];
            mvi.setSelectedBottomNavBar(itemId);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    private UserDetailDao getUserDetail(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(activity.getString(R.string.app_name), Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(activity.getString(R.string.token), "");

        assert token != null;
        if (!token.equals("")) {
            String dataString = null;
            try {
                dataString = JWTUtils.getDataString(token);
            } catch (Exception e) {
                e.printStackTrace();
            }
            UserDetailDao userDetailDao = new Gson().fromJson(dataString, UserDetailDao.class);

            return userDetailDao;
        }
        return null;
    }

}
