package com.tallerandroid.netgreen;

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

/**
 * Created by yesce on 16/05/2017.
 */

public class FragmentRanking extends Fragment {

    ListView lvRanking;
    AdaptadorListaRanking adaptadorListaRanking;
    private ArrayList<Ranking> aRanking;
    Ranking p1, p2;

    public static FragmentRanking newInstance() {
        FragmentRanking fragment = new FragmentRanking();
        return fragment;
    }

    public FragmentRanking() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_fragment_ranking, container, false);

    }
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);


        aRanking = new ArrayList<>();
        p1 = new Ranking();
        // p1.setImagen(null);
        p1.setNomUsuario("Usuario1");
        p1.setNomOrganismo("Organismo");
        aRanking.add(p1);

        p1.setNomUsuario("Usuario2");
        p1.setNomOrganismo("Organismo");
        aRanking.add(p1);

        p1.setNomUsuario("Usuario3");
        p1.setNomOrganismo("Organismo");
        aRanking.add(p1);

        p1.setNomUsuario("Usuario4");
        p1.setNomOrganismo("Organismo");
        aRanking.add(p1);

        p1.setNomUsuario("Usuario5");
        p1.setNomOrganismo("Organismo");
        aRanking.add(p1);

        p1.setNomUsuario("Usuario6");
        p1.setNomOrganismo("Organismo");
        aRanking.add(p1);

        p1.setNomUsuario("Usuario7");
        p1.setNomOrganismo("Organismo");
        aRanking.add(p1);

        lvRanking = (ListView) getView().findViewById(R.id.lvRanking);
        adaptadorListaRanking = new AdaptadorListaRanking(getActivity(), aRanking);
        lvRanking.setAdapter(adaptadorListaRanking);
    }
}

