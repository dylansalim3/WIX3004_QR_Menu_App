package com.dylansalim.qrmenuapp.ui.new_item_form;

import android.content.ContentResolver;
import android.net.Uri;

public interface NewItemFormPresenterInterface {
    void retrieveItemDetail(int itemId);

    void setCategoryId(int categoryId);

    void setRecommended(boolean recommended);

    void setIsPromo(boolean isPromo);

    void onItemImageResult(Uri uri, ContentResolver contentResolver);

    void onSubmitForm();

    void disposeObserver();
}
