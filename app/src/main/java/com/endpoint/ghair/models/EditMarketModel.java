package com.endpoint.ghair.models;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.endpoint.ghair.R;

import java.io.Serializable;

public class EditMarketModel extends BaseObservable implements Serializable {


    private String english_title;
    private String arabic_title;

    private String city_id;
    private String longitude;
    private String latitude;
    private String address;


    public ObservableField<String> error_english_title = new ObservableField<>();
    public ObservableField<String> error_arabic_title = new ObservableField<>();


    public ObservableField<String> address_error = new ObservableField<>();



    public EditMarketModel() {
        this.english_title = "";
        this.arabic_title = "";

        this.city_id = "";
        this.longitude = "";
        this.latitude = "";
        this.address = "";
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




    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(com.endpoint.ghair.BR.address);

    }






    @Bindable
    public String getCity_id() {
        return city_id;
    }



    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }



    public boolean isDataValid(Context context)
    {
        if (

                !TextUtils.isEmpty(english_title)&&
                !TextUtils.isEmpty(arabic_title)&&
                !TextUtils.isEmpty(address)&&

                !TextUtils.isEmpty(city_id)

        )
        {
            error_arabic_title.set(null);
            error_english_title.set(null);

            address_error.set(null);

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

            if (address.isEmpty())
            {
                address_error.set(context.getString(R.string.field_req));
            }else
            {
                address_error.set(null);
            }






            if (city_id.isEmpty())
            {
                Toast.makeText(context, context.getString(R.string.ch_city), Toast.LENGTH_SHORT).show();
            }




            return false;
        }
    }
}
