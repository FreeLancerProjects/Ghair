package com.endpoint.ghair.models;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.endpoint.ghair.R;

import java.io.Serializable;

public class SignUpMArketModel extends BaseObservable implements Serializable {

    private String english_title;
    private String arabic_title;
    private String national;

    private String phone_code;
    private String phone;
    private String password;
    private String city_id;
    private String longitude;
    private String latitude;
    private String address;
    private int isAcceptTerms;

    public ObservableField<String> error_english_title = new ObservableField<>();
    public ObservableField<String> error_arabic_title = new ObservableField<>();

    public ObservableField<String> error_national = new ObservableField<>();

    public ObservableField<String> error_phone_code = new ObservableField<>();
    public ObservableField<String> error_phone = new ObservableField<>();
    public ObservableField<String> address_error = new ObservableField<>();

    public ObservableField<String> error_password = new ObservableField<>();


    public SignUpMArketModel() {
        this.english_title = "";
        this.arabic_title = "";
        this.national = "";
        this.phone_code = "";
        this.phone = "";
        this.password = "";
        this.city_id = "";
        this.longitude = "";
        this.latitude = "";
        this.address = "";
        this.isAcceptTerms = 0;
    }

    @Bindable
    public String getPhone_code() {
        return phone_code;
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
    public String getNational() {
        return national;
    }

    public void setNational(String national) {
        this.national = national;
        notifyPropertyChanged(com.endpoint.ghair.BR.national);

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

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
        notifyPropertyChanged(com.endpoint.ghair.BR.phone_code);

    }

    @Bindable

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(com.endpoint.ghair.BR.phone);

    }



    @Bindable

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(com.endpoint.ghair.BR.password);

    }

    @Bindable
    public String getCity_id() {
        return city_id;
    }



    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public int getIsAcceptTerms() {
        return isAcceptTerms;
    }

    public void setIsAcceptTerms(int isAcceptTerms) {
        this.isAcceptTerms = isAcceptTerms;
    }

    public boolean isDataValid(Context context)
    {
        if (!TextUtils.isEmpty(phone_code)&&
                !TextUtils.isEmpty(phone)&&
                !TextUtils.isEmpty(password)&&

                password.length()>=6&&
                !TextUtils.isEmpty(english_title)&&
                !TextUtils.isEmpty(arabic_title)&&
                !TextUtils.isEmpty(national)&&
                !TextUtils.isEmpty(address)&&

                !TextUtils.isEmpty(city_id)&&isAcceptTerms==1

        )
        {
            error_arabic_title.set(null);
            error_english_title.set(null);
            error_national.set(null);
            error_phone_code.set(null);
            error_phone.set(null);
            error_password.set(null);
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
            if (national.isEmpty())
            {
                error_national.set(context.getString(R.string.field_req));
            }else
            {
                error_national.set(null);
            }
            if (address.isEmpty())
            {
                address_error.set(context.getString(R.string.field_req));
            }else
            {
                address_error.set(null);
            }

            if (phone_code.isEmpty())
            {
                error_phone_code.set(context.getString(R.string.field_req));
            }else
            {
                error_phone_code.set(null);
            }

            if (phone.isEmpty())
            {
                error_phone.set(context.getString(R.string.field_req));
            }else
            {
                error_phone.set(null);
            }


            if (password.isEmpty())
            {
                error_password.set(context.getString(R.string.field_req));
            }else if (password.length()<6)
            {
                error_password.set(context.getString(R.string.pass_short));
            }else
            {
                error_password.set(null);

            }

            if (city_id.isEmpty())
            {
                Toast.makeText(context, context.getString(R.string.ch_city), Toast.LENGTH_SHORT).show();
            }

            if (isAcceptTerms==0)
            {
                Toast.makeText(context, context.getString(R.string.please_accept_terms), Toast.LENGTH_SHORT).show();
            }


            return false;
        }
    }
}
