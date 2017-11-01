package com.tallerandroid.netgreen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by yesce on 15/05/2017.
 */

public class FragmentPerfilFotos extends Fragment {
    public static  FragmentPerfilFotos newInstance(){
        FragmentPerfilFotos fragment = new FragmentPerfilFotos();
        return fragment;
    }

    public FragmentPerfilFotos(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_fragment_perfil_fotos, container, false);
    }

    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
    }


}
