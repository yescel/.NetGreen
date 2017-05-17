package com.tallerandroid.netgreen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by yesce on 15/05/2017.
 */

public class FragmentPerfilProyectos extends Fragment {
    ListView lvPublicaciones;
    AdaptadorListaInicio adaptadorLista;
    private ArrayList<Inicio> publicaciones;
    Inicio p1, p2;


    public static  FragmentPerfilProyectos newInstance(){
        FragmentPerfilProyectos fragment = new FragmentPerfilProyectos();
        return fragment;
    }

    public FragmentPerfilProyectos(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_fracment_perfil_proyectos, container, false);
    }

    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        publicaciones = new ArrayList<>();
        p1 = new Inicio();
        // p1.setImagen(null);
        p1.setNomActividad_Noticia("hola2");
        p1.setDescripcionAct_Not("Esta es la descripcion de la actividad o noticia2");
        p1.setUsuario("Yessica2");
        p1.setCategoria("Pruebas2");
        //p1.setCantParticipantes(0);
        //p1.setCantSeguidores(3);
        publicaciones.add(p1);

        lvPublicaciones = (ListView) getView().findViewById(R.id.lvProyectos);
        adaptadorLista = new AdaptadorListaInicio(getActivity(), publicaciones);
        lvPublicaciones.setAdapter(adaptadorLista);
    }
}
