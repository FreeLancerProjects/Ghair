package com.endpoint.ghair.activities_fragments.activity_market;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.endpoint.ghair.R;
import com.endpoint.ghair.adapters.Category_Adapter;
import com.endpoint.ghair.adapters.Markets_Adapter;
import com.endpoint.ghair.databinding.ActivityMarketsBinding;
import com.endpoint.ghair.databinding.ActivityServiceRequireBinding;
import com.endpoint.ghair.interfaces.Listeners;
import com.endpoint.ghair.language.Language;
import com.endpoint.ghair.models.Slider_Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class MarketActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityMarketsBinding binding;
    private String lang;
    private Markets_Adapter markets_adapter;
    private List<Slider_Model.Data> dataList;


    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        markets_adapter=new Markets_Adapter(dataList,this,null);
        binding.recView.setLayoutManager(new GridLayoutManager(this,3));
        binding.recView.setAdapter(markets_adapter);
        setdtat();



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
