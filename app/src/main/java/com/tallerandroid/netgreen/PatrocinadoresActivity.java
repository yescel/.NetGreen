package com.tallerandroid.netgreen;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by yesce on 01/10/2017.
 */

public class PatrocinadoresActivity  extends AppCompatActivity {

    ListView lvPatrocinadores;
    AdaptadorListaPatrocinadores adaptadorLista;
    ArrayList<Patrocinadores> patrocinadores;
    Patrocinadores mPatrocinador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrocinadores);


        patrocinadores = new ArrayList<>();
        lvPatrocinadores = (ListView) findViewById(R.id.lvPatrocinadores);
        adaptadorLista = new AdaptadorListaPatrocinadores(this, patrocinadores);
        TareaWSCargarPatrocinadores tarea = new TareaWSCargarPatrocinadores();
        tarea.execute();

    }

    private class TareaWSCargarPatrocinadores extends AsyncTask<String,Integer,Boolean> {

        private String organismo = "";
        private String contacto="";
        private String telefono="";


        protected Boolean doInBackground(String... params) {
            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();


            HttpGet del = new HttpGet("http://netgreen.org.mx/ws/consulta_patrocinadores.php");

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
                    organismo = jsonobject.getString("nombre");
                    contacto = jsonobject.getString("contacto");
                    telefono = jsonobject.getString("telefono");


                    mPatrocinador = new Patrocinadores();
                    mPatrocinador.setOrganismo(organismo);
                    mPatrocinador.setContacto(contacto);
                    mPatrocinador.setTelefono(telefono);
                    patrocinadores.add(mPatrocinador);
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
                    lvPatrocinadores.setAdapter(adaptadorLista);
                }catch (Exception ex)
                {
                    String e =ex.toString();
                }
            }
            else

            {
                lvPatrocinadores.setEmptyView(findViewById(R.id.cdVacio_patrocinadores));

            }
        }
    }


}
