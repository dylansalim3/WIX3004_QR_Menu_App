package com.dylansalim.qrmenuapp.ui.store_qr;

import com.dylansalim.qrmenuapp.models.dao.StoreDao;

public interface StoreQRPresenterInterface {
    void setStoreDetail(StoreDao storeDetail);

    void onShareBtnClick();

    void disposeObserver();
}
