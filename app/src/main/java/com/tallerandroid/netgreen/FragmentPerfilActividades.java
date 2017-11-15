package com.tallerandroid.netgreen;

import android.app.ProgressDialog;
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
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yesce on 15/05/2017.
 */

public class FragmentPerfilActividades extends Fragment {
    public static final String ID_ACTIVIDAD = "com.tallerandroid.netgreen.ID_ACTIVIDAD";
    public static final String TIPO_ACTIVIDAD = "com.tallerandroid.netgreen.TIPO_ACTIVIDAD";

    ListView lvPublicaciones;
    AdaptadorListaInicio adaptadorLista;
    private ArrayList<Inicio> publicaciones;
    Inicio p1, p2;

    private SQLiteDatabase db;

    public static FragmentPerfilActividades newInstance(){
        FragmentPerfilActividades fragment = new FragmentPerfilActividades();
        return fragment;
    }

    public FragmentPerfilActividades(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_fragment_perfil_actividades, container, false);
    }

    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        publicaciones = new ArrayList<>();
        lvPublicaciones = (ListView) getView().findViewById(R.id.lvActividades_Perfil);
        //lvPublicaciones.setEmptyView(getView().findViewById(R.id.loadListView_perfil_actividades));
        adaptadorLista = new AdaptadorListaInicio(getActivity(), publicaciones);

        UsuarioLogueadoSQLiteHelper usdbh = new UsuarioLogueadoSQLiteHelper(getContext(), "DBUsuario", null, 1);
        db = usdbh.getWritableDatabase();
        String idUsuario = "";
        Cursor c = db.rawQuery("SELECT idUsuario FROM Usuario", null);
        if (c.moveToFirst()) {
            idUsuario = c.getString(0);
        }
        TareaWSCargarActividadesPerfil tarea = new TareaWSCargarActividadesPerfil();
        tarea.execute(idUsuario);


            lvPublicaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                TextView tvId = (TextView) v.findViewById(R.id.tvIdPublicacion);
                TextView tvTipo = (TextView) v.findViewById(R.id.tvTipoPubblicacion);
                String txtId = tvId.getText().toString();
                String txtTipo = tvTipo.getText().toString();

                Intent intent = new Intent(getActivity(), DetalleItemMiActividadActivity.class);
                intent.putExtra(ID_ACTIVIDAD, txtId);
                intent.putExtra(TIPO_ACTIVIDAD, txtTipo);
                startActivity(intent);
            }
        });
    }
    private class TareaWSCargarActividadesPerfil extends AsyncTask<String,Integer,Boolean> {
        private int id;
        private String tipoPublicacion;
        private String fecha;
        private String nombrePublicacion;
        private String descripcion;
        private Bitmap imagen;

        protected Boolean doInBackground(String... params) {
            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            String correo = params[0];

            HttpGet del = new HttpGet("http://netgreen.org.mx/ws/consulta_publicaciones.php?idUsuario="+params[0]+"&tipo=Actividad");

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
                    id = jsonobject.getInt("idPublicacion");
                    String idPublicacion = Integer.toString(id);
                    tipoPublicacion = jsonobject.getString("tipo");
                    fecha  = jsonobject.getString("fecha_hora_creacion");
                    nombrePublicacion = jsonobject.getString("nombrePublicacion");
                    descripcion = jsonobject.getString("descripcion");
                    imagen = BitmapFactory.decodeResource(getContext().getResources(),
                            R.drawable.ic_human_greeting_grey600_48dp);

                    p1 = new Inicio();
                    p1.setFechaPublicacion(fecha);
                    p1.setNomPublicacion(nombrePublicacion);
                    p1.setDescripcion(descripcion);
                    p1.setImagen(imagen);
                    p1.setIdPublicacion(idPublicacion);
                    p1.setTipo(tipoPublicacion);
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
        /*
        ProgressDialog m_dialog = new ProgressDialog(getContext());

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            // initialize the dialog
            m_dialog.setTitle("Loading...");
            m_dialog.setMessage("Please wait while loading...");
            m_dialog.setIndeterminate(true);
            m_dialog.setCancelable(true);
            m_dialog.show();

        }*/
        protected void onPostExecute(Boolean result) {

            if (result)
            {
                lvPublicaciones.setAdapter(adaptadorLista);
            }
            else
            {
                lvPublicaciones.setEmptyView(getActivity().findViewById(R.id.emptyListView_perfil_actividades));
            }
        }
    }



}
