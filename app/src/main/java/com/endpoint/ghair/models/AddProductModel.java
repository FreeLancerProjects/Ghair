package com.endpoint.ghair.models;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.endpoint.ghair.R;

import java.io.Serializable;

public class AddProductModel extends BaseObservable implements Serializable {

    private String english_title;
    private String arabic_title;
    private String english_des;
    private String arabic_des;
    private String price;

    private String model;

    private String amount;

    private String brand_id;
    private String cat_id;
    public ObservableField<String> error_english_title = new ObservableField<>();
    public ObservableField<String> error_arabic_title = new ObservableField<>();

    public ObservableField<String> error_price = new ObservableField<>();

    public ObservableField<String> error_model = new ObservableField<>();
    public ObservableField<String> error_english_des = new ObservableField<>();
    public ObservableField<String> error_arabic_des = new ObservableField<>();
    public ObservableField<String> error_amount = new ObservableField<>();

    public AddProductModel() {
        this.english_title = "";
        this.arabic_title = "";
        this.price = "";
        this.model = "";
        this.amount = "";
        this.english_des = "";
        this.arabic_des = "";
        this.cat_id = "";
        this.brand_id = "";

    }

    @Bindable
    public String getEnglish_des() {
        return english_des;
    }

    public void setEnglish_des(String english_des) {
        this.english_des = english_des;
    }

    @Bindable
    public String getArabic_des() {
        return arabic_des;
    }

    @Bindable
    public void setArabic_des(String arabic_des) {
        this.arabic_des = arabic_des;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    @Bindable
    public String getModel() {
        return model;
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


    public void setModel(String model) {
        this.model = model;
        notifyPropertyChanged(com.endpoint.ghair.BR.model);

    }

    @Bindable
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
        notifyPropertyChanged(com.endpoint.ghair.BR.phone_code);

    }


    @Bindable
    public String getBrand_id() {
        return brand_id;
    }


    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public boolean isDataValid(Context context) {
        if (!TextUtils.isEmpty(model) &&

                !TextUtils.isEmpty(english_title) &&
                !TextUtils.isEmpty(arabic_title) &&
                !TextUtils.isEmpty(price) &&
                !TextUtils.isEmpty(brand_id)
                && !TextUtils.isEmpty(amount)
                && !TextUtils.isEmpty(english_des)
                && !TextUtils.isEmpty(arabic_des)
                && !TextUtils.isEmpty(cat_id)
        ) {
            error_arabic_title.set(null);
            error_english_title.set(null);
            error_price.set(null);
            error_model.set(null);
            error_amount.set(null);
            error_arabic_des.set(null);
            error_english_des.set(null);
            return true;
        } else {
            if (english_title.isEmpty()) {
                error_english_title.set(context.getString(R.string.field_req));
            } else {
                error_english_title.set(null);
            }
            if (arabic_title.isEmpty()) {
                error_arabic_title.set(context.getString(R.string.field_req));
            } else {
                error_arabic_title.set(null);
            }
            if (price.isEmpty()) {
                error_price.set(context.getString(R.string.field_req));
            } else {
                error_price.set(null);
            }
            if (amount.isEmpty()) {
                error_amount.set(context.getString(R.string.field_req));
            } else {
                error_amount.set(null);
            }

            if (model.isEmpty()) {
                error_model.set(context.getString(R.string.field_req));
            } else {
                error_model.set(null);
            }

            if (english_des.isEmpty()) {
                error_english_des.set(context.getString(R.string.field_req));
            } else {
                error_english_des.set(null);
            }
            if (arabic_des.isEmpty()) {
                error_arabic_des.set(context.getString(R.string.field_req));
            } else {
                error_arabic_des.set(null);
            }
            if (brand_id.isEmpty()) {
                Toast.makeText(context, context.getString(R.string.ch_brand), Toast.LENGTH_SHORT).show();
            }
            if (cat_id.isEmpty()) {
                Toast.makeText(context, context.getString(R.string.ch_cat), Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }
}
