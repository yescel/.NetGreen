package com.tallerandroid.netgreen;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

/**
 * Created by yesce on 15/05/2017.
 */

public class MiFragmentPagerAdapterPerfil extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Info", "Fotos", "Proyectos"};

    public MiFragmentPagerAdapterPerfil(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = null;
        switch(position) {
            case 0:
                f = FragmentPerfil.newInstance();
                break;
            case 1:
                f = FragmentPerfilFotos.newInstance();
                break;
            case 2:
                f = FragmentPerfilProyectos.newInstance();
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
