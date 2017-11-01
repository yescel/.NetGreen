package com.tallerandroid.netgreen;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by yesce on 16/05/2017.
 */

public class Ranking {
    private Bitmap imagen;
    private String nomUsuario;
    private Drawable ivInsignida;
    private String puntos;

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public String getPuntos() {
        return puntos;
    }

    public void setPuntos(String puntos) {
        this.puntos = puntos;
    }


    public String getNomUsuario(){
        return nomUsuario;
    }
    public void setNomUsuario(String nomUsuario) {   this.nomUsuario = nomUsuario;  }


    public Drawable getIvInsignida() { return ivInsignida; }
    public void setIvInsignida(Drawable ivInsignida) {
        this.ivInsignida = ivInsignida;
    }


}
