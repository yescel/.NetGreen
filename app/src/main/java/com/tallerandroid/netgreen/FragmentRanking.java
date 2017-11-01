package com.tallerandroid.netgreen;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.R.attr.drawable;
import static android.R.attr.id;

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
        lvRanking = (ListView) getView().findViewById(R.id.lvRanking);
        adaptadorListaRanking = new AdaptadorListaRanking(getActivity(), aRanking);
        lvRanking.setAdapter(adaptadorListaRanking);

        TareaWSCargarRanking tarea = new TareaWSCargarRanking();
        tarea.execute("");
    }


    private class TareaWSCargarRanking extends AsyncTask<String,Integer,Boolean> {

        private String usuario;
        private String puntos;
        private Bitmap imagen;
        private Drawable insignia;


        protected Boolean doInBackground(String... params) {
            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del = new HttpGet("http://netgreen.org.mx/ws/consulta_usuario.php");

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);
                int contadorLugares = 0;

                for(int i=0; i < respJSON.length(); i++) {
                    JSONObject jsonobject = respJSON.getJSONObject(i);
                    usuario    = jsonobject.getString("nombre");
                    puntos  = jsonobject.getString("puntos");
                    byte[] decodeString = Base64.decode(jsonobject.getString("imagen"), Base64.DEFAULT);
                    imagen = BitmapFactory.decodeByteArray(decodeString,0, decodeString.length);

                    contadorLugares++;
                    p1 = new Ranking();
                    p1.setNomUsuario(usuario);
                    p1.setPuntos(puntos);
                    p1.setImagen(imagen);
                    switch (contadorLugares)
                    {
                        case 1:
                            p1.setIvInsignida(getResources().getDrawable(R.drawable.oro));
                            break;
                        case 2:
                            p1.setIvInsignida(getResources().getDrawable(R.drawable.plata));
                            break;
                        case 3:
                            p1.setIvInsignida(getResources().getDrawable(R.drawable.bronce));
                            break;
                        default:
                            p1.setIvInsignida(getResources().getDrawable(R.drawable.medalla));
                            break;
                    }
                    aRanking.add(p1);
                }

            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul = false;
            }



            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                lvRanking.setAdapter(adaptadorListaRanking);
            }
            else
            {

            }
        }
    }
}

