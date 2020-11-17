package com.dylansalim.qrmenuapp.ui.login_registration.login;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dylansalim.qrmenuapp.R;

public class LoginFragment extends Fragment implements LoginViewInterface {
    OnChangeFragmentListener mChangeFragmentCallback;
    OnSubmitLoginFormListener mSubmitFormCallback;

    LoginPresenter loginPresenter;

    public static final int LOGIN_FRAGMENT_INDEX = 1;

    EditText mEmail;

    EditText mPassword;

    TextView mRegistrationText;

    @Override
    public void submitLoginForm(String email, String password) {
        mSubmitFormCallback.onSubmitLoginForm(email, password);

    }

    @Override
    public String getEmail() {
        return mEmail.getText().toString();
    }

    @Override
    public String getPassword() {
        return mPassword.getText().toString();
    }

    @Override
    public void displayError(String s) {
        mSubmitFormCallback.displayError(s);
    }

    public interface OnChangeFragmentListener {
        public void onChangeFragment(int currentFragmentIndex);
    }

    public interface OnSubmitLoginFormListener {
        public void onSubmitLoginForm(String email, String password);

        public void displayError(String s);
    }

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            // This makes sure that the container activity has implemented
            // the callback interface. If not, it throws an exception.
            try {
                mChangeFragmentCallback = (OnChangeFragmentListener) context;
                mSubmitFormCallback = (OnSubmitLoginFormListener) context;
            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString()
                        + " must implement OnHeadlineSelectedListener");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mEmail = (EditText) view.findViewById(R.id.et_login_username);
        mPassword = (EditText) view.findViewById(R.id.et_login_password);
        mRegistrationText = (TextView) view.findViewById(R.id.tv_login_register_here);
        Button mSubmitBtn = view.findViewById(R.id.btn_login_submit);

        setupMVP();

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPresenter.onLoginButtonClicked();
            }
        });

        mRegistrationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("change fragment","clicked");
                mChangeFragmentCallback.onChangeFragment(LOGIN_FRAGMENT_INDEX);
            }
        });

        return view;
    }

    private void setupMVP() {
        loginPresenter = new LoginPresenter(this);
    }

}