package com.endpoint.ghair.activities_fragments.activity_home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.endpoint.ghair.activities_fragments.activity_home.fragments.Fragment_More;
import com.endpoint.ghair.activities_fragments.activity_home.fragments.fragment_profile.fragments.Fragment_Profile;
import com.endpoint.ghair.activities_fragments.activity_profile.ProfileActivity;
import com.endpoint.ghair.activities_fragments.activity_service_require.ServiceRequireActivity;
import com.endpoint.ghair.activities_fragments.activity_home.fragments.Fragment_Auction;
import com.endpoint.ghair.activities_fragments.activity_home.fragments.Fragment_Main;
import com.endpoint.ghair.activities_fragments.activity_home.fragments.Fragment_Require;
import com.endpoint.ghair.adapters.Side_Menu_Adapter;
import com.endpoint.ghair.language.Language;
import com.endpoint.ghair.models.Slider_Model;
import com.endpoint.ghair.preferences.Preferences;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private Fragment_Main fragment_main;
    private Fragment_Require fragment_require;
    private Side_Menu_Adapter side_menu_adapter;
    private List<Slider_Model.Data> dataList;
    private Fragment_More fragment_more;
    private Fragment_Profile fragment_profile;
    private Fragment_Auction fragment_auction;
    private AHBottomNavigation ahBottomNav;

    private TextView tv_title;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Preferences preferences;
    private RecyclerView recmenu;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private ImageView imaddauction, imagechat, imagecart;
    private ConstraintLayout cons_add;
    private CircleImageView improfile;

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
        if (savedInstanceState == null) {

            displayFragmentMain();
        }


    }

    @SuppressLint("RestrictedApi")
    private void initView() {
        dataList = new ArrayList<>();
        ahBottomNav = findViewById(R.id.ah_bottom_nav);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        fragmentManager = getSupportFragmentManager();
        toolbar = findViewById(R.id.toolbar);
        tv_title = findViewById(R.id.tvtitle);
        imaddauction = findViewById(R.id.imageplus);
        imagechat = findViewById(R.id.imagechat);
        imagecart = findViewById(R.id.imagecart);

        recmenu = findViewById(R.id.recView);
        improfile = findViewById(R.id.imageprofile);
        cons_add = findViewById(R.id.cons_add);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        side_menu_adapter = new Side_Menu_Adapter(dataList, this);
        recmenu.setLayoutManager(new LinearLayoutManager(this));
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
        setdtat();


    }

    private void setdtat() {
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());
        dataList.add(new Slider_Model.Data());

        side_menu_adapter.notifyDataSetChanged();
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

    private void displayFragmentMain() {
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

        if (fragment_main != null && fragment_main.isAdded() && fragment_main.isVisible()) {

        } else {
            displayFragmentMain();
        }


    }


}
