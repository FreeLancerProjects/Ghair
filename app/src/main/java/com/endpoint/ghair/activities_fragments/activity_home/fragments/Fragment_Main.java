package com.endpoint.ghair.activities_fragments.activity_home.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.endpoint.ghair.adapters.Category_Adapter;
import com.endpoint.ghair.adapters.SlidingImage_Adapter;
import com.endpoint.ghair.databinding.FragmnetMainBinding;
import com.endpoint.ghair.models.Slider_Model;
import com.endpoint.ghair.preferences.Preferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Main extends Fragment {
    private static Dialog dialog;
    private HomeActivity activity;
    private FragmnetMainBinding binding;
    private LinearLayoutManager manager, manager2;
    private Preferences preferences;
    private SlidingImage_Adapter slidingImage__adapter;
    private Category_Adapter category_adapter;

private List<Slider_Model.Data> dataList;
    public static Fragment_Main newInstance() {
        return new Fragment_Main();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragmnet_main, container, false);
        initView();


        return binding.getRoot();
    }



    private void initView() {
dataList=new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        Paper.init(activity);
        category_adapter=new Category_Adapter(dataList,activity,this);
        binding.recBestseler.setLayoutManager(new LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false));
        binding.recDeparment.setLayoutManager(new LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false));
binding.recDeparment.setAdapter(category_adapter);
binding.recBestseler.setAdapter(category_adapter);
        setdtat();



    }

    private void setdtat() {
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());

        slidingImage__adapter = new SlidingImage_Adapter(activity, dataList);
        binding.pager.setAdapter(slidingImage__adapter);
        binding.pager2.setAdapter(slidingImage__adapter);
        category_adapter.notifyDataSetChanged();
    }


}
