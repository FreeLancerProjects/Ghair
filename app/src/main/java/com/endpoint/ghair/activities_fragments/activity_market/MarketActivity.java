package com.endpoint.ghair.activities_fragments.activity_market;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.endpoint.ghair.R;
import com.endpoint.ghair.activities_fragments.activity_market_profile.MarketProfileActivity;
import com.endpoint.ghair.adapters.Markets_Adapter;
import com.endpoint.ghair.adapters.MostActiveMarkets_Adapter;
import com.endpoint.ghair.databinding.ActivityMarketsBinding;
import com.endpoint.ghair.interfaces.Listeners;
import com.endpoint.ghair.language.Language;
import com.endpoint.ghair.models.Market_Model;
import com.endpoint.ghair.remote.Api;
import com.endpoint.ghair.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarketActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityMarketsBinding binding;
    private String lang;
    private Markets_Adapter mostActiveMarkets_adapter;
    private List<Market_Model.Data> dataList;

    private String search_brand, search_catogry;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_markets);
        initView();
        getMArkets();


    }

    @SuppressLint("RestrictedApi")
    private void initView() {
        if(getIntent().getIntExtra("searchcat",-1)!=0){
            search_catogry=getIntent().getIntExtra("searchcat",-1)+"";
            search_brand=getIntent().getStringExtra("searchbrand");
        }
        dataList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());binding.setLang(lang);
        binding.setBackListener(this);
        mostActiveMarkets_adapter = new Markets_Adapter(dataList, this, null);
        binding.recView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recView.setAdapter(mostActiveMarkets_adapter);
        // setdtat();


    }

    private void getMArkets() {
        dataList.clear();
        mostActiveMarkets_adapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);
        try {


            Api.getService(Tags.base_url)
                    .get_Marketbycatogriesandbrand(search_brand, search_catogry, lang)
                    .enqueue(new Callback<Market_Model>() {
                        @Override
                        public void onResponse(Call<Market_Model> call, Response<Market_Model> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                dataList.clear();
                                dataList.addAll(response.body().getData());
                                if (response.body().getData().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);

                                    binding.llNoNotification.setVisibility(View.GONE);
                                    mostActiveMarkets_adapter.notifyDataSetChanged();
                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    mostActiveMarkets_adapter.notifyDataSetChanged();

                                    binding.llNoNotification.setVisibility(View.VISIBLE);

                                }
                            } else {
                                mostActiveMarkets_adapter.notifyDataSetChanged();

                                binding.llNoNotification.setVisibility(View.VISIBLE);

                                //Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Market_Model> call, Throwable t) {
                            try {

                                binding.progBar.setVisibility(View.GONE);
                                binding.llNoNotification.setVisibility(View.VISIBLE);
                                Toast.makeText(MarketActivity.this, getResources().getString(R.string.something), Toast.LENGTH_LONG).show();


                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e("errorsss", e.getMessage());

            binding.progBar.setVisibility(View.GONE);
            binding.llNoNotification.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void back() {
        finish();
    }

    public void showProfile(int id) {
        Intent intent = new Intent(MarketActivity.this, MarketProfileActivity.class);
        intent.putExtra("search", id);

        startActivity(intent);
    }
}
