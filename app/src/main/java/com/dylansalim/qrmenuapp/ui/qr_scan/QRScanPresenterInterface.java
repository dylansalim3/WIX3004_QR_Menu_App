package com.dylansalim.qrmenuapp.ui.qr_scan;

import android.app.Activity;

public interface QRScanPresenterInterface {
    void checkUserType(Activity activity);

    void setToken(String token);
}
