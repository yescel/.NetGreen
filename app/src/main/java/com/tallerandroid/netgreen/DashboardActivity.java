package com.tallerandroid.netgreen;

import android.app.FragmentManager;
import android.content.ClipData;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import static com.tallerandroid.netgreen.R.drawable.ic_user;

public class DashboardActivity extends AppCompatActivity {

    private static final Integer[] Icons = new Integer[]{
            R.drawable.ic_inicio,
            R.drawable.ic_crear_act,
            R.drawable.ic_crear_nota,
            R.drawable.ic_ranking};

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MiFragmentPagerAdapter(
                getSupportFragmentManager()));

        tabLayout = (TabLayout) findViewById(R.id.appbartabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();


       // TabLayout.Tab tabInicio = tabLayout.getTabAt(ic_user);
       // tabInicio.setIcon(R.drawable.ic_user);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Alternativa 1
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();//obtenemos el id del item que le dimos clic en la barra de menu
        //noinspection SimplifiableIfStatement
        if (id == R.id.mnuitem_action_perfil) {
            Intent intent = new Intent(this, PerfilActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(Icons[0]);
        tabLayout.getTabAt(1).setIcon(Icons[1]);
        tabLayout.getTabAt(2).setIcon(Icons[2]);
        tabLayout.getTabAt(3).setIcon(Icons[3]);

    }
}
