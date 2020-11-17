package com.dylansalim.qrmenuapp.ui.login;

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

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.dao.RoleDao;
import com.dylansalim.qrmenuapp.models.dto.RegistrationDto;
import com.dylansalim.qrmenuapp.models.dto.SpinnerDto;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class RegistrationFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    OnChangeFragmentListener mChangeFragmentCallback;
    ArrayList<RoleDao> roles;
    EditText mEmail, mPassword, mRepeatPassword, mPhoneNumber, mAddress;
    Spinner mRoles;
    Button mSubmitBtn;
    int selectedRoleId = -1;

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        RoleDao roleDao = (RoleDao) adapterView.getItemAtPosition(i);
        selectedRoleId = roleDao.getId();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public interface OnChangeFragmentListener {
        public void onChangeFragment(int currentFragmentIndex);
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
        mPhoneNumber = (EditText) view.findViewById(R.id.et_registration_phone_number);
        mAddress = (EditText) view.findViewById(R.id.et_registration_address);
        mSubmitBtn = (Button) view.findViewById(R.id.btn_registration_submit);

        mRoles = (Spinner) view.findViewById(R.id.sp_role);
        mRoles.setOnItemSelectedListener(this);

        ArrayAdapter<RoleDao> arrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, roles);
        mRoles.setAdapter(arrayAdapter);

        mSubmitBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onSubmitForm();
            }
        });

        return view;
    }
    
    private void onSubmitForm(){
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        String repeatPassword = mRepeatPassword.getText().toString();
        String phoneNumber = mPhoneNumber.getText().toString();
        String address = mAddress.getText().toString();

//        RegistrationDto registrationDto = new RegistrationDto();
    }
}