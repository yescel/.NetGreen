package com.tallerandroid.netgreen;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.ImageView;

/**
 * Created by yesce on 14/05/2017.
 */

public class MiFragmentPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 5;

    private String tabTitles[] = new String[] { "Inicio", "Crear Act", "Crear Not", "Ranking", "Not"};

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
            case 4:
                f = FragmentNotificaciones.newInstance();
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
        switch (position)
        {
            case 0:
                return "";
            case 1:
                return "";
            case 2:
                return "";
            case 3:
                return "";
            case 4:
                return "";
            default:
                return "";

        }
        //return tabTitles[position];

    }
}
