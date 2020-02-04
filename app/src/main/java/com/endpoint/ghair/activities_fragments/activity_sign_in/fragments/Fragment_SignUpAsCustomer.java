package com.endpoint.ghair.activities_fragments.activity_sign_in.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


import com.endpoint.ghair.R;
import com.endpoint.ghair.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.endpoint.ghair.databinding.FragmentSignUpAsCustomerBinding;
import com.endpoint.ghair.preferences.Preferences;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.listeners.OnCountryPickerListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_SignUpAsCustomer extends Fragment  {
    private SignInActivity activity;
    private String current_language;
    private FragmentSignUpAsCustomerBinding binding;
    private CountryPicker countryPicker;
    private Preferences preferences;

    public static Fragment_SignUpAsCustomer newInstance() {
        return new Fragment_SignUpAsCustomer();
    }


    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up_as_customer, container, false);

        initView();
        return binding.getRoot();
    }

    private void initView() {

        activity = (SignInActivity) getActivity();
        Paper.init(activity);
       binding.tvRegister.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               activity.DisplayFragmentSignUpMarketr();
           }
       });

    }



}
