package com.dylansalim.qrmenuapp.ui.QRScan;

public interface QRScanViewInterface {
    void navigateToNextScreen(Class<?> cls);

    void displayError(String s);
}
