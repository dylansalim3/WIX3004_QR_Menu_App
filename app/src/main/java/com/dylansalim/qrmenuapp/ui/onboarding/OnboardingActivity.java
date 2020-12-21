package com.dylansalim.qrmenuapp.ui.onboarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.ui.login_registration.LoginRegistrationActivity;
import com.dylansalim.qrmenuapp.ui.qr_scan.QRScanActivity;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {
    private ViewPager2 onboardingViewPager;
    private LinearLayout layoutOnboardingIndicators;
    ImageView backBtn;
    Animation btnAnim;
    Button proceedBtn, ignoreRegistrationBtn;
    OnboardingAdapter onboardingAdapter;
    int position = 0;
    List<ScreenItem> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        proceedBtn = findViewById(R.id.proceedBtn);
        ignoreRegistrationBtn = findViewById(R.id.ignoreRegisterBtn);
        backBtn = findViewById(R.id.backImg);
        layoutOnboardingIndicators = findViewById(R.id.layoutOnboardingIndicators);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.onboarding_button_animation);

//      Check if the user used the app before, if yes, onboarding will not run again
        if(restorePrefData()){
            Intent loginActivity = new Intent(getApplicationContext(), QRScanActivity.class);
            startActivity(loginActivity);
            finish();
        }

//      Setup the onboarding items
        setupOnboardingItems();

//      Setup the adapter
        onboardingViewPager.setAdapter(onboardingAdapter);
        setupOnboardingAdapter();

//      Setup current Onboarding indicator
        setCurrentOnboardingIndicator(0);
        onboardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnboardingIndicator(position);
            }
        });

//      return to previous page when back button clicked
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = onboardingViewPager.getCurrentItem();
                if(position > 0){
                    position = position - 1;
                    onboardingViewPager.setCurrentItem(position);
                }
            }
        });

    }

    private void setupOnboardingItems(){
        mList = new ArrayList<>();
        mList.add(new ScreenItem(
                "QR Menu helps you go paperless For menus, customise your digitalised Menu and we will generate unique QR code for you"
                ,R.drawable.illustration));
        mList.add(new ScreenItem(
                "Your customers can then easily scan the generated QR code to view the menu"
                , R.drawable.scanqr));
        mList.add(new ScreenItem("", R.drawable.qrmenu_logo));

        onboardingViewPager = findViewById(R.id.liquidPager);
        onboardingAdapter = new OnboardingAdapter(mList);
    }

    private void setupOnboardingAdapter(){
        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8,0,8,0);
        for(int i = 0; i < indicators.length; i++){
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.onboarding_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            layoutOnboardingIndicators.addView(indicators[i]);
        }
    }

    private void setCurrentOnboardingIndicator(int index){
        int childCount = layoutOnboardingIndicators.getChildCount();
        for(int i = 0; i < childCount; i++){
            ImageView imageView = (ImageView) layoutOnboardingIndicators.getChildAt(i);
            if (i == index) {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_active)
                );
            }else{
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_inactive)
                );
            }
        }

        if(index == 0){
            backBtn.setVisibility(View.INVISIBLE);
        }else{
            backBtn.setVisibility(View.VISIBLE);
        }

        if(index == onboardingAdapter.getItemCount()-1){
            proceedBtn.setVisibility(View.VISIBLE);
            proceedBtn.startAnimation(btnAnim);
            proceedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent loginActivity = new Intent(getApplicationContext(), LoginRegistrationActivity.class);
                    startActivity(loginActivity);
//                  save the footprint user entering the onboarding
                    savePrefsData();
                    finish();
                }
            });
            ignoreRegistrationBtn.setVisibility(View.VISIBLE);
            ignoreRegistrationBtn.startAnimation(btnAnim);
            ignoreRegistrationBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent closeActivity = new Intent(OnboardingActivity.this, QRScanActivity.class);
                    startActivity(closeActivity);
//                  save the footprint of user entering the onboarding
                    savePrefsData();
                    finish();
                }
            });
        }else{
            proceedBtn.setVisibility(View.INVISIBLE);
            ignoreRegistrationBtn.setVisibility(View.INVISIBLE);
        }
    }

    private boolean restorePrefData(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroActivityOpenedBefore = pref.getBoolean("isIntroOpened", false);
        return  isIntroActivityOpenedBefore;
    }

    private void savePrefsData(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpened", true);
        editor.apply();
    }

}