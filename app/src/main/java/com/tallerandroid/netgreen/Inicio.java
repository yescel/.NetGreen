package com.tallerandroid.netgreen;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.Date;

/**
 * Created by yesce on 14/05/2017.
 */

public class  Inicio {
    private String nomUsuario;
    private String fechaPublicacion;
    private int idPublicacion;
    private String nomPublicacion;
    private String Descripcion;
    private int tipo;
    private Bitmap imagen;
    private int idUsuario;

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNomUsuario() {
        return nomUsuario;
    }

    public void setNomUsuario(String nomUsuario) {
        this.nomUsuario = nomUsuario;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public int getIdPublicacion() {
        return idPublicacion;
    }

    public void setIdPublicacion(int idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    public String getNomPublicacion() {
        return nomPublicacion;
    }

    public void setNomPublicacion(String nomPublicacion) {
        this.nomPublicacion = nomPublicacion;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }
}