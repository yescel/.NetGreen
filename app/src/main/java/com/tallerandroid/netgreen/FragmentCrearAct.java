package com.tallerandroid.netgreen;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by yesce on 15/05/2017.
 */

public class FragmentCrearAct extends Fragment {
    Spinner spinnerCategorias;
    Spinner spinnerSubCategorias;
    String[] categorias;
    String[] subcategorias;

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

        spinnerCategorias = (Spinner) getActivity().findViewById(R.id.spinnerCategorias_CrearAct);
        TareaWSCargarSpinnerCatAct cargarCat = new TareaWSCargarSpinnerCatAct();
        cargarCat.execute(spinnerCategorias.toString());
        spinnerCategorias.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {
                        spinnerSubCategorias = (Spinner) getActivity().findViewById(R.id.spinnerSubCategorias_CrearAct);

                        TareaWSCargarSpinnerSubCatAct cargarSubcat = new TareaWSCargarSpinnerSubCatAct();
                        cargarSubcat.execute(Integer.toString(position + 1));
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
    }

    private class TareaWSCargarSpinnerCatAct extends AsyncTask<String,Integer,Boolean> {

        private int idCategoria;
        private String nombreCat;

        String[] aux;// =  new String[]{"usuarios"};
        protected Boolean doInBackground(String... params) {
            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del = new HttpGet("http://netgreen.org.mx/ws/consulta_categorias.php");

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);
                categorias = new String[respJSON.length() - 1];
                for(int i=0; i < respJSON.length(); i++) {
                    JSONObject jsonobject = respJSON.getJSONObject(i);
                    idCategoria    = jsonobject.getInt("idCategoria");
                    nombreCat  = jsonobject.getString("nombre");
                    if(idCategoria != 0)
                        categorias[i - 1]= nombreCat;
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
                ArrayAdapter<String> adaptador =  new ArrayAdapter<String>(getActivity(),  android.R.layout.simple_spinner_item, categorias);
                adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCategorias.setAdapter(adaptador);
            }
            else
            {

            }
        }
    }

    private class TareaWSCargarSpinnerSubCatAct extends AsyncTask<String,Integer,Boolean> {

        private int idSubcategoria;
        private String nombreSubCat;

        String[] aux;// =  new String[]{"usuarios"};
        protected Boolean doInBackground(String... params) {
            boolean resul = true;

            String categoria = params[0];
            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del = new HttpGet("http://netgreen.org.mx/ws/consulta_subcategorias.php?idCategoria="+categoria);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);
                subcategorias = new String[respJSON.length()];
                for(int i=0; i < respJSON.length(); i++) {
                    JSONObject jsonobject = respJSON.getJSONObject(i);
                    idSubcategoria = jsonobject.getInt("idCategoriaDetalle");
                    nombreSubCat  = jsonobject.getString("nombre");
                    subcategorias[i - 1]= nombreSubCat;

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
                ArrayAdapter<String> adaptador =  new ArrayAdapter<String>(getActivity(),  android.R.layout.simple_spinner_item, subcategorias);
                adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerSubCategorias.setAdapter(adaptador);

            }
            else
            {

            }
        }
    }
}
