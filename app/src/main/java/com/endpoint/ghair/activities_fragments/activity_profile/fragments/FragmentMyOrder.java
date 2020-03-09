package com.endpoint.ghair.activities_fragments.activity_profile.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.endpoint.ghair.R;
import com.endpoint.ghair.activities_fragments.activity_home.HomeActivity;
import com.endpoint.ghair.activities_fragments.activity_profile.ProfileActivity;
import com.endpoint.ghair.adapters.OrderAdapter;
import com.endpoint.ghair.databinding.FragmentOrdersBinding;
import com.endpoint.ghair.models.Slider_Model;
import com.endpoint.ghair.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;

public class FragmentMyOrder extends Fragment {
    private ProfileActivity activity;
    private FragmentOrdersBinding binding;
    private Preferences preferences;
    private OrderAdapter markets_adapter;
    private List<Slider_Model.Data> dataList;


    public static FragmentMyOrder newInstance()
    {
        return new FragmentMyOrder();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orders,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        dataList=new ArrayList<>();

        preferences = Preferences.getInstance();

        activity = (ProfileActivity) getActivity();
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.progBar.setVisibility(View.GONE);


      //  markets_adapter=new OrderAdapter(dataList,activity);
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


}
