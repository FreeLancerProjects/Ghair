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
import com.endpoint.ghair.activities_fragments.activity_sign_in.fragments.FragmentSignUpAsMarketr;
import com.endpoint.ghair.databinding.LoadMoreBinding;
import com.endpoint.ghair.databinding.ServiceRowBinding;
import com.endpoint.ghair.databinding.ServicesRowBinding;
import com.endpoint.ghair.models.Service_Model;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class ChooserService_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int LOAD = 2;
    private List<Service_Model.Data> orderlist;
    private Context context;
    private LayoutInflater inflater;
    private String lang;
private FragmentSignUpAsMarketr fragmentSignUpAsMarketr;
private Fragment fragment;
    public ChooserService_Adapter(List<Service_Model.Data> orderlist, Context context, Fragment fragment) {
        this.orderlist = orderlist;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
this.fragment=fragment;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==ITEM_DATA)
        {

        ServiceRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.service_row, parent, false);
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
            eventHolder.binding.setServicemodel(orderlist.get(position));
            eventHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fragment instanceof FragmentSignUpAsMarketr) {
                        fragmentSignUpAsMarketr = (FragmentSignUpAsMarketr) fragment;
                        fragmentSignUpAsMarketr.deletitem(holder.getLayoutPosition());
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
        public ServiceRowBinding binding;

        public EventHolder(@NonNull ServiceRowBinding binding) {
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
       Service_Model.Data order_Model = orderlist.get(position);
        if (order_Model!=null)
        {
            return ITEM_DATA;
        }else
        {
            return LOAD;
        }

    }

}
