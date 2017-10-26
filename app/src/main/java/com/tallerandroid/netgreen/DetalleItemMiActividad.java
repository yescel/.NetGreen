package com.tallerandroid.netgreen;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by yesce on 08/10/2017.
 */

public class DetalleItemMiActividad extends AppCompatActivity {
    private Button btnGuardarCambios = null;
    private Button btnEliminar = null;
    private Button btnValidar = null;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_item_actividad);


        btnGuardarCambios = (Button)findViewById(R.id.btnGuardar_ModificarAct);
        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                DialogoGuardarConfirmacion dialogo = new DialogoGuardarConfirmacion();
                dialogo.show(fragmentManager, "tagPersonalizado");
            }
        });


        btnEliminar = (Button)findViewById(R.id.btnCancelarActividad);
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                DialogoEliminarConfirmacion dialogo = new DialogoEliminarConfirmacion();
                dialogo.show(fragmentManager, "tagPersonalizado");
            }
        });


        btnValidar = (Button)findViewById(R.id.btnValidarActividad);
        btnValidar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                DialogoValidarActividad dialogo = new DialogoValidarActividad();
                dialogo.show(fragmentManager, "tagPersonalizado");
            }
        });
    }

}