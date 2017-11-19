package com.tallerandroid.netgreen;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yesce on 08/10/2017.
 */

public class DetalleItemMiNoticiaActivity extends AppCompatActivity {
    private Button btnGuardarCambios = null;
    private Button btnEliminar = null;
    EditText etTitulo;
    EditText etDescripcion;
    EditText etSubtitulo;
    EditText etCuerpo;
    Spinner spinnerCategorias;
    Spinner spinnerSubCategorias;
    String[] categorias;
    String[] subcategorias;
    String[][] idCategorias;
    String[][] idSubcategorias;

    int seleccionCat = 0;
    int seleccionSubCat = 0;
    private int idCategoria = 0;
    private int idCategoriaDetalle = 0;
    String idPublicacion = "";
    String tipoPublicacion = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_item_noticia);
        etTitulo = (EditText) findViewById(R.id.etTitulo_ModificarNot);
        etDescripcion = (EditText) findViewById(R.id.etDescripcion_ModificarNot);
        etSubtitulo = (EditText) findViewById(R.id.etSubtitulo_ModificarNot);
        etCuerpo = (EditText) findViewById(R.id.etCuerpo_ModificarNot);

        spinnerCategorias = (Spinner) findViewById(R.id.spnCategoria_ModificarNot);
        TareaWSCargarSpinnerCatAct cargarCat = new TareaWSCargarSpinnerCatAct();
        cargarCat.execute(spinnerCategorias.toString());
        spinnerCategorias.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {
                        spinnerSubCategorias = (Spinner) findViewById(R.id.spnSubcategoria_ModficarNot);

                        TareaWSCargarSpinnerSubCatAct cargarSubcat = new TareaWSCargarSpinnerSubCatAct();
                        cargarSubcat.execute(Integer.toString(position + 1));

                        if(idSubcategorias != null) {
                            for (int i = 0; i < idSubcategorias.length; i++) {
                                String aux3 = Integer.toString(idCategoriaDetalle);
                                String aux4 = idSubcategorias[i][0];
                                if (aux3.equals(aux4)) {
                                    seleccionSubCat = i;
                                }
                            }
                            spinnerSubCategorias.setSelection(seleccionSubCat);
                        }

                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        Intent intent = getIntent();
        idPublicacion = intent.getStringExtra(FragmentPerfilNoticias.ID_NOTICIA);
        tipoPublicacion = intent.getStringExtra(FragmentPerfilNoticias.TIPO_NOTICIA);
        TareaWSCargarDetalle tarea3 = new TareaWSCargarDetalle();
        tarea3.execute(idPublicacion, tipoPublicacion);

        btnGuardarCambios = (Button)findViewById(R.id.btnGuardar_ModificarNot);
        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int intCategoria = 1;
                int intSubcategoria = 1;
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

                TareaWSActualizarNot tarea = new TareaWSActualizarNot();
                tarea.execute(
                        idPublicacion,
                        etTitulo.getText().toString(),
                        etSubtitulo.getText().toString(),
                        etCuerpo.getText().toString(),
                        etDescripcion.getText().toString(),
                        Integer.toString(intCategoria),
                        Integer.toString(intSubcategoria)
                        );
            }
        });


        btnEliminar = (Button)findViewById(R.id.btnEliminar_ModificarNot);
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                DialogoEliminarConfirmacion dialogo = new DialogoEliminarConfirmacion();
                dialogo.show(fragmentManager, "tagPersonalizado");
                TareaWSEliminarNot tarea4 = new TareaWSEliminarNot();
                tarea4.execute(idPublicacion);
            }
        });

    }

    private class TareaWSCargarDetalle extends AsyncTask<String,Integer,Boolean> {

        private String titulo;
        private String subtitulo;
        private String cuerpo;
        private String descripcion;

        protected Boolean doInBackground(String... params) {
            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String idPub = params[0];
            String tipoPub = params[1];

            HttpGet del = new HttpGet("http://netgreen.org.mx/ws/consulta_publicaciones.php?idPublicacion="+idPub+"&tipo=Noticia");

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONArray respJSON = new JSONArray(respStr);

                for(int i=0; i < respJSON.length(); i++) {
                    JSONObject jsonobject = respJSON.getJSONObject(i);
                    titulo = jsonobject.getString("titulo");
                    subtitulo = jsonobject.getString("subtitulo");
                    cuerpo = jsonobject.getString("cuerpo");
                    descripcion = jsonobject.getString("descripcion");
                    idCategoria = jsonobject.getInt("idCategoria");
                    idCategoriaDetalle = jsonobject.getInt("idCategoriaDetalle");

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
                try {
                    for (int i = 0; i < idCategorias.length; i++) {
                        String aux = Integer.toString(idCategoria);
                        String aux2 = idCategorias[i][0];
                        if (aux.equals(aux2)) {
                            seleccionCat = i;
                        }
                    }
                    spinnerCategorias.setSelection(seleccionCat);

                    etTitulo.setText(titulo);
                    etSubtitulo.setText(subtitulo);
                    etCuerpo.setText(cuerpo);
                    etDescripcion.setText(descripcion);

                }catch (Exception ex)
                {
                    String e =ex.toString();
                }
            }
            else
            {

            }
        }
    }

    private class TareaWSCargarSpinnerCatAct extends AsyncTask<String,Integer,Boolean> {

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
                ArrayAdapter<String> adaptador =  new ArrayAdapter<String>(getApplication(),  android.R.layout.simple_spinner_item, categorias);
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
                ArrayAdapter<String> adaptador =  new ArrayAdapter<String>(getApplication(),  android.R.layout.simple_spinner_item, subcategorias);
                adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerSubCategorias.setAdapter(adaptador);

            }
            else
            {

            }
        }
    }

    private class TareaWSActualizarNot extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            try
            {
                HttpClient httpClient;
                List<NameValuePair> nameValuePairs;
                HttpPost httpPost;
                httpClient = new DefaultHttpClient();

                httpPost = new HttpPost("http://netgreen.org.mx/ws/modificar_noticia.php");
                nameValuePairs = new ArrayList<NameValuePair>(7 );
                nameValuePairs.add(new BasicNameValuePair("idNoticia", params[0]));
                nameValuePairs.add(new BasicNameValuePair("titulo", params[1]));
                nameValuePairs.add(new BasicNameValuePair("subtitulo", params[2]));
                nameValuePairs.add(new BasicNameValuePair("cuerpo", params[3]));
                nameValuePairs.add(new BasicNameValuePair("descripcion", params[4]));
                nameValuePairs.add(new BasicNameValuePair("idCategoria",params[5]));
                nameValuePairs.add(new BasicNameValuePair("idCategoriaDetalle", params[6]));

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
                Toast.makeText(getApplication(), "Tus cambios se han guardado exitosamente", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplication(), DashboardActivity.class);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(getApplication(), "Error al guardar los cambios, verifique sus datos", Toast.LENGTH_LONG).show();
                btnGuardarCambios.setEnabled(true);
            }
        }
    }

    private class TareaWSEliminarNot extends AsyncTask<String,Integer,Boolean> {
        protected Boolean doInBackground(String... params) {
            boolean resul = true;

            try
            {
                HttpClient httpClient;
                List<NameValuePair> nameValuePairs;
                HttpPost httpPost;
                httpClient = new DefaultHttpClient();

                httpPost = new HttpPost("http://netgreen.org.mx/ws/eliminar_noticia.php");
                nameValuePairs = new ArrayList<NameValuePair>(7 );
                nameValuePairs.add(new BasicNameValuePair("idNoticia", params[0]));
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
                Toast.makeText(getApplication(), "Tu noticia ha sido eliminada :(", Toast.LENGTH_LONG).show();


                Intent intent = new Intent(getApplication(), PerfilActivity.class);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(getApplication(), "Error al eliminar la noticia", Toast.LENGTH_LONG).show();
                btnGuardarCambios.setEnabled(true);
            }
        }
    }


}