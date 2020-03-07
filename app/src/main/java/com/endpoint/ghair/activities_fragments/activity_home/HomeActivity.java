package com.endpoint.ghair.activities_fragments.activity_home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import com.endpoint.ghair.R;
import com.endpoint.ghair.activities_fragments.activity_addauction.AddAuctionActivity;
import com.endpoint.ghair.activities_fragments.activity_addauction.AddProductActivity;
import com.endpoint.ghair.activities_fragments.activity_cart.CartActivity;
import com.endpoint.ghair.activities_fragments.activity_categories.CatogriesActivity;
import com.endpoint.ghair.activities_fragments.activity_home.fragments.Fragment_More;
import com.endpoint.ghair.activities_fragments.activity_home.fragments.fragment_profile.fragments.Fragment_Profile;
import com.endpoint.ghair.activities_fragments.activity_market_profile.MarketProfileActivity;
import com.endpoint.ghair.activities_fragments.activity_profile.ProfileActivity;
import com.endpoint.ghair.activities_fragments.activity_service_require.ServiceRequireActivity;
import com.endpoint.ghair.activities_fragments.activity_home.fragments.Fragment_Auction;
import com.endpoint.ghair.activities_fragments.activity_home.fragments.Fragment_Main;
import com.endpoint.ghair.activities_fragments.activity_home.fragments.Fragment_Require;
import com.endpoint.ghair.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.endpoint.ghair.adapters.Side_Menu_Adapter;
import com.endpoint.ghair.language.Language;
import com.endpoint.ghair.models.Brand_Model;
import com.endpoint.ghair.models.Service_Model;
import com.endpoint.ghair.models.UserModel;
import com.endpoint.ghair.preferences.Preferences;
import com.endpoint.ghair.remote.Api;
import com.endpoint.ghair.tags.Tags;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private Fragment_Main fragment_main;
    private Fragment_Require fragment_require;
    private Side_Menu_Adapter side_menu_adapter;
    private List<Brand_Model.Data> brandList;
    private Fragment_More fragment_more;
    private Fragment_Profile fragment_profile;
    private Fragment_Auction fragment_auction;
    private AHBottomNavigation ahBottomNav;
private LinearLayoutManager manager;
    private TextView tv_title,tvname,tvphone;
    private DrawerLayout drawer;
    private Preferences preferences;
    private UserModel userModel;
    private RecyclerView recmenu;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private ImageView imaddauction, imagechat, imagecart;
    private ConstraintLayout cons_add;
    private CircleImageView improfile;
    private boolean isLoading = false;
    private int current_page3 = 1;
    private String lang;
    private ProgressBar progBar;
    private LinearLayout llNobrands;
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        initView();
        if(userModel!=null){
            if(userModel.getUser_type().equals("client")){
                cons_add.setVisibility(View.GONE);
            }
            tvphone.setText(userModel.getPhone_code().replaceFirst("00","+")+userModel.getPhone());
            if(userModel.getUser_type().equals("market")){
                if(lang.equals("ar")){
                    tvname.setText(userModel.getAr_market_title());
                }
                else {
                    tvname.setText(userModel.getEn_market_title());

                }
            }
            else {
            tvname.setText(userModel.getName());}
            Picasso.with(this).load(Uri.parse(Tags.IMAGE_URL+userModel.getLogo())).placeholder(R.drawable.ic_user).fit().into(improfile);
        }
        getBrands();
        if (savedInstanceState == null) {

            displayFragmentMain();
        }


    }

    @SuppressLint("RestrictedApi")
    private void initView() {
        preferences=Preferences.getInstance();
        userModel=preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        brandList = new ArrayList<>();
        ahBottomNav = findViewById(R.id.ah_bottom_nav);
        drawer = findViewById(R.id.drawer_layout);
        fragmentManager = getSupportFragmentManager();
        toolbar = findViewById(R.id.toolbar);
        tv_title = findViewById(R.id.tvtitle);
        tvname=findViewById(R.id.tvName);
        tvphone=findViewById(R.id.tvphone);
        imaddauction = findViewById(R.id.imageplus);
        imagechat = findViewById(R.id.imagechat);
        imagecart = findViewById(R.id.imagecart);
        progBar = findViewById(R.id.progBar);
        llNobrands=findViewById(R.id.ll_no_brands);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        recmenu = findViewById(R.id.recView);
        improfile = findViewById(R.id.imageprofile);
        cons_add = findViewById(R.id.cons_add);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        side_menu_adapter = new Side_Menu_Adapter(brandList, this);
        manager=new LinearLayoutManager(this);
        recmenu.setLayoutManager(manager);
        recmenu.setAdapter(side_menu_adapter);


        toggle.syncState();
        setUpBottomNavigation();
        imaddauction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment_auction != null && fragment_auction.isVisible()) {
                    Intent intent = new Intent(HomeActivity.this, AddAuctionActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(HomeActivity.this, ServiceRequireActivity.class);
                    startActivity(intent);
                }
            }
        });
        imagecart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
        improfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        cons_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });

//        recmenu.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                if (dy > 0) {
//                    int totalItems = side_menu_adapter.getItemCount();
//                    int lastVisiblePos = manager.findLastCompletelyVisibleItemPosition();
//                    if (totalItems > 5 && (totalItems - lastVisiblePos) == 1 && !isLoading) {
//                        isLoading = true;
//                        brandList.add(null);
//                        side_menu_adapter.notifyItemInserted(brandList.size() - 1);
//                        int page = current_page3 + 1;
//                        loadMoreBrands(page);
//
//
//                    }
//                }
//            }
//        });
    }
    public void Logout() {
        userModel = null;
        preferences.create_update_userdata(this, userModel);
        preferences.create_update_session(this, Tags.session_logout);
        Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    private void getBrands() {
        brandList.clear();
        side_menu_adapter.notifyDataSetChanged();
        progBar.setVisibility(View.VISIBLE);
        try {


            Api.getService(Tags.base_url)
                    .getBrands(1,lang)
                    .enqueue(new Callback<Brand_Model>() {
                        @Override
                        public void onResponse(Call<Brand_Model> call, Response<Brand_Model> response) {
                          progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                brandList.clear();
                                brandList.addAll(response.body().getData());
                                if (response.body().getData().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);

                                   llNobrands.setVisibility(View.GONE);
                                    side_menu_adapter.notifyDataSetChanged();
                                    recmenu.setVisibility(View.VISIBLE);
                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    side_menu_adapter.notifyDataSetChanged();

                                    llNobrands.setVisibility(View.VISIBLE);

                                }
                            } else {
                                side_menu_adapter.notifyDataSetChanged();

                               llNobrands.setVisibility(View.VISIBLE);

                                //Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Brand_Model> call, Throwable t) {
                            try {

                                progBar.setVisibility(View.GONE);
                              llNobrands.setVisibility(View.VISIBLE);
                                Toast.makeText(HomeActivity.this, getResources().getString(R.string.something), Toast.LENGTH_LONG).show();


                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e("errorsss", e.getMessage());

            progBar.setVisibility(View.GONE);
            llNobrands.setVisibility(View.VISIBLE);

        }
    }

    private void loadMoreBrands(int page) {
        try {


            Api.getService(Tags.base_url)
                    .getBrands(page,lang)
                    .enqueue(new Callback<Brand_Model>() {
                        @Override
                        public void onResponse(Call<Brand_Model> call, Response<Brand_Model> response) {
                            brandList.remove(brandList.size() - 1);
                            side_menu_adapter.notifyItemRemoved(brandList.size() - 1);
                            isLoading = false;
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                                brandList.addAll(response.body().getData());
                                // categories.addAll(response.body().getCategories());
                                current_page3 = response.body().getCurrent_page();
                                side_menu_adapter.notifyDataSetChanged();

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
                        public void onFailure(Call<Brand_Model> call, Throwable t) {
                            try {
                                brandList.remove(brandList.size() - 1);
                                side_menu_adapter.notifyItemRemoved(brandList.size() - 1);
                                isLoading = false;
                                // Toast.makeText(this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            brandList.remove(brandList.size() - 1);
            side_menu_adapter.notifyItemRemoved(brandList.size() - 1);
            isLoading = false;
        }
    }

    private void setUpBottomNavigation() {

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getResources().getString(R.string.home), R.drawable.ic_home);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getResources().getString(R.string.auction), R.drawable.ic_auction);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getResources().getString(R.string.required), R.drawable.ic_required);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(getResources().getString(R.string.profile), R.drawable.ic_user);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(getResources().getString(R.string.more), R.drawable.ic_more);

        ahBottomNav.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        ahBottomNav.setDefaultBackgroundColor(ContextCompat.getColor(this, R.color.white));
        ahBottomNav.setTitleTextSizeInSp(14, 12);
        ahBottomNav.setForceTint(true);
        ahBottomNav.setAccentColor(ContextCompat.getColor(this, R.color.input));
        ahBottomNav.setInactiveColor(ContextCompat.getColor(this, R.color.gray8));

        ahBottomNav.addItem(item1);
        ahBottomNav.addItem(item2);
        ahBottomNav.addItem(item3);
        ahBottomNav.addItem(item4);
        ahBottomNav.addItem(item5);

        updateBottomNavigationPosition(0);

        ahBottomNav.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0:
                        displayFragmentMain();
                        break;
                    case 1:
                        displayFragmentAuction();

                        break;
                    case 2:
                        displayFragmentRequire();
                        break;
                    case 3:
                        displayFragmentProfile();
                        break;
                    case 4:
                        displayFragmentMore();
                        break;
                }
                return false;
            }
        });


    }

    private void updateBottomNavigationPosition(int pos) {

        ahBottomNav.setCurrentItem(pos, false);
        if (pos == 0) {
            imagechat.setVisibility(View.VISIBLE);
            imaddauction.setVisibility(View.GONE);
            tv_title.setText(getResources().getString(R.string.home));
        } else if (pos == 1) {
            imagechat.setVisibility(View.GONE);
            imaddauction.setVisibility(View.VISIBLE);
            tv_title.setText(getResources().getString(R.string.auction));
        } else if (pos == 2) {
            imagechat.setVisibility(View.GONE);
            imaddauction.setVisibility(View.VISIBLE);
            tv_title.setText(getResources().getString(R.string.require));
        } else if (pos == 3) {
            imagechat.setVisibility(View.GONE);
            imaddauction.setVisibility(View.VISIBLE);
            tv_title.setText(getResources().getString(R.string.profile));
        } else if (pos == 4) {
            imagechat.setVisibility(View.GONE);
            imaddauction.setVisibility(View.VISIBLE);
            tv_title.setText(getResources().getString(R.string.more));
        }
    }

    public void displayFragmentMain() {
        try {
            if (fragment_main == null) {
                fragment_main = Fragment_Main.newInstance();
            }

            if (fragment_auction != null && fragment_auction.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_auction).commit();
            }
            if (fragment_require != null && fragment_require.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_require).commit();
            }
            if (fragment_more != null && fragment_more.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_more).commit();
            }
            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragment_main.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_main).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_main, "fragment_main").addToBackStack("fragment_main").commit();

            }
            updateBottomNavigationPosition(0);
        } catch (Exception e) {
        }

    }

    private void displayFragmentAuction() {
        try {
            if (fragment_auction == null) {
                fragment_auction = Fragment_Auction.newInstance();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }
            if (fragment_require != null && fragment_require.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_require).commit();
            }
            if (fragment_more != null && fragment_more.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_more).commit();
            }
            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragment_auction.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_auction).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_auction, "fragment_auction").addToBackStack("fragment_auction").commit();

            }
            updateBottomNavigationPosition(1);
        } catch (Exception e) {
        }

    }

    private void displayFragmentRequire() {
        try {
            if (fragment_require == null) {
                fragment_require = Fragment_Require.newInstance();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }
            if (fragment_auction != null && fragment_auction.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_auction).commit();
            }
            if (fragment_more != null && fragment_more.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_more).commit();
            }
            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragment_require.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_require).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_require, "fragment_require").addToBackStack("fragment_require").commit();

            }
            updateBottomNavigationPosition(2);
        } catch (Exception e) {
        }

    }

    private void displayFragmentProfile() {
        try {
            if (fragment_profile == null) {
                fragment_profile = Fragment_Profile.newInstance();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }
            if (fragment_require != null && fragment_require.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_require).commit();
            }
            if (fragment_auction != null && fragment_auction.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_auction).commit();
            }
            if (fragment_more != null && fragment_more.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_more).commit();
            }
            if (fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_profile).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_profile, "fragment_profile").addToBackStack("fragment_profile").commit();

            }
            updateBottomNavigationPosition(3);
        } catch (Exception e) {
        }
    }

    private void displayFragmentMore() {
        try {
            if (fragment_more == null) {
                fragment_more = Fragment_More.newInstance();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }
            if (fragment_require != null && fragment_require.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_require).commit();
            }
            if (fragment_auction != null && fragment_auction.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_auction).commit();
            }
            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragment_more.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_more).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_more, "fragment_more").addToBackStack("fragment_more").commit();

            }
            updateBottomNavigationPosition(4);
        } catch (Exception e) {
        }
    }

    private void NavigateToSignInActivity() {
        // Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
        // startActivity(intent);
        // finish();
    }

    public void RefreshActivity(String lang)
    {
        Paper.book().write("lang",lang);
        Language.setNewLocale(this,lang);
        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent =  getIntent();
                        finish();
                        startActivity(intent);
                    }
                },1050);



    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onBackPressed() {
if(drawer.isDrawerOpen(GravityCompat.START)){
    drawer.closeDrawer(GravityCompat.START);
}
else {
    if (fragment_main != null && fragment_main.isAdded() && fragment_main.isVisible()) {
        finish();
    } else {
        displayFragmentMain();
    }

}
    }


    public void showCatogries(int id) {

            Intent intent=new Intent(HomeActivity.this, CatogriesActivity.class);
            intent.putExtra("search",id);

            startActivity(intent);

    }
}
