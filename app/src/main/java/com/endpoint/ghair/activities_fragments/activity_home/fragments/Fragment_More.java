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
import com.endpoint.ghair.activities_fragments.activity_about.AboutActivity;
import com.endpoint.ghair.activities_fragments.activity_addauction.AddProductActivity;
import com.endpoint.ghair.activities_fragments.activity_contact.ContactActivity;
import com.endpoint.ghair.activities_fragments.activity_edit_profile.Edit_Profile_Activity;
import com.endpoint.ghair.activities_fragments.activity_home.HomeActivity;
import com.endpoint.ghair.activities_fragments.activity_terms.TermsActivity;
import com.endpoint.ghair.databinding.FragmentMoreBinding;
import com.endpoint.ghair.models.UserModel;
import com.endpoint.ghair.preferences.Preferences;
import com.endpoint.ghair.share.Common;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_More extends Fragment {

    private HomeActivity activity;
    private FragmentMoreBinding binding;
    private Preferences preferences;
    private String lang;
    private UserModel userModel;

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
        userModel = preferences.getUserData(activity);
        Paper.init(activity);

        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        if (lang.equals("en")) {
            binding.tvEn.setBackground(activity.getResources().getDrawable(R.drawable.item_lang_shape));
            binding.tvEn.setTextColor(activity.getResources().getColor(R.color.white));
            binding.tvAr.setBackground(activity.getResources().getDrawable(R.drawable.lang_shape));
            binding.tvAr.setTextColor(activity.getResources().getColor(R.color.input));
        } else {
            binding.tvAr.setBackground(activity.getResources().getDrawable(R.drawable.item_lang_shape));
            binding.tvAr.setTextColor(activity.getResources().getColor(R.color.white));
            binding.tvEn.setBackground(activity.getResources().getDrawable(R.drawable.lang_shape));
            binding.tvEn.setTextColor(activity.getResources().getColor(R.color.input));
        }
        binding.llLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lang.equals("ar")) {
                    lang = "en";
                    binding.tvEn.setBackground(activity.getResources().getDrawable(R.drawable.item_lang_shape));
                    binding.tvEn.setTextColor(activity.getResources().getColor(R.color.white));
                    binding.tvAr.setBackground(activity.getResources().getDrawable(R.drawable.lang_shape));
                    binding.tvAr.setTextColor(activity.getResources().getColor(R.color.input));
                } else {
                    lang = "ar";
                    binding.tvAr.setBackground(activity.getResources().getDrawable(R.drawable.item_lang_shape));
                    binding.tvAr.setTextColor(activity.getResources().getColor(R.color.white));
                    binding.tvEn.setBackground(activity.getResources().getDrawable(R.drawable.lang_shape));
                    binding.tvEn.setTextColor(activity.getResources().getColor(R.color.input));
                }
                activity.RefreshActivity(lang);
            }
        });
        binding.llabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, AboutActivity.class);
                startActivity(intent);
            }
        });
        binding.llterms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, TermsActivity.class);
                startActivity(intent);
            }
        });
        binding.lledit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userModel != null) {
//                    Intent intent = new Intent(activity, Edit_Profile_Activity.class);
//                    startActivity(intent);
                } else {
Common.CreateNoSignAlertDialog(activity);
                }
            }
        });
        binding.lllogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userModel != null) {
                    activity.Logout();
                } else {

                }
            }
        });
        binding.llcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ContactActivity.class);
                startActivity(intent);
            }
        });
        binding.llshare.setOnClickListener(view -> {
            String app_url = "https://play.google.com/store/apps/details?id=" + activity.getPackageName();
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TITLE, "تطبيق غيار");
            intent.putExtra(Intent.EXTRA_TEXT, app_url);
            startActivity(intent);
        });
    }


}
