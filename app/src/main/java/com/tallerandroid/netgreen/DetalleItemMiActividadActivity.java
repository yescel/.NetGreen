package com.tallerandroid.netgreen;

import android.content.Intent;
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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

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

        spinnerCategorias = (Spinner) findViewById(R.id.spnCategoria_ModificarAct);
        TareaWSCargarSpinnerCatAct cargarCat = new TareaWSCargarSpinnerCatAct();
        cargarCat.execute(spinnerCategorias.toString());
        spinnerCategorias.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {
                        spinnerSubCategorias = (Spinner) findViewById(R.id.spnSubcategoria_ModficarAct);

                        TareaWSCargarSpinnerSubCatAct cargarSubcat = new TareaWSCargarSpinnerSubCatAct();
                        cargarSubcat.execute(Integer.toString(position + 1));
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        Intent intent = getIntent();
        String idPublicacion = intent.getStringExtra(FragmentInicio.ID_ACTIVIDAD);
        String tipoPublicacion = intent.getStringExtra(FragmentInicio.TIPO_ACTIVIDAD);


        TareaWSCargarDetalle tarea3 = new TareaWSCargarDetalle();
        tarea3.execute(idPublicacion, tipoPublicacion);


        btnGuardarCambios = (Button)findViewById(R.id.btnGuardar_ModificarAct);
        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                DialogoGuardarConfirmacion dialogo = new DialogoGuardarConfirmacion();
                dialogo.show(fragmentManager, "tagPersonalizado");
            }
        });


        btnEliminar = (Button)findViewById(R.id.btnCancelarActividad);
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                DialogoEliminarConfirmacion dialogo = new DialogoEliminarConfirmacion();
                dialogo.show(fragmentManager, "tagPersonalizado");
            }
        });


        btnValidar = (Button)findViewById(R.id.btnValidarActividad);
        btnValidar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                DialogoValidarActividad dialogo = new DialogoValidarActividad();
                dialogo.show(fragmentManager, "tagPersonalizado");
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
                    for (int i = 0; i < idSubcategorias.length; i++) {
                        String aux3 = Integer.toString(idCategoriaDetalle);
                        String aux4 = idSubcategorias[i][0];
                        if (aux3.equals(aux4)) {
                            seleccionSubCat = i;
                        }
                    }
                    spinnerSubCategorias.setSelection(seleccionSubCat);

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

}