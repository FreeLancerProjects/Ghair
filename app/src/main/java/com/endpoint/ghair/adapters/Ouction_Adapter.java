package com.endpoint.ghair.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.endpoint.ghair.R;
import com.endpoint.ghair.activities_fragments.activity_home.fragments.Fragment_Auction;
import com.endpoint.ghair.databinding.AuctionRowBinding;
import com.endpoint.ghair.databinding.LoadMoreBinding;
import com.endpoint.ghair.databinding.MostActiveRowBinding;
import com.endpoint.ghair.models.Auction_Model;
import com.endpoint.ghair.models.Market_Model;
import com.endpoint.ghair.models.Slider_Model;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Ouction_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Auction_Model.Data> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
Fragment_Auction fragment_auction;
    private final int ITEM_DATA = 1;
    private final int LOAD = 2;
    public Ouction_Adapter(List<Auction_Model.Data> orderlist, Context context, Fragment fragment) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        fragment_auction=(Fragment_Auction)fragment;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==ITEM_DATA) {

            AuctionRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.auction_row, parent, false);
            return new EventHolder(binding);
        }
        else
        {
            LoadMoreBinding binding = DataBindingUtil.inflate(inflater, R.layout.load_more,parent,false);
            return new LoadHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MostActiveMarkets_Adapter.EventHolder) {

            EventHolder eventHolder = (EventHolder) holder;
            ((EventHolder) holder).binding.setAuctionmodel(orderlist.get(position));
            if(orderlist.get(position).getAuction_image()!=null&&orderlist.get(position).getAuction_image().size()>0){
                ((EventHolder) holder).binding.image.setVisibility(View.GONE);
                SlidingImageAuction_Adapter slidingImageAuction_adapter=new SlidingImageAuction_Adapter(context,orderlist.get(position));
            ((EventHolder) holder).binding.pager.setAdapter(slidingImageAuction_adapter);
            }
            else {
                ((EventHolder) holder).binding.image.setVisibility(View.VISIBLE);
            }
            eventHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment_auction.show();
                }
            });

        }
        else
        {
            LoadHolder loadHolder = (LoadHolder) holder;
            loadHolder.binding.progBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class EventHolder extends RecyclerView.ViewHolder {
        public AuctionRowBinding binding;

        public EventHolder(@NonNull AuctionRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
    public class LoadHolder extends RecyclerView.ViewHolder {
        private LoadMoreBinding binding;
        public LoadHolder(@NonNull LoadMoreBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }

    }

    @Override
    public int getItemViewType(int position) {
        Auction_Model.Data order_Model = orderlist.get(position);
        if (order_Model!=null)
        {
            return ITEM_DATA;
        }else
        {
            return LOAD;
        }

    }


}
