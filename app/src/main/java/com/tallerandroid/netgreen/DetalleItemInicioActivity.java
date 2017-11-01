package com.tallerandroid.netgreen;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by yesce on 16/05/2017.
 */

public class DetalleItemInicioActivity extends AppCompatActivity {

    private SQLiteDatabase db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_item_inicio);
        String idPublicacion = "";
        String tipoPublicacion = "";


        try {
            PubSeleccionadaSQLiteHelper usdbh = new PubSeleccionadaSQLiteHelper(this,
                    "DBPubSeleccionada", null, 1);
            db = usdbh.getWritableDatabase();
            Cursor c = db.rawQuery("SELECT idPublicacion, tipoPublicacion FROM PubSeleccionada",
                    null);
            if (c.moveToFirst()) {
                idPublicacion = c.getString(0);
                tipoPublicacion = c.getString(1);
            }
        }catch (Exception ex){
            String e = ex.toString();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // db.delete("PublicacionSeleccionada", "", null);
    }

}
