package com.endpoint.ghair.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.endpoint.ghair.R;
import com.endpoint.ghair.activities_fragments.activity_addauction.AddProductActivity;
import com.endpoint.ghair.databinding.ImageRowBinding;

import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.MyHolder> {

    private List<Uri> urlList;
    private Context context;
    private AddProductActivity activity;
    public ImagesAdapter(List<Uri> urlList, Context context) {
        this.urlList = urlList;
        this.context = context;



    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageRowBinding bankRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.image_row,parent,false);
        return new MyHolder(bankRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {

        Uri url = urlList.get(position);

        holder.imageRowBinding.setUrl(url.toString());

        holder.imageRowBinding.imageDelete.setOnClickListener(view -> {
            if(context instanceof  AddProductActivity){
                activity=(AddProductActivity)context;
                    activity.deleteImage(holder.getAdapterPosition());}


                }
        );


    }

    @Override
    public int getItemCount() {
        return urlList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ImageRowBinding imageRowBinding;

        public MyHolder(ImageRowBinding imageRowBinding) {
            super(imageRowBinding.getRoot());
            this.imageRowBinding = imageRowBinding;



        }


    }
}
