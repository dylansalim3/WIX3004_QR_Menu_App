package com.dylansalim.qrmenuapp.ui.store_qr;

import com.dylansalim.qrmenuapp.models.dto.Store;

public interface StoreQRPresenterInterface {
    void setStoreDetail(Store storeDetail);

    void onShareBtnClick();

    void disposeObserver();
}
