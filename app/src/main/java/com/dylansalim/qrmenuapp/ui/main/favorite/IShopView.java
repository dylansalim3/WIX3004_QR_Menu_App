package com.dylansalim.qrmenuapp.ui.main.favorite;

import com.dylansalim.qrmenuapp.models.dao.Shop;

import java.util.List;

public interface IShopView {

    void onGetShopList(List<Shop> shopList);
}
