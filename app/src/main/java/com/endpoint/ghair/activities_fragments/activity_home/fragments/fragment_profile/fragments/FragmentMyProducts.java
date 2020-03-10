package com.endpoint.ghair.activities_fragments.activity_home.fragments.fragment_profile.fragments;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.endpoint.ghair.R;
import com.endpoint.ghair.activities_fragments.activity_home.HomeActivity;
import com.endpoint.ghair.adapters.MyRequiredAdapter;
import com.endpoint.ghair.adapters.Products_Adapter;
import com.endpoint.ghair.databinding.FragmentOrdersBinding;
import com.endpoint.ghair.models.Product_Model;
import com.endpoint.ghair.models.Product_Model;
import com.endpoint.ghair.models.UserModel;
import com.endpoint.ghair.preferences.Preferences;
import com.endpoint.ghair.remote.Api;
import com.endpoint.ghair.share.Common;
import com.endpoint.ghair.tags.Tags;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentMyProducts extends Fragment {
    private HomeActivity activity;
    private FragmentOrdersBinding binding;
    private Preferences preferences;
    private Products_Adapter products_adapter;
    private List<Product_Model.Data> dataList;
    private UserModel userModel;
    private int type=0;
    private String lang;
    private boolean isLoading2 = false;
    private int current_page4=1;
    private LinearLayoutManager manager;
    public static FragmentMyProducts newInstance()
    {
        return new FragmentMyProducts();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orders,container,false);
        initView();
getproducts();
        return binding.getRoot();
    }

    private void initView() {
        binding.tab1.setVisibility(View.GONE);

        dataList=new ArrayList<>();
        preferences = Preferences.getInstance();
        activity = (HomeActivity) getActivity();
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.input), PorterDuff.Mode.SRC_IN);
        binding.progBar.setVisibility(View.GONE);

       // markets_adapter=new OrderAdapter(dataList,activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        userModel=preferences.getUserData(activity);
        manager=new LinearLayoutManager(activity);
        binding.recView.setLayoutManager(manager);

        products_adapter=new Products_Adapter(dataList,activity);
        binding.recView.setAdapter(products_adapter);
        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    int totalItems = products_adapter.getItemCount();
                    int lastVisiblePos = manager.findLastCompletelyVisibleItemPosition();
                    if (totalItems > 5 && (totalItems - lastVisiblePos) == 1 && !isLoading2) {
                        isLoading2 = true;
                        dataList.add(null);
                        products_adapter.notifyItemInserted(dataList.size() - 1);
                        int page = current_page4 + 1;
              loadMoremarketcurrentrequired(page);
                        }


                    
                }
            }
        });



    }


    public void getproducts() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);
        dataList.clear();
        products_adapter.notifyDataSetChanged();
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        // rec_sent.setVisibility(View.GONE);
        try {

            Api.getService(Tags.base_url)
                    .get_myproductss(1,"Bearer" + " " + userModel.getToken(),lang)
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
                                    binding.tvNoOrder.setVisibility(View.VISIBLE);
                                }

                            } else {
                                binding.tvNoOrder.setVisibility(View.VISIBLE);

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
                                binding.tvNoOrder.setVisibility(View.VISIBLE);
                                dialog.dismiss();

                                // Toast.makeText(AdsDetialsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            binding.tvNoOrder.setVisibility(View.VISIBLE);
            dialog.dismiss();
        }
    }

    private void loadMoremarketcurrentrequired(int page) {
        try {


            Api.getService(Tags.base_url)
                    .get_myproductss(page,"Bearer" + " " + userModel.getToken(),lang)
                    .enqueue(new Callback<Product_Model>() {
                        @Override
                        public void onResponse(Call<Product_Model> call, Response<Product_Model> response) {
                            dataList.remove(dataList.size() - 1);
                            products_adapter.notifyItemRemoved(dataList.size() - 1);
                            isLoading2 = false;
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                                dataList.addAll(response.body().getData());
                                // categories.addAll(response.body().getCategories());
                                current_page4 = response.body().getCurrent_page();
                                products_adapter.notifyDataSetChanged();

                            } else {
                                //Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                dataList.remove(dataList.size() - 1);
                                products_adapter.notifyItemRemoved(dataList.size() - 1);
                                isLoading2 = false;
                                // Toast.makeText(this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("errorsssss", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            dataList.remove(dataList.size() - 1);
            products_adapter.notifyItemRemoved(dataList.size() - 1);
            isLoading2 = false;
        }
    }



}
