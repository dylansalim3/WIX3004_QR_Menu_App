package com.dylansalim.qrmenuapp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.ui.merchant.MerchantActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class MainActivity extends FragmentActivity implements MainViewInterface {

    private MainPresenter mainPresenter;

    private ViewPager2 viewPager2;
    private BottomNavigationView navView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.bottomNavigationView);
        fab = (FloatingActionButton) findViewById(R.id.main_fab);

        setupMVP();

        mainPresenter.populateView(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MerchantActivity.class);
                intent.putExtra("isStoreAdmin",true);
                startActivity(intent);
            }
        });

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mainPresenter.getNavigationItemSelected(item.getItemId());
                return true;
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mainPresenter.onPageSelected(position);
            }
        });

    }

    private void setupMVP() {
        mainPresenter = new MainPresenter(this);
    }

    @Override
    public void onBackPressed() {
        if (viewPager2.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() - 1);
        }
    }

    @Override
    public void populateBottomNavBar(@Nullable Integer itemId) {
        navView.getMenu().clear();
        if (itemId != null) {
            navView.inflateMenu(itemId);
        }
    }

    @Override
    public void populateFragmentAdapter(List<Fragment> fragments) {
        viewPager2 = (ViewPager2) findViewById(R.id.home_view_pager2);
        FragmentStateAdapter pagerAdapter = new FragmentSlidePagerAdapter(this, fragments);
        viewPager2.setAdapter(pagerAdapter);
    }

    @Override
    public void showFab() {
        fab.setVisibility(View.VISIBLE);
    }

    @Override
    public void setViewPagerIndex(int index) {
        viewPager2.setCurrentItem(index);
    }

    @Override
    public void setSelectedBottomNavBar(int itemId) {
        navView.setSelectedItemId(itemId);
    }
}