package com.dylansalim.qrmenuapp.ui.setting;

public class SettingPresenter implements SettingPresenterInterface {

    private SettingViewInterface view;

    public SettingPresenter(SettingViewInterface view) {
        this.view = view;
    }

    @Override //TODO: fang -> get notification current setting
    public Boolean getNotificationStatus() {
        return true;
    }

    @Override
    public void setNotificationStatus(Boolean status) {
        //TODO: fang -> set notification setting
    }

    @Override //TODO: fang -> get location service current setting
    public Boolean getLocationStatus() {
        return true;
    }

    @Override
    public void setLocationStatus(Boolean status) {
        //TODO: fang -> set location setting
    }
}
