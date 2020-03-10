package com.endpoint.ghair.general_ui_method;

import android.net.Uri;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;


import com.endpoint.ghair.R;
import com.endpoint.ghair.share.TimeAgo;
import com.endpoint.ghair.tags.Tags;
import com.github.siyamed.shapeimageview.HexagonImageView;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class GeneralMethod {

    @BindingAdapter("error")
    public static void errorValidation(View view, String error) {
        if (view instanceof EditText) {
            EditText ed = (EditText) view;
            ed.setError(error);
        } else if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setError(error);


        }
    }
    @BindingAdapter({"date"})
    public static void displayDate (TextView textView,long date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd/MMM", Locale.ENGLISH);
        String m_date = dateFormat.format(new Date(date*1000));

        textView.setText(String.format("%s",m_date));

    }
    @BindingAdapter("datetime")
    public static void convertToNotDate(TextView textView,long date)
    {
        long d = date*1000;
        String n_date = TimeAgo.getTimeAgo(d,textView.getContext());
        textView.setText(n_date);

    }
    @BindingAdapter("url")
    public static void imageUrl(RoundedImageView imageView,String url)
    {
        Picasso.with(imageView.getContext()).load(Uri.parse(url)).fit().into(imageView);

    }
    @BindingAdapter("image")
    public static void displayImage(View view,String endPoint)
    {
        if (endPoint!=null&&!endPoint.isEmpty())
        {
            if (view instanceof CircleImageView)
            {
                CircleImageView circleImageView = (CircleImageView) view;

                Picasso.with(view.getContext()).load(Uri.parse(Tags.IMAGE_URL)+endPoint).fit().into(circleImageView);
            }else if (view instanceof HexagonImageView)
            {
                HexagonImageView roundedImageView = (HexagonImageView) view;
                Picasso.with(view.getContext()).load(Uri.parse(Tags.IMAGE_URL)+endPoint).fit().into(roundedImageView);

            }
            else  if (view instanceof RoundedImageView)
            {
                RoundedImageView roundedImageView = (RoundedImageView) view;
                Picasso.with(view.getContext()).load(Uri.parse(Tags.IMAGE_URL)+endPoint).fit().into(roundedImageView);

            }
            else if (view instanceof ImageView)
            {
                ImageView imageView = (ImageView) view;
                Picasso.with(view.getContext()).load(Uri.parse(Tags.IMAGE_URL)+endPoint).fit().into(imageView);

            }
        }
    }









}
