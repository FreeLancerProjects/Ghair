package com.endpoint.ghair.activities_fragments.activity_home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.endpoint.ghair.R;
import com.endpoint.ghair.activities_fragments.activity_home.HomeActivity;
import com.endpoint.ghair.activities_fragments.activity_auction_detials.AuctionDetialsActivity;
import com.endpoint.ghair.adapters.Ouction_Adapter;
import com.endpoint.ghair.databinding.FragmentAuctionBinding;
import com.endpoint.ghair.models.Slider_Model;
import com.endpoint.ghair.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Auction extends Fragment {

    private HomeActivity activity;
    private FragmentAuctionBinding binding;
    private Preferences preferences;
    private Ouction_Adapter category_adapter;
    private List<Slider_Model.Data> dataList;


    public static Fragment_Auction newInstance() {
        return new Fragment_Auction();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_auction,container,false);
        initView();

        return binding.getRoot();
    }

    private void initView() {
        dataList=new ArrayList<>();

        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        category_adapter=new Ouction_Adapter(dataList,activity,this);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recView.setAdapter(category_adapter);
        setdtat();



    }

    private void setdtat() {
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());


        category_adapter.notifyDataSetChanged();
    }


    public void show() {
        Intent intent=new Intent (activity, AuctionDetialsActivity.class);
        startActivity(intent);
    }
}
