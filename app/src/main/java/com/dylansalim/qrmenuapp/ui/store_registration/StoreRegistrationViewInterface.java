package com.dylansalim.qrmenuapp.ui.store_registration;

import com.dylansalim.qrmenuapp.models.dao.StoreDao;
import com.dylansalim.qrmenuapp.models.dao.TokenDao;
import com.dylansalim.qrmenuapp.ui.component.CustomPhoneInputLayout;

public interface StoreRegistrationViewInterface {
    String getStoreName();

    String getAddress();

    String getPostalCode();

    String getCity();

    String getCountry();

    String getOpeningHour();

    String getClosingHour();

    String getSpecialOpeningNote();

    CustomPhoneInputLayout getCustomPhoneInputLayout();

    void setStoreName(String storeName);

    void setAddress(String address);

    void setPostalCode(String postalCode);

    void setCity(String city);

    void setCountry(String country);

    void setOpeningHour(String openingHour);

    void setClosingHour(String closingHour);

    void setSpecialOpeningNote(String specialOpeningNote);

    void setProfileImg(String profileImg);

    void displayErrorMessage(String s);

    void showProgressBar();

    void hideProgressBar();

    void navigateToNextScreen();

    void onFormSubmitted(StoreDao storeDetail);

}
