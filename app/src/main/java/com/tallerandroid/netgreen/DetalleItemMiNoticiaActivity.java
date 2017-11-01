package com.tallerandroid.netgreen;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by yesce on 08/10/2017.
 */

public class DetalleItemMiNoticiaActivity extends AppCompatActivity {
    private Button btnGuardarCambios = null;
    private Button btnEliminar = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_item_noticia);


        btnGuardarCambios = (Button)findViewById(R.id.btnGuardar_ModificarNot);
        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                DialogoGuardarConfirmacion dialogo = new DialogoGuardarConfirmacion();
                dialogo.show(fragmentManager, "tagPersonalizado");
            }
        });


        btnEliminar = (Button)findViewById(R.id.btnEliminar_ModificarNot);
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                DialogoEliminarConfirmacion dialogo = new DialogoEliminarConfirmacion();
                dialogo.show(fragmentManager, "tagPersonalizado");
            }
        });

    }


}