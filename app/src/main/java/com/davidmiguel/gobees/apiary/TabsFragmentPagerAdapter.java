package com.davidmiguel.gobees.apiary;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.davidmiguel.gobees.utils.BaseTabFragment;

import java.util.List;

/**
 * Adapter for ViewPager.
 * Contros the order of the tabs, the titles and their associated content.
 */
class TabsFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private List<BaseTabFragment> fragments;

    TabsFragmentPagerAdapter(FragmentManager fm, Context context, List<BaseTabFragment> fragments) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return (Fragment) fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(fragments.get(position).getTabName());
    }
}
