package com.tallerandroid.netgreen;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by yesce on 01/10/2017.
 */

public class PatrocinadoresActivity  extends AppCompatActivity {

    ListView lvPatrocinadores;
    AdaptadorListaPatrocinadores adaptadorListaPatrocinadores;
    private ArrayList<Patrocinadores> aPatrocinadores;
    Patrocinadores p1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrocinadores);

        aPatrocinadores = new ArrayList<>();
        p1 = new Patrocinadores();
        p1.setOrganismo("Usuario1");
        p1.setContacto("Organismo");
        p1.setTelefono("c");

        aPatrocinadores.add(p1);

        lvPatrocinadores = (ListView) findViewById(R.id.lvPatrocinadores);
        //adaptadorListaPatrocinadores = new AdaptadorListaRanking(getActivity(), aPatrocinadores);
        lvPatrocinadores.setAdapter(adaptadorListaPatrocinadores);
    }


}
