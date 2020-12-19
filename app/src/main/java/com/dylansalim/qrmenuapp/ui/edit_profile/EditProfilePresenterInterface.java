package com.dylansalim.qrmenuapp.ui.edit_profile;

import android.net.Uri;

public interface EditProfilePresenterInterface {

    void getProfileImage(int userId);

    void saveProfile(String firstName, String lastName, String phoneNum, String address, String token);

    void savePicture(int userId, Uri image, String token);

    void disposeObserver();
}
