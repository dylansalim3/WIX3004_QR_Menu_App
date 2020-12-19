package com.dylansalim.qrmenuapp.ui.edit_profile;

import android.net.Uri;

public interface EditProfilePresenterInterface {

    void getProfileImage(int userId);

    void saveProfile(int userId, String firstName, String lastName, String phoneNum, String address);

    void savePicture(int userId, Uri image);

    void disposeObserver();
}
