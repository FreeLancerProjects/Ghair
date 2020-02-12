package com.endpoint.ghair.activities_fragments.activity_market_profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.endpoint.ghair.R;
import com.endpoint.ghair.activities_fragments.activity_accesories.AccessoriesActivity;
import com.endpoint.ghair.activities_fragments.activity_market.MarketActivity;
import com.endpoint.ghair.activities_fragments.activity_products.ProductsActivity;
import com.endpoint.ghair.activities_fragments.activity_spare_parts.SparePartsActivity;
import com.endpoint.ghair.adapters.Accessories_Adapter;
import com.endpoint.ghair.databinding.ActivityMarketProfileBinding;
import com.endpoint.ghair.databinding.ActivityMarketsBinding;
import com.endpoint.ghair.interfaces.Listeners;
import com.endpoint.ghair.language.Language;
import com.endpoint.ghair.models.Slider_Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class MarketProfileActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityMarketProfileBinding binding;
    private String lang;
    private Accessories_Adapter markets_adapter;
    private List<Slider_Model.Data> dataList;


    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_market_profile);
        initView();


    }

    @SuppressLint("RestrictedApi")
    private void initView() {
        dataList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        markets_adapter = new Accessories_Adapter(dataList, this);
        binding.recView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recView.setAdapter(markets_adapter);
        binding.recView1.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recView1.setAdapter(markets_adapter);
        binding.recView2.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recView2.setAdapter(markets_adapter);
        setdtat();
binding.arrownext1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(MarketProfileActivity.this, ProductsActivity.class);
        startActivity(intent);
    }
});
        binding.arrownext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MarketProfileActivity.this, SparePartsActivity.class);
                startActivity(intent);
            }
        });
        binding.arrownext3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MarketProfileActivity.this, AccessoriesActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setdtat() {
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());


        markets_adapter.notifyDataSetChanged();
    }

    @Override
    public void back() {
        finish();
    }
}
