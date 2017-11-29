package com.tallerandroid.netgreen;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by yesce on 16/05/2017.
 */

public class DetalleItemInicioActivity extends AppCompatActivity {
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
    TextView tvTipo;
    EditText etComentario;
    Button btnComentar;
    Button btnParticipar;
    Button btnMeInteresa;
    boolean nuevoRegistro = false;
    boolean boolInteresado = false;
    boolean boolParticipante = false;

    int idCategoria = 0;
    int idCategoriaDetalle = 0;

    ListView lvComentarios;
    AdaptadorListaComentarios adaptadorLista;
    ArrayList<Comentario> comentarios;
    Comentario mComentario;
    int intUsuario = 0;

    private SQLiteDatabase db;


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
        tvTipo = (TextView) this.findViewById(R.id.tvTipo_Detalle);
        etComentario = (EditText) this.findViewById(R.id.etComentario_Detalle);
        btnComentar = (Button) this.findViewById(R.id.btnComentar);
        btnParticipar = (Button) this.findViewById(R.id.btnUnirme);
        btnMeInteresa = (Button) this.findViewById(R.id.btnMeInteresa);

        Intent intent = getIntent();
        final String idPublicacion = intent.getStringExtra(FragmentInicio.ID_ACTIVIDAD);
        final String tipoPublicacion = intent.getStringExtra(FragmentInicio.TIPO_ACTIVIDAD);

        intUsuario = 0;
        UsuarioLogueadoSQLiteHelper usdbh = new UsuarioLogueadoSQLiteHelper(getApplication(), "DBUsuario", null, 1);
        db = usdbh.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT idUsuario FROM Usuario", null);
        if (c.moveToFirst()) {
            intUsuario = c.getInt(0);
        }

        TareaWSCargarCatDetalle tarea1 = new TareaWSCargarCatDetalle();
        tarea1.execute();

        TareaWSCargarSubCatDetalle tarea2 = new TareaWSCargarSubCatDetalle();
        tarea2.execute();

        TareaWSCargarDetalle tarea3 = new TareaWSCargarDetalle();
        tarea3.execute(idPublicacion, tipoPublicacion);

        comentarios = new ArrayList<>();
        lvComentarios = (ListView) findViewById(R.id.lvComentarios);
        adaptadorLista = new AdaptadorListaComentarios(this, comentarios);

        if(tipoPublicacion.equals("Actividad"))
        {
            TareaWSCargarParSegAct tarea5 = new TareaWSCargarParSegAct();
            tarea5.execute(idPublicacion);

            TareaWSVerificarSegAct tarea7 = new TareaWSVerificarSegAct();
            tarea7.execute(idPublicacion, Integer.toString(intUsuario));
        }
        if(tipoPublicacion.equals("Noticia"))
        {
            btnParticipar.setVisibility(View.INVISIBLE);
            tvParticipantes.setVisibility(View.INVISIBLE);
            TareaWSCargarSegNot tarea6 = new TareaWSCargarSegNot();
            tarea6.execute(idPublicacion);

            TareaWSVerificarSegNot tarea7 = new TareaWSVerificarSegNot();
            tarea7.execute(idPublicacion, Integer.toString(intUsuario));
        }


        TareaWSCargarComentarios tarea4 = new TareaWSCargarComentarios();
        tarea4.execute(idPublicacion, tipoPublicacion);

        btnComentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date fechaActual = new Date();
                DateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
                DateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
                String strFechaComentario = formatoFecha.format(fechaActual) + " " +formatoHora.format(fechaActual);

                TareaWSInsertarComentario tarea = new TareaWSInsertarComentario();
                tarea.execute(
                        idPublicacion,
                        tipoPublicacion,
                        Integer.toString(intUsuario),
                        etComentario.getText().toString(),
                        strFechaComentario
                        );

                //comentarios = new ArrayList<>();
                adaptadorLista.clear();
                comentarios.clear();

                TareaWSCargarComentarios tarea4 = new TareaWSCargarComentarios();
                tarea4.execute(idPublicacion, tipoPublicacion);

                //adaptadorLista.notifyDataSetChanged();

            }
        });

        btnParticipar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int participa = 0;
                if(boolParticipante == true) {
                    participa = 0;
                    btnParticipar.setTextColor(Color.BLACK);
                    TareaWSActualizarPuntos tarea2 = new TareaWSActualizarPuntos();
                    tarea2.execute(Integer.toString(intUsuario), "10", "restar");
                }
                else {
                    participa = 1;
                    btnParticipar.setTextColor(Color.rgb(076,145,065));

                    TareaWSActualizarPuntos tarea2 = new TareaWSActualizarPuntos();
                    tarea2.execute(Integer.toString(intUsuario), "10", "sumar");
                }
                if(nuevoRegistro == true)
                {

                    TareaWSInsertarSegParAct tarea = new TareaWSInsertarSegParAct();
                    tarea.execute(
                            Integer.toString(intUsuario),
                            idPublicacion,
                            "participante",
                            Integer.toString(participa)
                    );
                }else
                {
                    TareaWSActualizarSegParAct tarea = new TareaWSActualizarSegParAct();
                    tarea.execute(
                            Integer.toString(intUsuario),
                            idPublicacion,
                            "participante",
                            Integer.toString(participa)
                    );
                }
                ///////////crear funcion actualizar participante/seguidores
                TareaWSVerificarSegAct tarea7 = new TareaWSVerificarSegAct();
                tarea7.execute(idPublicacion, Integer.toString(intUsuario));

                if(tipoPublicacion.equals("Actividad"))
                {
                    TareaWSCargarParSegAct tarea5 = new TareaWSCargarParSegAct();
                    tarea5.execute(idPublicacion);
                }
                ///fin funcioon (pendiente de crear)

            }
        });


        btnMeInteresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int interesa = 0;
                if(boolInteresado == true) {
                    interesa = 0;
                    btnMeInteresa.setTextColor(Color.BLACK);
                }
                else {
                    interesa = 1;
                    btnMeInteresa.setTextColor(Color.rgb(076,145,065));
                }
                if(nuevoRegistro == true)
                {
                    if(tipoPublicacion.equals("Actividad")) {
                        TareaWSInsertarSegParAct tarea = new TareaWSInsertarSegParAct();
                        tarea.execute(
                                Integer.toString(intUsuario),
                                idPublicacion,
                                "seguidor",
                                Integer.toString(interesa)
                        );
                    }else{
                        TareaWSInsertarSegNot tarea = new TareaWSInsertarSegNot();
                        tarea.execute(
                                Integer.toString(intUsuario),
                                idPublicacion,
                                Integer.toString(interesa)
                        );
                    }
                }else
                {
                    if(tipoPublicacion.equals("Actividad")) {
                        TareaWSActualizarSegParAct tarea = new TareaWSActualizarSegParAct();
                        tarea.execute(
                                Integer.toString(intUsuario),
                                idPublicacion,
                                "seguidor",
                                Integer.toString(interesa)
                        );
                    }else{
                        TareaWSActualizarSegNot tarea = new TareaWSActualizarSegNot();
                        tarea.execute(
                                Integer.toString(intUsuario),
                                idPublicacion,
                                Integer.toString(interesa)
                        );
                    }
                }

                if(tipoPublicacion.equals("Actividad"))
                {
                    TareaWSVerificarSegAct tarea7 = new TareaWSVerificarSegAct();
                    tarea7.execute(idPublicacion, Integer.toString(intUsuario));

                    TareaWSCargarParSegAct tarea5 = new TareaWSCargarParSegAct();
                    tarea5.execute(idPublicacion);
                }
                if(tipoPublicacion.equals("Noticia"))
                {
                    btnParticipar.setVisibility(View.INVISIBLE);
                    tvParticipantes.setVisibility(View.INVISIBLE);
                    TareaWSVerificarSegNot tarea7 = new TareaWSVerificarSegNot();
                    tarea7.execute(idPublicacion, Integer.toString(intUsuario));

                    TareaWSCargarSegNot tarea6 = new TareaWSCargarSegNot();
                    tarea6.execute(idPublicacion);
                }
            }
        });


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
        private String fechaAct = "";

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
                    descripcion = jsonobject.getString("descripcion");
                    fecha  = jsonobject.getString("fecha_hora_creacion");
                    if(tipoPub.equals("Actividad")) {
                        fechaAct = jsonobject.getString("fecha_hora_actividad");
                        nombrePublicacion = jsonobject.getString("nombrePublicacion");
                    }else{
                        nombrePublicacion = jsonobject.getString("titulo");
                    }
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
                    tvTipo.setText(tipoPublicacion);
                    tvDescripcion.setText(descripcion + "\nFecha: " + fechaAct);
                    tvFecha.setText(fecha);
                    String categoria = "";
                    for (int i = 0; i < idCategorias.length; i++) {
                        String aux = Integer.toString(idCategoria);
                        String aux2 = idCategorias[i][0];
                        if (aux.equals(aux2)) {
                            categoria = idCategorias[i][1];
                            break;
                        }
                    }
                    tvCategoria.setText(categoria);
                    String detallecategoria = "";
                    for (int i = 0; i < idSubcategorias.length; i++) {
                        String aux = Integer.toString(idCategoriaDetalle);
                        String aux2 = idSubcategorias[i][0];
                        if (aux.equals(aux2)) {
                            detallecategoria = idSubcategorias[i][1];
                            break;
                        }
                    }
                    tvCategoria.setText(categoria);
                    tvSubcategoria.setText(detallecategoria);
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

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del = new HttpGet("http://netgreen.org.mx/ws/consulta_detallecategoria.php");

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

    private class TareaWSCargarComentarios extends AsyncTask<String,Integer,Boolean> {

        private int id;
        private Bitmap imagen;
        private String comentario = "";
        private String fecha="";

        protected Boolean doInBackground(String... params) {
            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String idPub = params[0];
            String tipoPub = params[1];

            HttpGet del = new HttpGet("http://netgreen.org.mx/ws/consulta_comentarios.php?idPublicacion="+idPub+"&tipo="+tipoPub);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                if(respJSON.length() == 0)
                    resul = false;

                 for(int i=0; i < respJSON.length(); i++) {

                    JSONObject jsonobject = respJSON.getJSONObject(i);
                    byte[] decodeString = Base64.decode(jsonobject.getString("imagen"), Base64.DEFAULT);
                    imagen = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
                    comentario = jsonobject.getString("comentario");
                    fecha = jsonobject.getString("fecha_comentario");


                    mComentario = new Comentario();
                    mComentario.setComentario(comentario);
                     if(imagen!=null) {
                         mComentario.setImagen(imagen);
                     }
                     else {
                         Bitmap ic_user = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                                 R.drawable.ic_user);
                         mComentario.setImagen(ic_user);
                     }
                    mComentario.setFecha(fecha);

                    comentarios.add(mComentario);
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
                try
                {
                    lvComentarios.setAdapter(adaptadorLista);
                }catch (Exception ex)
                {
                    String e =ex.toString();
                }
            }
            else

            {
                lvComentarios.setEmptyView(findViewById(R.id.cdVacio));

            }
        }
    }

    private class TareaWSInsertarComentario extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            try
            {
                HttpClient httpClient;
                List<NameValuePair> nameValuePairs;
                HttpPost httpPost;
                httpClient = new DefaultHttpClient();

                httpPost = new HttpPost("http://netgreen.org.mx/ws/insertar_comentario.php");
                nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair("idPublicacion", params[0]));
                nameValuePairs.add(new BasicNameValuePair("tipo",params[1]));
                nameValuePairs.add(new BasicNameValuePair("idUsuario", params[2]));
                nameValuePairs.add(new BasicNameValuePair("comentario", params[3]));
                nameValuePairs.add(new BasicNameValuePair("fecha_comentario", params[4]));

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
                Toast.makeText(getApplication(), "Comentario agregado correctamente", Toast.LENGTH_LONG).show();
                btnComentar.setEnabled(true);
                etComentario.setText("");
            }
            else
            {
                Toast.makeText(getApplication(), "Error al publicar el comentario", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class TareaWSVerificarSegAct extends AsyncTask<String,Integer,Boolean> {
        private String participante = "";
        private String seguidor = "";


        protected Boolean doInBackground(String... params) {
            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String idPub = params[0];
            String usu = params[1];

            HttpGet del = new HttpGet("http://netgreen.org.mx/ws/consulta_verificacion_seguidor_act.php?idPublicacion="+idPub+"&idUsuario="+usu);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                if(respJSON.length() == 0)
                    nuevoRegistro = true;

                for(int i=0; i < respJSON.length(); i++) {
                    JSONObject jsonobject = respJSON.getJSONObject(i);
                    participante = jsonobject.getString("participante");
                    seguidor = jsonobject.getString("interesado");
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
                try
                {
                    if(!participante.equals("0")) {
                        btnParticipar.setTextColor(Color.rgb(076,145,065));
                        boolParticipante = true;
                        nuevoRegistro = false;
                    }
                    if(!seguidor.equals("0")) {
                        btnMeInteresa.setTextColor(Color.rgb(076,145,065));
                        boolInteresado = true;
                        nuevoRegistro = false;
                    }
                    if(participante.equals("0")) {
                        btnParticipar.setTextColor(Color.BLACK);
                        boolParticipante = false;
                    }
                    if(seguidor.equals("0")) {
                        btnMeInteresa.setTextColor(Color.BLACK);
                        boolInteresado = false;
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

    private class TareaWSCargarParSegAct extends AsyncTask<String,Integer,Boolean> {
        private int noParticipantes = 0;
        private int noSeguidores = 0;


        protected Boolean doInBackground(String... params) {
            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String idPub = params[0];

            HttpGet del = new HttpGet("http://netgreen.org.mx/ws/consulta_seguidores_act.php?idPublicacion="+idPub);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);
                if(respStr.equals("[{\"noParticipantes\":null,\"noInteresados\":null}]")) {
                    nuevoRegistro = true;
                }
                else {
                    for (int i = 0; i < respJSON.length(); i++) {
                        JSONObject jsonobject = respJSON.getJSONObject(i);
                        noParticipantes = jsonobject.getInt("noParticipantes");
                        noSeguidores = jsonobject.getInt("noInteresados");
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
                tvParticipantes.setText("Participantes: "+ Integer.toString(noParticipantes));
                tvSeguidores.setText("Seguidores: "+ Integer.toString(noSeguidores));
            }
            else
            {

            }
        }
    }

    private class TareaWSCargarSegNot extends AsyncTask<String,Integer,Boolean> {
        private int noSeguidores = 0;

        protected Boolean doInBackground(String... params) {
            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String idPub = params[0];

            HttpGet del = new HttpGet("http://netgreen.org.mx/ws/consulta_seguidores_not.php?idPublicacion="+idPub);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                if(respStr.equals("[{\"noInteresados\":null}]")) {
                    nuevoRegistro = true;
                }
                else {
                    for (int i = 0; i < respJSON.length(); i++) {
                        JSONObject jsonobject = respJSON.getJSONObject(i);
                        noSeguidores = jsonobject.getInt("noInteresados");
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
                tvSeguidores.setText("Seguidores: "+ Integer.toString(noSeguidores));
            }
            else
            {
            }
        }
    }

    private class TareaWSInsertarSegParAct extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {
            String idUsuario = params[0];
            String idActividad = params[1];
            String par_seg = params[2];
            String valor = params[3];

            boolean resul = true;

            try
            {
                HttpClient httpClient;
                List<NameValuePair> nameValuePairs;
                HttpPost httpPost;
                httpClient = new DefaultHttpClient();

                httpPost = new HttpPost("http://netgreen.org.mx/ws/insertar_seguidores_act.php");
                nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("idUsuario", idUsuario));
                nameValuePairs.add(new BasicNameValuePair("idActividad", idActividad));
                if(par_seg.equals("participante"))
                    nameValuePairs.add(new BasicNameValuePair("participante", valor));
                if(par_seg.equals("seguidor"))
                    nameValuePairs.add(new BasicNameValuePair("interesado", valor));



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

    private class TareaWSActualizarSegParAct extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {
            String idUsuario = params[0];
            String idActividad = params[1];
            String par_seg = params[2];
            String valor = params[3];

            boolean resul = true;

            try
            {
                HttpClient httpClient;
                List<NameValuePair> nameValuePairs;
                HttpPost httpPost;
                httpClient = new DefaultHttpClient();

                httpPost = new HttpPost("http://netgreen.org.mx/ws/modificar_seguidores_act.php");
                nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("idUsuario", idUsuario));
                nameValuePairs.add(new BasicNameValuePair("idActividad", idActividad));
                if(par_seg.equals("participante"))
                    nameValuePairs.add(new BasicNameValuePair("participante", valor));
                if(par_seg.equals("seguidor"))
                    nameValuePairs.add(new BasicNameValuePair("interesado", valor));



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

    private class TareaWSVerificarSegNot extends AsyncTask<String,Integer,Boolean> {
        private String seguidor = "";

        protected Boolean doInBackground(String... params) {
            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String idPub = params[0];
            String usu = params[1];

            HttpGet del = new HttpGet("http://netgreen.org.mx/ws/consulta_verificacion_seguidor_not.php?idPublicacion="+idPub+"&idUsuario="+usu);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                if(respJSON.length() == 0)
                    nuevoRegistro = true;

                for(int i=0; i < respJSON.length(); i++) {
                    JSONObject jsonobject = respJSON.getJSONObject(i);
                    seguidor = jsonobject.getString("interesado");
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
                try
                {
                    if(!seguidor.equals("0")) {
                        btnMeInteresa.setTextColor(Color.rgb(076,145,065));
                        boolInteresado = true;
                        nuevoRegistro = false;
                    }
                    if(seguidor.equals("0")) {
                        btnMeInteresa.setTextColor(Color.BLACK);
                        boolInteresado = false;
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

    private class TareaWSInsertarSegNot extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {
            String idUsuario = params[0];
            String idNoticia = params[1];
            String valor = params[2];

            boolean resul = true;

            try
            {
                HttpClient httpClient;
                List<NameValuePair> nameValuePairs;
                HttpPost httpPost;
                httpClient = new DefaultHttpClient();

                httpPost = new HttpPost("http://netgreen.org.mx/ws/insertar_seguidores_not.php");
                nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("idUsuario", idUsuario));
                nameValuePairs.add(new BasicNameValuePair("idNoticia", idNoticia));
                nameValuePairs.add(new BasicNameValuePair("interesado", valor));



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

    private class TareaWSActualizarSegNot extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {
            String idUsuario = params[0];
            String idNoticia = params[1];
            String valor = params[2];

            boolean resul = true;

            try
            {
                HttpClient httpClient;
                List<NameValuePair> nameValuePairs;
                HttpPost httpPost;
                httpClient = new DefaultHttpClient();

                httpPost = new HttpPost("http://netgreen.org.mx/ws/modificar_seguidores_not.php");
                nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("idUsuario", idUsuario));
                nameValuePairs.add(new BasicNameValuePair("idNoticia", idNoticia));
                nameValuePairs.add(new BasicNameValuePair("interesado", valor));



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
