package com.endpoint.ghair.activities_fragments.activity_home.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.endpoint.ghair.R;
import com.endpoint.ghair.activities_fragments.activity_home.HomeActivity;
import com.endpoint.ghair.databinding.FragmentAuctionBinding;
import com.endpoint.ghair.databinding.FragmentRequireBinding;
import com.endpoint.ghair.preferences.Preferences;

public class Fragment_Require extends Fragment {

    private HomeActivity activity;
    private FragmentRequireBinding binding;
    private Preferences preferences;


    public static Fragment_Require newInstance() {
        return new Fragment_Require();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_require,container,false);
        initView();

        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();

    }






}
