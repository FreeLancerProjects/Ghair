package com.endpoint.ghair.activities_fragments.activity_auction_detials;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.endpoint.ghair.R;
import com.endpoint.ghair.activities_fragments.activity_home.HomeActivity;
import com.endpoint.ghair.activities_fragments.chat_activity.ChatActivity;
import com.endpoint.ghair.adapters.Offer_Adapter;
import com.endpoint.ghair.adapters.SlidingImageAuction_Adapter;
import com.endpoint.ghair.databinding.ActivityAuctionDetialsBinding;
import com.endpoint.ghair.databinding.DialogAddPriceBinding;
import com.endpoint.ghair.interfaces.Listeners;
import com.endpoint.ghair.language.Language;
import com.endpoint.ghair.models.Auction_Model;
import com.endpoint.ghair.models.MessageModel;
import com.endpoint.ghair.models.UserModel;
import com.endpoint.ghair.preferences.Preferences;
import com.endpoint.ghair.remote.Api;
import com.endpoint.ghair.share.Common;
import com.endpoint.ghair.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuctionDetialsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityAuctionDetialsBinding binding;
    private String lang;
    private Offer_Adapter category_adapter;
    private LinearLayoutManager manager;
private SlidingImageAuction_Adapter slidingImageAuction_adapter;
    private List<Auction_Model.Data> dataList;
private String search_id;
    private int current_page = 0, NUM_PAGES;
    private boolean isLoading2 = false;
    private int current_page4=1;
    private Preferences preferences;
    private UserModel userModel;
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_auction_detials);
        initView();
change_slide_image();
getsingleads();


    }

    @SuppressLint("RestrictedApi")
    private void initView() {
        preferences=Preferences.getInstance();
        userModel=preferences.getUserData(this);
        if(getIntent().getIntExtra("search",-1)!=0){
            search_id=getIntent().getIntExtra("search",-1)+"";
        }
        dataList=new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAuctionDialogs(AuctionDetialsActivity.this);
            }
        });
        manager=new LinearLayoutManager(this);
        category_adapter=new Offer_Adapter(dataList,this);
        binding.recView.setLayoutManager(manager);
        binding.recView.setAdapter(category_adapter);
        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    int totalItems = category_adapter.getItemCount();
                    int lastVisiblePos = manager.findLastCompletelyVisibleItemPosition();
                    if (totalItems > 5 && (totalItems - lastVisiblePos) == 1 && !isLoading2) {
                        isLoading2 = true;
                        dataList.add(null);
                        category_adapter.notifyItemInserted(dataList.size() - 1);
                        int page = current_page4 + 1;
                        loadMoreauction(page);


                    }
                }
            }
        });
binding.offer.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(binding.expand.isExpanded()){
            binding.expand.collapse(true);
            binding.image.setRotation(180f);
        }
        else {
            binding.expand.expand(true);
            binding.image.setRotation(90f);


        }
    }
});

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

    private void sendauction(String detials,String price)
    {
        try {


            Api.getService(Tags.base_url)
                    .sendAuction( price, detials, search_id,"Bearer" + " " + userModel.getToken())
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful() && response.body() != null) {
                               getsingleads();
                            } else {

                                try {

                                    Log.e("errorcode", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (response.code() == 500) {
                                 //   Toast.makeText(ChatActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                                } else {
                                    if(response.code()==402) {
                                        // Toast.makeText(ChatActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();//
                                        Toast.makeText(AuctionDetialsActivity.this, getResources().getString(R.string.offerless), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            try {
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(AuctionDetialsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AuctionDetialsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }

    public  void CreateAuctionDialogs(Context context) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .create();

        DialogAddPriceBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_add_price, null, false);

binding.btnSend.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(userModel!=null){
            String price=binding.edtstprice.getText().toString();
            String detials=binding.edtdetials.getText().toString();
            if(!TextUtils.isEmpty(price)&&!TextUtils.isEmpty(detials)){
                binding.edtdetials.setError(null);
                binding.edtstprice.setError(null);
                sendauction(detials,price);
            }
            else {
                if(price.isEmpty()){
                    binding.edtstprice.setError(context.getResources().getString(R.string.field_req));
                }
                else {
                    binding.edtstprice.setError(null);
                }
                if(detials.isEmpty()){
                    binding.edtdetials.setError(context.getResources().getString(R.string.field_req));
                }
                else {
                    binding.edtdetials.setError(null);
                }
            }
        }
        else {
            Common.CreateNoSignAlertDialog(AuctionDetialsActivity.this);

        }
        dialog.dismiss();
    }
});

        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }
    public void getsingleads() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);

        ProgressDialog dialog = Common.createProgressDialog(AuctionDetialsActivity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        // rec_sent.setVisibility(View.GONE);
        try {


            Api.getService( Tags.base_url)
                    .get_singleauction(search_id,lang,1)
                    .enqueue(new Callback<Auction_Model>() {
                        @Override
                        public void onResponse(Call<Auction_Model> call, Response<Auction_Model> response) {
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
                        public void onFailure(Call<Auction_Model> call, Throwable t) {
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
    private void loadMoreauction(int page) {
        try {


            Api.getService(Tags.base_url)
                    .get_singleauction(search_id,lang,page)
                    .enqueue(new Callback<Auction_Model>() {
                        @Override
                        public void onResponse(Call<Auction_Model> call, Response<Auction_Model> response) {
                            dataList.remove(dataList.size() - 1);
                            category_adapter.notifyItemRemoved(dataList.size() - 1);
                            isLoading2 = false;
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                                dataList.addAll(response.body().getData());
                                // categories.addAll(response.body().getCategories());
                                current_page4 = response.body().getCurrent_page();
                                category_adapter.notifyDataSetChanged();

                            } else {
                                //Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Auction_Model> call, Throwable t) {
                            try {
                                dataList.remove(dataList.size() - 1);
                                category_adapter.notifyItemRemoved(dataList.size() - 1);
                                isLoading2 = false;
                                // Toast.makeText(this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            dataList.remove(dataList.size() - 1);
            category_adapter.notifyItemRemoved(dataList.size() - 1);
            isLoading2 = false;
        }
    }
    private void setdata(Auction_Model body) {
        binding.setAuctionmodel(body.getAuction_data());
        if(body.getAuction_data().getAuction_image()!=null&&body.getAuction_data().getAuction_image().size()>0){
            binding.imBanner.setVisibility(View.GONE);
            slidingImageAuction_adapter=new SlidingImageAuction_Adapter(this,body.getAuction_data());
            binding.pager.setAdapter(slidingImageAuction_adapter);
NUM_PAGES=body.getAuction_data().getAuction_image().size() ;
        }
        else {
            binding.imBanner.setVisibility(View.VISIBLE);
        }
   if(body.getAuction_data()!=null){
       dataList.addAll( body.getData());
       category_adapter.notifyDataSetChanged();
   }

    }

    @Override
    public void back() {
        finish();
    }
}
