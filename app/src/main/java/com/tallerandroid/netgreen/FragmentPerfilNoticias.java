package com.tallerandroid.netgreen;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yesce on 08/10/2017.
 */

public class FragmentPerfilNoticias extends Fragment {
    ListView lvPublicaciones;
    AdaptadorListaInicio adaptadorLista;
    private ArrayList<Inicio> publicaciones;
    Inicio p1, p2;

    private SQLiteDatabase db;

    public static FragmentPerfilNoticias newInstance(){
        FragmentPerfilNoticias fragment = new FragmentPerfilNoticias();
        return fragment;
    }

    public FragmentPerfilNoticias(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_fragment_perfil_noticias, container, false);
    }

    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        publicaciones = new ArrayList<>();
        lvPublicaciones = (ListView) getView().findViewById(R.id.lvNoticias_Perfil);
        adaptadorLista = new AdaptadorListaInicio(getActivity(), publicaciones);

        UsuarioLogueadoSQLiteHelper usdbh = new UsuarioLogueadoSQLiteHelper(getContext(), "DBUsuario", null, 1);
        db = usdbh.getWritableDatabase();
        String idUsuario = "";
        Cursor c = db.rawQuery("SELECT idUsuario FROM Usuario", null);
        if (c.moveToFirst()) {
            idUsuario = c.getString(0);
        }
        TareaWSCargarNoticiasPerfil tarea = new TareaWSCargarNoticiasPerfil();
        tarea.execute(idUsuario);

        lvPublicaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                Intent intent = new Intent(getActivity(), DetalleItemMiActividadActivity.class);
                startActivity(intent);

            }
        });
    }
    private class TareaWSCargarNoticiasPerfil extends AsyncTask<String,Integer,Boolean> {
        private String fecha;
        private String nombrePublicacion;
        private String descripcion;
        private Bitmap imagen;

        protected Boolean doInBackground(String... params) {
            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String correo = params[0];

            HttpGet del = new HttpGet("http://netgreen.org.mx/ws/consulta_publicaciones.php?idUsuario="+params[0]+"&tipo=Noticia");

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                for(int i=0; i < respJSON.length(); i++) {
                    JSONObject jsonobject = respJSON.getJSONObject(i);
                    fecha  = jsonobject.getString("fecha_hora_creacion");
                    nombrePublicacion = jsonobject.getString("nombrePublicacion");
                    descripcion = jsonobject.getString("descripcion");
                    imagen = BitmapFactory.decodeResource(getContext().getResources(),
                            R.drawable.ic_star_outline_black_36dp);
                    p1 = new Inicio();
                    p1.setFechaPublicacion(fecha);
                    p1.setNomPublicacion(nombrePublicacion);
                    p1.setDescripcion(descripcion);
                    p1.setImagen(imagen);
                    publicaciones.add(p1);
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
                lvPublicaciones.setAdapter(adaptadorLista);
            }
            else
            {

            }
        }
    }



}
