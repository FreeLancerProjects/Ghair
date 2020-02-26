package com.endpoint.ghair.models;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;



import java.io.Serializable;
import com.endpoint.ghair.BR;
import com.endpoint.ghair.R;
public class SignUpCustomerModel extends BaseObservable implements Serializable {

    private String name;
    private String phone_code;
    private String phone;
    private String password;
    private String city_id;
private int isAcceptTerms;
    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_phone_code = new ObservableField<>();
    public ObservableField<String> error_phone = new ObservableField<>();

    public ObservableField<String> error_password = new ObservableField<>();


    public SignUpCustomerModel() {
        this.name = "";
        this.phone_code = "";
        this.phone="";
        this.password="";
        this.city_id = "";
        isAcceptTerms=0;
    }

    public SignUpCustomerModel(String name, String city_id, String phone_code, String phone ,String password, int isAcceptTerms) {
        setName(name);
        setPhone_code(phone_code);
        setPhone(phone);
        setPassword(password);
        setCity_id(city_id);
        setIsAcceptTerms(isAcceptTerms);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);


    }

    @Bindable
    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
        notifyPropertyChanged(BR.phone_code);

    }

    @Bindable

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);

    }



    @Bindable

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);

    }

    @Bindable
    public String getCity_id() {
        return city_id;
    }

    public void setIsAcceptTerms(int  isAcceptTerms) {
        this.isAcceptTerms = isAcceptTerms;
    }
    @Bindable
    public int getIsAcceptTerms() {
        return isAcceptTerms;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }
    public boolean isDataValid(Context context)
    {
        if (!TextUtils.isEmpty(phone_code)&&
                !TextUtils.isEmpty(phone)&&
                password.length()>=6&&
                !TextUtils.isEmpty(name)&&
                !TextUtils.isEmpty(city_id)&&isAcceptTerms==1
        )
        {
            error_name.set(null);
            error_phone_code.set(null);
            error_phone.set(null);
            error_password.set(null);

            return true;
        }else
        {
            if (name.isEmpty())
            {
                error_name.set(context.getString(R.string.field_req));
            }else
            {
                error_name.set(null);
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
