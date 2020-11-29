package com.dylansalim.qrmenuapp.ui.new_item_form;

public interface NewItemFormViewInterface {
    String getItemName();

    String getDesc();

    String getPrice();

    String getPromoPrice();

    void setItemName(String itemName);

    void setDesc(String desc);

    void setPrice(String price);

    void setPromoPrice(String promoPrice);

    void showProgressBar();

    void hideProgressBar();

    void displayError(String s);
}
