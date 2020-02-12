package com.endpoint.ghair.activities_fragments.activity_auction_detials;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.endpoint.ghair.R;
import com.endpoint.ghair.adapters.Offer_Adapter;
import com.endpoint.ghair.adapters.Ouction_Adapter;
import com.endpoint.ghair.databinding.ActivityAcuireDetialsBinding;
import com.endpoint.ghair.databinding.ActivityServiceRequireBinding;
import com.endpoint.ghair.databinding.DialogAddPriceBinding;
import com.endpoint.ghair.interfaces.Listeners;
import com.endpoint.ghair.language.Language;
import com.endpoint.ghair.models.Slider_Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class AuctionDetialsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityAcuireDetialsBinding binding;
    private String lang;
    private Offer_Adapter category_adapter;

    private List<Slider_Model.Data> dataList;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_acuire_detials);
        initView();



    }

    @SuppressLint("RestrictedApi")
    private void initView() {
        dataList=new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNoSignAlertDialogs(AuctionDetialsActivity.this);
            }
        });
        category_adapter=new Offer_Adapter(dataList,this);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(category_adapter);
        setdtat();



    }

    private void setdtat() {
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());


        category_adapter.notifyDataSetChanged();
    }

    public static void CreateNoSignAlertDialogs(Context context) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .create();

        DialogAddPriceBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_add_price, null, false);



        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    @Override
    public void back() {
        finish();
    }
}
