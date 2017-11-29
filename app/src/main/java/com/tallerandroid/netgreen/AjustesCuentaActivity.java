package com.tallerandroid.netgreen;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yesce on 01/10/2017.
 */

public class AjustesCuentaActivity extends FragmentActivity {

    private Button btnGuardarCambios;
    private Button btnSeleccionarFoto;
    private EditText etNombreCompleto;
    private EditText etNombreUsuario;
    private EditText etCorreo;
    private EditText etPass;
    private EditText etCambiarPass;
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_SELECT_PHOTO = 0;
    public static final int PICK_IMAGE = 1;
    Bitmap imageBitmap;
    ImageView ivPerfil;
    SQLiteDatabase db;
    String nombreCompleto = "";
    String nombreUsuario = "";
    String correo ="";
    Bitmap imagen;
    int intUsuario = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes_cuenta);


        btnGuardarCambios = (Button)findViewById(R.id.btnGuardarCambios);
        btnSeleccionarFoto = (Button)findViewById(R.id.btnSeleccionarFotoPerfil);
        etNombreCompleto = (EditText)findViewById(R.id.etNombreCompleto_Ajustes);
        etNombreUsuario = (EditText)findViewById(R.id.etNombreUsuario_Ajustes);
        etCorreo = (EditText) findViewById(R.id.etCorreoUsuario_Ajustesl);
        etPass = (EditText) findViewById(R.id.etPassword_Ajustes);
        etCambiarPass = (EditText) findViewById(R.id.etPassword2_Ajustes);
        ivPerfil = (ImageView)findViewById(R.id.ivPerfilAjustes);

        UsuarioLogueadoSQLiteHelper usdbh = new UsuarioLogueadoSQLiteHelper(getApplication(), "DBUsuario", null, 1);
        db = usdbh.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT idUsuario FROM Usuario", null);
        if (c.moveToFirst()) {
            intUsuario = c.getInt(0);
        }

        TareaWSCargarUsuarioPerfil tarea = new TareaWSCargarUsuarioPerfil();
        tarea.execute(Integer.toString(intUsuario));

        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //byte[] blob;
                String imagen ="";
                //ByteArrayOutputStream baos;
                if(imageBitmap != null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    imagen = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    imagen = imagen.replaceAll("\n", "");

                }
                else
                {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    ivPerfil.buildDrawingCache();
                    imageBitmap = ivPerfil.getDrawingCache();
                    imageBitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                    byte[] byteArray = stream.toByteArray();
                    imagen = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    imagen = imagen.replaceAll("\n", "");
                }
                if(!etPass.getText().toString().equals(etCambiarPass.getText().toString()))
                {
                    Toast.makeText(getBaseContext(), "El password no coincide", Toast.LENGTH_LONG).show();
                    btnGuardarCambios.setEnabled(true);
                }else {
                    TareaWSActualizarUsuario tarea = new TareaWSActualizarUsuario();
                    tarea.execute(
                            Integer.toString(intUsuario),
                            etNombreCompleto.getText().toString(),
                            etNombreUsuario.getText().toString(),
                            etCorreo.getText().toString(),
                            etPass.getText().toString());


                    TareaWSActualizarImagenUsuario tarea2 = new TareaWSActualizarImagenUsuario();
                    tarea2.execute(
                            Integer.toString(intUsuario),
                            imagen,
                            "Perfil");
                }
            }
        });

        btnSeleccionarFoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_CAMERA);
                    }
                }
                catch (Exception ex){

                }
            }
        });
    }

    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (reqCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            ivPerfil.setImageBitmap(imageBitmap);
        }
    }

    private class TareaWSActualizarUsuario extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            try
            {
                HttpClient httpClient;
                List<NameValuePair> nameValuePairs;
                HttpPost httpPost;
                httpClient = new DefaultHttpClient();

                httpPost = new HttpPost("http://netgreen.org.mx/ws/modificar_usuario.php");
                nameValuePairs = new ArrayList<NameValuePair>(6);
                nameValuePairs.add(new BasicNameValuePair("idUsuario", params[0]));
                nameValuePairs.add(new BasicNameValuePair("nombre", params[1]));
                nameValuePairs.add(new BasicNameValuePair("usuario",params[2]));
                nameValuePairs.add(new BasicNameValuePair("correo", params[3]));
                nameValuePairs.add(new BasicNameValuePair("passwd", params[4]));
                nameValuePairs.add(new BasicNameValuePair("tipoUsuario", "B"));


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
                Toast.makeText(getApplication(), "Cambios guardados", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplication(), AjustesCuentaActivity.class);
                startActivity(intent);

            }
            else
            {
                Toast.makeText(getApplication(), "Verifique sus datos", Toast.LENGTH_LONG).show();
                btnGuardarCambios.setEnabled(true);
            }
        }
    }

    private class TareaWSActualizarImagenUsuario extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            try
            {
                HttpClient httpClient;
                List<NameValuePair> nameValuePairs;
                HttpPost httpPost;
                httpClient = new DefaultHttpClient();

                httpPost = new HttpPost("http://netgreen.org.mx/ws/modificar_imagen.php");
                nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("idUsuario", params[0]));
                nameValuePairs.add(new BasicNameValuePair("imagen", params[1]));
                nameValuePairs.add(new BasicNameValuePair("tipo",params[2]));

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
                Toast.makeText(getApplication(), "Cambios guardados", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplication(), AjustesCuentaActivity.class);
                startActivity(intent);

            }
            else
            {
                Toast.makeText(getApplication(), "Verifique sus datos", Toast.LENGTH_LONG).show();
                btnGuardarCambios.setEnabled(true);
            }
        }
    }

    private class TareaWSCargarUsuarioPerfil extends AsyncTask<String,Integer,Boolean> {
        protected Boolean doInBackground(String... params) {
            boolean resul = true;


            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del = new HttpGet("http://netgreen.org.mx/ws/consulta_usuario.php?idUsuario="+  params[0]);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);
                for(int i=0; i < respJSON.length(); i++) {
                    JSONObject jsonobject = respJSON.getJSONObject(i);
                    nombreCompleto    = jsonobject.getString("nombre");
                    nombreUsuario    = jsonobject.getString("usuario");
                    correo = jsonobject.getString("correo");
                    byte[] decodeString = Base64.decode(jsonobject.getString("imagen"), Base64.DEFAULT);
                    imagen = BitmapFactory.decodeByteArray(decodeString,0, decodeString.length);
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

                    etNombreCompleto.setText(nombreCompleto);
                    etNombreUsuario.setText(nombreUsuario);
                    etCorreo.setText(correo);
                    if(imagen != null)
                        ivPerfil.setImageBitmap(imagen);
                    else
                        ivPerfil.setImageDrawable(getResources().getDrawable(R.drawable.ic_user));

                }
                catch (Exception ex)
                {
                    String exc = ex.toString();

                }
            }
            else
            {

            }
        }
    }


}
