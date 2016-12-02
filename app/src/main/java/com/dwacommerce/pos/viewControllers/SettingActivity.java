package com.dwacommerce.pos.viewControllers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.dwacommerce.pos.R;
import com.dwacommerce.pos.adapters.SettingsFragmentPagerAdapter;

/**
 * Created by Admin on 10/20/2016.
 */
public class SettingActivity extends FragmentActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {
    private TabLayout tabLayoutSettings;
    private ImageView imgBackSettingsCustomer;
    private ViewPager settingViewPager;
    private SettingsFragmentPagerAdapter settingsFragmentPagerAdapter;
    public boolean setResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_screen_layout);
        init();
    }

    private void init() {
        tabLayoutSettings = (TabLayout) findViewById(R.id.tabLayoutSettings);
        imgBackSettingsCustomer = (ImageView) findViewById(R.id.imgBackSettingsCustomer);
        settingViewPager = (ViewPager) findViewById(R.id.settingViewPager);
        imgBackSettingsCustomer.setOnClickListener(this);
        tabLayoutSettings.addTab(tabLayoutSettings.newTab().setText("General"));
        tabLayoutSettings.addTab(tabLayoutSettings.newTab().setText("Printer"));
        tabLayoutSettings.addTab(tabLayoutSettings.newTab().setText("Store"));
        settingsFragmentPagerAdapter = new SettingsFragmentPagerAdapter(getSupportFragmentManager(), tabLayoutSettings.getTabCount());
        settingViewPager.setAdapter(settingsFragmentPagerAdapter);
        settingViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutSettings));
        tabLayoutSettings.setOnTabSelectedListener(this);
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        settingViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View v) {
        int vid = v.getId();
        if (vid == R.id.imgBackSettingsCustomer) {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        if (setResult) {
            setResult(RESULT_OK);
        }
        super.onBackPressed();
    }
}
