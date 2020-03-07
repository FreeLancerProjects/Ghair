package com.endpoint.ghair.activities_fragments.activity_products_detials;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.endpoint.ghair.R;
import com.endpoint.ghair.activities_fragments.activity_auction_detials.AuctionDetialsActivity;
import com.endpoint.ghair.adapters.Accessories_Adapter;
import com.endpoint.ghair.adapters.Image_Adapter;
import com.endpoint.ghair.adapters.SlidingImageAuction_Adapter;
import com.endpoint.ghair.adapters.SlidingImageProduct_Adapter;
import com.endpoint.ghair.databinding.ActivityProductDetialsBinding;
import com.endpoint.ghair.databinding.ActivityProductsBinding;
import com.endpoint.ghair.interfaces.Listeners;
import com.endpoint.ghair.language.Language;
import com.endpoint.ghair.models.Add_Order_Model;
import com.endpoint.ghair.models.Auction_Model;
import com.endpoint.ghair.models.SingleProductModel;
import com.endpoint.ghair.models.Slider_Model;
import com.endpoint.ghair.preferences.Preferences;
import com.endpoint.ghair.remote.Api;
import com.endpoint.ghair.share.Common;
import com.endpoint.ghair.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsDetialsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityProductDetialsBinding binding;
    private String lang;
    private Image_Adapter markets_adapter;
    private List<Slider_Model.Data> dataList;
    private SlidingImageProduct_Adapter slidingImageProduct_adapter;
    private String search_id;
    private int current_page = 0, NUM_PAGES;
private SingleProductModel singleProductModel;
private Preferences preferences;
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_product_detials);
        initView();
        getsingleads();

change_slide_image();

    }

    @SuppressLint("RestrictedApi")
    private void initView() {
        preferences=Preferences.getInstance();
        dataList=new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        markets_adapter=new Image_Adapter(dataList,this);
        binding.recView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false));
        binding.recView.setAdapter(markets_adapter);
        //setdtat();
        if(getIntent().getIntExtra("search",-1)!=0){
            search_id=getIntent().getIntExtra("search",-1)+"";
        }
binding.btnSend.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        addtocart();
    }
});

    }

    private void addtocart() {
        Add_Order_Model add_order_model=preferences.getUserOrder(this);
        if(add_order_model!=null){
                List<Add_Order_Model.Products> order_details=add_order_model.getDetails();
                Add_Order_Model.Products products1 = null;
                int pos = 0;
                for(int i=0;i<order_details.size();i++){
                    if(singleProductModel.getId()==order_details.get(i).getProduct_id()&&singleProductModel.getMarket_id()==order_details.get(i).getMarket_id()){
                        products1 =order_details.get(i);
                        pos=i;
                    }
                }
                if(products1 !=null){
                    products1.setAmount(1+order_details.get(pos).getAmount());
                    // Log.e("to",add_order_model.getTotal_cost()+(Double.parseDouble(single_product_model.getPrice())*amount)+""+((amount+order_details.get(pos).getAmount())*Double.parseDouble(single_product_model.getPrice())));
                    products1.setPrice(products1.getPrice()+singleProductModel.getPrice());

                    order_details.remove(pos);
                    order_details.add(pos, products1);

                }
                else {
                    products1 =new Add_Order_Model.Products();
                    products1.setAr_title(singleProductModel.getAr_title());
                    products1.setEn_title(singleProductModel.getEn_title());
                    products1.setAmount(1);
                    products1.setPrice(singleProductModel.getPrice());
                    products1.setProduct_id(singleProductModel.getId());
                    products1.setMarket_id(singleProductModel.getMarket_id());
                    products1.setImage(singleProductModel.getMain_image());
                    order_details.add(products1);

                }
                add_order_model.setDetails(order_details);
              //  Common.CreateDialogAlert3(ProductDetialsActivity.this,getResources().getString(R.string.suc));


        }
        else {
            add_order_model=new Add_Order_Model();
            List<Add_Order_Model.Products> order_details=new ArrayList<>();
            Add_Order_Model.Products products1 =new Add_Order_Model.Products();
            products1.setProduct_id(singleProductModel.getId());
            products1.setPrice(singleProductModel.getPrice());
            products1.setAmount(1);
            products1.setAr_title(singleProductModel.getAr_title());
            products1.setEn_title(singleProductModel.getEn_title());
            products1.setMarket_id(singleProductModel.getMarket_id());
            products1.setImage(singleProductModel.getMain_image());

            order_details.add(products1);
            add_order_model.setDetails(order_details);

            //Common.CreateDialogAlert3(ProductDetialsActivity.this,getResources().getString(R.string.suc));


        }
        preferences.create_update_order(this,add_order_model);
    }

    private void change_slide_image() {
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (current_page == NUM_PAGES) {
                    current_page = 0;
                }

                binding.pager.setCurrentItem(current_page++, true);

            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
    }

    private void setdtat() {
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());


        markets_adapter.notifyDataSetChanged();
    }

    @Override
    public void back() {
        finish();
    }

    public void showimage(int layoutPosition) {
        if(layoutPosition%2!=0){
           binding.imBanner.setImageDrawable(getResources().getDrawable(R.drawable.index));
        }
        else {
            binding.imBanner.setImageDrawable(getResources().getDrawable(R.drawable.ic_auction));

        }
    }
    public void getsingleads() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);

        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        // rec_sent.setVisibility(View.GONE);
        Log.e("lllllll",search_id);
        try {


            Api.getService( Tags.base_url)
                    .get_singleproducr(search_id,lang)
                    .enqueue(new Callback<SingleProductModel>() {
                        @Override
                        public void onResponse(Call<SingleProductModel> call, Response<SingleProductModel> response) {
                            dialog.dismiss();

                            //  binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body() != null) {
                                //binding.coord1.scrollTo(0,0);
                                setdata(response.body());

                            } else {


                                //   Toast.makeText(AuctionDetialsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<SingleProductModel> call, Throwable t) {
                            try {

                                dialog.dismiss();

                                // Toast.makeText(AdsDetialsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        }catch (Exception e){

            dialog.dismiss();
        }
    }

    private void setdata(SingleProductModel body) {
        this.singleProductModel=body;
        binding.setProductmodel(singleProductModel);
        if(body.getProduct_images()!=null&&body.getProduct_images().size()>0){
        slidingImageProduct_adapter=new SlidingImageProduct_Adapter(this,body.getProduct_images());
        binding.pager.setAdapter(slidingImageProduct_adapter);
        binding.imBanner.setVisibility(View.GONE);
        NUM_PAGES=body.getProduct_images().size();
    }
    else {
        binding.imBanner.setVisibility(View.VISIBLE);
        binding.pager.setVisibility(View.GONE);
        }
    }

}
