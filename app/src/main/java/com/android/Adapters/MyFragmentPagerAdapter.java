package com.android.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Ngoc Vu on 12/18/2017.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> fragmentList;
    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentList){
        super(fm);
        this.fragmentList=fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return this.fragmentList.size();
    }
}
