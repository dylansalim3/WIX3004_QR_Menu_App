package com.dylansalim.qrmenuapp.ui.login_registration.registration;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.dto.Role;
import com.dylansalim.qrmenuapp.models.dto.RegistrationDto;
import com.dylansalim.qrmenuapp.ui.component.CustomPhoneInputLayout;
import com.dylansalim.qrmenuapp.ui.login_registration.login.LoginFragment;
import com.dylansalim.qrmenuapp.utils.TextValidator;

import java.util.ArrayList;

public class RegistrationFragment extends Fragment implements RegistrationViewInterface {
    OnChangeFragmentListener mChangeFragmentCallback;
    OnSubmitRegistrationFormListener mOnSubmitRegistrationFormCallback;
    ArrayList<Role> roles;
    EditText mEmail, mPassword, mRepeatPassword, mFirstName, mLastName, mAddress;
    Spinner mRoles;
    Button mSubmitBtn;
    RegistrationPresenter registrationPresenter;
    TextView mLoginText;
    private CustomPhoneInputLayout mPhoneInputLayout;

    public static final int REGISTER_FRAGMENT_INDEX = 0;

    @Override
    public String getEmail() {
        return mEmail.getText().toString();
    }

    @Override
    public String getPassword() {
        return mPassword.getText().toString();
    }

    @Override
    public String getRepeatPassword() {
        return mRepeatPassword.getText().toString();
    }

    @Override
    public String getFirstName() {
        return mFirstName.getText().toString();
    }

    @Override
    public String getLastName() {
        return mLastName.getText().toString();
    }

    @Override
    public String getPhoneNumber() {
        return mPhoneInputLayout.getPhoneNumber();
    }

    @Override
    public String getAddress() {
        return mAddress.getText().toString();
    }

    @Override
    public void submitRegistrationForm(RegistrationDto registrationDto) {
        mOnSubmitRegistrationFormCallback.onSubmitRegistrationForm(registrationDto);
    }

    @Override
    public void displayErrorMessage(String s) {
        mOnSubmitRegistrationFormCallback.displayError(s);
    }


    public interface OnChangeFragmentListener {
        void onChangeFragment(int currentFragmentIndex);
    }

    public interface OnSubmitRegistrationFormListener {
        void onSubmitRegistrationForm(RegistrationDto registrationDto);

        void displayError(String s);
    }

    public RegistrationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            roles = bundle.getParcelableArrayList("Roles");
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            // This makes sure that the container activity has implemented
            // the callback interface. If not, it throws an exception.
            try {
                mChangeFragmentCallback = (RegistrationFragment.OnChangeFragmentListener) context;
                mOnSubmitRegistrationFormCallback = (RegistrationFragment.OnSubmitRegistrationFormListener) context;
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
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        mEmail = (EditText) view.findViewById(R.id.et_registration_username);
        mPassword = (EditText) view.findViewById(R.id.et_registration_password);
        mRepeatPassword = (EditText) view.findViewById(R.id.et_registration_password_repeat);
        mFirstName = (EditText) view.findViewById(R.id.et_registration_first_name);
        mLastName = (EditText) view.findViewById(R.id.et_registration_last_name);
        mPhoneInputLayout = (CustomPhoneInputLayout) view.findViewById(R.id.registration_phone_input_layout);
        mAddress = (EditText) view.findViewById(R.id.et_registration_address);
        mLoginText = (TextView) view.findViewById(R.id.tv_register_login_here);
        mSubmitBtn = (Button) view.findViewById(R.id.btn_registration_submit);
        mPhoneInputLayout.setDefaultCountry("MY");


        validateForm();
        mRoles = (Spinner) view.findViewById(R.id.sp_role);
        mRoles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Role role = (Role) adapterView.getItemAtPosition(i);
                registrationPresenter.setSelectedRole(role);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<Role> arrayAdapter = new ArrayAdapter<>(requireActivity(), R.layout.spinner_item, roles);

        mRoles.setAdapter(arrayAdapter);

        mSubmitBtn.setOnClickListener(view1 -> registrationPresenter.onRegistrationButtonClicked());

        mLoginText.setOnClickListener(view12 -> mChangeFragmentCallback.onChangeFragment(LoginFragment.LOGIN_FRAGMENT_INDEX));

        setupMVP();

        return view;
    }

    private void setupMVP() {
        registrationPresenter = new RegistrationPresenter(this);
        if (roles != null && roles.size() > 0) {
            registrationPresenter.setSelectedRole(roles.get(0));
        }
    }

    private void validateForm() {
        mEmail.addTextChangedListener(new TextValidator(mEmail) {

            @Override
            public void validate(TextView textView, String text) {
                registrationPresenter.validateEmail(textView, text);
            }
        });
        mPassword.addTextChangedListener(new TextValidator(mPassword) {
            @Override
            public void validate(TextView textView, String text) {
                registrationPresenter.validatePassword(textView, text);
            }
        });
        mRepeatPassword.addTextChangedListener(new TextValidator(mRepeatPassword) {
            @Override
            public void validate(TextView textView, String text) {
                registrationPresenter.validateRepeatPassword(textView, text, mPassword.getText().toString());
            }
        });
        EditText mPhoneNumber = mPhoneInputLayout.getTextInputLayout().getEditText();
        if(mPhoneNumber!=null){
            mPhoneNumber.addTextChangedListener(new TextValidator(mPhoneNumber) {
                @Override
                public void validate(TextView textView, String text) {
                    registrationPresenter.validatePhoneNumber(textView, getPhoneNumber());
                }
            });
        }
        mAddress.addTextChangedListener(new TextValidator(mAddress) {
            @Override
            public void validate(TextView textView, String text) {
                registrationPresenter.validateNonNullField(textView, text);
            }
        });
    }




}