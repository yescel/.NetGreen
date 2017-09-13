package com.tallerandroid.netgreen;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

public class FragmentInicio extends Fragment {
    Spinner spinnerCategorias;
    Spinner spinnerSubCategorias;

    ListView lvPublicaciones;
    AdaptadorListaInicio adaptadorLista;
    private ArrayList<Inicio> publicaciones;
    Inicio p1, p2;

    public static FragmentInicio newInstance() {
        FragmentInicio fragment = new FragmentInicio();
        return fragment;
    }

    public FragmentInicio() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_fragment_inicio, container, false);

    }
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);


        final String[] categorias =  new String[]{"Social","Inclusion social","Ecologia","Animal"};
        ArrayAdapter<String> adaptador =  new ArrayAdapter<String>(getActivity(),  android.R.layout.simple_spinner_item, categorias);
        spinnerCategorias = (Spinner) getActivity().findViewById(R.id.spinnerCategorias);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorias.setAdapter(adaptador);

        spinnerCategorias.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {
                        ArrayAdapter<String> adaptador;
                        switch (position)
                        {
                            case 0:
                                final String[] subCategorias0 =  new String[]{"Casas hogar",
                                        "Discapacitados",
                                        "Personas de la tercera edad",
                                        "Dafmificados",
                                        "Migrantes",
                                        "Ninos enfermos",
                                        "Indigentes",
                                        "Brigadas contra el hambre",
                                        "Brigadas contra el frio"};
                                adaptador =  new ArrayAdapter<String>(getActivity(),  android.R.layout.simple_spinner_item, subCategorias0);
                                spinnerSubCategorias = (Spinner) getActivity().findViewById(R.id.spinnerSubCategorias);
                                adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerSubCategorias.setAdapter(adaptador);
                                break;
                            case 1:
                                final String[] subCategorias1 =  new String[]{"Cursos de capacitacion"};
                                adaptador =  new ArrayAdapter<String>(getActivity(),  android.R.layout.simple_spinner_item, subCategorias1);
                                spinnerSubCategorias = (Spinner) getActivity().findViewById(R.id.spinnerSubCategorias);
                                adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerSubCategorias.setAdapter(adaptador);
                                break;
                            case 2:
                                final String[] subCategorias2 =  new String[]{"Reforestacion",
                                        "Rescate de areas verdes",
                                        "Reciclaje",
                                        "Limpieza",
                                        "Energia renovable"};
                                adaptador =  new ArrayAdapter<String>(getActivity(),  android.R.layout.simple_spinner_item, subCategorias2);
                                spinnerSubCategorias = (Spinner) getActivity().findViewById(R.id.spinnerSubCategorias);
                                adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerSubCategorias.setAdapter(adaptador);
                                break;
                            case 3:
                                final String[] subCategorias3 =  new String[]{"Especies en peligro",
                                        "Rescate de animales maltratados",
                                        "Casa de perros"};
                                adaptador =  new ArrayAdapter<String>(getActivity(),  android.R.layout.simple_spinner_item, subCategorias3);
                                spinnerSubCategorias = (Spinner) getActivity().findViewById(R.id.spinnerSubCategorias);
                                adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerSubCategorias.setAdapter(adaptador);
                                break;
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });



        publicaciones = new ArrayList<>();
        p1 = new Inicio();
        // p1.setImagen(null);
        p1.setNomActividad_Noticia("Reforestacion");
        p1.setDescripcionAct_Not("Reforestacion de parque el Laguito");
        p1.setUsuario("Yessica");
        p1.setCategoria("Ambiental");
        publicaciones.add(p1);

        p1 = new Inicio();
        // p1.setImagen(null);
        p1.setNomActividad_Noticia("Acitividad 1");
        p1.setDescripcionAct_Not("Esta es la descripcion de la actividad o noticia");
        p1.setUsuario("Usuario Organizador");
        p1.setCategoria("Categoria");
        publicaciones.add(p1);

        p1 = new Inicio();
        // p1.setImagen(null);
        p1.setNomActividad_Noticia("Actividad 2");
        p1.setDescripcionAct_Not("Esta es la descripcion de la actividad o noticia");
        p1.setUsuario("Usuario Organizador");
        p1.setCategoria("Categoria");
        publicaciones.add(p1);

        p1 = new Inicio();
        // p1.setImagen(null);
        p1.setNomActividad_Noticia("Actividad 3");
        p1.setDescripcionAct_Not("Esta es la descripcion de la actividad o noticia");
        p1.setUsuario("Usuario Organizador");
        p1.setCategoria("Categoria");
        publicaciones.add(p1);


        p1 = new Inicio();
        // p1.setImagen(null);
        p1.setNomActividad_Noticia("Actividad 4");
        p1.setDescripcionAct_Not("Esta es la descripcion de la actividad o noticia");
        p1.setUsuario("Usuario Organizador");
        p1.setCategoria("Categoria");
        publicaciones.add(p1);


        p1 = new Inicio();
        // p1.setImagen(null);
        p1.setNomActividad_Noticia("Actividad 5");
        p1.setDescripcionAct_Not("Esta es la descripcion de la actividad o noticia");
        p1.setUsuario("Usuario Organizador");
        p1.setCategoria("Pruebas");
        publicaciones.add(p1);

        p1 = new Inicio();
        // p1.setImagen(null);
        p1.setNomActividad_Noticia("Actividad 6");
        p1.setDescripcionAct_Not("Esta es la descripcion de la actividad o noticia");
        p1.setUsuario("Usuario Organizador");
        p1.setCategoria("Pruebas");
        publicaciones.add(p1);

        lvPublicaciones = (ListView) getView().findViewById(R.id.lvInicio);
        adaptadorLista = new AdaptadorListaInicio(getActivity(), publicaciones);
        lvPublicaciones.setAdapter(adaptadorLista);

        lvPublicaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                Intent intent = new Intent(getActivity(), DetalleItemInicioActivity.class);
                startActivity(intent);
                //Verificar tipoPublicacio (Act o noticia) para abrir Activity correspondiente a cada una
               // String opcionSeleccionada = ((Inicio)a.getItemAtPosition(position)).getNomUsuario();
                //Alternativa 2:
                //String opcionSeleccionada = ((TextView)v.findViewById(R.id.LblTitulo)).getText().toString();
              //  lblEtiqueta.setText("Opción seleccionada: " + opcionSeleccionada);
            }
        });
    }
}
