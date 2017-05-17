package com.tallerandroid.netgreen;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by yesce on 14/05/2017.
 */

public class MiFragmentPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[] { "Ini", "CA", "CN", "Ran"};

    public MiFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment f = null;

        switch(position) {
            case 0:
                f = FragmentInicio.newInstance();
                break;
            case 1:
                f = FragmentCrearAct.newInstance();
                break;
            case 2:
                f = FragmentCrearNot.newInstance();
                break;
            case 3:
                f = FragmentRanking.newInstance();
                break;
        }

        return f;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
