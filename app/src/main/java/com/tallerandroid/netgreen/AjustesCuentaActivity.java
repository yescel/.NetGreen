package com.tallerandroid.netgreen;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by yesce on 01/10/2017.
 */

public class AjustesCuentaActivity extends FragmentActivity {

    private Button btnGuardarCambios = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes_cuenta);


        btnGuardarCambios = (Button)findViewById(R.id.btnGuardarCambios);

        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                DialogoGuardarConfirmacion dialogo = new DialogoGuardarConfirmacion();
                dialogo.show(fragmentManager, "tagPersonalizado");
            }
        });

    }



}
