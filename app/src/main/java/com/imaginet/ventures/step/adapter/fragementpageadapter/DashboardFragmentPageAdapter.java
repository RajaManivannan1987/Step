package com.imaginet.ventures.step.adapter.fragementpageadapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.imaginet.ventures.step.fragment.DashboardFragment;
import com.imaginet.ventures.step.fragment.DetailedViewFragment;
import com.imaginet.ventures.step.fragment.StepScoreFragment;

/**
 * Created by IM028 on 9/7/16.
 */
public class DashboardFragmentPageAdapter extends FragmentPagerAdapter {
    private String[] tabName = {"DASHBOARD", "STEP SCORE", "DETAILED VIEW"};
    public DashboardFragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (tabName[position]){
            case "DASHBOARD":
                return new DashboardFragment();
            case "STEP SCORE":
                return new StepScoreFragment();
            case "DETAILED VIEW":
                return new DetailedViewFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabName.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabName[position];
    }
}
