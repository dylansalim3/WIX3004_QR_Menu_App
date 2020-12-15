package com.dylansalim.qrmenuapp.ui.edit_profile;

public interface EditProfilePresenterInterface {

    void saveProfile(int userId, String firstName, String lastName, String phoneNum, String address);

    void disposeObserver();
}
