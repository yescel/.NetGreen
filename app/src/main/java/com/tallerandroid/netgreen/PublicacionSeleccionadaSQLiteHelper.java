package com.tallerandroid.netgreen;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yesce on 31/10/2017.
 */

public class PublicacionSeleccionadaSQLiteHelper extends SQLiteOpenHelper {
    String sqlCreate = "CREATE TABLE PublicacionSeleccionada (idPublicacion VARCHAR(10), tipoPublicacion VARCHAR(15))";

    public PublicacionSeleccionadaSQLiteHelper(Context contexto, String nombre,
                                       SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {

        db.execSQL("DROP TABLE IF EXISTS PublicacionSeleccionada");
        db.execSQL(sqlCreate);
    }
}
