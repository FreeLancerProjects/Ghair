package com.endpoint.ghair.activities_fragments.activity_cart;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;


import com.endpoint.ghair.R;
import com.endpoint.ghair.activities_fragments.activity_complete_order.CompleteOrderActivity;
import com.endpoint.ghair.activities_fragments.activity_products.ProductsActivity;
import com.endpoint.ghair.activities_fragments.activity_products_detials.ProductsDetialsActivity;
import com.endpoint.ghair.adapters.Cart_Adapter;
import com.endpoint.ghair.databinding.ActivityCartBinding;
import com.endpoint.ghair.interfaces.Listeners;
import com.endpoint.ghair.language.Language;
import com.endpoint.ghair.models.Add_Order_Model;
import com.endpoint.ghair.models.Slider_Model;
import com.endpoint.ghair.preferences.Preferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityCartBinding binding;

    private Preferences preferences;
    private String lang;
private List<Add_Order_Model.Details> order_details;
private Cart_Adapter cart_adapter;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }

    @Override
    protected void onResume() {
        super.onResume();
        getdata();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        initView();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            getdata();
        }
    }

    @SuppressLint("RestrictedApi")
    private void initView() {
preferences=Preferences.getInstance();
        binding.toolbar.setTitle("");
        order_details = new ArrayList<>();
        cart_adapter = new Cart_Adapter(order_details, this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.recCart.setLayoutManager(new GridLayoutManager(this, 1));
        binding.recCart.setAdapter(cart_adapter);
        //setdtat();
getdata();
binding.btCom.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Complete();
    }
});

    }

    private void getdata() {
        if(preferences.getUserOrder(this)!=null&&preferences.getUserOrder(this).getDetails()!=null){
            order_details.clear();
            order_details.addAll(preferences.getUserOrder(this).getDetails());
            cart_adapter.notifyDataSetChanged();
            gettotal();
        }
        else {
            binding.llNoStore.setVisibility(View.VISIBLE);
            binding.tvTotal.setVisibility(View.GONE);
            binding.btCom.setVisibility(View.GONE);
order_details.clear();
cart_adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void back() {
        finish();
    }

    public void Complete() {
        Intent intent=new Intent(CartActivity.this, CompleteOrderActivity.class);
        startActivityForResult(intent,1);
    }
    public void removeitem(int layoutPosition) {
        order_details.remove(layoutPosition);
        if(order_details.size()>0){
            Add_Order_Model add_order_model=preferences.getUserOrder(this);
            add_order_model.setDetails(order_details);
            preferences.create_update_order(this,add_order_model);
            gettotal();
        }
        else {
            preferences.create_update_order(this,null);
            binding.llNoStore.setVisibility(View.VISIBLE);
            binding.tvTotal.setVisibility(View.GONE);
            binding.btCom.setVisibility(View.GONE);

        }

        cart_adapter.notifyDataSetChanged();

    }
    public void additem(int layoutPosition) {
        Add_Order_Model.Details products1 =order_details.get(layoutPosition);
        products1.setPrice((products1.getPrice()/ products1.getAmount())*(products1.getAmount()+1));
        products1.setAmount(products1.getAmount()+1);
        order_details.remove(layoutPosition);
        order_details.add(layoutPosition, products1);
        Add_Order_Model add_order_model=preferences.getUserOrder(this);
        add_order_model.setDetails(order_details);
        preferences.create_update_order(this,add_order_model);
        cart_adapter.notifyDataSetChanged();
        gettotal();
    }
    public void minusitem(int layoutPosition) {

        Add_Order_Model.Details products1 =order_details.get(layoutPosition);
        if(products1.getAmount()>1){
            products1.setPrice((products1.getPrice()/ products1.getAmount())*(products1.getAmount()-1));
            products1.setAmount(products1.getAmount()-1);
            order_details.remove(layoutPosition);
            order_details.add(layoutPosition, products1);
            Add_Order_Model add_order_model=preferences.getUserOrder(this);
            add_order_model.setDetails(order_details);
            preferences.create_update_order(this,add_order_model);
            cart_adapter.notifyDataSetChanged();
            gettotal();

        }
    }
    private void gettotal() {

        double total=0;
        for(int i=0;i<order_details.size();i++){
            total+=order_details.get(i).getPrice();

        }




        binding.tvTotal.setText(getResources().getString(R.string.total)+total+" "+getResources().getString(R.string.real));
    }
}
