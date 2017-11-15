package com.tallerandroid.netgreen;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Base64;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.io.Console;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by yesce on 16/05/2017.
 */

public class DetalleItemInicioActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    String[][] idCategorias;
    String[][] idSubcategorias;

    ImageView ivUsuario;
    TextView tvUsuario;
    TextView tvPublicacion;
    TextView tvDescripcion;
    TextView tvFecha;
    TextView tvCategoria;
    TextView tvSubcategoria;
    TextView tvParticipantes;
    TextView tvSeguidores;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_item_inicio);
        ivUsuario = (ImageView) this.findViewById(R.id.ivUsuario_Detalle);
        tvUsuario = (TextView) this.findViewById(R.id.tvUsuario_Detalle);
        tvPublicacion = (TextView) this.findViewById(R.id.tvNombrePublicacion_Detalle);
        tvDescripcion = (TextView) this.findViewById(R.id.tvDescripcion_Detalle);
        tvFecha = (TextView) this. findViewById(R.id.tvFechaPublicacion_Detalle);
        tvCategoria = (TextView) this.findViewById(R.id.tvCategoria_Detalle);
        tvSubcategoria = (TextView) this.findViewById(R.id.tvSubCategoria_Detalle);
        tvParticipantes = (TextView) this.findViewById(R.id.tvParticipantes_Detalle);
        tvSeguidores = (TextView) this.findViewById(R.id.tvSeguidores_Detalle);

        Intent intent = getIntent();
        String idPublicacion = intent.getStringExtra(FragmentInicio.ID_ACTIVIDAD);
        String tipoPublicacion = intent.getStringExtra(FragmentInicio.TIPO_ACTIVIDAD);

        TareaWSCargarCatDetalle tarea1 = new TareaWSCargarCatDetalle();
        tarea1.execute();

        TareaWSCargarDetalle tarea3 = new TareaWSCargarDetalle();
        tarea3.execute(idPublicacion, tipoPublicacion);
    }


    private class TareaWSCargarDetalle extends AsyncTask<String,Integer,Boolean> {

        private int id;
        private Bitmap imagen;
        private String nombre = "";
        private String usuario= "";
        private String nombrePublicacion;
        private String descripcion;
        private String fecha;
        private int idCategoria;
        private int idCategoriaDetalle;
        private String tipoPublicacion;

        protected Boolean doInBackground(String... params) {
            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String idPub = params[0];
            String tipoPub = params[1];

            HttpGet del = new HttpGet("http://netgreen.org.mx/ws/consulta_publicaciones.php?idPublicacion="+idPub+"&tipo="+tipoPub);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                for(int i=0; i < respJSON.length(); i++) {
                    JSONObject jsonobject = respJSON.getJSONObject(i);
                    byte[] decodeString = Base64.decode(jsonobject.getString("imagen"), Base64.DEFAULT);
                    imagen = BitmapFactory.decodeByteArray(decodeString,0, decodeString.length);
                    nombre = jsonobject.getString("nombre");
                    usuario    = jsonobject.getString("usuario");
                    nombrePublicacion = jsonobject.getString("nombrePublicacion");
                    descripcion = jsonobject.getString("descripcion");
                    fecha  = jsonobject.getString("fecha_hora_creacion");
                    idCategoria = jsonobject.getInt("idCategoria");
                    idCategoriaDetalle = jsonobject.getInt("idCategoriaDetalle");
                    tipoPublicacion = jsonobject.getString("tipo");
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
                    ivUsuario.setImageBitmap(imagen);
                    if (usuario != "")
                        tvUsuario.setText(usuario);
                    else
                        tvUsuario.setText(nombre);
                    tvPublicacion.setText(nombrePublicacion);
                    tvDescripcion.setText(tipoPublicacion + "\n" + descripcion);
                    tvFecha.setText(fecha);
                    String categoria = "";
                    for (int i = 0; i < idCategorias.length; i++) {
                        String aux = Integer.toString(idCategoria);
                        String aux2 = idCategorias[i][0];
                        if (aux.equals(aux2)) {
                            categoria = idCategorias[i][1];
                            TareaWSCargarSubCatDetalle tarea2 = new TareaWSCargarSubCatDetalle();
                            tarea2.execute(idCategorias[i][0]);
                            break;
                        }
                    }
                    tvCategoria.setText(categoria);
                    for (int i = 0; i < idSubcategorias.length; i++) {
                        String aux3 = Integer.toString(idCategoriaDetalle);
                        String aux4 = idSubcategorias[i][0];
                        if (aux3.equals(aux4)) {
                            String subcategoria = idSubcategorias[i][1];
                            tvSubcategoria.setText(subcategoria);
                            break;
                        }
                    }
                    tvParticipantes.setText("Participantes: 10");
                    tvSeguidores.setText("Seguidores: 15");
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

    private class TareaWSCargarCatDetalle extends AsyncTask<String,Integer,Boolean> {

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
                idCategorias = new String[respJSON.length() - 1][2];
                for(int i=0; i < respJSON.length(); i++) {
                    JSONObject jsonobject = respJSON.getJSONObject(i);
                    idCategoria    = jsonobject.getInt("idCategoria");
                    nombreCat  = jsonobject.getString("nombre");
                    if(idCategoria != 0) {
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
            }
            else
            {

            }
        }
    }

    private class TareaWSCargarSubCatDetalle extends AsyncTask<String,Integer,Boolean> {

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
                idSubcategorias = new String[respJSON.length()][2];
                for(int i=0; i < respJSON.length(); i++) {
                    JSONObject jsonobject = respJSON.getJSONObject(i);
                    idSubcategoria = jsonobject.getInt("idCategoriaDetalle");
                    nombreSubCat  = jsonobject.getString("nombre");
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

            }
            else
            {

            }
        }
    }


}
