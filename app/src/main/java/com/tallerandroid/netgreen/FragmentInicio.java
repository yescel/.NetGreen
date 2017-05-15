package com.tallerandroid.netgreen;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FragmentInicio extends Fragment {
    ListView lvPublicaciones;
    AdaptadorListaInicio adaptadorLista;
    private ArrayList<Inicio> publicaciones;
    Inicio p1, p2;

    public static FragmentInicio newInstance() {
        FragmentInicio fragment = new FragmentInicio();
        return fragment;
    }

    public FragmentInicio() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        publicaciones = new ArrayList<>();
        p1 = new Inicio();
       // p1.setImagen(null);
        p1.setNomActividad_Noticia("hola");
        p1.setDescripcionAct_Not("Esta es la descripcion de la actividad o noticia");
        p1.setUsuario("Yessica");
        p1.setCategoria("Pruebas");
        p1.setCantParticipantes(0);
        p1.setCantSeguidores(3);
        publicaciones.add(p1);

        lvPublicaciones = (ListView)getView().findViewById(R.id.lvInicio);
        adaptadorLista = new AdaptadorListaInicio(getActivity(), publicaciones);
        lvPublicaciones.setAdapter(adaptadorLista);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_fragment_inicio, container, true);
    }


}
