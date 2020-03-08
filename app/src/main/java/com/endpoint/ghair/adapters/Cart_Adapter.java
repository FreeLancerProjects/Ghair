package com.endpoint.ghair.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.endpoint.ghair.R;
import com.endpoint.ghair.activities_fragments.activity_cart.CartActivity;
import com.endpoint.ghair.databinding.CartRowBinding;
import com.endpoint.ghair.databinding.OrderRowBinding;
import com.endpoint.ghair.models.Add_Order_Model;
import com.endpoint.ghair.models.Slider_Model;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Cart_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Add_Order_Model.Details> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
private CartActivity cartActivity;
    public Cart_Adapter(List<Add_Order_Model.Details> orderlist, Context context) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

cartActivity=(CartActivity)context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        CartRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.cart_row, parent, false);
        return new EventHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        EventHolder eventHolder = (EventHolder) holder;
       eventHolder.binding.setAddordermodel(orderlist.get(position));
       eventHolder.binding.setLang(lang);
//        eventHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cartActivity.Complete();
//            }
//        });
        eventHolder.binding.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof  CartActivity){
                    cartActivity=(CartActivity)context;
                    cartActivity.removeitem(eventHolder.getLayoutPosition());
                }
            }
        });
        eventHolder.binding.imageIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof  CartActivity){
                    cartActivity=(CartActivity)context;
                    cartActivity.additem(eventHolder.getLayoutPosition());
                }
            }
        });
        eventHolder.binding.imageDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof  CartActivity){
                    cartActivity=(CartActivity)context;
                    cartActivity.minusitem(eventHolder.getLayoutPosition());
                }
            }
        });
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
        public CartRowBinding binding;

        public EventHolder(@NonNull CartRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
