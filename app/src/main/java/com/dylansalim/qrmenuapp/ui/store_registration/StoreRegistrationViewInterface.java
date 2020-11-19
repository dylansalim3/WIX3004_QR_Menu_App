package com.dylansalim.qrmenuapp.ui.store_registration;

public interface StoreRegistrationViewInterface {
    String getStoreName();

    String getPhoneNumber();

    String getAddress();

    String getPostalCode();

    String getCity();

    String getCountry();

    void setAddress(String address);

    void setPostalCode(String postalCode);

    void setCity(String city);

    void setCountry(String country);
}
