package com.dylansalim.qrmenuapp.ui.merchant.item;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.ListItem;
import com.dylansalim.qrmenuapp.ui.merchant.EditItemAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ItemFragment extends Fragment implements ItemViewInterface {
    private RecyclerView mRecyclerView;
    private ItemPresenter itemPresenter;
    private LinearLayoutManager linearLayoutManager;

    private TabLayout mTabLayout;
    private static final String TAG = "if";

    public ItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.item_recycler_view);
        mTabLayout = (TabLayout) view.findViewById(R.id.item_tab_layout);

        setupMVP();
        itemPresenter.getAllItems(getContext());

        return view;
    }

    private void setupMVP() {
        itemPresenter = new ItemPresenter(this);
    }

    @Override
    public void setupRecyclerView(List<ListItem> listItems) {
        linearLayoutManager = new LinearLayoutManager(getContext());
        EditItemAdapter editItemAdapter = new EditItemAdapter(listItems);
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
                    itemPresenter.onRecyclerViewScrolled(position);
                }
            }
        });
    }

    @Override
    public void setupTabLayout(List<String> titles) {
//        if(titles.size()>2){
//            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
//        }
        titles.stream().forEach(title -> {
            mTabLayout.addTab(mTabLayout.newTab().setText(title));
        });

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabSelectedIndex = tab.getPosition();
                itemPresenter.onTabSelectionChanged(tabSelectedIndex);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void scrollRecyclerViewToPosition(int position) {
        if (mRecyclerView != null && linearLayoutManager != null) {
            linearLayoutManager.scrollToPositionWithOffset(position, 0);
            float y = mRecyclerView.getY() + mRecyclerView.getChildAt(position).getY();
            Log.d(TAG, "scrollRecyclerViewToPosition: " + y);
//            mNestedScrollView.smoothScrollTo(0, (int) y);
        }
    }

    @Override
    public void updateSelectedTab(int position) {
        mTabLayout.setScrollPosition(position, 0f, true);
    }
}