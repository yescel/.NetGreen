package com.tallerandroid.netgreen;

import android.widget.ImageView;

/**
 * Created by yesce on 16/05/2017.
 */

public class Ranking {
    private ImageView ivImagen;
    private String nomUsuario;
    private String nomOrganismo;
    private ImageView ivInsignida;

    public ImageView getIvImagen() {
        return ivImagen;
    }
    public void setIvImagen(ImageView ivImagen) {
        this.ivImagen = ivImagen;
    }

    public String getNomUsuario(){
        return nomUsuario;
    }
    public void setNomUsuario(String nomUsuario) {   this.nomUsuario = nomUsuario;  }

    public String getNomOrganismo(){
        return nomOrganismo;
    }
    public void setNomOrganismo(String nomOrganismo) {   this.nomOrganismo = nomOrganismo;  }

    public ImageView getIvInsignida() { return ivInsignida; }
    public void setIvInsignida(ImageView ivInsignida) {
        this.ivInsignida = ivInsignida;
    }


}
