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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by yesce on 16/05/2017.
 */

public class DetalleItemInicioActivity extends AppCompatActivity {

    private SQLiteDatabase db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_item_inicio);
        Intent intent = getIntent();

        String idPublicacion = intent.getStringExtra(FragmentInicio.ID_ACTIVIDAD);
        String tipoPublicacion = intent.getStringExtra(FragmentInicio.TIPO_ACTIVIDAD);


        try {
            PubSeleccionadaSQLiteHelper usdbh = new PubSeleccionadaSQLiteHelper(this,
                    "DBPubSeleccionada", null, 1);
            db = usdbh.getWritableDatabase();
            Cursor c = db.rawQuery("SELECT idPublicacion, tipoPublicacion FROM PubSeleccionada",
                    null);
            if (c.moveToFirst()) {
                idPublicacion = c.getString(0);
                tipoPublicacion = c.getString(1);
            }


        }catch (Exception ex){
            String e = ex.toString();
        }

        //TareaWSCargarInicio tarea = new TareaWSCargarInicio();
        //tarea.execute(idPublicacion + tipoPublicacion);
    }

    @Override
    protected void onPause() {
        PubSeleccionadaSQLiteHelper usdbh = new PubSeleccionadaSQLiteHelper(this,
                "DBPubSeleccionada", null, 1);
        db = usdbh.getWritableDatabase();
        db.delete("PubSeleccionada", "", null);
        super.onPause();
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

            String idPub = params[0];
            String tipoPub = params[1];

            HttpGet del = new HttpGet("http://netgreen.org.mx/ws/consulta_publicaciones.php?");

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
