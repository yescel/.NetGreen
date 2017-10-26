package com.tallerandroid.netgreen;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yesce on 26/10/2017.
 */

public class UsuarioLogueadoSQLiteHelper extends SQLiteOpenHelper{
    String sqlCreate = "CREATE TABLE Usuario (idUsuario INTEGER)";

    public UsuarioLogueadoSQLiteHelper(Context contexto, String nombre,
                              SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {

        db.execSQL("DROP TABLE IF EXISTS Usuario");
        db.execSQL(sqlCreate);
    }
}
