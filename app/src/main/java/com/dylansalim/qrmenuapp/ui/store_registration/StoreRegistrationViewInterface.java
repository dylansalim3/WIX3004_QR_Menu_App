package com.dylansalim.qrmenuapp.ui.store_registration;

import com.dylansalim.qrmenuapp.models.dao.TokenDao;
import com.dylansalim.qrmenuapp.ui.component.CustomPhoneInputLayout;

public interface StoreRegistrationViewInterface {
    String getStoreName();

//    String getPhoneNumber();

    String getAddress();

    String getPostalCode();

    String getCity();

    String getCountry();

    String getOpeningHour();

    String getClosingHour();

    String getSpecialOpeningNote();

    CustomPhoneInputLayout getCustomPhoneInputLayout();

    void setAddress(String address);

    void setPostalCode(String postalCode);

    void setCity(String city);

    void setCountry(String country);

    void displayErrorMessage(String s);

    void showProgressBar();

    void hideProgressBar();

    void navigateToNextScreen();

}
