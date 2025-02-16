package com.dylansalim.qrmenuapp.ui.merchant;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.EditListItem;
import com.dylansalim.qrmenuapp.models.dto.Store;
import com.dylansalim.qrmenuapp.ui.component.SingleEditTextDialog;
import com.dylansalim.qrmenuapp.ui.merchant_info.MerchantInfoActivity;
import com.dylansalim.qrmenuapp.ui.new_item_form.NewItemFormActivity;
import com.dylansalim.qrmenuapp.ui.store_qr.StoreQRActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MerchantActivity extends AppCompatActivity
        implements MerchantViewInterface, SingleEditTextDialog.EditTextDialogListener,
        EditItemAdapter.OnCategoryDeleteListener, EditItemAdapter.OnItemListener, EditItemAdapter.OnAddNewBtnListener {
    private MerchantPresenter merchantPresenter;
    private TextView mToolbarExpandedTitle;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TabLayout mTabLayout;
    private Menu menu;
    private int tabSelectedIndex;
    CollapsingToolbarLayout mCollapsingToolbar;
    private static final String TAG = "MA";
    private ViewGroup progressView;
    protected boolean isProgressShowing = false;
    public static int SUBMISSION_FORM_REQUEST_CODE = 102;
    public static int MERCHANT_INFO_REQUEST_CODE = 104;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant);

        mRecyclerView = (RecyclerView) findViewById(R.id.item_recycler_view);
        mTabLayout = (TabLayout) findViewById(R.id.item_tab_layout);
        AppBarLayout mAppBarLayout = findViewById(R.id.merchant_appBarLayout);
        mToolbarExpandedTitle = findViewById(R.id.tv_merchant_toolbar_expanded_title);
        Toolbar toolbar = findViewById(R.id.merchant_toolbar);
        setSupportActionBar(toolbar);


        mCollapsingToolbar = findViewById(R.id.merchant_collapsingToolbarLayout);
        LinearLayout mExpandedTitle = findViewById(R.id.merchant_ll_title_expanded);
        setupMVP();
        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.getBoolean(getResources().getString(R.string.is_store_admin))) {
            merchantPresenter.getAllItems(this, null);
        } else {
            Integer storeId = bundle.getInt(getResources().getString(R.string.store_id));
            merchantPresenter.getAllItems(this, storeId);
        }

        mCollapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, R.color.zxing_transparent));
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                    //  Collapsed
                    mExpandedTitle.setVisibility(View.INVISIBLE);

                } else {
                    mExpandedTitle.setVisibility(View.VISIBLE);
                }
            }
        });

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void setupMVP() {
        merchantPresenter = new MerchantPresenter(this);
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
    public void setOverallRating(String overallRating) {
        ((TextView) findViewById(R.id.merchant_overall_rating)).setText(overallRating);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getBoolean(getResources().getString(R.string.is_store_admin))) {
            getMenuInflater().inflate(R.menu.merchant_appbar_admin_action_menu, menu);
        } else {
            getMenuInflater().inflate(R.menu.merchant_appbar_action_menu, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_share) {
            merchantPresenter.onShareBtnClick();
            return true;
        }
        if (id == R.id.action_info) {
            merchantPresenter.onInfoButtonClick();
            return true;
        }

        if (id == R.id.action_edit) {
            merchantPresenter.onEditActionButtonClick();
        }

        if(id == R.id.action_fav){
            merchantPresenter.onFavActionButtonClick();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void displayError(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setupRecyclerView(List<EditListItem> editListItems) {
        linearLayoutManager = new LinearLayoutManager(this);
        EditItemAdapter editItemAdapter = new EditItemAdapter(editListItems, this, this, this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(editItemAdapter);


        TextView mEmptyListTv = findViewById(R.id.tv_merchant_empty_list);

        if (editListItems.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyListTv.setVisibility(View.GONE);
        } else {
            mEmptyListTv.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int position = ((LinearLayoutManager) Objects.requireNonNull(mRecyclerView.getLayoutManager()))
                            .findFirstVisibleItemPosition();
                    merchantPresenter.onRecyclerViewScrolled(position);
                }
            }
        });
    }

    @Override
    public void setupTabLayout(List<String> titles) {
        mTabLayout.removeAllTabs();
        titles.stream().forEach(title -> {
            mTabLayout.addTab(mTabLayout.newTab().setText(title));
        });

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabSelectedIndex != tab.getPosition()) {
                    tabSelectedIndex = tab.getPosition();
                    merchantPresenter.onTabSelectionChanged(tabSelectedIndex);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (!SingleEditTextDialog.openStatus) {
                    SingleEditTextDialog.openStatus = true;
                    tabSelectedIndex = tab.getPosition();
                    merchantPresenter.onTabSelectionChanged(tabSelectedIndex);
                }
            }
        });
    }

    @Override
    public void scrollRecyclerViewToPosition(int position) {
        if (mRecyclerView != null && linearLayoutManager != null) {
            linearLayoutManager.scrollToPositionWithOffset(position, 0);
        }
    }

    @Override
    public void updateSelectedTab(int position) {
        mTabLayout.setScrollPosition(position, 0f, true, true);
    }

    @Override
    public void setupToolbarTitle(String title) {
        if (mCollapsingToolbar != null && mToolbarExpandedTitle != null) {
            mCollapsingToolbar.setTitle(title);
            mToolbarExpandedTitle.setText(title);
        }
    }

    @Override
    public void updateEditActionIcon(int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(this, drawableId);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, getResources().getColor(R.color.colorWhite));
        if (menu != null) {
            menu.getItem(0).setIcon(drawable);
        }
    }

    @Override
    public void updateFavActionIcon(int color) {
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_24);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, getResources().getColor(color));
        if (menu != null) {
            menu.getItem(0).setIcon(drawable);
        }
    }

    @Override
    public void showAddNewCategoryDialog() {
        SingleEditTextDialog singleEditTextDialog = new SingleEditTextDialog("Add New Category", "Category Name", "Cancel", "Submit");
        singleEditTextDialog.show(getSupportFragmentManager(), "Category");
    }

    @Override
    public void navigateToMerchantInfoActivity(Store storeResult, boolean isStoreAdmin) {
        Intent intent = new Intent(MerchantActivity.this, MerchantInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(getResources().getString(R.string.store_result), storeResult);
        bundle.putBoolean(getResources().getString(R.string.is_store_admin), isStoreAdmin);
        intent.putExtras(bundle);
        startActivityForResult(intent, MERCHANT_INFO_REQUEST_CODE);
    }

    @Override
    public void navigateToStoreQRActivity(Store storeResult) {
        Intent intent = new Intent(MerchantActivity.this, StoreQRActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(getResources().getString(R.string.store_result), storeResult);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    public void applyTexts(String text) {
        merchantPresenter.onAddNewCategory(text);
    }

    @Override
    public void onDeleteCategory(int categoryId) {
        merchantPresenter.onDeleteItemCategory(categoryId);
    }

    @Override
    public void onDeleteItem(int itemId) {
        merchantPresenter.onDeleteItem(itemId);
    }

    @Override
    public void onEditItem(int itemId) {
        navigateToNewItemFormActivity(getResources().getString(R.string.edit_item), itemId);
    }

    @Override
    public void onAddItem(int categoryId) {
        navigateToNewItemFormActivity(getResources().getString(R.string.add_item), categoryId);
    }

    private void navigateToNewItemFormActivity(String type, int id) {
        Intent intent = new Intent(this, NewItemFormActivity.class);
        int resourceId = type.equals(getResources().getString(R.string.add_item)) ? R.string.category_id : R.string.item_id;
        Bundle bundle = new Bundle();
        bundle.putString(getResources().getString(R.string.action_type), type);
        bundle.putInt(getResources().getString(resourceId), id);
        intent.putExtras(bundle);
        startActivityForResult(intent, SUBMISSION_FORM_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SUBMISSION_FORM_REQUEST_CODE && null != data) {
            String message = data.getStringExtra("MESSAGE");
            displayError(message);
            merchantPresenter.retrieveItemDetail();
        } else if (requestCode == MERCHANT_INFO_REQUEST_CODE && null != data) {
            Log.d(TAG, "QWERTY");
            merchantPresenter.retrieveStoreDetail(false);
        }
    }

    @Override
    public void setProfileImg(String profileImg) {
        ImageView mProfileImg = findViewById(R.id.merchant_iv_profile);
        Picasso.get().load(profileImg)
                .placeholder(R.drawable.common_google_signin_btn_icon_dark_focused)
                .into(mProfileImg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        merchantPresenter.disposeObserver();
    }
}