package com.dylansalim.qrmenuapp.ui.merchant;

import android.content.Context;
import android.content.SharedPreferences;

import com.dylansalim.qrmenuapp.BuildConfig;
import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.EditListItem;
import com.dylansalim.qrmenuapp.models.dao.AllItemDao;
import com.dylansalim.qrmenuapp.models.dao.ItemCategoryDao;
import com.dylansalim.qrmenuapp.models.dao.OverallRating;
import com.dylansalim.qrmenuapp.models.dao.Result;
import com.dylansalim.qrmenuapp.models.dao.StoreDao;
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
import androidx.annotation.Nullable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MerchantPresenter implements MerchantPresenterInterface {
    MerchantViewInterface mvi;
    public static boolean editMode = false;

    public MerchantPresenter(MerchantViewInterface mvi) {
        this.mvi = mvi;
    }

    private UserDetailDao userDetailDao;
    private int storeId;
    private List<EditListItem> editListItems;
    private List<String> titles;
    private int currentTabPosition = 0;
    private HashMap<Integer, Integer> tabIndexHashMap;
    private List<DisposableObserver<?>> disposableObservers = new ArrayList<>();
    private boolean isStoreAdmin = false;
    private String overallRating;
    private static final String TAG = "mvi";

    private StoreDao storeResult;

    @Override
    public void getAllItems(Context context, @Nullable Integer storeId) {
        mvi.showProgressBar();
        if (storeId == null) {
            getUserDetails(context);
            isStoreAdmin = true;
            DisposableObserver disposableObserver = getMerchantItemNetworkInterface().getStoreDetailByUserId(userDetailDao.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(getStoreDetailObserver(true));
            disposableObservers.add(disposableObserver);

        } else {
            this.storeId = storeId;
            isStoreAdmin = false;
            retrieveStoreDetail(true);
        }
    }

    @Override
    public void retrieveItemDetail() {
        disposableObservers.add(getMerchantItemNetworkInterface().getAllItems(this.storeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getAllItemObserver()));
    }

    public void retrieveOverallRating() {
        disposableObservers.add(getMerchantItemNetworkInterface().getRatingByStoreId(this.storeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getOverallRatingObserver()));
    }

    @Override
    public void retrieveStoreDetail(@Nullable Boolean retrieveItem) {
        disposableObservers.add(getMerchantItemNetworkInterface().getStoreDetail(this.storeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getStoreDetailObserver(retrieveItem)));
    }

    private void setupStoreDetail(@Nullable String initialName, String profileImgUrl) {
        String storeName = initialName != null ? initialName : userDetailDao.getStoreName();
        if (storeName != null) {

            mvi.setupToolbarTitle(storeName);
        }

        if (profileImgUrl != null) {
            mvi.setProfileImg(BuildConfig.SERVER_API_URL + "/" + profileImgUrl);
        }
    }

    @Override
    public void onTabSelectionChanged(int tabIndex) {
        if (tabIndex > tabIndexHashMap.size() - 1) {
            mvi.showAddNewCategoryDialog();
            mvi.updateSelectedTab(0);
        } else if (tabIndexHashMap != null && currentTabPosition != tabIndex && tabIndexHashMap.get(currentTabPosition) != null) {
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
    public void onEditActionButtonClick() {
        editMode = !editMode;
        if (editMode) {
            mvi.updateEditActionIcon(R.drawable.ic_baseline_done_24);
            restoreRecyclerViewWithAddBtn();
        } else {
            mvi.updateEditActionIcon(R.drawable.ic_baseline_edit_24);
            restoreRecyclerView();
        }
    }

    @Override
    public void onShareBtnClick() {
        mvi.navigateToStoreQRActivity(storeResult);
    }

    private void restoreRecyclerViewWithAddBtn() {
        addNewEditableTab();
        setupRecyclerViewWithAddNewButton();
    }

    private void restoreRecyclerView() {
        mvi.setupTabLayout(titles);
        mvi.setupRecyclerView(editListItems);
    }

    @Override
    public void onAddNewCategory(String categoryName) {
        if (userDetailDao != null && categoryName != null && storeId != -1 && categoryName.length() > 0) {
            mvi.showProgressBar();
            ItemCategoryDao itemCategoryDao = new ItemCategoryDao(categoryName, storeId);
            disposableObservers.add(getMerchantItemNetworkInterface().createItemCategory(itemCategoryDao)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(addNewCategoryObserver()));
        }
    }

    @Override
    public void onDeleteItem(int itemId) {
        disposableObservers.add(getMerchantItemNetworkInterface().deleteItem(itemId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getDeleteItemObserver()));
    }

    @Override
    public void onDeleteItemCategory(int itemCategoryId) {
        disposableObservers.add(getMerchantItemNetworkInterface().deleteItemCategory(itemCategoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getDeleteItemObserver()));
    }

    @Override
    public void onInfoButtonClick() {
        mvi.navigateToMerchantInfoActivity(storeResult, isStoreAdmin);
    }

    public DisposableObserver<Result<String>> getDeleteItemObserver() {
        return new DisposableObserver<Result<String>>() {
            @Override
            public void onNext(@NonNull Result<String> deleteItemResult) {
                mvi.displayError(deleteItemResult.getMsg());
                retrieveItemDetail();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
                mvi.displayError(e.toString());
            }

            @Override
            public void onComplete() {
                mvi.hideProgressBar();
            }
        };
    }


    private void setupRecyclerViewWithAddNewButton() {
        List<EditListItem> editListItemsWithAddBtn = new ArrayList<>(editListItems);
        if (editListItems.size() > 1) {
            int addedQuantity = 0;
            for (int i = 1; i < editListItems.size(); i++) {
                if (editListItems.get(i).isHeader()) {
                    editListItemsWithAddBtn.add(i + addedQuantity, new EditListItem(editListItems.get(i - 1).getCategoryId()));
                    addedQuantity++;
                }
            }
        }
        if (editListItemsWithAddBtn.size() > 0) {
            int categoryId;
            EditListItem lastIndexItem = editListItemsWithAddBtn.get(editListItemsWithAddBtn.size() - 1);
            categoryId = editListItemsWithAddBtn.get(editListItemsWithAddBtn.size() - 1).getCategoryId();


            editListItemsWithAddBtn.add(editListItemsWithAddBtn.size(), new EditListItem(categoryId));
        }
        mvi.setupRecyclerView(editListItemsWithAddBtn);
    }

    private void addNewEditableTab() {
        List<String> newTitles = new ArrayList<>(titles);
        newTitles.add("Add New Category");
        mvi.setupTabLayout(newTitles);
    }

    private void getUserDetails(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(context.getString(R.string.token), "");


        if (!token.equals("")) {
            String dataString = null;
            try {
                dataString = JWTUtils.getDataString(token);
            } catch (Exception e) {
                e.printStackTrace();
            }
            userDetailDao = new Gson().fromJson(dataString, UserDetailDao.class);

        }
    }

    private MerchantItemNetworkInterface getMerchantItemNetworkInterface() {
        return NetworkClient.getNetworkClient().create(MerchantItemNetworkInterface.class);
    }

    public DisposableObserver<Result<StoreDao>> getStoreDetailObserver(@Nullable Boolean retrieveItem) {
        return new DisposableObserver<Result<StoreDao>>() {
            @Override
            public void onNext(@NonNull Result<StoreDao> storeDaoResult) {
                if (storeDaoResult.getData() != null) {
                    String storeName = storeDaoResult.getData().getName();
                    String profileImgUrl = storeDaoResult.getData().getProfileImg();
                    storeResult = storeDaoResult.getData();
                    setupStoreDetail(storeName, profileImgUrl);
                    storeId = storeResult.getId();
                }
                retrieveOverallRating();

                if (null != retrieveItem && retrieveItem) {
                    retrieveItemDetail();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                mvi.hideProgressBar();
            }
        };
    }

    public DisposableObserver<Result<List<AllItemDao>>> getAllItemObserver() {
        return new DisposableObserver<Result<List<AllItemDao>>>() {
            @Override
            public void onNext(@NonNull Result<List<AllItemDao>> allItemsDao) {
                if (allItemsDao.getData() != null) {
                    editListItems = mapAllItemsToListItems(allItemsDao.getData());
                    titles = getItemsTitle(allItemsDao.getData());
                    setTabIndexHashMap(editListItems);
                    if (editMode) {
                        restoreRecyclerViewWithAddBtn();
                    } else {
//                        mvi.setupTabLayout(titles);
//                        mvi.setupRecyclerView(editListItems);
                        restoreRecyclerView();
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
                mvi.displayError(e.toString());
            }

            @Override
            public void onComplete() {
                mvi.hideProgressBar();
            }
        };
    }

    public DisposableObserver<Result<ItemCategoryDao>> addNewCategoryObserver() {
        return new DisposableObserver<Result<ItemCategoryDao>>() {
            @Override
            public void onNext(@NonNull Result<ItemCategoryDao> itemCategoryDaoResult) {
                retrieveItemDetail();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
                mvi.displayError(e.toString());
            }

            @Override
            public void onComplete() {
            }
        };
    }

    public DisposableObserver<Result<OverallRating>> getOverallRatingObserver() {
        return new DisposableObserver<Result<OverallRating>>() {
            @Override
            public void onNext(@NonNull Result<OverallRating> allItemsDao) {
                if (null != allItemsDao.getData()) {
                    OverallRating rating = allItemsDao.getData();
                    overallRating = String.format("%.2f (%d)", rating.getAverage(), rating.getCount());
                    mvi.setOverallRating(overallRating);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
                mvi.displayError(e.toString());
            }

            @Override
            public void onComplete() {
                mvi.hideProgressBar();
            }
        };
    }

    private List<String> getItemsTitle(List<AllItemDao> allItems) {
        return allItems.stream().map(AllItemDao::getName).collect(Collectors.toList());
    }

    private List<EditListItem> mapAllItemsToListItems(List<AllItemDao> allItems) {
        List<EditListItem> editListItems = new ArrayList<>();
        allItems.stream().forEach(category -> {
            editListItems.add(new EditListItem(category.getName(), category.getId()));
            List<EditListItem> categoryChildItems = category.getItems().stream()
                    .map(item -> new EditListItem(item.getName(), item.getDesc(), item.getId(), item.getPrice(), item.isRecommmended(), item.getPromoPrice(), item.getItemCategoryId(), item.getItemImg(), item.getCurrency())).collect(Collectors.toList());
            editListItems.addAll(categoryChildItems);
        });
        return editListItems;
    }

    private void setTabIndexHashMap(List<EditListItem> editListItems) {
        tabIndexHashMap = new HashMap<>();
        tabIndexHashMap = editListItems.stream().filter(EditListItem::isHeader).collect(HashMap<Integer, Integer>::new,
                (map, streamValue) ->
                        map.put(map.size(), editListItems.indexOf(streamValue))
                , (map, map2) -> {
                });
    }

    @Override
    public void disposeObserver() {
        for (DisposableObserver<?> disposableObserver : disposableObservers) {
            disposableObserver.dispose();
        }
    }

}
