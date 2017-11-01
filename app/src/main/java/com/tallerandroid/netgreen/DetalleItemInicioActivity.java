package com.tallerandroid.netgreen;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by yesce on 16/05/2017.
 */

public class DetalleItemInicioActivity extends AppCompatActivity {

    private SQLiteDatabase db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_item_inicio);
        Intent intent = getIntent();

        String idPublicacion = intent.getStringExtra(FragmentInicio.ID_ACTIVIDAD);
        String tipoPublicacion = intent.getStringExtra(FragmentInicio.TIPO_ACTIVIDAD);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // db.delete("PublicacionSeleccionada", "", null);
    }

}
