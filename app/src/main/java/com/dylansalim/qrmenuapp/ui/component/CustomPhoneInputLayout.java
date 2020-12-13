package com.dylansalim.qrmenuapp.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;

import com.dylan.phoneNumberInput.PhoneField;
import com.dylansalim.qrmenuapp.R;
import com.google.android.material.textfield.TextInputLayout;

public class CustomPhoneInputLayout extends PhoneField {


    private TextInputLayout mTextInputLayout;

    public CustomPhoneInputLayout(Context context) {
        this(context, null);
    }

    public CustomPhoneInputLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomPhoneInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void updateLayoutAttributes() {
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        setGravity(Gravity.TOP);
        setOrientation(HORIZONTAL);
    }

    @Override
    protected void prepareView() {
        super.prepareView();
        mTextInputLayout = (TextInputLayout) findViewWithTag(getResources().getString(com.dylan.phoneNumberInput.R.string.phonefield_til_phone));
    }

    @Override
    public int getLayoutResId() {
        return R.layout.layout_phone_input_layout;
    }

    @Override
    public void setHint(int resId) {
        mTextInputLayout.setHint(getContext().getString(resId));
    }

    @Override
    public void setError(String error) {
        if (error == null || error.length() == 0) {
            mTextInputLayout.setErrorEnabled(false);
        } else {
            mTextInputLayout.setErrorEnabled(true);
        }
        mTextInputLayout.setError(error);
    }

    public TextInputLayout getTextInputLayout() {
        return mTextInputLayout;
    }

}