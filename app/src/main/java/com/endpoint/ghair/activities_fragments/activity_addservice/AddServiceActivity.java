package com.endpoint.ghair.activities_fragments.activity_addservice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.endpoint.ghair.R;
import com.endpoint.ghair.databinding.ActivityAddAuctionBinding;
import com.endpoint.ghair.databinding.ActivityAddServiceBinding;
import com.endpoint.ghair.interfaces.Listeners;
import com.endpoint.ghair.language.Language;

import java.util.Locale;

import io.paperdb.Paper;

public class AddServiceActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityAddServiceBinding binding;
    private String lang;


    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_service);
        initView();



    }

    @SuppressLint("RestrictedApi")
    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);

    }


    @Override
    public void back() {
        finish();
    }
}
