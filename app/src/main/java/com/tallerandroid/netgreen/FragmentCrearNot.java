package com.tallerandroid.netgreen;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yesce on 16/05/2017.
 */

public class FragmentCrearNot extends Fragment {
    Spinner spinnerCategorias;
    Spinner spinnerSubCategorias;
    String[] categorias;
    String[] subcategorias;
    String[][] idCategorias;
    String[][] idSubcategorias;

    EditText txtTitulo;
    EditText txtSubtitulo;
    EditText txtCuerpo;
    EditText txtDescripcion;

    Button btnRegistrarNot;

    private SQLiteDatabase db;

    public static FragmentCrearNot newInstance() {
        FragmentCrearNot fragment = new FragmentCrearNot();
        return fragment;
    }

    public FragmentCrearNot() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_fragment_crearnot, container, false);
    }

    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        btnRegistrarNot = (Button) getActivity().findViewById(R.id.btnRegistrarNoticia );
        txtTitulo = (EditText) getActivity().findViewById(R.id.etNombre_Noticia);
        txtSubtitulo = (EditText) getActivity().findViewById(R.id.etSubtitulo_Noticia);
        txtCuerpo = (EditText) getActivity().findViewById(R.id.etCuerpo_Noticia);
        txtDescripcion = (EditText) getActivity().findViewById(R.id.etDescripcion_Noticia);

        spinnerCategorias = (Spinner) getActivity().findViewById(R.id.spinnerCategorias_CrearNot);
        TareaWSCargarSpinnerCatNot cargarCat = new TareaWSCargarSpinnerCatNot();
        cargarCat.execute(spinnerCategorias.toString());
        spinnerCategorias.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {
                        spinnerSubCategorias = (Spinner) getActivity().findViewById(R.id.spinnerSubCategorias_CrearNot);

                        TareaWSCargarSpinnerSubCatNot cargarSubcat = new TareaWSCargarSpinnerSubCatNot();
                        cargarSubcat.execute(Integer.toString(position + 1));
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });


        btnRegistrarNot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int intCategoria = 1;
                int intSubcategoria = 1;
                int intUsuario = 0;
                String strFechaActividad ="";

                Date fechaActual = new Date();
                DateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
                DateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
                String strFechaPublicacion = formatoFecha.format(fechaActual) + " " +formatoHora.format(fechaActual);

                for(int i=0; i < idCategorias.length; i++) {
                    String aux = spinnerCategorias.getSelectedItem().toString();
                    String aux2 = idCategorias[i][1];
                    if(aux.equals(aux2))
                    {
                        intCategoria = Integer.parseInt(idCategorias[i][0]);
                        break;
                    }
                }

                for(int i=0; i < idSubcategorias.length; i++) {
                    String aux3 = spinnerSubCategorias.getSelectedItem().toString();
                    String aux4 = idSubcategorias[i][1];
                    if(aux3.equals(aux4))
                    {
                        intSubcategoria = Integer.parseInt(idSubcategorias[i][0]);
                        break;
                    }
                }
                UsuarioLogueadoSQLiteHelper usdbh = new UsuarioLogueadoSQLiteHelper(getContext(), "DBUsuario", null, 1);
                db = usdbh.getWritableDatabase();

                Cursor c = db.rawQuery("SELECT idUsuario FROM Usuario", null);
                if (c.moveToFirst()) {
                    intUsuario = c.getInt(0);
                }
                TareaWSInsertarNot tarea = new TareaWSInsertarNot();
                tarea.execute(
                        Integer.toString(intUsuario),
                        Integer.toString(intCategoria),
                        Integer.toString(intSubcategoria),
                        txtTitulo.getText().toString(),
                        txtSubtitulo.getText().toString(),
                        txtCuerpo.getText().toString(),
                        txtDescripcion.getText().toString(),
                        strFechaPublicacion);
            }
        });




    }


    private class TareaWSCargarSpinnerCatNot extends AsyncTask<String,Integer,Boolean> {

        private int idCategoria;
        private String nombreCat;

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
                idCategorias = new String[respJSON.length() - 1][2];
                for(int i=0; i < respJSON.length(); i++) {
                    JSONObject jsonobject = respJSON.getJSONObject(i);
                    idCategoria    = jsonobject.getInt("idCategoria");
                    nombreCat  = jsonobject.getString("nombre");
                    if(idCategoria != 0) {
                        categorias[i - 1] = nombreCat;
                        idCategorias[i - 1][0] = Integer.toString(idCategoria);
                        idCategorias[i - 1][1] = nombreCat;
                    }
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

    private class TareaWSCargarSpinnerSubCatNot extends AsyncTask<String,Integer,Boolean> {

        private int idSubcategoria;
        private String nombreSubCat;

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
                idSubcategorias = new String[respJSON.length()][2];
                for(int i=0; i < respJSON.length(); i++) {
                    JSONObject jsonobject = respJSON.getJSONObject(i);
                    idSubcategoria = jsonobject.getInt("idCategoriaDetalle");
                    nombreSubCat  = jsonobject.getString("nombre");
                    subcategorias[i]= nombreSubCat;
                    idSubcategorias[i][ 0] = Integer.toString(idSubcategoria);
                    idSubcategorias[i][ 1] = nombreSubCat;
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

    private class TareaWSInsertarNot extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            try
            {
                HttpClient httpClient;
                List<NameValuePair> nameValuePairs;
                HttpPost httpPost;
                httpClient = new DefaultHttpClient();

                httpPost = new HttpPost("http://netgreen.org.mx/ws/insertar_noticia.php");
                nameValuePairs = new ArrayList<NameValuePair>(8);
                nameValuePairs.add(new BasicNameValuePair("idUsuario", params[0]));
                nameValuePairs.add(new BasicNameValuePair("idCategoria",params[1]));
                nameValuePairs.add(new BasicNameValuePair("idCategoriaDetalle", params[2]));
                nameValuePairs.add(new BasicNameValuePair("titulo", params[3]));
                nameValuePairs.add(new BasicNameValuePair("subtitulo", params[4]));
                nameValuePairs.add(new BasicNameValuePair("cuerpo", params[5]));
                nameValuePairs.add(new BasicNameValuePair("descripcion", params[6]));
                nameValuePairs.add(new BasicNameValuePair("fecha_hora_creacion", params[7]));

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                httpClient.execute(httpPost);
                resul = true;
            }
            catch (UnsupportedEncodingException ex)
            {
                resul = false;
                ex.printStackTrace();
            }catch (ClientProtocolException ex)
            {
                resul = false;
                ex.printStackTrace();
            }catch (IOException ex)
            {
                resul = false;
                ex.printStackTrace();
            }catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                Toast.makeText(getContext(), "Noticia en validacion!", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getContext(), "Error al crear la actividad, intente nuevamente", Toast.LENGTH_LONG).show();
                btnRegistrarNot.setEnabled(true);
            }
        }
    }

}
