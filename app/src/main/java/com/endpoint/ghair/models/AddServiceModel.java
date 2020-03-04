package com.endpoint.ghair.models;

import android.content.Context;
import android.text.TextUtils;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.endpoint.ghair.R;

import java.io.Serializable;

public class AddServiceModel extends BaseObservable implements Serializable {

    private String date;
    private String address;
    private String details;

    private String time;
    private String longitude;
    private String latitude;


    public ObservableField<String> error_date = new ObservableField<>();
    public ObservableField<String> error_address = new ObservableField<>();

    public ObservableField<String> error_details = new ObservableField<>();

    public ObservableField<String> error_time = new ObservableField<>();


    public AddServiceModel() {
        this.date = "";
        this.address = "";
        this.details = "";
        this.time = "";
        this.longitude = "";
        this.latitude = "";

    }

    @Bindable
    public String getTime() {
        return time;
    }
   @Bindable
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        notifyPropertyChanged(com.endpoint.ghair.BR.date);

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
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
        notifyPropertyChanged(com.endpoint.ghair.BR.details);

    }

    public void setTime(String time) {
        this.time = time;
        notifyPropertyChanged(com.endpoint.ghair.BR.phone_code);

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









    public boolean isDataValid(Context context)
    {
        if (!TextUtils.isEmpty(time)&&

                !TextUtils.isEmpty(date)&&
                !TextUtils.isEmpty(address)&&
                !TextUtils.isEmpty(details)


        )
        {
            error_address.set(null);
            error_date.set(null);
            error_details.set(null);
            error_time.set(null);


            return true;
        }else
        {
            if (date.isEmpty())
            {
                error_date.set(context.getString(R.string.field_req));
            }else
            {
                error_date.set(null);
            }
            if (address.isEmpty())
            {
                error_address.set(context.getString(R.string.field_req));
            }else
            {
                error_address.set(null);
            }
            if (details.isEmpty())
            {
                error_details.set(context.getString(R.string.field_req));
            }else
            {
                error_details.set(null);
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
