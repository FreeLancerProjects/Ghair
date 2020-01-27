package com.endpoint.ghair.activities_fragments.activity_home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import com.endpoint.ghair.R;
import com.endpoint.ghair.activities_fragments.activity_addauction.AddAuctionActivity;
import com.endpoint.ghair.activities_fragments.activity_service_require.ServiceRequireActivity;
import com.endpoint.ghair.activities_fragments.activity_home.fragments.Fragment_Auction;
import com.endpoint.ghair.activities_fragments.activity_home.fragments.Fragment_Main;
import com.endpoint.ghair.activities_fragments.activity_home.fragments.Fragment_Require;
import com.endpoint.ghair.language.Language;
import com.endpoint.ghair.preferences.Preferences;
import com.google.android.material.navigation.NavigationView;

import java.util.Locale;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity  {
    private FragmentManager fragmentManager;
    private Fragment_Main fragment_main;
    private Fragment_Auction fragment_auction;
    private AHBottomNavigation ahBottomNav;
private TextView tv_title;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Preferences preferences;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
private ImageView imaddauction,imagechat;
    private Fragment_Require fragment_require;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        if (savedInstanceState == null) {

            displayFragmentMain();
        }


    }

    @SuppressLint("RestrictedApi")
    private void initView() {
        ahBottomNav = findViewById(R.id.ah_bottom_nav);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        fragmentManager = getSupportFragmentManager();
        toolbar = findViewById(R.id.toolbar);
        tv_title=findViewById(R.id.tvtitle);
imaddauction=findViewById(R.id.imageplus);
imagechat=findViewById(R.id.imagechat);
        toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.input));




        toggle.syncState();
        setUpBottomNavigation();
        imaddauction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragment_auction!=null&&fragment_auction.isVisible()){
                Intent intent=new Intent(HomeActivity.this, AddAuctionActivity.class);
                startActivity(intent);}
                else {
                    Intent intent=new Intent(HomeActivity.this, ServiceRequireActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


    private void setUpBottomNavigation() {

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getResources().getString(R.string.home), R.drawable.ic_home);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getResources().getString(R.string.auction), R.drawable.ic_auction);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getResources().getString(R.string.required), R.drawable.ic_required);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(getResources().getString(R.string.my_orders), R.drawable.ic_orders);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(getResources().getString(R.string.more), R.drawable.ic_more);

       ahBottomNav.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);
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
                       break;
                   case 4:
                       break;
               }
               return false;
           }
       });


    }

    private void updateBottomNavigationPosition(int pos) {

       ahBottomNav.setCurrentItem(pos, false);
       if(pos==0){
           imagechat.setVisibility(View.VISIBLE);
           imaddauction.setVisibility(View.GONE);
           tv_title.setText(getResources().getString(R.string.home));
       }
       else if(pos==1){
           imagechat.setVisibility(View.GONE);
           imaddauction.setVisibility(View.VISIBLE);
           tv_title.setText(getResources().getString(R.string.auction));
       }
       else if(pos==2){
           imagechat.setVisibility(View.GONE);
           imaddauction.setVisibility(View.VISIBLE);
           tv_title.setText(getResources().getString(R.string.require));
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

            if (fragment_require.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_require).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_require, "fragment_require").addToBackStack("fragment_require").commit();

            }
            updateBottomNavigationPosition(2);
        } catch (Exception e) {
        }

    }



    private void NavigateToSignInActivity() {
       // Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
       // startActivity(intent);
       // finish();
    }

    public void refreshActivity(String lang) {
        Paper.book().write("lang", lang);
        Language.setNewLocale(this, lang);
        Intent intent = getIntent();
        finish();
        startActivity(intent);

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
