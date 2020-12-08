package com.dylansalim.qrmenuapp.ui.merchant_info;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.dao.StoreDao;
import com.dylansalim.qrmenuapp.ui.main.FragmentSlidePagerAdapter;
import com.dylansalim.qrmenuapp.ui.merchant_info.about.AboutMerchantFragment;
import com.dylansalim.qrmenuapp.ui.merchant_info.review.MerchantReviewFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class MerchantInfoActivity extends AppCompatActivity implements MerchantInfoViewInterface {

    private MerchantInfoPresenter merchantInfoPresenter;
    private TextView mToolbarExpandedTitle;
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager2;
    private ViewGroup progressView;
    protected boolean isProgressShowing = false;
    private static final String TAG = "MIA";

    private static final String[] TAB_TITLES = new String[]{"About", "Review"};
    private static final List<Fragment> MERCHANT_INFO_FRAGMENTS = Arrays.asList(new AboutMerchantFragment(), new MerchantReviewFragment());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_info);

        Toolbar toolbar = findViewById(R.id.merchant_info_toolbar);
        mToolbarExpandedTitle = findViewById(R.id.tv_merchant_info_toolbar_expanded_title);
        mTabLayout = findViewById(R.id.merchant_info_item_tab_layout);
        setSupportActionBar(toolbar);

        setupMVP();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            boolean isStoreAdmin = bundle.getBoolean(getResources().getString(R.string.is_store_admin));
            StoreDao storeResult = bundle.getParcelable(getResources().getString(R.string.store_result));
            if (storeResult != null) {
                merchantInfoPresenter.setStoreInfo(storeResult, isStoreAdmin);
            }
        }


        // Setting the collapsing toolbar as transparent
        CollapsingToolbarLayout mCollapsingToolbar = findViewById(R.id.merchant_info_collapsingToolbarLayout);
        mCollapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, R.color.zxing_transparent));

        // Set title visibility
        LinearLayout mExpandedTitle = findViewById(R.id.merchant_info_ll_title_expanded);
        AppBarLayout mAppBarLayout = findViewById(R.id.merchant_info_appBarLayout);
        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                //  Collapsed
                mExpandedTitle.setVisibility(View.INVISIBLE);

            } else {
                mExpandedTitle.setVisibility(View.VISIBLE);
            }
        });
        // Populating fragments
        merchantInfoPresenter.setupFragments(MERCHANT_INFO_FRAGMENTS);

        // Enable change of fragment by clicking on tab
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Enable changing of tab by swiping
        new TabLayoutMediator(mTabLayout, mViewPager2, (tab, position) -> {
            tab.setText(TAB_TITLES[position]);
        }).attach();


        // on back will go back
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setupMVP() {
        merchantInfoPresenter = new MerchantInfoPresenter(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void displayError(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressBar() {
        isProgressShowing = true;
        progressView = (ViewGroup) getLayoutInflater().inflate(R.layout.progressbar_layout, null);
        View v = this.findViewById(android.R.id.content).getRootView();
        ViewGroup viewGroup = (ViewGroup) v;
        viewGroup.addView(progressView);
    }

    @Override
    public void hideProgressBar() {
        View v = this.findViewById(android.R.id.content).getRootView();
        ViewGroup viewGroup = (ViewGroup) v;
        viewGroup.removeView(progressView);
        isProgressShowing = false;
    }

    @Override
    public void setupToolbar(String title) {
        if (getSupportActionBar() != null && mToolbarExpandedTitle != null) {
            getSupportActionBar().setTitle(title);
            mToolbarExpandedTitle.setText(title);
        }
    }

    @Override
    public void populateView(List<Fragment> fragments, StoreDao storeDetail) {
        mViewPager2 = (ViewPager2) findViewById(R.id.merchant_info_view_pager2);
        FragmentSlidePagerAdapter pagerAdapter = new FragmentSlidePagerAdapter(this, fragments);
        Log.d(TAG, storeDetail.toString());
        Bundle bundle = new Bundle();
        bundle.putParcelable(getResources().getString(R.string.store_result), storeDetail);
        pagerAdapter.setBundle(bundle);
        mViewPager2.setAdapter(pagerAdapter);
    }
}