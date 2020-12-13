package com.dylansalim.qrmenuapp.ui.report;

public interface ReportViewInterface {

    void showLoading();

    void hideLoading();

    void showFinish();

    void showError(String error);
}
