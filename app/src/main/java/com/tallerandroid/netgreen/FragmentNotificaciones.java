package com.tallerandroid.netgreen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;



/**
 * Created by yesce on 01/10/2017.
 */

public class FragmentNotificaciones extends Fragment {

    ListView lvNotificaciones;
    AdaptadorListaNotificaciones adaptadorListaNotificaciones;
    private ArrayList<Notificacion> aNotificaciones;
    Notificacion n1, n2;

    public static FragmentNotificaciones newInstance() {
        FragmentNotificaciones fragment = new FragmentNotificaciones();
        return fragment;
    }

    public  FragmentNotificaciones()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_fragment_notificaciones, container, false);
    }

    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);


        aNotificaciones = new ArrayList<>();
        n1 = new Notificacion();
        // p1.setImagen(null);
        n1.setUsuario("Yessica Valdes");
        n1.setAccion("cambio la hora del evento");
        n1.setActividad_noticia("Recoleccion de comida");
        aNotificaciones.add(n1);

        n1 = new Notificacion();
        n1.setUsuario("Scarleth Hernandez");
        n1.setAccion("cambio el lugar del evento");
        n1.setActividad_noticia("Reforestacion");
        aNotificaciones.add(n1);

        lvNotificaciones = (ListView) getView().findViewById(R.id.lvNotificaciones);
        adaptadorListaNotificaciones = new AdaptadorListaNotificaciones(getActivity(), aNotificaciones);
        lvNotificaciones.setAdapter(adaptadorListaNotificaciones);
    }

    }
