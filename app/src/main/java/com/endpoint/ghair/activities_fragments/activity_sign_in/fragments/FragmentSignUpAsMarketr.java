package com.endpoint.ghair.activities_fragments.activity_sign_in.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


import com.endpoint.ghair.R;
import com.endpoint.ghair.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.endpoint.ghair.databinding.FragmentSignUpAsCustomerBinding;
import com.endpoint.ghair.databinding.FragmentSignUpAsMarketBinding;
import com.endpoint.ghair.preferences.Preferences;
import com.mukesh.countrypicker.CountryPicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentSignUpAsMarketr extends Fragment {
    private SignInActivity activity;
    private String current_language;
    private FragmentSignUpAsMarketBinding binding;
    private CountryPicker countryPicker;
    private Preferences preferences;

    public static FragmentSignUpAsMarketr newInstance() {
        return new FragmentSignUpAsMarketr();
    }


    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up_as_market, container, false);

        initView();
        return binding.getRoot();
    }

    private void initView() {

        activity = (SignInActivity) getActivity();
        Paper.init(activity);


    }


}
