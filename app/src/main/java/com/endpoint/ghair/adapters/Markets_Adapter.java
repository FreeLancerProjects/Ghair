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
import com.endpoint.ghair.activities_fragments.activity_home.fragments.Fragment_Main;
import com.endpoint.ghair.activities_fragments.activity_market.MarketActivity;
import com.endpoint.ghair.databinding.LoadMoreBinding;
import com.endpoint.ghair.databinding.MarketRowBinding;
import com.endpoint.ghair.databinding.MostActiveRowBinding;
import com.endpoint.ghair.language.Language;
import com.endpoint.ghair.models.Market_Model;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Markets_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int LOAD = 2;
    private List<Market_Model.Data> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
private Fragment_Main fragment_main;
private Fragment fragment;
private MarketActivity marketActivity;
    public Markets_Adapter(List<Market_Model.Data> orderlist, Context context, Fragment fragment) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
 lang = Paper.book().read("lang", Locale.getDefault().getLanguage());this.fragment=fragment;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==ITEM_DATA)
        {

        MarketRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.market_row, parent, false);
        return new EventHolder(binding);

    }else
    {
        LoadMoreBinding binding = DataBindingUtil.inflate(inflater, R.layout.load_more,parent,false);
        return new LoadHolder(binding);
    }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EventHolder) {
            EventHolder eventHolder = (EventHolder) holder;
            eventHolder.binding.setMarketmodel(orderlist.get(position));
            eventHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fragment instanceof Fragment_Main) {
                        fragment_main = (Fragment_Main) fragment;
fragment_main.showProfile(orderlist.get(eventHolder.getLayoutPosition()).getId());
                    }
                    else {
                        if(context instanceof MarketActivity){
                            marketActivity=(MarketActivity)context;
                            marketActivity.showProfile(orderlist.get(eventHolder.getLayoutPosition()).getId());
                        }
                    }
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
        public MarketRowBinding binding;

        public EventHolder(@NonNull MarketRowBinding binding) {
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
       Market_Model.Data order_Model = orderlist.get(position);
        if (order_Model!=null)
        {
            return ITEM_DATA;
        }else
        {
            return LOAD;
        }

    }

}
