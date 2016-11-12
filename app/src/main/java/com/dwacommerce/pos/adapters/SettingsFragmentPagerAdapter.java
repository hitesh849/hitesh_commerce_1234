package com.dwacommerce.pos.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dwacommerce.pos.viewControllers.settingsFragments.DisplaySettingsFragment;
import com.dwacommerce.pos.viewControllers.settingsFragments.PrinterScannerFragment;
import com.dwacommerce.pos.viewControllers.settingsFragments.StoreSettingsFragment;

/**
 * Created by Admin on 10/23/2016.
 */
public class SettingsFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private int noOfTabs;

    public SettingsFragmentPagerAdapter(FragmentManager fm, int noOfTabs) {
        super(fm);
        this.noOfTabs = noOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: {
                StoreSettingsFragment fragment = new StoreSettingsFragment();
                return fragment;
            }
            case 1: {
                DisplaySettingsFragment displaySettingsFragment = new DisplaySettingsFragment();
                return displaySettingsFragment;
            }
            case 2: {
                PrinterScannerFragment printerScannerFragment = new PrinterScannerFragment();
                return printerScannerFragment;
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}
