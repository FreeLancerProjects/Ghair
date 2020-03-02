package com.endpoint.ghair.adapters;


import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

import com.endpoint.ghair.R;
import com.endpoint.ghair.activities_fragments.activity_home.HomeActivity;
import com.endpoint.ghair.databinding.SliderBinding;
import com.endpoint.ghair.models.Auction_Model;
import com.endpoint.ghair.models.Slider_Model;
import com.endpoint.ghair.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class SlidingImageAuction_Adapter extends PagerAdapter {


   Auction_Model.Data auction_model;
    private LayoutInflater inflater;
     Context context;
private List<String> IMAGES;
    public SlidingImageAuction_Adapter(Context context, Auction_Model.Data auction_model) {
        this.context = context;
        this.auction_model=auction_model;
        inflater = LayoutInflater.from(context);

        IMAGES=auction_model.getAuction_image();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        SliderBinding rowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.slider,view,false);
        Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+IMAGES.get(position))).fit().into(rowBinding.image);


        view.addView(rowBinding.getRoot());
        return rowBinding.getRoot();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

}
