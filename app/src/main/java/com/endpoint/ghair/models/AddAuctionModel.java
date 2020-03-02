package com.endpoint.ghair.models;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.endpoint.ghair.R;

import java.io.Serializable;

public class AddAuctionModel extends BaseObservable implements Serializable {

    private String english_title;
    private String arabic_title;
    private String price;

    private String time;



    public ObservableField<String> error_english_title = new ObservableField<>();
    public ObservableField<String> error_arabic_title = new ObservableField<>();

    public ObservableField<String> error_price = new ObservableField<>();

    public ObservableField<String> error_time = new ObservableField<>();


    public AddAuctionModel() {
        this.english_title = "";
        this.arabic_title = "";
        this.price = "";
        this.time = "";


    }

    @Bindable
    public String getTime() {
        return time;
    }
   @Bindable
    public String getEnglish_title() {
        return english_title;
    }

    public void setEnglish_title(String english_title) {
        this.english_title = english_title;
        notifyPropertyChanged(com.endpoint.ghair.BR.english_title);

    }
    @Bindable
    public String getArabic_title() {
        return arabic_title;
    }

    public void setArabic_title(String arabic_title) {
        this.arabic_title = arabic_title;
        notifyPropertyChanged(com.endpoint.ghair.BR.arabic_title);

    }
    @Bindable
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
        notifyPropertyChanged(com.endpoint.ghair.BR.national);

    }







    public void setTime(String time) {
        this.time = time;
        notifyPropertyChanged(com.endpoint.ghair.BR.phone_code);

    }











    public boolean isDataValid(Context context)
    {
        if (!TextUtils.isEmpty(time)&&

                !TextUtils.isEmpty(english_title)&&
                !TextUtils.isEmpty(arabic_title)&&
                !TextUtils.isEmpty(price)


        )
        {
            error_arabic_title.set(null);
            error_english_title.set(null);
            error_price.set(null);
            error_time.set(null);


            return true;
        }else
        {
            if (english_title.isEmpty())
            {
                error_english_title.set(context.getString(R.string.field_req));
            }else
            {
                error_english_title.set(null);
            }
            if (arabic_title.isEmpty())
            {
                error_arabic_title.set(context.getString(R.string.field_req));
            }else
            {
                error_arabic_title.set(null);
            }
            if (price.isEmpty())
            {
                error_price.set(context.getString(R.string.field_req));
            }else
            {
                error_price.set(null);
            }


            if (time.isEmpty())
            {
                error_time.set(context.getString(R.string.field_req));
            }else
            {
                error_time.set(null);
            }






            return false;
        }
    }
}
