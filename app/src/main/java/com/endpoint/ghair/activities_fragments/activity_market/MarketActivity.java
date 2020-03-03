package com.endpoint.ghair.activities_fragments.activity_market;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.endpoint.ghair.R;
import com.endpoint.ghair.activities_fragments.activity_market_profile.MarketProfileActivity;
import com.endpoint.ghair.adapters.MostActiveMarkets_Adapter;
import com.endpoint.ghair.databinding.ActivityMarketsBinding;
import com.endpoint.ghair.interfaces.Listeners;
import com.endpoint.ghair.language.Language;
import com.endpoint.ghair.models.Market_Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class MarketActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityMarketsBinding binding;
    private String lang;
    private MostActiveMarkets_Adapter mostActiveMarkets_adapter;
    private List<Market_Model.Data> dataList;


    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_markets);
        initView();



    }

    @SuppressLint("RestrictedApi")
    private void initView() {
        dataList=new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        mostActiveMarkets_adapter =new MostActiveMarkets_Adapter(dataList,this,null);
        binding.recView.setLayoutManager(new GridLayoutManager(this,3));
        binding.recView.setAdapter(mostActiveMarkets_adapter);
       // setdtat();



    }


    @Override
    public void back() {
        finish();
    }

    public void showProfile(int id) {
        Intent intent=new Intent(MarketActivity.this, MarketProfileActivity.class);
        intent.putExtra("search",id);

        startActivity(intent);
    }
}
