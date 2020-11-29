package com.dylansalim.qrmenuapp.ui.new_item_form;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.dylansalim.qrmenuapp.R;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;

public class NewItemFormActivity extends AppCompatActivity implements NewItemFormViewInterface {

    private NewItemFormPresenter newItemFormPresenter;
    private TextInputLayout mItemName, mDesc, mPrice, mPromoPrice;
    private SwitchMaterial mIsPromo, mIsRecommended;
    private Button mBackBtn, mSubmitBtn;
    private ViewGroup progressView;
    protected boolean isProgressShowing = false;
    private static final String TAG = "nifa";


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


        mIsPromo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mPromoPrice.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
            }
        });

        mIsRecommended.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                newItemFormPresenter.setRecommended(b);
            }
        });

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
}