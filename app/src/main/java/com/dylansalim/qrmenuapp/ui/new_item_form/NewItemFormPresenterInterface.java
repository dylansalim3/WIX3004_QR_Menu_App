package com.dylansalim.qrmenuapp.ui.new_item_form;

public interface NewItemFormPresenterInterface {
    void retrieveItemDetail(int itemId);

    void setCategoryId(int categoryId);

    void setRecommended(boolean recommended);

    void submitForm();
}
