package com.tallerandroid.netgreen;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class DashboardActivity extends AppCompatActivity {

    private static final Integer[] Icons = new Integer[]{
            R.drawable.ic_inicio,
            R.drawable.ic_crear_act,
            R.drawable.ic_crear_nota,
            R.drawable.ic_ranking,
            R.drawable.ic_notificacion
            };

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
        if (id == R.id.mnuitem_action_mapa) {
            Intent intent = new Intent(this, MapaActivity.class);
            startActivity(intent);
        }
        if (id == R.id.mnuitem_action_ajustes_cuenta) {
            Intent intent = new Intent(this, AjustesCuentaActivity.class);
            startActivity(intent);
        }
        if (id == R.id.mnuitem_action_patrocinadores) {
            Intent intent = new Intent(this, PatrocinadoresActivity.class);
            startActivity(intent);
        }

        if (id == R.id.mnuitem_action_cerrar_sesion) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            DialogoCerrarSesion dialogo = new DialogoCerrarSesion();
            dialogo.show(fragmentManager, "tagPersonalizado");
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(Icons[0]);
        tabLayout.getTabAt(1).setIcon(Icons[1]);
        tabLayout.getTabAt(2).setIcon(Icons[2]);
        tabLayout.getTabAt(3).setIcon(Icons[3]);
        tabLayout.getTabAt(4).setIcon(Icons[4]);

        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#2ecc71"), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#6B6B6B"), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#6B6B6B"), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(3).getIcon().setColorFilter(Color.parseColor("#6B6B6B"), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(4).getIcon().setColorFilter(Color.parseColor("#6B6B6B"), PorterDuff.Mode.SRC_IN);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#2ecc71"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#6B6B6B"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
