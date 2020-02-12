package com.endpoint.ghair.activities_fragments.activity_cart;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;


import com.endpoint.ghair.R;
import com.endpoint.ghair.adapters.Cart_Adapter;
import com.endpoint.ghair.databinding.ActivityCartBinding;
import com.endpoint.ghair.interfaces.Listeners;
import com.endpoint.ghair.language.Language;
import com.endpoint.ghair.models.Slider_Model;
import com.endpoint.ghair.preferences.Preferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityCartBinding binding;

    private Preferences preferences;
    private String lang;
private List<Slider_Model.Data> order_details;
private Cart_Adapter cart_adapter;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        initView();

    }



    @SuppressLint("RestrictedApi")
    private void initView() {

        binding.toolbar.setTitle("");
        order_details = new ArrayList<>();
        cart_adapter = new Cart_Adapter(order_details, this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.recCart.setLayoutManager(new GridLayoutManager(this, 1));
        binding.recCart.setAdapter(cart_adapter);
        setdtat();



    }

    private void setdtat() {
        order_details.add(new Slider_Model.Data());
        order_details.add(new Slider_Model.Data());
        order_details.add(new Slider_Model.Data());
        order_details.add(new Slider_Model.Data());


        cart_adapter.notifyDataSetChanged();
    }

    @Override
    public void back() {
        finish();
    }

}
