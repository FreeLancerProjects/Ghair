package com.endpoint.ghair.activities_fragments.activity_home.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.endpoint.ghair.R;
import com.endpoint.ghair.activities_fragments.activity_addservice.AddServiceActivity;
import com.endpoint.ghair.activities_fragments.activity_home.HomeActivity;
import com.endpoint.ghair.activities_fragments.activity_market.MarketActivity;
import com.endpoint.ghair.adapters.MostActiveMarkets_Adapter;
import com.endpoint.ghair.adapters.Service_Adapter;
import com.endpoint.ghair.adapters.SlidingImage_Adapter;
import com.endpoint.ghair.databinding.FragmnetMainBinding;
import com.endpoint.ghair.models.Market_Model;
import com.endpoint.ghair.models.Service_Model;
import com.endpoint.ghair.models.Slider_Model;
import com.endpoint.ghair.preferences.Preferences;
import com.endpoint.ghair.remote.Api;
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

public class Fragment_Main extends Fragment {
    private static Dialog dialog;
    private HomeActivity activity;
    private FragmnetMainBinding binding;
    private LinearLayoutManager manager, manager2;
    private Preferences preferences;
    private SlidingImage_Adapter slidingImage__adapter,slidingImage_adapter;
    private MostActiveMarkets_Adapter marketsAdapter;
    private Service_Adapter service_adapter;
    private int current_page = 0,current_page2 = 0, NUM_PAGES,NUM_PAGES2;
    private boolean isLoading = false,isLoading2 = false;
    private int current_page3 = 1,current_page4=1;
private List<Service_Model.Data> servicedatalist;
    private List<Market_Model.Data> marketlist;

    private String lang;

    public static Fragment_Main newInstance() {
        return new Fragment_Main();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragmnet_main, container, false);
        initView();
get_sliderup();
get_sliderdown();
change_slide_image();
getService();
getMArkets();
        return binding.getRoot();
    }



    private void initView() {

servicedatalist=new ArrayList<>();
marketlist=new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        marketsAdapter=new MostActiveMarkets_Adapter(marketlist,activity,this);
        service_adapter=new Service_Adapter(servicedatalist,activity,this);
manager=new LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false);
        manager2=new LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false);
binding.recservice.setDrawingCacheEnabled(true);
binding.recservice.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
binding.recservice.setItemViewCacheSize(25);
        binding.recBestseler.setDrawingCacheEnabled(true);
        binding.recBestseler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recBestseler.setItemViewCacheSize(25);
        binding.recBestseler.setLayoutManager(manager2);
        binding.recservice.setLayoutManager(manager);
binding.recservice.setAdapter(service_adapter);
binding.recBestseler.setAdapter(marketsAdapter);
binding.tabLayout.setupWithViewPager(binding.pager);
        binding.tabLayout2.setupWithViewPager(binding.pager2);
        binding.progBarservice.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.input), PorterDuff.Mode.SRC_IN);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.input), PorterDuff.Mode.SRC_IN);
        binding.progBarSlider.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.input), PorterDuff.Mode.SRC_IN);
        binding.progBarSlider2.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.input), PorterDuff.Mode.SRC_IN);

//        binding.recservice.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                if (dy > 0) {
//                    int totalItems = service_adapter.getItemCount();
//                    int lastVisiblePos = manager.findLastCompletelyVisibleItemPosition();
//                    if (totalItems > 5 && (totalItems - lastVisiblePos) == 1 && !isLoading) {
//                        isLoading = true;
//                        servicedatalist.add(null);
//                        service_adapter.notifyItemInserted(servicedatalist.size() - 1);
//                        int page = current_page3 + 1;
//                        loadMoreService(page);
//
//
//                    }
//                }
//            }
//        });
        binding.recBestseler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    int totalItems = marketsAdapter.getItemCount();
                    int lastVisiblePos = manager2.findLastCompletelyVisibleItemPosition();
                    if (totalItems > 5 && (totalItems - lastVisiblePos) == 1 && !isLoading2) {
                        isLoading2 = true;
                        marketlist.add(null);
                        marketsAdapter.notifyItemInserted(marketlist.size() - 1);
                        int page = current_page4 + 1;
                        loadMoreMarkets(page);


                    }
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
                if (current_page2 == NUM_PAGES2) {
                    current_page2 = 0;
                }
                binding.pager.setCurrentItem(current_page++, true);
                binding.pager2.setCurrentItem(current_page2++, true);

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
    private void get_sliderup() {

        Api.getService(Tags.base_url).get_slider("up").enqueue(new Callback<Slider_Model>() {
            @Override
            public void onResponse(Call<Slider_Model> call, Response<Slider_Model> response) {
                binding.progBarSlider.setVisibility(View.GONE);

                if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null) {
                    if (response.body().getData().size() > 0) {
                        NUM_PAGES = response.body().getData().size();
                        slidingImage__adapter = new SlidingImage_Adapter(activity, response.body().getData());
                        binding.pager.setAdapter(slidingImage__adapter);

                    } else {

                        binding.pager.setVisibility(View.GONE);
                    }
                } else if (response.code() == 404) {
                    binding.pager.setVisibility(View.GONE);
                } else {
                    binding.pager.setVisibility(View.GONE);
                    try {
                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<Slider_Model> call, Throwable t) {
                try {
                    binding.progBarSlider.setVisibility(View.GONE);
                    binding.pager.setVisibility(View.GONE);

                    Log.e("Error", t.getMessage());

                } catch (Exception e) {

                }

            }
        });

    }
    private void get_sliderdown() {

        Api.getService(Tags.base_url).get_slider("down").enqueue(new Callback<Slider_Model>() {
            @Override
            public void onResponse(Call<Slider_Model> call, Response<Slider_Model> response) {
                binding.progBarSlider2.setVisibility(View.GONE);

                if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null) {
                    if (response.body().getData().size() > 0) {
                        NUM_PAGES2 = response.body().getData().size();
                        slidingImage_adapter = new SlidingImage_Adapter(activity, response.body().getData());
                        binding.pager2.setAdapter(slidingImage_adapter);

                    } else {

                        binding.pager2.setVisibility(View.GONE);
                    }
                } else if (response.code() == 404) {
                    binding.pager2.setVisibility(View.GONE);
                } else {
                    binding.pager2.setVisibility(View.GONE);
                    try {
                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<Slider_Model> call, Throwable t) {
                try {
                    binding.progBarSlider2.setVisibility(View.GONE);
                    binding.pager2.setVisibility(View.GONE);

                    Log.e("Error", t.getMessage());

                } catch (Exception e) {

                }

            }
        });

    }
    private void getService() {
        servicedatalist.clear();
        service_adapter.notifyDataSetChanged();
        binding.progBarservice.setVisibility(View.VISIBLE);
        try {


            Api.getService(Tags.base_url)
                    .getServices(lang)
                    .enqueue(new Callback<Service_Model>() {
                        @Override
                        public void onResponse(Call<Service_Model> call, Response<Service_Model> response) {
                            binding.progBarservice.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                servicedatalist.clear();
                                servicedatalist.addAll(response.body().getData());
                                if (response.body().getData().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);
                                      Log.e("datasssss",response.body().getData().get(0).getAr_title());

                                    binding.llNoServices.setVisibility(View.GONE);
                                    service_adapter.notifyDataSetChanged();
                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    service_adapter.notifyDataSetChanged();

                                    binding.llNoServices.setVisibility(View.VISIBLE);

                                }
                            } else {
                                service_adapter.notifyDataSetChanged();

                                binding.llNoServices.setVisibility(View.VISIBLE);

                                //Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Service_Model> call, Throwable t) {
                            try {

                                binding.progBarservice.setVisibility(View.GONE);
                                binding.llNoServices.setVisibility(View.VISIBLE);
                                Toast.makeText(activity, getResources().getString(R.string.something), Toast.LENGTH_LONG).show();


                                Log.e("errorsssss", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e("errorsss", e.getMessage());

            binding.progBarservice.setVisibility(View.GONE);
            binding.llNoServices.setVisibility(View.VISIBLE);

        }
    }

//    private void loadMoreService(int page) {
//        try {
//
//
//            Api.getService(Tags.base_url)
//                    .getServices(page,lang)
//                    .enqueue(new Callback<Service_Model>() {
//                        @Override
//                        public void onResponse(Call<Service_Model> call, Response<Service_Model> response) {
//                            servicedatalist.remove(servicedatalist.size() - 1);
//                            service_adapter.notifyItemRemoved(servicedatalist.size() - 1);
//                            isLoading = false;
//                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
//
//                                servicedatalist.addAll(response.body().getData());
//                                // categories.addAll(response.body().getCategories());
//                                current_page3 = response.body().getCurrent_page();
//                                service_adapter.notifyDataSetChanged();
//
//                            } else {
//                                //Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//                                try {
//                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<Service_Model> call, Throwable t) {
//                            try {
//                                servicedatalist.remove(servicedatalist.size() - 1);
//                                service_adapter.notifyItemRemoved(servicedatalist.size() - 1);
//                                isLoading = false;
//                                // Toast.makeText(this, getString(R.string.something), Toast.LENGTH_SHORT).show();
//                                Log.e("error", t.getMessage());
//                            } catch (Exception e) {
//                            }
//                        }
//                    });
//        } catch (Exception e) {
//            servicedatalist.remove(servicedatalist.size() - 1);
//            service_adapter.notifyItemRemoved(servicedatalist.size() - 1);
//            isLoading = false;
//        }
//    }
    private void getMArkets() {
        servicedatalist.clear();
        service_adapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);
        try {


            Api.getService(Tags.base_url)
                    .getMarkets(1,lang)
                    .enqueue(new Callback<Market_Model>() {
                        @Override
                        public void onResponse(Call<Market_Model> call, Response<Market_Model> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                marketlist.clear();
                                marketlist.addAll(response.body().getData());
                                if (response.body().getData().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);

                                    binding.llNoServices.setVisibility(View.GONE);
                                    marketsAdapter.notifyDataSetChanged();
                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    marketsAdapter.notifyDataSetChanged();

                                    binding.llNoStore.setVisibility(View.VISIBLE);

                                }
                            } else {
                                marketsAdapter.notifyDataSetChanged();

                                binding.llNoStore.setVisibility(View.VISIBLE);

                                //Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Market_Model> call, Throwable t) {
                            try {

                                binding.progBar.setVisibility(View.GONE);
                                binding.llNoStore.setVisibility(View.VISIBLE);
                                Toast.makeText(activity, getResources().getString(R.string.something), Toast.LENGTH_LONG).show();


                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e("errorsss", e.getMessage());

            binding.progBar.setVisibility(View.GONE);
            binding.llNoStore.setVisibility(View.VISIBLE);

        }
    }

    private void loadMoreMarkets(int page) {
        try {


            Api.getService(Tags.base_url)
                    .getMarkets(page,lang)
                    .enqueue(new Callback<Market_Model>() {
                        @Override
                        public void onResponse(Call<Market_Model> call, Response<Market_Model> response) {
                            marketlist.remove(marketlist.size() - 1);
                            marketsAdapter.notifyItemRemoved(marketlist.size() - 1);
                            isLoading2 = false;
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                                marketlist.addAll(response.body().getData());
                                // categories.addAll(response.body().getCategories());
                                current_page4 = response.body().getCurrent_page();
                                marketsAdapter.notifyDataSetChanged();

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
                        public void onFailure(Call<Market_Model> call, Throwable t) {
                            try {
                                marketlist.remove(marketlist.size() - 1);
                                marketsAdapter.notifyItemRemoved(marketlist.size() - 1);
                                isLoading2 = false;
                                // Toast.makeText(this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            marketlist.remove(marketlist.size() - 1);
            marketsAdapter.notifyItemRemoved(marketlist.size() - 1);
            isLoading2 = false;
        }
    }


    public void showmarkets() {
        Intent intent=new Intent(activity, AddServiceActivity.class);
        startActivity(intent);
    }
    public void showmarkets2() {
        Intent intent=new Intent(activity, MarketActivity.class);
        startActivity(intent);
    }
}
