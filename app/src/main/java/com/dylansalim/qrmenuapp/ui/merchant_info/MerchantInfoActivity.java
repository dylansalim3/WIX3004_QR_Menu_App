package com.dylansalim.qrmenuapp.ui.merchant_info;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.dao.StoreDao;
import com.dylansalim.qrmenuapp.models.dao.UserDetailDao;
import com.dylansalim.qrmenuapp.services.SessionService;
import com.dylansalim.qrmenuapp.ui.component.RatingDialog;
import com.dylansalim.qrmenuapp.ui.main.FragmentSlidePagerAdapter;
import com.dylansalim.qrmenuapp.ui.merchant.MerchantActivity;
import com.dylansalim.qrmenuapp.ui.merchant_info.about.AboutMerchantFragment;
import com.dylansalim.qrmenuapp.ui.merchant_info.review.MerchantReviewFragment;
import com.dylansalim.qrmenuapp.ui.report.ReportActivity;
import com.dylansalim.qrmenuapp.ui.store_qr.StoreQRActivity;
import com.dylansalim.qrmenuapp.ui.store_registration.StoreRegistrationActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

public class MerchantInfoActivity extends AppCompatActivity implements MerchantInfoViewInterface,
        RatingDialog.RatingDialogListener {

    private MerchantInfoPresenter merchantInfoPresenter;
    private Toolbar toolbar;
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager2;
    private Button btnReport;
    private ViewGroup progressView;
    protected boolean isProgressShowing = false;
    private static final String TAG = "MIA";

    private static final String[] TAB_TITLES = new String[]{"About", "Review"};
    private static final List<Fragment> MERCHANT_INFO_FRAGMENTS = Arrays.asList(new AboutMerchantFragment(), new MerchantReviewFragment());
    public static int UPDATE_FORM_REQUEST_CODE = 103;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_info);

        toolbar = findViewById(R.id.merchant_info_toolbar);
        setSupportActionBar(toolbar);

        setupMVP();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            boolean isStoreAdmin = bundle.getBoolean(getResources().getString(R.string.is_store_admin));
            StoreDao storeResult = bundle.getParcelable(getResources().getString(R.string.store_result));
            if (storeResult != null) {
                merchantInfoPresenter.setStoreInfo(storeResult, isStoreAdmin);
            }
        } else {
            finish();
        }

        // Populating fragments
        merchantInfoPresenter.setupFragments(MERCHANT_INFO_FRAGMENTS);

        // Enable change of fragment by clicking on tab
        mTabLayout = findViewById(R.id.merchant_info_item_tab_layout);
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

        btnReport = findViewById(R.id.merchant_info_btn_report);
        btnReport.setOnClickListener(view -> merchantInfoPresenter.onReportBtnClick());

        setupTabMediator();

        // on back will go back
        toolbar.setNavigationOnClickListener(view -> {
            onBackPressed();
        });
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setupTabMediator() {
//        if (null != mTabLayout && null != mViewPager2) {
        // Enable changing of tab by swiping
        new TabLayoutMediator(mTabLayout, mViewPager2, (tab, position) -> {
            tab.setText(TAB_TITLES[position]);
        }).attach();
//        }
    }

    private void setupMVP() {
        merchantInfoPresenter = new MerchantInfoPresenter(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(MerchantActivity.MERCHANT_INFO_REQUEST_CODE, intent);
        finish();
    }

    public void displayToast(String s) {
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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            ((TextView) findViewById(R.id.tv_merchant_info_store_title)).setText(title);
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

    @Override
    public void navigateToStoreQRActivity(StoreDao storeDetail) {
        Intent intent = new Intent(this, StoreQRActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(getResources().getString(R.string.store_result), storeDetail);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void navigateToEditStoreActivity(StoreDao storeDetail) {
        Intent intent = new Intent(this, StoreRegistrationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(getResources().getString(R.string.edit_store), true);
        bundle.putParcelable(getResources().getString(R.string.store_result), storeDetail);
        intent.putExtras(bundle);
        startActivityForResult(intent, UPDATE_FORM_REQUEST_CODE);
    }

    @Override
    public void navigateToReportActivity(StoreDao storeDetail) {
        Intent intent = new Intent(this, ReportActivity.class);
        intent.putExtra(getResources().getString(R.string.store_name), storeDetail.getName());
        intent.putExtra(getResources().getString(R.string.store_id), storeDetail.getId());
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_FORM_REQUEST_CODE && null != data) {
            String message = data.getStringExtra(getResources().getString(R.string.message));
            displayToast(message);
            StoreDao storeResult = data.getParcelableExtra(getResources().getString(R.string.store_result));
            merchantInfoPresenter.setStoreInfo(storeResult, true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Bundle bundle = getIntent().getExtras();
        UserDetailDao userDetail = SessionService.getUserDetails(this);
        if (bundle != null && bundle.getBoolean(getResources().getString(R.string.is_store_admin))) {
            getMenuInflater().inflate(R.menu.merchant_info_admin_action_menu, menu);
        } else if (userDetail != null) {
            getMenuInflater().inflate(R.menu.merchant_info_customer_action_menu, menu);
        } else {
            getMenuInflater().inflate(R.menu.merchant_info_action_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_get_qr:
                merchantInfoPresenter.onQRActionBtnClick();
                return true;
            case R.id.action_leave_review:
                RatingDialog ratingDialog = new RatingDialog(this);
                ratingDialog.show(getSupportFragmentManager(), "Review");
                return true;

            case R.id.action_edit:
                merchantInfoPresenter.onEditBtnClick();
                return true;
        }

        return true;
    }

    @Override
    public void submitReviewDialog(String text, float rating) {
        merchantInfoPresenter.onReviewSubmit(text, rating, this);
    }

    @Override
    public void setProfileImg(String profileImg) {
        if (getSupportActionBar() != null) {
            Picasso.get().load(profileImg)
                    .placeholder(R.drawable.sample)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            Drawable d = new BitmapDrawable(getResources(), bitmap);
                            getSupportActionBar().setBackgroundDrawable(d);
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        merchantInfoPresenter.disposeObserver();
    }
}