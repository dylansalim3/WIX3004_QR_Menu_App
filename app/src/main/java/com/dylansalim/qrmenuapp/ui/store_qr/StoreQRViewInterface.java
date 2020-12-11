package com.dylansalim.qrmenuapp.ui.store_qr;

import android.graphics.Bitmap;

public interface StoreQRViewInterface {
    void setQRDetail(Bitmap bitmap, String title, String desc);

    void shareQR(Bitmap bitmap, String desc);
}
