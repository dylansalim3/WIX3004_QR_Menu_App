package com.dylansalim.qrmenuapp.ui.main.favorite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.dto.Shop;
import com.dylansalim.qrmenuapp.models.dto.UserDetail;
import com.dylansalim.qrmenuapp.ui.merchant.MerchantActivity;
import com.dylansalim.qrmenuapp.utils.JWTUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FavoriteFragment extends Fragment implements IShopView {
    public static final String TAG = "FavoriteFragment";
    public static final String FAVORITE_LIST = "FavoriteList";

    private ShopPresenter shopPresenter;

    private FavShopListAdapter adapter;

    private FavoriteViewModel favoriteViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopPresenter = new ShopPresenter(this);

        if (savedInstanceState != null) {
            List<Shop> favoriteList = savedInstanceState.getParcelableArrayList(FAVORITE_LIST);
            if (favoriteList != null && favoriteList.size() > 0) {
                onGetShopList(favoriteList);
            }
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorite, container, false);
        initView(root);
        initData();
        return root;
    }

    private void initView(View root) {
        adapter = new FavShopListAdapter(getContext());
        RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        //创建并设置Adapter
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //navigate to Merchant Activity
        adapter.setOnItemClickListener((storeId) -> {
            Intent intent = new Intent(getActivity(), MerchantActivity.class);
            intent.putExtra(getResources().getString(R.string.store_id),storeId);
            startActivity(intent);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (shopPresenter != null) {
            shopPresenter.onDestroy();
        }
    }

    private void initData() {
        String userId = getUserId();
        shopPresenter.getFavoriteList(userId);
    }

    @Override
    public void onGetShopList(List<Shop> shopList) {
        if (shopList != null) {
            adapter.fillData(shopList);
        }
    }

    private String getUserId() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getContext().getString(R.string.app_name), Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(getContext().getString(R.string.token), "");
        if (!token.equals("")) {
            String dataString;
            try {
                dataString = JWTUtils.getDataString(token);
                UserDetail userDetail = new Gson().fromJson(dataString, UserDetail.class);
                Log.d(TAG, userDetail.toString());
                return userDetail.getId() + "";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    @Override
    public void onResume() {
        super.onResume();
        if (shopPresenter != null) {
            Log.d(TAG, "fav initData");
            initData();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        List<Shop> favoriteList = shopPresenter.getFavoriteList();
        if (favoriteList != null && favoriteList.size() > 0) {
            outState.putParcelableArrayList(FAVORITE_LIST, new ArrayList<>(favoriteList));
        }
    }
}