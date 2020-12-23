package com.dylansalim.qrmenuapp.ui.new_item_form;

public interface NewItemFormViewInterface {
    String getItemName();

    String getDesc();

    String getPrice();

    String getPromoPrice();

    String getPriceCurrency();

    void setItemName(String itemName);

    void setDesc(String desc);

    void setPrice(String price);

    void setPromoPrice(String promoPrice);

    void setRecommended(boolean recommended);

    void setItemImage(String itemImageUrl);

    void setPriceCurrency(String priceCurrency);

    void showProgressBar();

    void hideProgressBar();

    void onSuccessSubmission(String text);

    void displayError(String s);
}
