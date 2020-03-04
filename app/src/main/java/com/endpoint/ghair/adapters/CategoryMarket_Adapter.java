package com.endpoint.ghair.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.endpoint.ghair.R;
import com.endpoint.ghair.activities_fragments.activity_home.fragments.Fragment_Main;
import com.endpoint.ghair.activities_fragments.activity_market_profile.MarketProfileActivity;
import com.endpoint.ghair.databinding.MarketCatRowBinding;
import com.endpoint.ghair.databinding.MostActiveRowBinding;
import com.endpoint.ghair.models.MarketCatogryModel;
import com.endpoint.ghair.models.Slider_Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class CategoryMarket_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MarketCatogryModel.Data> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
    private ProductsCat_Adapter productsCat_adapter;
    private MarketProfileActivity marketProfileActivity;
    public CategoryMarket_Adapter(List<MarketCatogryModel.Data> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
marketProfileActivity=(MarketProfileActivity)context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        MarketCatRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.market_cat_row, parent, false);
        return new EventHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        EventHolder eventHolder = (EventHolder) holder;
        eventHolder.binding.setCatogrymodel(orderlist.get(position));
        eventHolder.binding.setLang(lang);
        eventHolder.binding.recView1.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
        if(orderlist.get(position).getProducts()!=null&&orderlist.get(position).getProducts().size()>0){
            productsCat_adapter=new ProductsCat_Adapter(orderlist.get(position).getProducts(),context);
            eventHolder.binding.recView1.setAdapter(productsCat_adapter);
        }
        eventHolder.binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
marketProfileActivity.showproducts(orderlist.get(eventHolder.getLayoutPosition()).getId());
            }
        });
        if(position%2!=0){
            eventHolder.binding.tvTitle.setBackground(context.getResources().getDrawable(R.drawable.accessories));
        }
        else {
            eventHolder.binding.tvTitle.setBackground(context.getResources().getDrawable(R.drawable.spare_parts));

        }
//        eventHolder.itemView.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//        if(fragment instanceof  Fragment_Main){
//            fragment_main=(Fragment_Main)fragment;
//         //   fragment_main.showmarkets2();
//        }
//    }
//});
/*
if(i==position){
    if(i!=0) {
        if (((EventHolder) holder).binding.expandLayout.isExpanded()) {
            ((EventHolder) holder).binding.tvTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            ((EventHolder) holder).binding.recView.setLayoutParams(new FrameLayout.LayoutParams(0, 0));
            ((EventHolder) holder).binding.expandLayout.collapse(true);
            ((EventHolder) holder).binding.expandLayout.setVisibility(View.GONE);



        }
        else {

          //  ((EventHolder) holder).binding.tvTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            ((EventHolder) holder).binding.recView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
            ((EventHolder) holder).binding.expandLayout.setVisibility(View.VISIBLE);

           ((EventHolder) holder).binding.expandLayout.expand(true);
        }
    }
    else {
        eventHolder.binding.tvTitle.setBackground(activity.getResources().getDrawable(R.drawable.linear_bg_green));

        ((EventHolder) holder).binding.tvTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ((EventHolder) holder).binding.recView.setLayoutParams(new FrameLayout.LayoutParams(0, 0));

    }
}
if(i!=position) {
    eventHolder.binding.tvTitle.setBackground(activity.getResources().getDrawable(R.drawable.linear_bg_white));
    ((EventHolder) holder).binding.tvTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

    ((EventHolder) holder).binding.recView.setLayoutParams(new FrameLayout.LayoutParams(0, 0));
    ((EventHolder) holder).binding.expandLayout.collapse(true);


}*/

    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public MarketCatRowBinding binding;

        public EventHolder(@NonNull MarketCatRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
