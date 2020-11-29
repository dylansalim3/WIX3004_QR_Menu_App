package com.dylansalim.qrmenuapp.ui.merchant;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.ListItem;
import com.dylansalim.qrmenuapp.ui.component.SingleEditTextDialog;
import com.dylansalim.qrmenuapp.ui.merchant_info.MerchantInfoActivity;
import com.dylansalim.qrmenuapp.ui.new_item_form.NewItemFormActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MerchantActivity extends AppCompatActivity
        implements MerchantViewInterface, SingleEditTextDialog.EditTextDialogListener,
        EditItemAdapter.OnCategoryDeleteListener, EditItemAdapter.OnItemListener, EditItemAdapter.OnAddNewBtnListener {
    private Toolbar toolbar;
    private MerchantPresenter merchantPresenter;
    private TextView mToolbarExpandedTitle;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TabLayout mTabLayout;
    private AppBarLayout mAppBarLayout;
    private Menu menu;
    private int tabSelectedIndex;
    private static final String TAG = "MA";
    private ViewGroup progressView;
    protected boolean isProgressShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant);

        mRecyclerView = (RecyclerView) findViewById(R.id.item_recycler_view);
        mTabLayout = (TabLayout) findViewById(R.id.item_tab_layout);
        mAppBarLayout = findViewById(R.id.merchant_appBarLayout);
        mToolbarExpandedTitle = findViewById(R.id.tv_merchant_toolbar_expanded_title);
        toolbar = findViewById(R.id.merchant_toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout mCollapsingToolbar = findViewById(R.id.merchant_collapsingToolbarLayout);
        LinearLayout mExpandedTitle = findViewById(R.id.merchant_title_expanded);
        setupMVP();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.getBoolean("isStoreAdmin")) {
            merchantPresenter.getAllItems(this, null);
        } else {
            Integer storeId = bundle.getInt("storeId");
            merchantPresenter.getAllItems(this, storeId);
        }

//        setupFrameLayout(savedInstanceState);
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

//    private void setupFrameLayout(Bundle savedInstanceState) {
//        if (findViewById(R.id.merchant_fragment_container) != null) {
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
//                    .add(R.id.merchant_fragment_container, itemFragment)
//                    .commit();
//        }
//    }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getBoolean("isStoreAdmin")) {
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
            displayError("Action share clicked");
            return true;
        }
        if (id == R.id.action_info) {
            Intent intent = new Intent(MerchantActivity.this, MerchantInfoActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_edit) {
            merchantPresenter.onEditActionButtonClicked();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void displayError(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setupRecyclerView(List<ListItem> listItems) {
        linearLayoutManager = new LinearLayoutManager(this);
        EditItemAdapter editItemAdapter = new EditItemAdapter(listItems, this, this, this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(editItemAdapter);

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
        Log.d(TAG, "title is " + title);
        if (getSupportActionBar() != null && mToolbarExpandedTitle != null) {
            getSupportActionBar().setTitle(title);
            mToolbarExpandedTitle.setText(title);
        }
    }

    @Override
    public void updateEditActionIcon(int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(this, drawableId);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, getResources().getColor(R.color.colorWhite));
        menu.getItem(0).setIcon(drawable);
    }

    @Override
    public void showAddNewCategoryDialog() {
        SingleEditTextDialog singleEditTextDialog = new SingleEditTextDialog("Add New Category", "Category Name", "Cancel", "Submit");
        singleEditTextDialog.show(getSupportFragmentManager(), "Category");
    }

    @Override
    public void applyTexts(String text) {
        merchantPresenter.onAddNewCategory(text);
    }

    @Override
    public void onDeleteCategory(int categoryId) {
        displayError("delete " + categoryId);
    }

    @Override
    public void onDeleteItem(int itemId) {
        displayError("delete item " + itemId);

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
        startActivity(intent);
    }

}