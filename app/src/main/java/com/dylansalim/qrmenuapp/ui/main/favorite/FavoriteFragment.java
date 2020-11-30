package com.dylansalim.qrmenuapp.ui.main.favorite;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.dao.Shop;
import com.dylansalim.qrmenuapp.models.dao.UserDetailDao;
import com.dylansalim.qrmenuapp.utils.JWTUtils;
import com.google.gson.Gson;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FavoriteFragment extends Fragment implements IShopView {
    public static final String TAG = "FavoriteFragment";

    private ShopPresenter shopPresenter;

    private ShopListAdapter adapter;

    private FavoriteViewModel favoriteViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopPresenter = new ShopPresenter(this);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        initView(root);
        initData();
        return root;
    }

    private void initView(View root) {
        adapter = new ShopListAdapter(getContext());
        RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        //创建并设置Adapter
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter.setOnItemClickListener((view, position) -> Log.d(TAG, "click---" + position));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (shopPresenter != null) {
            shopPresenter.onDestory();
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
                UserDetailDao userDetailDao = new Gson().fromJson(dataString, UserDetailDao.class);
                Log.d(TAG, userDetailDao.toString());
                return userDetailDao.getId() + "";
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
}