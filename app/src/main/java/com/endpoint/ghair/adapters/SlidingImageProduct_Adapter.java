package com.endpoint.ghair.adapters;


import android.content.Context;
import android.net.Uri;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

import com.endpoint.ghair.R;
import com.endpoint.ghair.databinding.SliderBinding;
import com.endpoint.ghair.models.Auction_Model;
import com.endpoint.ghair.models.SingleProductModel;
import com.endpoint.ghair.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;


public class SlidingImageProduct_Adapter extends PagerAdapter {


    private LayoutInflater inflater;
     Context context;
private List<SingleProductModel.Product_Images> IMAGES;
    public SlidingImageProduct_Adapter(Context context,  List<SingleProductModel.Product_Images> IMAGES) {
        this.context = context;
        inflater = LayoutInflater.from(context);

        this.IMAGES=IMAGES;
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
        Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+IMAGES.get(position))).placeholder(R.drawable.shiop).fit().into(rowBinding.image);


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
