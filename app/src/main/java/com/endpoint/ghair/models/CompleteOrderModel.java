package com.endpoint.ghair.models;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.endpoint.ghair.R;

import java.io.Serializable;

public class CompleteOrderModel extends BaseObservable implements Serializable {


    private String longitude;
    private String latitude;
    private String address;


    public ObservableField<String> address_error = new ObservableField<>();



    public CompleteOrderModel() {

        this.longitude = "";
        this.latitude = "";
        this.address = "";
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













    public boolean isDataValid(Context context)
    {
        if (

                !TextUtils.isEmpty(address)


        )
        {

            address_error.set(null);

            return true;
        }else
        {

            if (address.isEmpty())
            {
                address_error.set(context.getString(R.string.field_req));
            }else
            {
                address_error.set(null);
            }




            return false;
        }
    }
}
