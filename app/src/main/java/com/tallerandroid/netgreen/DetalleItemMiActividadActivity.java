package com.tallerandroid.netgreen;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
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
 * Created by yesce on 08/10/2017.
 */

public class DetalleItemMiActividadActivity extends AppCompatActivity {
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
    private SQLiteDatabase db;
    int intUsuario;

    EditText etNombrePub;
    EditText etDescripcionPub;
    DatePicker dtFechaAct;
    TimePicker tmHoraAct;


    private Button btnGuardarCambios = null;
    private Button btnEliminar = null;
    private Button btnValidar = null;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_item_actividad);
        etNombrePub = (EditText) findViewById(R.id.etNombre_ModificarAct);
        etDescripcionPub = (EditText) findViewById(R.id.etDescripcion_ModificarAct);
        dtFechaAct = (DatePicker) findViewById(R.id.dtFecha_ModificarAct);
        tmHoraAct = (TimePicker) findViewById(R.id.tmHora_ModificarAct);
        btnGuardarCambios = (Button)findViewById(R.id.btnGuardar_ModificarAct);

        spinnerCategorias = (Spinner) findViewById(R.id.spnCategoria_ModificarAct);
        TareaWSCargarSpinnerCatAct cargarCat = new TareaWSCargarSpinnerCatAct();
        cargarCat.execute(spinnerCategorias.toString());
        spinnerCategorias.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {
                        spinnerSubCategorias = (Spinner) findViewById(R.id.spnSubcategoria_ModficarAct);

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
        idPublicacion = intent.getStringExtra(FragmentPerfilActividades.ID_ACTIVIDAD);
        tipoPublicacion = intent.getStringExtra(FragmentPerfilActividades.TIPO_ACTIVIDAD);
        TareaWSCargarDetalle tarea3 = new TareaWSCargarDetalle();
        tarea3.execute(idPublicacion, tipoPublicacion);

        UsuarioLogueadoSQLiteHelper usdbh = new UsuarioLogueadoSQLiteHelper(this, "DBUsuario", null, 1);
        db = usdbh.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT idUsuario FROM Usuario", null);
        if (c.moveToFirst()) {
            intUsuario = c.getInt(0);
        }

        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int intCategoria = 1;
                int intSubcategoria = 1;
                //int intUsuario = 0;
                String strFechaActividad ="";

                Date fechaActual = new Date();
                DateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
                DateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
                String strFechaPublicacion = formatoFecha.format(fechaActual) + " " +formatoHora.format(fechaActual);

                try {
                    strFechaActividad = Integer.toString(dtFechaAct.getYear());
                    String longitudMes = Integer.toString(dtFechaAct.getMonth());
                    if(longitudMes.length() == 1)
                        strFechaActividad+= "-0" + Integer.toString(dtFechaAct.getMonth());
                    else
                        strFechaActividad+= "-" + Integer.toString(dtFechaAct.getMonth());
                    String longitudDia = Integer.toString(dtFechaAct.getDayOfMonth());
                    if(longitudDia.length() ==1)
                        strFechaActividad+="-0" + Integer.toString(dtFechaAct.getDayOfMonth());
                    else
                        strFechaActividad+="-" + Integer.toString(dtFechaAct.getDayOfMonth());

                    if(Build.VERSION.SDK_INT >= 23) {
                        String longitudHora = Integer.toString(tmHoraAct.getHour());
                        String longitudMin = Integer.toString(tmHoraAct.getMinute());
                        if(longitudHora.length() == 1)
                            strFechaActividad += " 0" + Integer.toString(tmHoraAct.getHour());
                        else
                            strFechaActividad += " " + Integer.toString(tmHoraAct.getHour());

                        if(longitudMin.length() == 1)
                            strFechaActividad += ":0"+tmHoraAct.getMinute()+":00";
                        else
                            strFechaActividad += ":"+tmHoraAct.getMinute()+":00";

                    } else {
                        String longitudHora = Integer.toString(tmHoraAct.getCurrentHour());
                        String longitudMin = Integer.toString(tmHoraAct.getCurrentMinute());
                        if(longitudHora.length() == 1)
                            strFechaActividad += " 0" + tmHoraAct.getCurrentHour();
                        else
                            strFechaActividad += " " + tmHoraAct.getCurrentHour();

                        if(longitudMin.length() == 1)
                            strFechaActividad+=":0"+tmHoraAct.getCurrentMinute()+":00";
                        else
                            strFechaActividad+=":"+tmHoraAct.getCurrentMinute()+":00";
                    }
                }
                catch (Exception ex)
                {
                }
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

                TareaWSActualizarAct tarea = new TareaWSActualizarAct();
                tarea.execute(
                        idPublicacion,
                        Integer.toString(intCategoria),
                        Integer.toString(intSubcategoria),
                        etNombrePub.getText().toString(),
                        etDescripcionPub.getText().toString(),
                        strFechaActividad);

            }
        });


        btnEliminar = (Button)findViewById(R.id.btnCancelarActividad);
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TareaWSEstatusAct tarea = new TareaWSEstatusAct();
                tarea.execute(
                        idPublicacion,
                        "cancelada");
                TareaWSActualizarPuntos tarea2 = new TareaWSActualizarPuntos();
                tarea2.execute(Integer.toString(intUsuario), "15", "restar");
            }
        });


        btnValidar = (Button)findViewById(R.id.btnValidarActividad);
        btnValidar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TareaWSEstatusAct tarea = new TareaWSEstatusAct();
                tarea.execute(
                        idPublicacion,
                        "completada");
            }
        });
    }

    private class TareaWSCargarDetalle extends AsyncTask<String,Integer,Boolean> {

        private String nombrePublicacion;
        private String descripcion;
        private String fecha;

        protected Boolean doInBackground(String... params) {
            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String idPub = params[0];
            String tipoPub = params[1];

            HttpGet del = new HttpGet("http://netgreen.org.mx/ws/consulta_publicaciones.php?idPublicacion="+idPub+"&tipo=Actividad");

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONArray respJSON = new JSONArray(respStr);

                for(int i=0; i < respJSON.length(); i++) {
                    JSONObject jsonobject = respJSON.getJSONObject(i);
                    nombrePublicacion = jsonobject.getString("nombrePublicacion");
                    descripcion = jsonobject.getString("descripcion");
                    fecha  = jsonobject.getString("fecha_hora_actividad");
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


                    etNombrePub.setText(nombrePublicacion);
                    etDescripcionPub.setText(descripcion);

                    String[] parts = fecha.split(" ");
                    String fechaAux = parts[0]; // 123
                    String horaAux = parts[1]; // 654321

                    String[] parts2 = fechaAux.split("-");
                    String anio = parts2[0]; // 123
                    String mes = parts2[1]; // 654321
                    String dia = parts2[2 ]; // 123

                    String[] parts3 = horaAux.split(":");
                    String hora = parts3[0]; // 123
                    String min = parts3[1]; // 654321
                    dtFechaAct.updateDate(Integer.parseInt(anio), Integer.parseInt(mes), Integer.parseInt(dia));

                    if(Build.VERSION.SDK_INT >= 23) {
                        tmHoraAct.setHour(Integer.parseInt(hora));
                        tmHoraAct.setMinute(Integer.parseInt(min));
                    } else {
                        tmHoraAct.setCurrentHour(Integer.parseInt(hora));
                        tmHoraAct.setCurrentMinute(Integer.parseInt(min));
                    }

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

    private class TareaWSActualizarAct extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            try
            {
                HttpClient httpClient;
                List<NameValuePair> nameValuePairs;
                HttpPost httpPost;
                httpClient = new DefaultHttpClient();

                httpPost = new HttpPost("http://netgreen.org.mx/ws/modificar_actividad.php");
                nameValuePairs = new ArrayList<NameValuePair>(7 );
                nameValuePairs.add(new BasicNameValuePair("idActividad", params[0]));
                nameValuePairs.add(new BasicNameValuePair("idCategoria",params[1]));
                nameValuePairs.add(new BasicNameValuePair("idCategoriaDetalle", params[2]));
                nameValuePairs.add(new BasicNameValuePair("nombreActividad", params[3]));
                nameValuePairs.add(new BasicNameValuePair("descripcion", params[4]));
                nameValuePairs.add(new BasicNameValuePair("fecha_hora_actividad", params[5]));

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

    private class TareaWSEstatusAct extends AsyncTask<String,Integer,Boolean> {

        String estatus;
        protected Boolean doInBackground(String... params) {

            estatus = params[1];
            boolean resul = true;

            try
            {
                HttpClient httpClient;
                List<NameValuePair> nameValuePairs;
                HttpPost httpPost;
                httpClient = new DefaultHttpClient();

                httpPost = new HttpPost("http://netgreen.org.mx/ws/modificar_actividad.php");
                nameValuePairs = new ArrayList<NameValuePair>(7 );
                nameValuePairs.add(new BasicNameValuePair("idActividad", params[0]));
                nameValuePairs.add(new BasicNameValuePair("estatus",params[1]));
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
                if(estatus.equals("completada")) {
                    Toast.makeText(getApplication(), "Tu actividad ha sido concluida!! :D", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplication(), "Tu actividad ha sido cancelada :(", Toast.LENGTH_LONG).show();
                }

                Intent intent = new Intent(getApplication(), PerfilActivity.class);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(getApplication(), "Error al validar la actividad", Toast.LENGTH_LONG).show();
                btnGuardarCambios.setEnabled(true);
            }
        }
    }

    private class TareaWSActualizarPuntos extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            try
            {
                HttpClient httpClient;
                List<NameValuePair> nameValuePairs;
                HttpPost httpPost;
                httpClient = new DefaultHttpClient();

                httpPost = new HttpPost("http://netgreen.org.mx/ws/modificar_usuario.php");
                nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("idUsuario", params[0]));
                nameValuePairs.add(new BasicNameValuePair("puntos", params[1]));
                nameValuePairs.add(new BasicNameValuePair("accion", params[2]));

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
            }
            else
            {

            }
        }
    }

}