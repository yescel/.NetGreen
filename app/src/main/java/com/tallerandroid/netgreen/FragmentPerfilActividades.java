package com.tallerandroid.netgreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by yesce on 15/05/2017.
 */

public class FragmentPerfilActividades extends Fragment {
    ListView lvPublicaciones;
    AdaptadorListaInicio adaptadorLista;
    private ArrayList<Inicio> publicaciones;
    Inicio p1, p2;


    public static FragmentPerfilActividades newInstance(){
        FragmentPerfilActividades fragment = new FragmentPerfilActividades();
        return fragment;
    }

    public FragmentPerfilActividades(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_fragment_perfil_actividades, container, false);
    }

    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        publicaciones = new ArrayList<>();
        p1 = new Inicio();
        // p1.setImagen(null);
        p1.setNomPublicacion("Reforestacion");
        p1.setFechaPublicacion("08/10/2017 17:37");
        p1.setDescripcion("Se plantaran arboles en el boulevard colosio");
        publicaciones.add(p1);

        lvPublicaciones = (ListView) getView().findViewById(R.id.lvActividades_Perfil);
        adaptadorLista = new AdaptadorListaInicio(getActivity(), publicaciones);
        lvPublicaciones.setAdapter(adaptadorLista);

        lvPublicaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                Intent intent = new Intent(getActivity(), DetalleItemMiActividad.class);
                startActivity(intent);

            }
        });
    }
}
