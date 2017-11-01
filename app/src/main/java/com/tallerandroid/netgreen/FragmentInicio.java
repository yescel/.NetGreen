package com.tallerandroid.netgreen;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentInicio extends Fragment {
    Spinner spinnerCategorias;
    Spinner spinnerSubCategorias;

    ListView lvPublicaciones;
    AdaptadorListaInicio adaptadorLista;
    private ArrayList<Inicio> publicaciones;
    Inicio p1;
    private SQLiteDatabase db;
    String[] categorias;
    String[] subcategorias;

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

        spinnerCategorias = (Spinner) getActivity().findViewById(R.id.spinnerCategorias_Inicio);
        TareaWSCargarSpinnerCat cargarCat = new TareaWSCargarSpinnerCat();
        cargarCat.execute(spinnerCategorias.toString());
        spinnerCategorias.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {
                        spinnerSubCategorias = (Spinner) getActivity().findViewById(R.id.spinnerSubCategorias_Inicio);

                        TareaWSCargarSpinnerSubCat cargarSubcat = new TareaWSCargarSpinnerSubCat();
                        cargarSubcat.execute(Integer.toString(position));
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });


        publicaciones = new ArrayList<>();
        lvPublicaciones = (ListView) getView().findViewById(R.id.lvInicio);
        adaptadorLista = new AdaptadorListaInicio(getActivity(), publicaciones);

        TareaWSCargarInicio tarea = new TareaWSCargarInicio();
        tarea.execute(spinnerCategorias.toString());

        lvPublicaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                TextView tvId = (TextView) v.findViewById(R.id.tvIdPublicacion);
                TextView tvTipo = (TextView) v.findViewById(R.id.tvTipoPubblicacion);
                String txtId = tvId.getText().toString();
                String txtTipo = tvTipo.getText().toString();
                String idPublicacion = "";
                String tipoPublicacion = "";

                try {
                    PubSeleccionadaSQLiteHelper usdbh = new PubSeleccionadaSQLiteHelper(
                            getContext(), "DBPubSeleccionada", null, 1);
                    db = usdbh.getWritableDatabase();

                    ContentValues nuevoRegistro = new ContentValues();
                    nuevoRegistro.put("idPublicacion", txtId);
                    nuevoRegistro.put("tipoPublicacion", txtTipo);
                    db.insert("PubSeleccionada", null, nuevoRegistro);
                    Cursor c = db.rawQuery("SELECT idPublicacion, tipoPublicacion FROM PubSeleccionada",
                            null);
                    if (c.moveToFirst()) {
                        idPublicacion = c.getString(0);
                        tipoPublicacion = c.getString(1);
                    }
                    Intent intent = new Intent(getActivity(), DetalleItemInicioActivity.class);
                    startActivity(intent);
                }catch (Exception ex){
                    String e = ex.toString();

                }


            }
        });
    }


    private class TareaWSCargarInicio extends AsyncTask<String,Integer,Boolean> {

        private int id;
        private String tipoPublicacion;
        private String usuario;
        private String fecha;
        private String nombrePublicacion;
        private String descripcion;
        private Bitmap imagen;

        protected Boolean doInBackground(String... params) {
            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String correo = params[0];

            HttpGet del = new HttpGet("http://netgreen.org.mx/ws/consulta_publicaciones.php");

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                for(int i=0; i < respJSON.length(); i++) {
                    JSONObject jsonobject = respJSON.getJSONObject(i);
                    id = jsonobject.getInt("idPublicacion");
                    tipoPublicacion = jsonobject.getString("tipo");
                    usuario    = jsonobject.getString("nombre");
                    fecha  = jsonobject.getString("fecha_hora_creacion");
                    nombrePublicacion = jsonobject.getString("nombrePublicacion");
                    descripcion = jsonobject.getString("descripcion");
                    byte[] decodeString = Base64.decode(jsonobject.getString("imagen"), Base64.DEFAULT);
                    imagen = BitmapFactory.decodeByteArray(decodeString,0, decodeString.length);
                    String idPublicacion = Integer.toString(id);
                    p1 = new Inicio();
                    p1.setImagen(null);
                    p1.setNomUsuario(usuario);
                    p1.setFechaPublicacion(fecha);
                    p1.setNomPublicacion(nombrePublicacion);
                    p1.setDescripcion(descripcion);
                    p1.setImagen(imagen);
                    p1.setIdPublicacion(idPublicacion);
                    p1.setTipo(tipoPublicacion);
                    publicaciones.add(p1);
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
                lvPublicaciones.setAdapter(adaptadorLista);
            }
            else
            {

            }
        }
    }

    private class TareaWSCargarSpinnerCat extends AsyncTask<String,Integer,Boolean> {

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
                categorias = new String[respJSON.length()];
                for(int i=0; i < respJSON.length(); i++) {
                    JSONObject jsonobject = respJSON.getJSONObject(i);
                    idCategoria    = jsonobject.getInt("idCategoria");
                    nombreCat  = jsonobject.getString("nombre");
                    categorias[i]= nombreCat;
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

    private class TareaWSCargarSpinnerSubCat extends AsyncTask<String,Integer,Boolean> {

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
                    subcategorias[i]= nombreSubCat;
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
