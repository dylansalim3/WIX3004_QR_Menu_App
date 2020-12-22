package com.dylansalim.qrmenuapp.ui.report;

import android.util.Log;

import com.dylansalim.qrmenuapp.models.dto.ReportDto;
import com.dylansalim.qrmenuapp.network.NetworkClient;
import com.dylansalim.qrmenuapp.network.ReportNetworkInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ReportPresenter implements ReportPresenterInterface {

    final String TAG = "Report Presenter";

    ReportViewInterface reportActivity;
    Disposable disposable;

    public ReportPresenter(ReportViewInterface reportActivity) {
        this.reportActivity = reportActivity;
    }

    @Override
    public void sendReport(int storeId, String email, String title, String desc, String token) {
        reportActivity.showLoading();
        ReportDto reportDto = new ReportDto(storeId, title, desc, email);

        disposable = NetworkClient.getNetworkClient(token).create(ReportNetworkInterface.class)
                .submitReport(reportDto)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Log.d(TAG, "report sent");
                    reportActivity.hideLoading();
                    reportActivity.showFinish();
                }, error -> {
                    Log.d(TAG, "report send error -> " + error);
                    reportActivity.hideLoading();
                    reportActivity.showError("Send Report Error");
                });
    }

    @Override
    public void disposeObserver() {
        if (disposable != null) {
            Log.d(TAG, "dispose observer");
            disposable.dispose();
        }
    }
}
