package com.endpoint.ghair.activities_fragments.activity_market_profile;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.endpoint.ghair.R;
import com.endpoint.ghair.activities_fragments.activity_products.ProductsActivity;
import com.endpoint.ghair.adapters.Accessories_Adapter;
import com.endpoint.ghair.adapters.CategoryMarket_Adapter;
import com.endpoint.ghair.adapters.Products_Adapter;
import com.endpoint.ghair.adapters.SlidingImageMarket_Adapter;
import com.endpoint.ghair.databinding.ActivityMarketProfileBinding;
import com.endpoint.ghair.interfaces.Listeners;
import com.endpoint.ghair.language.Language;
import com.endpoint.ghair.models.MarketCatogryModel;
import com.endpoint.ghair.models.Product_Model;
import com.endpoint.ghair.models.Slider_Model;
import com.endpoint.ghair.models.UserModel;
import com.endpoint.ghair.remote.Api;
import com.endpoint.ghair.share.Common;
import com.endpoint.ghair.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarketProfileActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityMarketProfileBinding binding;
    private String lang;
    private Products_Adapter products_adapter;
    private List<Product_Model.Data> dataList;
    private CategoryMarket_Adapter categoryMarket_adapter;
    private List<MarketCatogryModel.Data> marketcatlist;
    private String search_id;
    private SlidingImageMarket_Adapter slidingImageMarket_adapter;

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
        getsingleads();
        getproducts();
        getCatogries();
        change_slide_image();

    }

    private int current_page = 0, NUM_PAGES;

    @SuppressLint("RestrictedApi")
    private void initView() {
        if (getIntent().getIntExtra("search", -1) != 0) {
            search_id = getIntent().getIntExtra("search", -1) + "";
        }
        dataList = new ArrayList<>();
        marketcatlist = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        products_adapter = new Products_Adapter(dataList, this);
        binding.recView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recView.setAdapter(products_adapter);
        categoryMarket_adapter = new CategoryMarket_Adapter(marketcatlist, this);
        binding.recservice.setLayoutManager(new LinearLayoutManager(this));
        binding.recservice.setAdapter(categoryMarket_adapter);
        binding.arrownext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MarketProfileActivity.this, ProductsActivity.class);
                intent.putExtra("search", search_id);
                startActivity(intent);
            }
        });
//        binding.arrownext3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(MarketProfileActivity.this, AccessoriesActivity.class);
//                startActivity(intent);
//            }
//        });
    }


    public void getsingleads() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        // rec_sent.setVisibility(View.GONE);
        try {


            Api.getService(Tags.base_url)
                    .get_singlemarket(search_id, lang)
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();

                            //  binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body() != null) {
                                //binding.coord1.scrollTo(0,0);
                                // setdata(response.body());
                                setdata(response.body());

                            } else {


                                //   Toast.makeText(AuctionDetialsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            try {

                                dialog.dismiss();

                                // Toast.makeText(AdsDetialsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

            dialog.dismiss();
        }
    }

    public void getproducts() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        // rec_sent.setVisibility(View.GONE);
        try {


            Api.getService(Tags.base_url)
                    .get_productss("All", "All", search_id, lang)
                    .enqueue(new Callback<Product_Model>() {
                        @Override
                        public void onResponse(Call<Product_Model> call, Response<Product_Model> response) {
                            dialog.dismiss();

                            //  binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                //binding.coord1.scrollTo(0,0);
                                // setdata(response.body());
                                dataList.addAll(response.body().getData());
                                if(dataList.size()>0){
                                    products_adapter.notifyDataSetChanged();

                                }
                                else {
                                    binding.llProducts.setVisibility(View.GONE);
                                    binding.llNoServices.setVisibility(View.VISIBLE);
                                }

                            } else {
                                binding.llProducts.setVisibility(View.GONE);
                                binding.llNoServices.setVisibility(View.VISIBLE);

                                //   Toast.makeText(AuctionDetialsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Product_Model> call, Throwable t) {
                            try {
                                binding.llProducts.setVisibility(View.GONE);
                                binding.llNoServices.setVisibility(View.VISIBLE);
                                dialog.dismiss();

                                // Toast.makeText(AdsDetialsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            binding.llProducts.setVisibility(View.GONE);
            binding.llNoServices.setVisibility(View.VISIBLE);
            dialog.dismiss();
        }
    }
    public void getCatogries() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        // rec_sent.setVisibility(View.GONE);
        try {


            Api.getService(Tags.base_url)
                    .get_Catogries( search_id, lang)
                    .enqueue(new Callback<MarketCatogryModel>() {
                        @Override
                        public void onResponse(Call<MarketCatogryModel> call, Response<MarketCatogryModel> response) {
                            dialog.dismiss();

                            //  binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                //binding.coord1.scrollTo(0,0);
                                // setdata(response.body());
                                marketcatlist.addAll(response.body().getData());
                                if(marketcatlist.size()>0){
                                categoryMarket_adapter.notifyDataSetChanged();}
                                else {
                                    binding.llNoCatogry.setVisibility(View.VISIBLE);
                                }

                            } else {
                                binding.llNoCatogry.setVisibility(View.VISIBLE);

                                //   Toast.makeText(AuctionDetialsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                binding.llNoCatogry.setVisibility(View.VISIBLE);
                                dialog.dismiss();

                                // Toast.makeText(AdsDetialsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            binding.llNoCatogry.setVisibility(View.VISIBLE);
            dialog.dismiss();
        }
    }

    private void setdata(UserModel body) {
        binding.setMarketmodel(body);
        if (body.getBanners() != null && body.getBanners().size() > 0) {
            binding.imBanner.setVisibility(View.GONE);
            NUM_PAGES = body.getBanners().size();
            slidingImageMarket_adapter = new SlidingImageMarket_Adapter(this, body.getBanners());
            binding.pager.setAdapter(slidingImageMarket_adapter);
        } else {
            binding.imBanner.setVisibility(View.VISIBLE);
        }

    }

    private void change_slide_image() {
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (current_page == NUM_PAGES) {
                    current_page = 0;
                }

                binding.pager.setCurrentItem(current_page++, true);

            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
    }


    @Override
    public void back() {
        finish();
    }

    public void showproducts(int id) {
        Intent intent = new Intent(MarketProfileActivity.this, ProductsActivity.class);
        intent.putExtra("search", search_id);
        intent.putExtra("searchcat", id+"");

        startActivity(intent);
    }
}
