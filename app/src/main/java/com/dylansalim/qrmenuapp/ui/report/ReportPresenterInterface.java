package com.dylansalim.qrmenuapp.ui.report;

public interface ReportPresenterInterface {

    void sendReport(int storeId, String email, String title, String desc, String token);

    void disposeObserver();
}
