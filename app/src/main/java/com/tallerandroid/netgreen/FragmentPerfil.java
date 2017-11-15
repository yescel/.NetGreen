package com.tallerandroid.netgreen;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by yesce on 15/05/2017.
 */

public class FragmentPerfil extends Fragment {

    TextView tvNombreEncabezado;
    ImageView ivFotoPerfil;
    TextView tvNombreCompleto;
    RadioButton radHombre;
    RadioButton radMujer;
    TextView tvCorreo;
    TextView tvNombreUsuario;
    TextView tvPuntos;
    TextView tvGenero;

    private String nombre;
    private Bitmap imagen;
    private String genero;
    private String correo;
    private String usuario;
    private String puntos;

    private SQLiteDatabase db;

    public static FragmentPerfil newInstance() {
        FragmentPerfil fragment = new FragmentPerfil();
        return fragment;
    }

    public FragmentPerfil(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_fragment_perfil_info, container, false);
    }

    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        tvNombreEncabezado = (TextView) getActivity().findViewById(R.id.tvNomUsuario_Perfil);
        ivFotoPerfil = (ImageView) getActivity().findViewById(R.id.ivFotoUsuario_Perfil);

        tvNombreCompleto = (TextView) getActivity().findViewById(R.id.tvNombreCompleto_Informacion);
        tvCorreo = (TextView) getActivity().findViewById(R.id.tvEmailUsuario_Informacion);
        tvNombreUsuario = (TextView) getActivity().findViewById(R.id.tvNombreUsuario_Informacion);
        tvPuntos = (TextView) getActivity().findViewById(R.id.tvPuntos_Informacion);
        tvGenero = (TextView) getActivity().findViewById(R.id.tvGenero_Informacion);

        UsuarioLogueadoSQLiteHelper usdbh = new UsuarioLogueadoSQLiteHelper(getContext(), "DBUsuario", null, 1);
        db = usdbh.getWritableDatabase();

        String idUsuario = "";
        Cursor c = db.rawQuery("SELECT idUsuario FROM Usuario", null);
        if (c.moveToFirst()) {
            idUsuario = c.getString(0);
        }
        TareaWSCargarUsuarioPerfil tarea = new TareaWSCargarUsuarioPerfil();
        tarea.execute(idUsuario);
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
                    nombre    = jsonobject.getString("nombre");
                    byte[] decodeString = Base64.decode(jsonobject.getString("imagen"), Base64.DEFAULT);
                    imagen = BitmapFactory.decodeByteArray(decodeString,0, decodeString.length);
                    genero    = jsonobject.getString("genero");
                    correo = jsonobject.getString("correo");
                    usuario    = jsonobject.getString("usuario");
                    puntos  = jsonobject.getString("puntos");
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
                    if(usuario == null) {
                        tvNombreEncabezado.setText(nombre);
                        tvNombreUsuario.setText("No ha cambiado su nombre de usuario");
                    }
                    else {
                        tvNombreEncabezado.setText(usuario);
                        tvNombreUsuario.setText("Usuario: "+usuario);
                    }
                    ivFotoPerfil.setImageBitmap(imagen);
                    tvNombreCompleto.setText("Nombre: "+nombre);
                    if(genero == "Hombre")
                        tvGenero.setText("Género: Hombre");
                    else
                        tvGenero.setText("Género: Mujer");

                    tvCorreo.setText("Correo electrónico: "+correo);
                    tvPuntos.setText("Puntos acumulados: " + puntos);



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
