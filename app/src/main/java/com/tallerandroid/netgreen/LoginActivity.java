package com.tallerandroid.netgreen;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;




public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    private SQLiteDatabase db;

    int intUsuario;
    private String nuevo;


    @InjectView(R.id.etCorreoUsuario_Ajustesl)
    EditText _emailText;
    @InjectView(R.id.etPassword_Ajustes)
    EditText _passwordText;
    @InjectView(R.id.btnRegistrar)
    Button _loginButton;
    @InjectView(R.id.link_recuperaPwd)
    TextView _recuperaPwdLink;
    @InjectView(R.id.link_signup)
    TextView _signupLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();


            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegistrarActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
        _recuperaPwdLink.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                DialogoRecuperarPwd dialogo = new DialogoRecuperarPwd();
                dialogo.show(fragmentManager, "tagPersonalizado");
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Autentificando...");
        progressDialog.show();


        // TODO: Implement your own authentication logic here.
        TareaWSValidarUsu tarea = new TareaWSValidarUsu();
        tarea.execute(_emailText.getText().toString(), _passwordText.getText().toString());




        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implementar la lógica de registro exitosa aquí
                // De forma predeterminada, acabamos de terminar la Actividad y registrarlos automáticamente
                this.finish();
            }
        }
    }
    @Override
    public void onBackPressed() {
        // Desactivar volver a la MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        UsuarioLogueadoSQLiteHelper usdbh = new UsuarioLogueadoSQLiteHelper(this, "DBUsuario", null, 1);
        db = usdbh.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT idUsuario FROM Usuario", null);
        if (c.moveToFirst()) {
            intUsuario = c.getInt(0);
        }

        if(nuevo.equals("1")) {
            TareaWSInsertarImagenUsuario tarea2 = new TareaWSInsertarImagenUsuario();
            tarea2.execute(Integer.toString(intUsuario));

            //Cambiar el usuario despues de la introduccion a la app
            TareaWSActualizarUsuarioN tarea3 = new TareaWSActualizarUsuarioN();
            tarea3.execute(Integer.toString(intUsuario), "0");
        }

        Intent mainIntent = new Intent().setClass(this, DashboardActivity.class);
        startActivity(mainIntent);
    }
    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Verifique sus datos!", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }
    //Validación de email y password
    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Ingrese un correo válido");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("Entre 4 a 10 carácteres alfanuméricos");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }




    private class TareaWSValidarUsu extends AsyncTask<String,Integer,Boolean> {

        private int idUsuario;
        private String correoUsu;
        private String passwordUsu;

        protected Boolean doInBackground(String... params) {
            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String correo = params[0];
            String passw = params[1];

            HttpGet del = new HttpGet("http://www.netgreen.org.mx/ws/consulta_usuario.php?correo="+ correo);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                for(int i=0; i < respJSON.length(); i++) {
                    JSONObject jsonobject = respJSON.getJSONObject(i);
                    idUsuario = jsonobject.getInt("idUsuario");
                    correoUsu    = jsonobject.getString("correo");
                    passwordUsu  = jsonobject.getString("passwd");
                    nuevo = jsonobject.getString("nuevo");

                }

            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul = false;
            }

            if(!passw.equals(passwordUsu))
                resul = false;

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                UsuarioLogueadoSQLiteHelper usdbh = new UsuarioLogueadoSQLiteHelper(getApplicationContext(), "DBUsuario", null, 1);
                db = usdbh.getWritableDatabase();

                ContentValues nuevoRegistro = new ContentValues();
                nuevoRegistro.put("idUsuario", idUsuario);
                db.insert("Usuario", null, nuevoRegistro);
                onLoginSuccess();
            }
            else
            {
                onLoginFailed();
            }
        }
    }

    private class TareaWSInsertarImagenUsuario extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            try
            {
                HttpClient httpClient;
                List<NameValuePair> nameValuePairs;
                HttpPost httpPost;
                httpClient = new DefaultHttpClient();

                httpPost = new HttpPost("http://netgreen.org.mx/ws/insertar_imagen.php");
                nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair("descripcion", "img"));
                nameValuePairs.add(new BasicNameValuePair("idUsuario",params[0]));
                nameValuePairs.add(new BasicNameValuePair("tipo", "Perfil"));

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

    private class TareaWSActualizarUsuarioN extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            try
            {
                HttpClient httpClient;
                List<NameValuePair> nameValuePairs;
                HttpPost httpPost;
                httpClient = new DefaultHttpClient();

                httpPost = new HttpPost("http://netgreen.org.mx/ws/modificar_usuario.php");
                nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("idUsuario", params[0]));
                nameValuePairs.add(new BasicNameValuePair("nuevo", params[1]));


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