package com.tallerandroid.netgreen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;

/**
 * Created by yesce on 08/10/2017.
 */

public class DialogoCerrarSesion extends DialogFragment {
    private SQLiteDatabase db;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        UsuarioLogueadoSQLiteHelper usdbh = new UsuarioLogueadoSQLiteHelper(getContext(), "DBUsuario", null, 1);
        db = usdbh.getWritableDatabase();

        builder.setView(inflater.inflate(R.layout.dialogo_cerrar_sesion, null))
                .setTitle("Confirmacion")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        db.delete("Usuario", "", null);
                        Intent intent = new Intent(getContext(), SplashActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("Dialogos", "Confirmacion Cancelada.");
                        dialog.cancel();
                    }
                });


        return builder.create();
    }
}