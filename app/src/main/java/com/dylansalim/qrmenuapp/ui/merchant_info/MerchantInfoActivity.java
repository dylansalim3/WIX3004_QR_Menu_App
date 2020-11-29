package com.dylansalim.qrmenuapp.ui.merchant_info;

import android.os.Bundle;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.ui.merchant_info.about.AboutMerchantFragment;
import com.dylansalim.qrmenuapp.ui.merchant_info.review.MerchantReviewFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MerchantInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_info);

        setupFrameLayout(savedInstanceState);
    }

    private void setupFrameLayout(Bundle savedInstanceState) {
//        if (findViewById(R.id.merchant_info_fragment_container) != null) {
//
//            if (savedInstanceState != null) {
//                return;
//            }
//
//            ItemFragment itemFragment = new ItemFragment();
//
//            itemFragment.setArguments(getIntent().getExtras());
//
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.merchant_info_fragment_container, itemFragment)
//                    .commit();
//        }
    }

    public void onChangeFragment(int destFragmentIndex) {
        Fragment newFragment;
        if (destFragmentIndex == AboutMerchantFragment.FRAGMENT_INDEX) {
            newFragment = new AboutMerchantFragment();
        } else {
            newFragment = new MerchantReviewFragment();
        }
        newFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.merchant_info_fragment_container, newFragment)
                .addToBackStack(null)
                .commit();
    }
}