package com.dylansalim.qrmenuapp.ui.report;

public interface ReportPresenterInterface {

    void sendReport(int userId, int storeId, String email, String title, String desc);

    void disposeObserver();
}
