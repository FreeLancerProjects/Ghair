package com.endpoint.ghair.activities_fragments.activity_products;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.endpoint.ghair.R;
import com.endpoint.ghair.activities_fragments.activity_products_detials.ProductsDetialsActivity;
import com.endpoint.ghair.adapters.Accessories_Adapter;
import com.endpoint.ghair.adapters.Products_Adapter;
import com.endpoint.ghair.adapters.SpinnerBrandAdapter;
import com.endpoint.ghair.adapters.SpinnerCatogryAdapter;
import com.endpoint.ghair.databinding.ActivityProductsBinding;
import com.endpoint.ghair.interfaces.Listeners;
import com.endpoint.ghair.language.Language;
import com.endpoint.ghair.models.Brand_Model;
import com.endpoint.ghair.models.MarketCatogryModel;
import com.endpoint.ghair.models.Product_Model;
import com.endpoint.ghair.models.Slider_Model;
import com.endpoint.ghair.remote.Api;
import com.endpoint.ghair.share.Common;
import com.endpoint.ghair.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityProductsBinding binding;
    private String lang;
    private List<Brand_Model.Data> brandList;
    private SpinnerBrandAdapter spinnerBrandAdapter;
    private List<MarketCatogryModel.Data> maDataList;
    private SpinnerCatogryAdapter spinnerCatogryAdapter;
    private String brand = "All";
    private String search_id;
    private String cat = "All";
    private Products_Adapter products_adapter;
    private List<Product_Model.Data> dataList;
    private String name="";

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_products);
        initView();
        getBrands();
        getCatogries();
        getproducts();
    }

    @SuppressLint("RestrictedApi")
    private void initView() {
        if (getIntent().getStringExtra("search")!=null) {
            search_id = getIntent().getStringExtra("search");
        }
        if (getIntent().getStringExtra("searchcat")!=null) {
            cat = getIntent().getStringExtra("searchcat");
        }
        dataList = new ArrayList<>();
        brandList = new ArrayList<>();
        maDataList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        products_adapter = new Products_Adapter(dataList, this);
        binding.recView.setLayoutManager(new GridLayoutManager(this, 3));
        binding.recView.setAdapter(products_adapter);
        spinnerBrandAdapter = new SpinnerBrandAdapter(brandList, this);
        spinnerCatogryAdapter = new SpinnerCatogryAdapter(maDataList, this);
        binding.spBrand.setAdapter(spinnerBrandAdapter);
        binding.spCat.setAdapter(spinnerCatogryAdapter);
        binding.spBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {

                    brand = "All";
                } else {
                    brand = brandList.get(i).getId() + "";

                }
                getproducts();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {

                    cat = "All";
                } else {
                    cat = maDataList.get(i).getId() + "";
                    binding.tvTitle.setText(maDataList.get(i).getTitle());

                }
                getproducts();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = binding.edtprname.getText().toString();
              //  Log.e("kkkkkk",name);
                getproducts();
            }
        });

    }


    @Override
    public void back() {
        finish();
    }

    private void getBrands() {
        brandList.clear();
        spinnerBrandAdapter.notifyDataSetChanged();
        try {


            Api.getService(Tags.base_url)
                    .getBrands(1, lang)
                    .enqueue(new Callback<Brand_Model>() {
                        @Override
                        public void onResponse(Call<Brand_Model> call, Response<Brand_Model> response) {
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                brandList.clear();
                                brandList.add(new Brand_Model.Data(getResources().getString(R.string.ch_brand)));
                                brandList.addAll(response.body().getData());
                                if (response.body().getData().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);

                                    spinnerBrandAdapter.notifyDataSetChanged();
                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    spinnerBrandAdapter.notifyDataSetChanged();


                                }
                            } else {
                                spinnerBrandAdapter.notifyDataSetChanged();


                                //Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Brand_Model> call, Throwable t) {
                            try {


                                Toast.makeText(ProductsActivity.this, getResources().getString(R.string.something), Toast.LENGTH_LONG).show();


                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e("errorsss", e.getMessage());


        }
    }

    private void getCatogries() {
        maDataList.clear();
        spinnerCatogryAdapter.notifyDataSetChanged();
        try {


            Api.getService(Tags.base_url)
                    .get_catogries(lang)
                    .enqueue(new Callback<MarketCatogryModel>() {
                        @Override
                        public void onResponse(Call<MarketCatogryModel> call, Response<MarketCatogryModel> response) {
                            Log.e("lfkkfkkfk", response.code() + "");
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                maDataList.clear();
                                maDataList.add(new MarketCatogryModel.Data(getResources().getString(R.string.ch_cat)));
                                maDataList.addAll(response.body().getData());
                                if (response.body().getData().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);

                                    spinnerCatogryAdapter.notifyDataSetChanged();
                                    applydata();
                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    spinnerCatogryAdapter.notifyDataSetChanged();


                                }
                            } else {
                                spinnerCatogryAdapter.notifyDataSetChanged();


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


                                Toast.makeText(ProductsActivity.this, getResources().getString(R.string.something), Toast.LENGTH_LONG).show();


                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e("errorsss", e.getMessage());


        }
    }

    private void applydata() {
        if(cat!=null){
            for(int i=0;i<maDataList.size();i++){
                if(cat.equals(maDataList.get(i).getId()+"")){
                    binding.spCat.setSelection(i);
                    binding.tvTitle.setText(maDataList.get(i).getTitle());
                    break;
                }
            }
        }
    }

    public void getproducts() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);
dataList.clear();
products_adapter.notifyDataSetChanged();
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        // rec_sent.setVisibility(View.GONE);
        try {
Log.e("jjjjj",brand+" "+cat+" "+search_id+" "+name);

            Api.getService(Tags.base_url)
                    .get_productss(brand, cat, search_id, name, lang)
                    .enqueue(new Callback<Product_Model>() {
                        @Override
                        public void onResponse(Call<Product_Model> call, Response<Product_Model> response) {
                            dialog.dismiss();

                            //  binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                //binding.coord1.scrollTo(0,0);
                                // setdata(response.body());
                                dataList.addAll(response.body().getData());
                                if (dataList.size() > 0) {
                                    products_adapter.notifyDataSetChanged();

                                } else {
                                    binding.llNoNotification.setVisibility(View.VISIBLE);
                                }

                            } else {
                                binding.llNoNotification.setVisibility(View.VISIBLE);

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
                                binding.llNoNotification.setVisibility(View.VISIBLE);
                                dialog.dismiss();

                                // Toast.makeText(AdsDetialsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            binding.llNoNotification.setVisibility(View.VISIBLE);
            dialog.dismiss();
        }
    }

    public void showdetials(int id) {
        Intent intent = new Intent(ProductsActivity.this, ProductsDetialsActivity.class);
        intent.putExtra("search",id);

        startActivity(intent);
    }
}
