package com.endpoint.ghair.activities_fragments.activity_complete_order;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.endpoint.ghair.R;
import com.endpoint.ghair.activities_fragments.activity_products_detials.ProductsDetialsActivity;
import com.endpoint.ghair.databinding.ActivityCompleteOrderBinding;
import com.endpoint.ghair.interfaces.Listeners;
import com.endpoint.ghair.language.Language;

import java.util.Locale;

import io.paperdb.Paper;

public class CompleteOrderActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityCompleteOrderBinding binding;
    private String lang;



    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_complete_order);
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
