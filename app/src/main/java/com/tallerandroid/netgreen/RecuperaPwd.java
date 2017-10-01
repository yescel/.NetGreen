package com.tallerandroid.netgreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by yesce on 01/10/2017.
 */

public class RecuperaPwd extends AppCompatActivity {
    TextView intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperapwd);

        intent = (TextView) findViewById(R.id.link_login);
        intent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecuperaPwd.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
