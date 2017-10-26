package com.tallerandroid.netgreen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
        p1.setNomUsuario("Yessica");
        p1.setPuntos("120");
        aRanking.add(p1);

        p1 = new Ranking();
        p1.setNomUsuario("Eric");
        p1.setPuntos("200");
        aRanking.add(p1);

        p1 = new Ranking();
        p1.setNomUsuario("Scarleth");
        p1.setPuntos("130");
        aRanking.add(p1);



        lvRanking = (ListView) getView().findViewById(R.id.lvRanking);
        adaptadorListaRanking = new AdaptadorListaRanking(getActivity(), aRanking);
        lvRanking.setAdapter(adaptadorListaRanking);
    }
}

