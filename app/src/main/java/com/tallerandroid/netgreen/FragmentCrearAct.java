package com.tallerandroid.netgreen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by yesce on 15/05/2017.
 */

public class FragmentCrearAct extends Fragment {
    Spinner spinnerCategorias;
    Spinner spinnerSubCategorias;

    public static FragmentCrearAct newInstance() {
        FragmentCrearAct fragment = new FragmentCrearAct();
        return fragment;
    }

    public FragmentCrearAct() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_fragment_crearact, container, false);
    }

    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        final String[] categorias = new String[]{"Social", "Inclusion social", "Ecologia", "Animal"};
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categorias);
        spinnerCategorias = (Spinner) getActivity().findViewById(R.id.spinnerCategorias);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorias.setAdapter(adaptador);

        spinnerCategorias.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {
                        ArrayAdapter<String> adaptador;
                        switch (position) {
                            case 0:
                                final String[] subCategorias0 = new String[]{"Casas hogar",
                                        "Discapacitados",
                                        "Personas de la tercera edad",
                                        "Dafmificados",
                                        "Migrantes",
                                        "Ninos enfermos",
                                        "Indigentes",
                                        "Brigadas contra el hambre",
                                        "Brigadas contra el frio"};
                                adaptador = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, subCategorias0);
                                spinnerSubCategorias = (Spinner) getActivity().findViewById(R.id.spinnerSubCategorias);
                                adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerSubCategorias.setAdapter(adaptador);
                                break;
                            case 1:
                                final String[] subCategorias1 = new String[]{"Cursos de capacitacion"};
                                adaptador = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, subCategorias1);
                                spinnerSubCategorias = (Spinner) getActivity().findViewById(R.id.spinnerSubCategorias);
                                adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerSubCategorias.setAdapter(adaptador);
                                break;
                            case 2:
                                final String[] subCategorias2 = new String[]{"Reforestacion",
                                        "Rescate de areas verdes",
                                        "Reciclaje",
                                        "Limpieza",
                                        "Energia renovable"};
                                adaptador = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, subCategorias2);
                                spinnerSubCategorias = (Spinner) getActivity().findViewById(R.id.spinnerSubCategorias);
                                adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerSubCategorias.setAdapter(adaptador);
                                break;
                            case 3:
                                final String[] subCategorias3 = new String[]{"Especies en peligro",
                                        "Rescate de animales maltratados",
                                        "Casa de perros"};
                                adaptador = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, subCategorias3);
                                spinnerSubCategorias = (Spinner) getActivity().findViewById(R.id.spinnerSubCategorias);
                                adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerSubCategorias.setAdapter(adaptador);
                                break;
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
    }

}
