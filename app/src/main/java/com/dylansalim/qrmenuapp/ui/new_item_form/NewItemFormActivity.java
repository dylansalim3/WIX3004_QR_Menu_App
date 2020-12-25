package com.dylansalim.qrmenuapp.ui.new_item_form;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.services.FileIOService;
import com.dylansalim.qrmenuapp.ui.merchant.MerchantActivity;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class NewItemFormActivity extends AppCompatActivity implements NewItemFormViewInterface {

    private NewItemFormPresenter newItemFormPresenter;
    private TextInputLayout mItemName, mDesc, mPrice, mPromoPrice;
    private SwitchMaterial mIsPromo, mIsRecommended;
    private Button mBackBtn, mSubmitBtn;
    private ViewGroup progressView;
    private ImageView mItemImage;
    protected boolean isProgressShowing = false;
    private static final String TAG = "nifa";
    private int RESULT_LOAD_IMAGE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item_form);

        mItemName = (TextInputLayout) findViewById(R.id.til_new_item_form_item_name);
        mDesc = (TextInputLayout) findViewById(R.id.til_new_item_form_item_desc);
        mPrice = (TextInputLayout) findViewById(R.id.til_new_item_form_item_price);
        mPromoPrice = (TextInputLayout) findViewById(R.id.til_new_item_form_item_promo_price);
        mIsPromo = (SwitchMaterial) findViewById(R.id.switch_new_item_is_promo);
        mIsRecommended = (SwitchMaterial) findViewById(R.id.switch_new_item_is_recommended);
        mBackBtn = (Button) findViewById(R.id.btn_new_item_form_back);
        mSubmitBtn = (Button) findViewById(R.id.btn_new_item_form_submit);
        mItemImage = (ImageView) findViewById(R.id.iv_avatar_new_item);

        setupMVP();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            String actionType = bundle.getString(getResources().getString(R.string.action_type));
            Log.d(TAG, "Action type " + actionType);
            if (getResources().getString(R.string.edit_item).equalsIgnoreCase(actionType)) {
                Integer itemId = bundle.getInt(getResources().getString(R.string.item_id));
                newItemFormPresenter.retrieveItemDetail(itemId);
                mSubmitBtn.setText(getResources().getString(R.string.update));
            } else {
                Integer categoryId = bundle.getInt(getResources().getString(R.string.category_id));
                newItemFormPresenter.setCategoryId(categoryId);
            }
        }


        mIsPromo.setOnCheckedChangeListener((compoundButton, b) -> {
            newItemFormPresenter.setIsPromo(b);
            mPromoPrice.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
        });

        mIsRecommended.setOnCheckedChangeListener((compoundButton, b) -> newItemFormPresenter.setRecommended(b));

        mBackBtn.setOnClickListener(view -> finish());


        mSubmitBtn.setOnClickListener(view -> newItemFormPresenter.onSubmitForm());

        mItemImage.setOnClickListener(view -> {
            Log.d(TAG, "CLICKED");
            if (FileIOService.requestReadIOPermission(this)) {
                pickImage();
                Log.d(TAG, "have permission");
            }
        });

        Picasso.get().load(R.drawable.common_google_signin_btn_icon_dark)
                .placeholder(R.drawable.common_google_signin_btn_icon_dark)
                .into(mItemImage);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && requestCode == FileIOService.PICK_IMAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage();
        }
    }

    private void pickImage() {
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Log.d(TAG, "IMAGE LOADED");
            Uri selectedImage = data.getData();
            newItemFormPresenter.onItemImageResult(selectedImage, getContentResolver());
        }
    }

    private void setupMVP() {
        newItemFormPresenter = new NewItemFormPresenter(this);
    }

    @Override
    public String getItemName() {
        return mItemName.getEditText().getText().toString();
    }

    @Override
    public String getDesc() {
        return mDesc.getEditText().getText().toString();
    }

    @Override
    public String getPrice() {
        return mPrice.getEditText().getText().toString();
    }

    @Override
    public String getPromoPrice() {
        return mPromoPrice.getEditText().getText().toString();
    }

    @Override
    public void setItemName(String itemName) {
        mItemName.getEditText().setText(itemName);
    }

    @Override
    public void setDesc(String desc) {
        mDesc.getEditText().setText(desc);
    }

    @Override
    public void setPrice(String price) {
        mPrice.getEditText().setText(price);
    }

    @Override
    public void setPromoPrice(String promoPrice) {
        mPromoPrice.getEditText().setText(promoPrice);
    }

    @Override
    public void setRecommended(boolean recommended) {
        mIsRecommended.setChecked(recommended);
    }

    @Override
    public void setItemImage(String itemImageUrl) {
        if (mItemImage != null) {
            Picasso.get().load(itemImageUrl)
                    .placeholder(R.drawable.common_google_signin_btn_icon_dark)
                    .into(mItemImage);
        }
    }

    @Override
    public void displayError(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressBar() {
        isProgressShowing = true;
        progressView = (ViewGroup) getLayoutInflater().inflate(R.layout.progressbar_layout, null);
        View v = this.findViewById(android.R.id.content).getRootView();
        ViewGroup viewGroup = (ViewGroup) v;
        viewGroup.addView(progressView);
    }

    @Override
    public void hideProgressBar() {
        View v = this.findViewById(android.R.id.content).getRootView();
        ViewGroup viewGroup = (ViewGroup) v;
        viewGroup.removeView(progressView);
        isProgressShowing = false;
    }

    @Override
    public void onSuccessSubmission(String text) {
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", text);
        setResult(MerchantActivity.SUBMISSION_FORM_REQUEST_CODE, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        newItemFormPresenter.disposeObserver();
    }
}