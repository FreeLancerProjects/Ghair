package com.endpoint.ghair.activities_fragments.activity_products;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.endpoint.ghair.R;
import com.endpoint.ghair.adapters.Accessories_Adapter;
import com.endpoint.ghair.databinding.ActivityAccessoriesBinding;
import com.endpoint.ghair.databinding.ActivityProductsBinding;
import com.endpoint.ghair.interfaces.Listeners;
import com.endpoint.ghair.language.Language;
import com.endpoint.ghair.models.Slider_Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class ProductsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityProductsBinding binding;
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
        binding = DataBindingUtil.setContentView(this,R.layout.activity_products);
        initView();



    }

    @SuppressLint("RestrictedApi")
    private void initView() {
        dataList=new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        markets_adapter=new Accessories_Adapter(dataList,this);
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
