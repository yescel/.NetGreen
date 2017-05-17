package com.tallerandroid.netgreen;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by yesce on 15/05/2017.
 */

public class PerfilActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);


        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpagerPerfil);
        viewPager.setAdapter(new MiFragmentPagerAdapterPerfil(
                getSupportFragmentManager()));


        TabLayout tabLayout = (TabLayout) findViewById(R.id.appbartabsPerfil);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
    }
}
