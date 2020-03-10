package com.endpoint.ghair.activities_fragments.activity_home.fragments.fragment_profile.fragments;

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
import com.endpoint.ghair.adapters.MyServicesAdapter;
import com.endpoint.ghair.adapters.OrderAdapter;
import com.endpoint.ghair.databinding.FragmentOrdersBinding;
import com.endpoint.ghair.language.Language;
import com.endpoint.ghair.models.MyRequiredModel;
import com.endpoint.ghair.models.MyRequiredModel;
import com.endpoint.ghair.models.Slider_Model;
import com.endpoint.ghair.models.UserModel;
import com.endpoint.ghair.preferences.Preferences;
import com.endpoint.ghair.remote.Api;
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

public class FragmentRequired extends Fragment {
    private HomeActivity activity;
    private FragmentOrdersBinding binding;
    private Preferences preferences;
    private MyRequiredAdapter markets_adapter;
    private List<MyRequiredModel.Data> dataList;
    private UserModel userModel;
    private int type=0;
    private String lang;
    private boolean isLoading2 = false;
    private int current_page4=1;
    private LinearLayoutManager manager;
    public static FragmentRequired newInstance()
    {
        return new FragmentRequired();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orders,container,false);
        initView();
        if(userModel.getUser_type().equals("client")){
            getClientcurrentrequired();
        }
        else {
            getmarketcurrentrequired();
        }
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
lang = Paper.book().read("lang", Locale.getDefault().getLanguage());userModel=preferences.getUserData(activity);
        manager=new LinearLayoutManager(activity);
        binding.recView.setLayoutManager(manager);

        markets_adapter=new MyRequiredAdapter(dataList,activity);
        binding.recView.setAdapter(markets_adapter);
        binding.recView.setAdapter(markets_adapter);
        binding.tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.tvNoOrder.setVisibility(View.GONE);
                if(tab.getPosition()==0){
                    type=0;
                    if(userModel.getUser_type().equals("client")){
                        getClientcurrentrequired();
                    }
                    else {
                        getmarketcurrentrequired();
                    }
                }
                else {
                    type=1;
                    if(userModel.getUser_type().equals("client")){
                        getClientpriviuosrequired();
                    }
                    else {
                        getmarketpriviuosrequired();
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    int totalItems = markets_adapter.getItemCount();
                    int lastVisiblePos = manager.findLastCompletelyVisibleItemPosition();
                    if (totalItems > 5 && (totalItems - lastVisiblePos) == 1 && !isLoading2) {
                        isLoading2 = true;
                        dataList.add(null);
                        markets_adapter.notifyItemInserted(dataList.size() - 1);
                        int page = current_page4 + 1;
                        if(type==0){
                            if(userModel.getUser_type().equals("client")){
                                loadMoreclientcurrentrequired(page);
                            }
                            else {
                                loadMoremarketcurrentrequired(page);
                            }}
                        else {
                            if(userModel.getUser_type().equals("client")){
                                loadMoreclientprerequired(page);
                            }
                            else {
                                loadMoremarketprerequired(page);
                            }
                        }


                    }
                }
            }
        });



    }
    private void getClientpriviuosrequired() {
        dataList.clear();
        markets_adapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);
        current_page4=1;

        try {


            Api.getService(Tags.base_url)
                    .getclentpreviousrequired(1,"Bearer" + " " + userModel.getToken(),lang)
                    .enqueue(new Callback<MyRequiredModel>() {
                        @Override
                        public void onResponse(Call<MyRequiredModel> call, Response<MyRequiredModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                dataList.clear();
                                dataList.addAll(response.body().getData());
                                if (response.body().getData().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);

                                    binding.tvNoOrder.setVisibility(View.GONE);
                                    markets_adapter.notifyDataSetChanged();
                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    markets_adapter.notifyDataSetChanged();

                                    binding.tvNoOrder.setVisibility(View.VISIBLE);

                                }
                            } else {
                                markets_adapter.notifyDataSetChanged();

                                binding.tvNoOrder.setVisibility(View.VISIBLE);

                                //Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyRequiredModel> call, Throwable t) {
                            try {

                                binding.progBar.setVisibility(View.GONE);
                                binding.tvNoOrder.setVisibility(View.VISIBLE);
                                Toast.makeText(activity, getResources().getString(R.string.something), Toast.LENGTH_LONG).show();


                                Log.e("errorsssss", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e("errorssssssss", e.getMessage());

            binding.progBar.setVisibility(View.GONE);
            binding.tvNoOrder.setVisibility(View.VISIBLE);

        }
    }

    private void loadMoreclientprerequired(int page) {
        try {


            Api.getService(Tags.base_url)
                    .getclentpreviousrequired(page,"Bearer" + " " + userModel.getToken(),lang)
                    .enqueue(new Callback<MyRequiredModel>() {
                        @Override
                        public void onResponse(Call<MyRequiredModel> call, Response<MyRequiredModel> response) {
                            dataList.remove(dataList.size() - 1);
                            markets_adapter.notifyItemRemoved(dataList.size() - 1);
                            isLoading2 = false;
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                                dataList.addAll(response.body().getData());
                                // categories.addAll(response.body().getCategories());
                                current_page4 = response.body().getCurrent_page();
                                markets_adapter.notifyDataSetChanged();

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
                        public void onFailure(Call<MyRequiredModel> call, Throwable t) {
                            try {
                                dataList.remove(dataList.size() - 1);
                                markets_adapter.notifyItemRemoved(dataList.size() - 1);
                                isLoading2 = false;
                                // Toast.makeText(this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("errorsssss", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            dataList.remove(dataList.size() - 1);
            markets_adapter.notifyItemRemoved(dataList.size() - 1);
            isLoading2 = false;
        }
    }

    private void getClientcurrentrequired() {
        current_page4=1;

        dataList.clear();
        markets_adapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);
        try {


            Api.getService(Tags.base_url)
                    .getclentcurrentrequired(1,"Bearer" + " " + userModel.getToken(),lang)
                    .enqueue(new Callback<MyRequiredModel>() {
                        @Override
                        public void onResponse(Call<MyRequiredModel> call, Response<MyRequiredModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            Log.e("kkkkkk",response.code()+"");
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                dataList.clear();
                                dataList.addAll(response.body().getData());
                                if (response.body().getData().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);

                                    binding.tvNoOrder.setVisibility(View.GONE);
                                    markets_adapter.notifyDataSetChanged();
                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    markets_adapter.notifyDataSetChanged();

                                    binding.tvNoOrder.setVisibility(View.VISIBLE);

                                }
                            } else {
                                markets_adapter.notifyDataSetChanged();

                                binding.tvNoOrder.setVisibility(View.VISIBLE);
                                Log.e("Error_code", response.code()+"");
                                //Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyRequiredModel> call, Throwable t) {
                            try {

                                binding.progBar.setVisibility(View.GONE);
                                binding.tvNoOrder.setVisibility(View.VISIBLE);
                                Toast.makeText(activity, getResources().getString(R.string.something), Toast.LENGTH_LONG).show();


                                Log.e("errorsssss", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e("errorsss", e.getMessage());

            binding.progBar.setVisibility(View.GONE);
            binding.tvNoOrder.setVisibility(View.VISIBLE);

        }
    }

    private void loadMoreclientcurrentrequired(int page) {
        try {


            Api.getService(Tags.base_url)
                    .getclentcurrentrequired(page,"Bearer" + " " + userModel.getToken(),lang)
                    .enqueue(new Callback<MyRequiredModel>() {
                        @Override
                        public void onResponse(Call<MyRequiredModel> call, Response<MyRequiredModel> response) {
                            dataList.remove(dataList.size() - 1);
                            markets_adapter.notifyItemRemoved(dataList.size() - 1);
                            isLoading2 = false;
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                                dataList.addAll(response.body().getData());
                                // categories.addAll(response.body().getCategories());
                                current_page4 = response.body().getCurrent_page();
                                markets_adapter.notifyDataSetChanged();

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
                        public void onFailure(Call<MyRequiredModel> call, Throwable t) {
                            try {
                                dataList.remove(dataList.size() - 1);
                                markets_adapter.notifyItemRemoved(dataList.size() - 1);
                                isLoading2 = false;
                                // Toast.makeText(this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("errorsssss", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            dataList.remove(dataList.size() - 1);
            markets_adapter.notifyItemRemoved(dataList.size() - 1);
            isLoading2 = false;
        }
    }
    private void getmarketpriviuosrequired() {
        current_page4=1;

        dataList.clear();
        markets_adapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);
        try {


            Api.getService(Tags.base_url)
                    .getmarketpreviousrequired(1,"Bearer" + " " + userModel.getToken(),lang)
                    .enqueue(new Callback<MyRequiredModel>() {
                        @Override
                        public void onResponse(Call<MyRequiredModel> call, Response<MyRequiredModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                dataList.clear();
                                dataList.addAll(response.body().getData());
                                if (response.body().getData().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);

                                    binding.tvNoOrder.setVisibility(View.GONE);
                                    markets_adapter.notifyDataSetChanged();
                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    markets_adapter.notifyDataSetChanged();

                                    binding.tvNoOrder.setVisibility(View.VISIBLE);

                                }
                            } else {
                                markets_adapter.notifyDataSetChanged();

                                binding.tvNoOrder.setVisibility(View.VISIBLE);

                                //Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyRequiredModel> call, Throwable t) {
                            try {

                                binding.progBar.setVisibility(View.GONE);
                                binding.tvNoOrder.setVisibility(View.VISIBLE);
                                Toast.makeText(activity, getResources().getString(R.string.something), Toast.LENGTH_LONG).show();


                                Log.e("errorsssss", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e("errorsss", e.getMessage());

            binding.progBar.setVisibility(View.GONE);
            binding.tvNoOrder.setVisibility(View.VISIBLE);

        }
    }

    private void loadMoremarketprerequired(int page) {
        try {


            Api.getService(Tags.base_url)
                    .getmarketpreviousrequired(page,"Bearer" + " " + userModel.getToken(),lang)
                    .enqueue(new Callback<MyRequiredModel>() {
                        @Override
                        public void onResponse(Call<MyRequiredModel> call, Response<MyRequiredModel> response) {
                            dataList.remove(dataList.size() - 1);
                            markets_adapter.notifyItemRemoved(dataList.size() - 1);
                            isLoading2 = false;
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                                dataList.addAll(response.body().getData());
                                // categories.addAll(response.body().getCategories());
                                current_page4 = response.body().getCurrent_page();
                                markets_adapter.notifyDataSetChanged();

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
                        public void onFailure(Call<MyRequiredModel> call, Throwable t) {
                            try {
                                dataList.remove(dataList.size() - 1);
                                markets_adapter.notifyItemRemoved(dataList.size() - 1);
                                isLoading2 = false;
                                // Toast.makeText(this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("errorsssss", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            dataList.remove(dataList.size() - 1);
            markets_adapter.notifyItemRemoved(dataList.size() - 1);
            isLoading2 = false;
        }
    }

    private void getmarketcurrentrequired() {
        current_page4=1;
        dataList.clear();
        markets_adapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);
        try {


            Api.getService(Tags.base_url)
                    .gettmarketcurrentrequired(1,"Bearer" + " " + userModel.getToken(),lang)
                    .enqueue(new Callback<MyRequiredModel>() {
                        @Override
                        public void onResponse(Call<MyRequiredModel> call, Response<MyRequiredModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            Log.e("llllll",response.code()+"");
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                dataList.clear();
                                dataList.addAll(response.body().getData());
                                if (response.body().getData().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);

                                    binding.tvNoOrder.setVisibility(View.GONE);
                                    markets_adapter.notifyDataSetChanged();
                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    markets_adapter.notifyDataSetChanged();

                                    binding.tvNoOrder.setVisibility(View.VISIBLE);

                                }
                            } else {
                                markets_adapter.notifyDataSetChanged();

                                binding.tvNoOrder.setVisibility(View.VISIBLE);

                                //Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyRequiredModel> call, Throwable t) {
                            try {

                                binding.progBar.setVisibility(View.GONE);
                                binding.tvNoOrder.setVisibility(View.VISIBLE);
                                Toast.makeText(activity, getResources().getString(R.string.something), Toast.LENGTH_LONG).show();


                                Log.e("errorsssss", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e("errorsss", e.getMessage());

            binding.progBar.setVisibility(View.GONE);
            binding.tvNoOrder.setVisibility(View.VISIBLE);

        }
    }

    private void loadMoremarketcurrentrequired(int page) {
        try {


            Api.getService(Tags.base_url)
                    .gettmarketcurrentrequired(page,"Bearer" + " " + userModel.getToken(),lang)
                    .enqueue(new Callback<MyRequiredModel>() {
                        @Override
                        public void onResponse(Call<MyRequiredModel> call, Response<MyRequiredModel> response) {
                            dataList.remove(dataList.size() - 1);
                            markets_adapter.notifyItemRemoved(dataList.size() - 1);
                            isLoading2 = false;
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                                dataList.addAll(response.body().getData());
                                // categories.addAll(response.body().getCategories());
                                current_page4 = response.body().getCurrent_page();
                                markets_adapter.notifyDataSetChanged();

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
                        public void onFailure(Call<MyRequiredModel> call, Throwable t) {
                            try {
                                dataList.remove(dataList.size() - 1);
                                markets_adapter.notifyItemRemoved(dataList.size() - 1);
                                isLoading2 = false;
                                // Toast.makeText(this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("errorsssss", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            dataList.remove(dataList.size() - 1);
            markets_adapter.notifyItemRemoved(dataList.size() - 1);
            isLoading2 = false;
        }
    }



}
