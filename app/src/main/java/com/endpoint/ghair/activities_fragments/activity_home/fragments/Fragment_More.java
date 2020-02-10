package com.endpoint.ghair.activities_fragments.activity_home.fragments;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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
import com.endpoint.ghair.databinding.FragmentMoreBinding;
import com.endpoint.ghair.preferences.Preferences;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_More extends Fragment {

    private HomeActivity activity;
    private FragmentMoreBinding binding;
    private Preferences preferences;
    private String lang;


    public static Fragment_More newInstance() {

        return new Fragment_More();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());


    }




}
