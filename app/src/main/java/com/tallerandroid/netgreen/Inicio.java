package com.tallerandroid.netgreen;

import android.widget.ImageView;

/**
 * Created by yesce on 14/05/2017.
 */

public class  Inicio {
    private ImageView imagen;
    private  int idActividad_Noticia;
    private String nomActividad_Noticia;
    private String descripcionAct_Not;
    private int idUsuario;
    private String nomUsuario;
    private int cantParticipantes;
    private int cantSeguidores;
    private String categoria;
    private char tipo;


    public int getIdActividad_Noticia(){
        return idActividad_Noticia;
    }
    public ImageView getImagen() {
        return imagen;
    }
    public void setImagen(ImageView imagen) {
        this.imagen = imagen;
    }

    public String getNomActividad_Noticia(){
        return nomActividad_Noticia;
    }
    public void setNomActividad_Noticia(String nomActividad_Noticia) {
        this.nomActividad_Noticia = nomActividad_Noticia;
    }

    public String getDescripcionAct_Not(){
        return descripcionAct_Not;
    }
    public void setDescripcionAct_Not(String descripcionAct_Not) {
        this.descripcionAct_Not = descripcionAct_Not;
    }

    public int getIdUsuario(){
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {   this.idUsuario = idUsuario;  }

    public String getNomUsuario(){
        return nomUsuario;
    }
    public void setUsuario(String nomUsuario) {   this.nomUsuario = nomUsuario;  }

    public int getCantParticipantes(){
        return cantParticipantes;
    }
    public void setCantParticipantes(int cantParticipantes) {   this.cantParticipantes = cantParticipantes;  }

    public int getCantSeguidores(){
        return cantSeguidores;
    }
    public void setCantSeguidores(int cantSeguidores) {   this.cantSeguidores = cantSeguidores;  }

    public String getCategoria(){
        return categoria;
    }
    public void setCategoria(String categoria) {   this.categoria = categoria;  }

    public char getTipo(){
        return tipo;
    }
    public void setTipo(char tipo) {   this.tipo = tipo;  }

}