package com.endpoint.ghair.activities_fragments.activity_home.fragments;

import android.content.Intent;
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
import com.endpoint.ghair.activities_fragments.activity_auction_detials.AuctionDetialsActivity;
import com.endpoint.ghair.adapters.Ouction_Adapter;
import com.endpoint.ghair.databinding.FragmentAuctionBinding;
import com.endpoint.ghair.language.Language;
import com.endpoint.ghair.models.Auction_Model;
import com.endpoint.ghair.models.Market_Model;
import com.endpoint.ghair.preferences.Preferences;
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

public class Fragment_Auction extends Fragment {

    private HomeActivity activity;
    private FragmentAuctionBinding binding;
    private Preferences preferences;
    private Ouction_Adapter auction_adapter;
    private List<Auction_Model.Data> dataList;
    private String lang;
    private boolean isLoading2 = false;
    private int current_page4=1;
    private LinearLayoutManager manager;

    public static Fragment_Auction newInstance() {
        return new Fragment_Auction();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_auction,container,false);
        initView();
getAuction();
        return binding.getRoot();
    }

    private void initView() {

        dataList=new ArrayList<>();

        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());preferences = Preferences.getInstance();
        auction_adapter =new Ouction_Adapter(dataList,activity,this);
        manager=new LinearLayoutManager(activity);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.input), PorterDuff.Mode.SRC_IN);

        binding.recView.setLayoutManager(manager);
        binding.recView.setAdapter(auction_adapter);
        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    int totalItems = auction_adapter.getItemCount();
                    int lastVisiblePos = manager.findLastCompletelyVisibleItemPosition();
                    if (totalItems > 5 && (totalItems - lastVisiblePos) == 1 && !isLoading2) {
                        isLoading2 = true;
                        dataList.add(null);
                        auction_adapter.notifyItemInserted(dataList.size() - 1);
                        int page = current_page4 + 1;
                        loadMoreauction(page);


                    }
                }
            }
        });



    }

    private void getAuction() {
        dataList.clear();
        auction_adapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);
        try {


            Api.getService(Tags.base_url)
                    .getAuctions(1,lang)
                    .enqueue(new Callback<Auction_Model>() {
                        @Override
                        public void onResponse(Call<Auction_Model> call, Response<Auction_Model> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                dataList.clear();
                                dataList.addAll(response.body().getData());
                                if (response.body().getData().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);

                                    binding.llNoNotification.setVisibility(View.GONE);
                                    auction_adapter.notifyDataSetChanged();
                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    auction_adapter.notifyDataSetChanged();

                                    binding.llNoNotification.setVisibility(View.VISIBLE);

                                }
                            } else {
                                auction_adapter.notifyDataSetChanged();

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
                        public void onFailure(Call<Auction_Model> call, Throwable t) {
                            try {

                                binding.progBar.setVisibility(View.GONE);
                                binding.llNoNotification.setVisibility(View.VISIBLE);
                                Toast.makeText(activity, getResources().getString(R.string.something), Toast.LENGTH_LONG).show();


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

    private void loadMoreauction(int page) {
        try {


            Api.getService(Tags.base_url)
                    .getAuctions(page,lang)
                    .enqueue(new Callback<Auction_Model>() {
                        @Override
                        public void onResponse(Call<Auction_Model> call, Response<Auction_Model> response) {
                            dataList.remove(dataList.size() - 1);
                            auction_adapter.notifyItemRemoved(dataList.size() - 1);
                            isLoading2 = false;
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                                dataList.addAll(response.body().getData());
                                // categories.addAll(response.body().getCategories());
                                current_page4 = response.body().getCurrent_page();
                                auction_adapter.notifyDataSetChanged();

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
                        public void onFailure(Call<Auction_Model> call, Throwable t) {
                            try {
                                dataList.remove(dataList.size() - 1);
                                auction_adapter.notifyItemRemoved(dataList.size() - 1);
                                isLoading2 = false;
                                // Toast.makeText(this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            dataList.remove(dataList.size() - 1);
            auction_adapter.notifyItemRemoved(dataList.size() - 1);
            isLoading2 = false;
        }
    }

    public void show(int id) {
        Intent intent=new Intent (activity, AuctionDetialsActivity.class);
        intent.putExtra("search",id);

        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        getAuction();
    }
}
