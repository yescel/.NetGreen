package com.tallerandroid.netgreen;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class RegistrarActivity extends AppCompatActivity {
    TextView intent;
    private Button btnInsertar;
    private EditText txtNombre;
    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtPasswordR;
    private RadioButton rbtnHombre;
    private String tipoUsuario = "Normal";
    private String genero = "";
    private static final int REQUEST_LOGIN = 0;
    @InjectView(R.id.link_login)
    TextView _loginLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        ButterKnife.inject(this);

        //intent = (TextView) findViewById(R.id.link_login);
        btnInsertar = (Button)findViewById(R.id.btnRegistrar);
        txtNombre = (EditText)findViewById(R.id.etNombreUsuario_Ajustes);
        txtEmail = (EditText)findViewById(R.id.etCorreoUsuario_Ajustesl);
        txtPassword = (EditText)findViewById(R.id.etPassword_Ajustes);
        txtPasswordR = (EditText)findViewById(R.id.etPassword2_Ajustes);
        rbtnHombre = (RadioButton)findViewById(R.id.radHombre);

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent, REQUEST_LOGIN);
            }
        });

        btnInsertar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TareaWSInsertarUsuario tarea = new TareaWSInsertarUsuario();
                if(!txtPassword.getText().toString().equals(txtPasswordR.getText().toString()))
                {
                    Toast.makeText(getBaseContext(), "El password no coincide", Toast.LENGTH_LONG).show();
                    btnInsertar.setEnabled(true);
                }
                else {
                    if(rbtnHombre.isChecked())
                        genero = "Hombre";
                    else
                        genero = "Mujer";

                    tarea.execute(
                            txtNombre.getText().toString(),
                            txtPassword.getText().toString(),
                            txtEmail.getText().toString(),
                            genero,
                            tipoUsuario);
                }

            }
        });

    }


    private class TareaWSInsertarUsuario extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            try
            {
                HttpClient httpClient;
                List<NameValuePair> nameValuePairs;
                HttpPost httpPost;
                httpClient = new DefaultHttpClient();

                httpPost = new HttpPost("http://netgreen.org.mx/ws/insertar_usuario.php");
                nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair("nombre", params[0]));
                nameValuePairs.add(new BasicNameValuePair("passwd",params[1]));
                nameValuePairs.add(new BasicNameValuePair("correo", params[2]));
                nameValuePairs.add(new BasicNameValuePair("genero", params[3]));
                nameValuePairs.add(new BasicNameValuePair("tipoUsuario", params[4]));

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
                Intent intent = new Intent(RegistrarActivity.this, LoginActivity.class);
                startActivity(intent);

                Toast.makeText(getBaseContext(), "Se ha registrado existosamente!", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getBaseContext(), "Error al registrarse, intente nuevamente", Toast.LENGTH_LONG).show();
                btnInsertar.setEnabled(true);
            }
        }
    }


}
