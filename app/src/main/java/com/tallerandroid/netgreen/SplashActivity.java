package com.tallerandroid.netgreen;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Window;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yesce on 23/10/2017.
 */

public class SplashActivity extends Activity {
    // Set the duration of the splash screen
    private static final long SPLASH_SCREEN_DELAY = 2000;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);

        UsuarioLogueadoSQLiteHelper usdbh = new UsuarioLogueadoSQLiteHelper(this, "DBUsuario", null, 1);
        db = usdbh.getWritableDatabase();


        TimerTask taskNoLogueado = new TimerTask() {
            @Override
            public void run() {
                Intent mainIntent = new Intent().setClass(
                        SplashActivity.this, LoginActivity.class);
                startActivity(mainIntent);
                finish();
            }
        };
        TimerTask taskLogueado = new TimerTask() {
            @Override
            public void run() {
                Intent mainIntent = new Intent().setClass(SplashActivity.this, DashboardActivity.class);
                startActivity(mainIntent);
                finish();
            }
        };

        Cursor c = db.rawQuery("SELECT idUsuario FROM Usuario", null);
        if (c.moveToFirst()) {
            Timer timer = new Timer();
            timer.schedule(taskLogueado, SPLASH_SCREEN_DELAY);
        }
        else
        {
            Timer timer = new Timer();
            timer.schedule(taskNoLogueado, SPLASH_SCREEN_DELAY);
        }

        // Simulate a long loading process on application startup.

    }
}
