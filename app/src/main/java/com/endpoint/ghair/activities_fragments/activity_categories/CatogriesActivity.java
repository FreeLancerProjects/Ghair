package com.endpoint.ghair.activities_fragments.activity_categories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.endpoint.ghair.R;
import com.endpoint.ghair.activities_fragments.activity_market.MarketActivity;
import com.endpoint.ghair.activities_fragments.activity_market_profile.MarketProfileActivity;
import com.endpoint.ghair.adapters.Category_Adapter;
import com.endpoint.ghair.databinding.ActivityCatogriesBinding;
import com.endpoint.ghair.interfaces.Listeners;
import com.endpoint.ghair.language.Language;
import com.endpoint.ghair.models.MarketCatogryModel;
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

public class CatogriesActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityCatogriesBinding binding;
    private String lang;
    private Category_Adapter category_adapter;
    private List<MarketCatogryModel.Data> dataList;
    private String search_id;


    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_catogries);
        initView();

getCatogries();

    }

    @SuppressLint("RestrictedApi")
    private void initView() {
        if(getIntent().getIntExtra("search",-1)!=0){
            search_id=getIntent().getIntExtra("search",-1)+"";
        }
        dataList=new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        category_adapter =new Category_Adapter(dataList,this);
        binding.recView.setLayoutManager(new GridLayoutManager(this,2));
        binding.recView.setAdapter(category_adapter);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        // setdtat();



    }

    private void getCatogries() {
        dataList.clear();
        category_adapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);
        try {


            Api.getService(Tags.base_url)
                    .get_catogriesbybrand(search_id,lang)
                    .enqueue(new Callback<MarketCatogryModel>() {
                        @Override
                        public void onResponse(Call<MarketCatogryModel> call, Response<MarketCatogryModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            Log.e("resddd",response.code()+"");
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                dataList.clear();
                                dataList.addAll(response.body().getData());
                                if (response.body().getData().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);

                                    binding.llNoNotification.setVisibility(View.GONE);
                                    category_adapter.notifyDataSetChanged();
                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    category_adapter.notifyDataSetChanged();

                                    binding.llNoNotification.setVisibility(View.VISIBLE);

                                }
                            } else {
                                category_adapter.notifyDataSetChanged();

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
                        public void onFailure(Call<MarketCatogryModel> call, Throwable t) {
                            try {

                                binding.progBar.setVisibility(View.GONE);
                                binding.llNoNotification.setVisibility(View.VISIBLE);
                                Toast.makeText(CatogriesActivity.this, getResources().getString(R.string.something), Toast.LENGTH_LONG).show();


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
    public void showProfile(int id) {
        Intent intent = new Intent(CatogriesActivity.this, MarketActivity.class);
        intent.putExtra("searchbrand", search_id);
        intent.putExtra("searchcat", id);

        startActivity(intent);
    }
    @Override
    public void back() {
        finish();
    }


}
