package com.dylansalim.qrmenuapp.ui.qr_scan;

public interface QRScanViewInterface {
    void navigateToNextScreen(Class<?> cls);

    void displayError(String s);

    void showStoreNotFoundAlert();
}
