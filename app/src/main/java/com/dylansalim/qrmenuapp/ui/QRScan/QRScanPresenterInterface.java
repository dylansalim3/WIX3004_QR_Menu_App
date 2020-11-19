package com.dylansalim.qrmenuapp.ui.QRScan;

import android.app.Activity;

public interface QRScanPresenterInterface {
    void checkUserType(Activity activity);

    void setToken(String token);
}
