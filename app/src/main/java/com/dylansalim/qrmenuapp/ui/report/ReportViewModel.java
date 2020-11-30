package com.dylansalim.qrmenuapp.ui.report;

import androidx.lifecycle.ViewModel;

import com.dylansalim.qrmenuapp.models.dto.ReportDto;
import com.dylansalim.qrmenuapp.network.NetworkClient;
import com.dylansalim.qrmenuapp.network.ReportNetworkInterface;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ReportViewModel extends ViewModel {

    int userId = 0;
    int storeId = 0;
    String email = "";
    String title = "";
    String desc = "";
    Boolean showLoading = false;
    Boolean showDialog = false;
    String inputMissing = null;
    Disposable disposable;

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }

    public void onSendClicked() {
        ReportDto reportDto = new ReportDto(storeId, userId, title, desc, email);
        setShowLoading(true);

        disposable = NetworkClient.getNetworkClient().create(ReportNetworkInterface.class)
                .submitReport(reportDto)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> setShowLoading(false) );
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Boolean getShowLoading() {
        return showLoading;
    }

    public void setShowLoading(Boolean showLoading) {
        this.showLoading = showLoading;
    }

    public Boolean getShowDialog() {
        return showDialog;
    }

    public void setShowDialog(Boolean showDialog) {
        this.showDialog = showDialog;
    }

    public String getInputMissing() {
        return inputMissing;
    }

    public void setInputMissing(String inputMissing) {
        this.inputMissing = inputMissing;
    }
}
