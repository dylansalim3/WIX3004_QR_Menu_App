package com.dylansalim.qrmenuapp.ui.login_registration;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.dao.RoleDao;
import com.dylansalim.qrmenuapp.models.dao.TokenDao;
import com.dylansalim.qrmenuapp.models.dto.RegistrationDto;
import com.dylansalim.qrmenuapp.ui.qr_scan.QRScanActivity;
import com.dylansalim.qrmenuapp.ui.login_registration.login.LoginFragment;
import com.dylansalim.qrmenuapp.ui.login_registration.registration.RegistrationFragment;

import java.util.ArrayList;
import java.util.List;

public class LoginRegistrationActivity extends AppCompatActivity
        implements LoginFragment.OnChangeFragmentListener,
        RegistrationFragment.OnChangeFragmentListener,
        LoginFragment.OnSubmitLoginFormListener,
        RegistrationFragment.OnSubmitRegistrationFormListener,
        LoginRegistrationViewInterface {

    ProgressBar mProgressBar;

    LoginRegistrationPresenter loginRegistrationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_registration);
        Log.d("oncreate", "init");

        mProgressBar = (ProgressBar) findViewById(R.id.pb_login_loading);

        // If fragment container is available, we need to add fragment to it.
        if (findViewById(R.id.login_registration_fragment_container) != null) {

            // if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            LoginFragment loginFragment = new LoginFragment();

            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            loginFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.login_registration_fragment_container, loginFragment)
                    .commit();
        }
        setupMVP();
    }

    private void setupMVP() {
        loginRegistrationPresenter = new LoginRegistrationPresenter(this);
    }


    @Override
    public void onChangeFragment(int destFragmentIndex) {
        Fragment newFragment;
        if (destFragmentIndex == 0) {
            newFragment = new RegistrationFragment();
            getRoles();
        } else {
            newFragment = new LoginFragment();
            newFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.login_registration_fragment_container, newFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }


    private void getRoles() {
        loginRegistrationPresenter.getRoles();
    }

    @Override
    public void onSubmitLoginForm(String email, String password) {
        loginRegistrationPresenter.submitLoginForm(email, password);
    }

    @Override
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onSubmitRegistrationForm(RegistrationDto registrationDto) {
        loginRegistrationPresenter.submitRegistrationForm(registrationDto);
    }

    @Override
    public void displayError(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToRegistrationFragment(List<RoleDao> roleDaoList) {
        Fragment newFragment = new RegistrationFragment();
        Bundle mBundle = new Bundle();
        ArrayList<RoleDao> mArrayList = new ArrayList<>(roleDaoList);
        mBundle.putParcelableArrayList("Roles", mArrayList);
        newFragment.setArguments(mBundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_registration_fragment_container, newFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void navigateToNextActivity(TokenDao tokenDao) {
        Log.d("LR Activity",tokenDao.getToken());

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.app_name),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.token), tokenDao.getToken());
        editor.apply();

        Intent intent = new Intent(this, QRScanActivity.class);
        startActivity(intent);
        finish();
    }
}