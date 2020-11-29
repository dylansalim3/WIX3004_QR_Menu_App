package com.dylansalim.qrmenuapp.ui.merchant.item;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.ListItem;
import com.dylansalim.qrmenuapp.models.dao.AllItemDao;
import com.dylansalim.qrmenuapp.models.dao.Result;
import com.dylansalim.qrmenuapp.models.dao.UserDetailDao;
import com.dylansalim.qrmenuapp.network.MerchantItemNetworkInterface;
import com.dylansalim.qrmenuapp.network.NetworkClient;
import com.dylansalim.qrmenuapp.utils.JWTUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class ItemPresenter implements ItemPresenterInterface {

    private ItemViewInterface ivi;
    private UserDetailDao userDetailDao;
    private int storeId;
    private List<ListItem> listItems;
    private int currentTabPosition = 0;
    private HashMap<Integer, Integer> tabIndexHashMap;
    private static final String TAG = "ivi";

    public ItemPresenter(ItemViewInterface ivi) {
        this.ivi = ivi;
    }

    @Override
    public void getAllItems(Context context) {
        getUserDetails(context);
        getMerchantItemNetworkInterface().getAllItems(storeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getAllItemObserver());
    }

    @Override
    public void onTabSelectionChanged(int tabIndex) {
        if (tabIndexHashMap != null && currentTabPosition != tabIndex && tabIndexHashMap.get(currentTabPosition) != null) {
            Log.d(TAG, "current tab position" + tabIndex);
            Log.d(TAG, tabIndexHashMap.toString());
            ivi.scrollRecyclerViewToPosition(tabIndexHashMap.get(tabIndex));
            currentTabPosition = tabIndex;
        }
    }

    @Override
    public void onRecyclerViewScrolled(int recyclerViewItemIndex) {
        if (tabIndexHashMap != null) {
            Integer startingRange = tabIndexHashMap.get(currentTabPosition);
            if (currentTabPosition != tabIndexHashMap.size() - 1) {
                Integer nextStartingRange = tabIndexHashMap.get(currentTabPosition + 1);
                if (nextStartingRange != null && recyclerViewItemIndex >= nextStartingRange) {
                    ++currentTabPosition;
                    ivi.updateSelectedTab(currentTabPosition);
                }
            }
            if (startingRange != null && startingRange >= 0 && recyclerViewItemIndex < startingRange) {
                --currentTabPosition;
                ivi.updateSelectedTab(currentTabPosition);
            }
        }
    }

    private void getUserDetails(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(context.getString(R.string.token), "");

        Log.d(TAG, "check user type");
        Log.d(TAG, token);

        if (!token.equals("")) {
            String dataString = null;
            try {
                dataString = JWTUtils.getDataString(token);
            } catch (Exception e) {
                e.printStackTrace();
            }
            userDetailDao = new Gson().fromJson(dataString, UserDetailDao.class);

            if (userDetailDao.getStoreId() != null) {
                storeId = userDetailDao.getStoreId();
            }

            Log.d(TAG, userDetailDao.toString());
        }
    }

    private MerchantItemNetworkInterface getMerchantItemNetworkInterface() {
        return NetworkClient.getNetworkClient().create(MerchantItemNetworkInterface.class);
    }

    public DisposableObserver<Result<List<AllItemDao>>> getAllItemObserver() {
        return new DisposableObserver<Result<List<AllItemDao>>>() {
            @Override
            public void onNext(@NonNull Result<List<AllItemDao>> allItemsDao) {
                Log.d(TAG, "OnNext " + allItemsDao);
                if (allItemsDao != null && allItemsDao.getData() != null) {
                    listItems = mapAllItemsToListItems(allItemsDao.getData());
                    List<String> titles = getItemsTitle(allItemsDao.getData());
                    ivi.setupTabLayout(titles);
                    ivi.setupRecyclerView(listItems);
                    setTabIndexHashMap(listItems);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "Error" + e);
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Completed");
            }
        };
    }

    private List<String> getItemsTitle(List<AllItemDao> allItems) {
        return allItems.stream().map(AllItemDao::getName).collect(Collectors.toList());
    }

    private List<ListItem> mapAllItemsToListItems(List<AllItemDao> allItems) {
        List<ListItem> listItems = new ArrayList<>();
        allItems.stream().forEach(category -> {
            listItems.add(new ListItem(category.getName(), category.getId()));
            List<ListItem> categoryChildItems = category.getItems().stream()
                    .map(item -> new ListItem(item.getName(), item.getDesc(), item.getId(), item.getPrice(),item.getItemCategoryId())).collect(Collectors.toList());
            listItems.addAll(categoryChildItems);
        });
        return listItems;
    }

    private void setTabIndexHashMap(List<ListItem> listItems) {
        tabIndexHashMap = new HashMap<>();
        tabIndexHashMap = listItems.stream().filter(ListItem::isHeader).collect(HashMap<Integer, Integer>::new,
                (map, streamValue) ->
                        map.put(map.size(), listItems.indexOf(streamValue))
                , (map, map2) -> {
                });
    }


}