package com.endpoint.ghair.activities_fragments.activity_profile;

import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


import com.endpoint.ghair.R;
import com.endpoint.ghair.activities_fragments.activity_profile.fragments.FragmentMyOrder;
import com.endpoint.ghair.activities_fragments.activity_profile.fragments.FragmentMyAuction;
import com.endpoint.ghair.activities_fragments.activity_profile.fragments.FragmentMyServices;
import com.endpoint.ghair.activities_fragments.activity_profile.fragments.FragmentRequired;
import com.endpoint.ghair.adapters.ViewPagerAdapter;
import com.endpoint.ghair.databinding.ActivityProfileBinding;
import com.endpoint.ghair.interfaces.Listeners;
import com.endpoint.ghair.language.Language;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class ProfileActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityProfileBinding binding;
    private String lang;
    private List<Fragment> fragmentList;
    private List<String> titles;
    private ViewPagerAdapter adapter;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        initView();

    }

    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());binding.setLang(lang);
        fragmentList = new ArrayList<>();
        titles = new ArrayList<>();
binding.setBackListener(this);
       binding.tab.setupWithViewPager(binding.pager);
        addFragments_Titles();
        binding.pager.setOffscreenPageLimit(fragmentList.size());

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragments(fragmentList);
        adapter.addTitles(titles);
        binding.pager.setAdapter(adapter);







    }
    private void addFragments_Titles() {
        fragmentList.add(FragmentMyOrder.newInstance());
        fragmentList.add(FragmentMyAuction.newInstance());

        fragmentList.add(FragmentRequired.newInstance());
fragmentList.add(FragmentMyServices.newInstance());
        titles.add(getString(R.string.my_orders));
        titles.add(getString(R.string.my_auction));
        titles.add(getString(R.string.require));
        titles.add(getString(R.string.my_services));



    }


    @Override
    public void back() {
        finish();
    }



}
