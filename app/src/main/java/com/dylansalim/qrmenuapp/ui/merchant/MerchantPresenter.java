package com.dylansalim.qrmenuapp.ui.merchant;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.ListItem;
import com.dylansalim.qrmenuapp.models.dao.AllItemDao;
import com.dylansalim.qrmenuapp.models.dao.ItemCategoryDao;
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

public class MerchantPresenter implements MerchantPresenterInterface {
    MerchantViewInterface mvi;
    private boolean editMode = false;

    public MerchantPresenter(MerchantViewInterface mvi) {
        this.mvi = mvi;
    }

    private UserDetailDao userDetailDao;
    private int storeId;
    private List<ListItem> listItems;
    private List<String> titles;
    private int currentTabPosition = 0;
    private HashMap<Integer, Integer> tabIndexHashMap;
    private static final String TAG = "mvi";

    @Override
    public void getAllItems(Context context) {
        mvi.showProgressBar();
        getUserDetails(context);
        setupStoreName();
        getMerchantItemNetworkInterface().getAllItems(storeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getAllItemObserver());
    }

    private void setupStoreName() {
        String storeName = userDetailDao.getStoreName();
        if (storeName != null) {
            mvi.setupToolbarTitle(storeName);
        }
    }

    @Override
    public void onTabSelectionChanged(int tabIndex) {
        if (tabIndex > tabIndexHashMap.size() - 1) {
            mvi.showAddNewCategoryDialog();
            mvi.updateSelectedTab(0);
        } else if (tabIndexHashMap != null && currentTabPosition != tabIndex && tabIndexHashMap.get(currentTabPosition) != null) {
            Log.d(TAG, "current tab position" + tabIndex);
            Log.d(TAG, tabIndexHashMap.toString());
            mvi.scrollRecyclerViewToPosition(tabIndexHashMap.get(tabIndex));
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
                    mvi.updateSelectedTab(currentTabPosition);
                }
            }
            if (startingRange != null && startingRange >= 0 && recyclerViewItemIndex < startingRange) {
                --currentTabPosition;
                mvi.updateSelectedTab(currentTabPosition);
            }
        }
    }

    @Override
    public void onEditActionButtonClicked() {
        editMode = !editMode;
        if (editMode) {
            mvi.updateEditActionIcon(R.drawable.ic_baseline_done_24);
            addNewEditableTab();
            addNewItemButton();
        } else {
            mvi.updateEditActionIcon(R.drawable.ic_baseline_edit_24);
            mvi.setupTabLayout(titles);
            mvi.setupRecyclerView(listItems);
        }
    }

    @Override
    public void onAddNewCategory(String categoryName) {
        if (userDetailDao != null && categoryName != null && userDetailDao.getStoreId() != null && categoryName.length() > 0) {
            mvi.showProgressBar();
            ItemCategoryDao itemCategoryDao = new ItemCategoryDao(categoryName, userDetailDao.getStoreId());
            getMerchantItemNetworkInterface().createItemCategory(itemCategoryDao)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(addNewCategoryObserver());
        }
    }

    private void addNewItemButton() {
        List<ListItem> listItemsWithAddBtn = new ArrayList<>(listItems);
        if (listItems.size() > 1) {
            for (int i = 1; i < listItems.size(); i++) {
                if (listItems.get(i).isHeader()) {
                    listItemsWithAddBtn.add(i, new ListItem(listItems.get(i - 1).getCategoryId()));
                }
            }
        }
//        if (listItems.size() > 0) {
//            int categoryId;
//            ListItem lastIndexItem = listItems.get(listItems.size() - 1);
//            if (lastIndexItem.isHeader()) {
//                categoryId = listItems.get(listItems.size() - 1).getId();
//            } else {
//                categoryId = listItems.get(listItems.size() - 1).getCategoryId();
//            }
//
//            listItemsWithAddBtn.add(listItems.size() + 1, new ListItem(categoryId));
//        }
        mvi.setupRecyclerView(listItemsWithAddBtn);
    }

    private void addNewEditableTab() {
        List<String> newTitles = new ArrayList<>(titles);
        newTitles.add("Add New Category");
        mvi.setupTabLayout(newTitles);
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
                    titles = getItemsTitle(allItemsDao.getData());
                    mvi.setupTabLayout(titles);
                    mvi.setupRecyclerView(listItems);
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
                mvi.hideProgressBar();
            }
        };
    }

//    Result<ItemCategoryDao>

    public DisposableObserver<Result<ItemCategoryDao>> addNewCategoryObserver() {
        return new DisposableObserver<Result<ItemCategoryDao>>() {
            @Override
            public void onNext(@NonNull Result<ItemCategoryDao> itemCategoryDaoResult) {
                Log.d(TAG, "OnNext " + itemCategoryDaoResult);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "Error" + e);
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Completed");
                getMerchantItemNetworkInterface().getAllItems(storeId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(getAllItemObserver());
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
                    .map(item -> new ListItem(item.getName(), item.getDesc(), item.getId(), item.getPrice(), item.getItemCategoryId())).collect(Collectors.toList());
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
